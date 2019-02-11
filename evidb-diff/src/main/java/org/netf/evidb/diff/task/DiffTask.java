package org.netf.evidb.diff.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.netf.evidb.core.exception.ApplicationException;

import com.opencsv.CSVReader;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

		File beforeDir = new File(before);
		File afterDir = new File(after);

		List<String> beforeFiles = getFilenameList(beforeDir);

		for (String beforeFile : beforeFiles) {
			List<Set<String>> beforeCsv = readCSVFile(new File(beforeDir, beforeFile));
			List<Set<String>> afterCsv = readCSVFile(new File(afterDir, beforeFile));
			Patch<Set<String>> patch = DiffUtils.diff(beforeCsv, afterCsv);
			reportDiff(beforeFile, patch);
		}

	}

	/**
	 * データ差分をレポート
	 *
	 * @param fileName
	 * @param patch
	 */
	private void reportDiff(String fileName, Patch<Set<String>> patch) {

		List<Delta<Set<String>>> deltas = patch.getDeltas();

		System.out.println(String.format("%s - %d", fileName, deltas.size()));



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
