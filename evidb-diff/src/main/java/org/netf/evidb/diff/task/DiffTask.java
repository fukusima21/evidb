package org.netf.evidb.diff.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.netf.evidb.core.exception.ApplicationException;

import com.opencsv.CSVReader;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.sf.jett.transform.ExcelTransformer;

/**
 * DiffTask
 *
 * @author n296
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DiffTask extends Task {

	private String dumpDir;

	private String reportDir;

	private String before;

	private String after;

	/* (非 Javadoc)
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException {

		if (StringUtils.isEmpty(before) && StringUtils.isEmpty(after)) {
			// 両方 null
			List<File> recentFiles = getRecentFiles();

			if (recentFiles.size() < 2) {
				throw new ApplicationException("There is no data");
			}

			after = recentFiles.get(0).getAbsolutePath();
			before = recentFiles.get(1).getAbsolutePath();

		} else if (StringUtils.isEmpty(before) || StringUtils.isEmpty(after)) {
			// 片方 null

			List<File> recentFiles = getRecentFiles();

			if (recentFiles.size() < 1) {
				throw new ApplicationException("There is no data");
			}

			if (StringUtils.isNotEmpty(after)) {
				before = after;
			}

			after = recentFiles.get(0).getAbsolutePath();

		} else {
		}

	}

	/**
	 * データ差分を出力
	 */
	protected void diffAll() {

		List<org.netf.evidb.diff.model.Delta> deltas = new ArrayList<>();

		File beforeDir = new File(before);
		File afterDir = new File(after);

		List<String> beforeFiles = getFilenameList(beforeDir);

		for (String beforeFile : beforeFiles) {

			List<Set<String>> beforeCsv = readCSVFile(new File(beforeDir, beforeFile));
			List<Set<String>> afterCsv = readCSVFile(new File(afterDir, beforeFile));
			Patch<Set<String>> patch = DiffUtils.diff(beforeCsv, afterCsv);
			org.netf.evidb.diff.model.Delta delta = reportDiff(beforeFile, patch);

			delta.setBefore(beforeCsv.size());
			delta.setAfter(afterCsv.size());

			deltas.add(delta);
		}

		Map<String, Object> beans = new HashMap<>();
		beans.put("deltas", deltas);

		// 出力フォルダ作成
		File reportDir = new File(getReportDir());
		reportDir.mkdirs();

		// 出力ファイル名作成
		String reportFile = String.format("%s.xlsx",
				DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));

		List<String> templateSheetNames = new ArrayList<String>();
		List<String> sheetNames = new ArrayList<String>();

		templateSheetNames.add("summary");
		sheetNames.add("サマリー");

		templateSheetNames.add("report");
		sheetNames.add("dummy");

		ExcelTransformer transformer = new ExcelTransformer();

		try (InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("org/netf/evidb/diff/template/report.xlsx");
				OutputStream out = new FileOutputStream(new File(reportDir, reportFile))) {
			Workbook workbook = transformer.transform(in, templateSheetNames, sheetNames, Arrays.asList(beans));
			workbook.write(out);
			workbook.close();
		} catch (InvalidFormatException | IOException e) {
			throw new ApplicationException(e.getMessage(), e);
		}

	}

	/**
	 * データ差分をレポート
	 *
	 * @param fileName
	 * @param patch
	 */
	private org.netf.evidb.diff.model.Delta reportDiff(String fileName, Patch<Set<String>> patch) {

		org.netf.evidb.diff.model.Delta delta = new org.netf.evidb.diff.model.Delta();

		List<Delta<Set<String>>> deltas = patch.getDeltas();

		delta.setCreate(0);
		delta.setUpdate(0);
		delta.setDelete(0);

		for (Delta<Set<String>> d : deltas) {

			int before = d.getOriginal().size();
			int after = d.getRevised().size();

			if (before == after) {
				delta.setUpdate(delta.getUpdate() + before);
			} else if (before < after) {
				delta.setCreate(delta.getCreate() + after - before);
			} else if (before > after) {
				delta.setDelete(delta.getDelete() + before - after);
			}

		}

		delta.setName(fileName);
		return delta;

	}

	/**
	 * 名前順（降順）にディレクトリを取得
	 *
	 * @return
	 */
	protected List<File> getRecentFiles() {

		File dumpDir = new File(this.dumpDir);

		File[] dumpDirs = dumpDir.listFiles();

		if (dumpDirs == null) {
			return Collections.emptyList();
		}

		List<File> resultList = Arrays.asList(dumpDirs);
		Collections.sort(resultList, NameFileComparator.NAME_REVERSE);

		return resultList;

	}

	/**
	 * ファイル名一覧取得
	 *
	 * @param targetDir
	 * @return
	 */
	protected List<String> getFilenameList(File targetDir) {

		File[] targetFiles = targetDir.listFiles();

		List<File> resultList = Arrays.asList(targetFiles);
		Collections.sort(resultList, NameFileComparator.NAME_COMPARATOR);

		List<String> filenameList = new ArrayList<String>();

		for (File file : resultList) {
			filenameList.add(file.getName());
		}

		return filenameList;

	}

	/**
	 * CSVファイル読み込み
	 *
	 * @param inFile
	 * @return
	 */
	protected List<Set<String>> readCSVFile(File inFile) {

		List<Set<String>> csvList = new ArrayList<Set<String>>();

		try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(inFile), "utf-8");
				CSVReader csvReader = new CSVReader(inputStreamReader);) {

			csvReader.forEach(item -> {
				Set<String> tmp = new LinkedHashSet<String>();
				for (String s : item) {
					tmp.add(s);
				}
				csvList.add(tmp);
			});

			return csvList;

		} catch (IOException e) {
			throw new ApplicationException(e.getMessage(), e);
		}

	}

}
