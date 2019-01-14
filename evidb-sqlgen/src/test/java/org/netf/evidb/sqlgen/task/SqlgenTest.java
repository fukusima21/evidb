package org.netf.evidb.sqlgen.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class SqlgenTest {

	@Test
	public void testExecute() throws Exception {

		String outputPath = SqlgenTest.class.getResource("sqlgen.yml").getFile();

		SqlgenTask sqlgen = new SqlgenTask();

		sqlgen.setUser("postgres");
		sqlgen.setPassword("admin");
		sqlgen.setUrl("jdbc:postgresql://localhost:5432/dvdrental");
		sqlgen.setDriver("org.postgresql.Driver");

		sqlgen.setTableNamePattern(".*");
		sqlgen.setIgnoredTableNamePattern("");
		sqlgen.setDialect("postgres");
		sqlgen.setTableTypes("TABLE,VIEW");

		sqlgen.setOutputPath(outputPath);

		sqlgen.execute();
	}

}
