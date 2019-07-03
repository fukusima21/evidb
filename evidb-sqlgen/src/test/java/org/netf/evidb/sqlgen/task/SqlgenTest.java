package org.netf.evidb.sqlgen.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class SqlgenTest {

	@Test
	public void test() throws Exception {

		DateTimeFormatter formtter = DateTimeFormatter
				.ofPattern("yyyy/M/d")
				.withResolverStyle(ResolverStyle.STRICT);

//		TemporalAccessor accessor = formtter.parse("2019/01/01");
		LocalDate.parse("2019/01/01", formtter);



//		System.out.println(accessor);
	}

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
