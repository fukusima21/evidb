package org.netf.evidb.dump.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class DumpTaskTest {

	@Test
	public void testExecute() throws Exception {

		String configFile = Thread.currentThread().getContextClassLoader().getResource("sqlgen.yml").getFile();

		DumpTask dumptask = new DumpTask();

		dumptask.setConfigFile(configFile);

		dumptask.setUser("postgres");
		dumptask.setPassword("admin");
		dumptask.setUrl("jdbc:postgresql://localhost:5432/dvdrental");
		dumptask.setDriver("org.postgresql.Driver");

		dumptask.execute();
	}

}
