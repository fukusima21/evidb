package org.netf.evidb.dump.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class DumpTaskTest {

	@Test
	public void testExecute() throws Exception {

		DumpTask dumptask = new DumpTask();

		dumptask.execute();
	}

}
