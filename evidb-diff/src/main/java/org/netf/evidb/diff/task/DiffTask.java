package org.netf.evidb.diff.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.netf.evidb.core.exception.ApplicationException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * DiffTask
 *
 * @author n296
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
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

	protected List<String> getFilenameList(File targetDir) {

		File[] targetFiles = targetDir.listFiles();

		List<File> resultList = Arrays.asList(targetFiles);
		Collections.sort(resultList, NameFileComparator.NAME_COMPARATOR);

		List<String> filenameList = new ArrayList();

		for (File file : resultList) {
			filenameList.add(file.getName());
		}

		return filenameList;

	}

}
