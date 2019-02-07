package org.netf.evidb.diff.task;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class DiffTaskTest {

	@Test
	public void testGetRecentFiles() throws Exception {

		DiffTask diffTask = new DiffTask();

		String dumpDir = this.getClass().getResource("./dump").toURI().getPath();

		diffTask.setDumpDir(dumpDir);

		List<File> dirs = diffTask.getRecentFiles();

		Assert.assertEquals("20190107004829", dirs.get(0).getName());
		Assert.assertEquals("20190107004008", dirs.get(1).getName());

	}

	@Test
	public void testGetFiles() throws Exception {

		DiffTask diffTask = new DiffTask();

		String dumpDir = this.getClass().getResource("./dump").toURI().getPath();

		List<String> result = diffTask.getFilenameList(new File(dumpDir, "20190107004829"));

		Assert.assertEquals(
				Arrays.asList("actor.csv", "address.csv", "category.csv", "city.csv", "country.csv", "customer.csv",
						"film.csv", "film_actor.csv", "film_category.csv", "inventory.csv", "language.csv",
						"payment.csv", "rental.csv", "staff.csv", "store.csv"),
				result);

	}

}
