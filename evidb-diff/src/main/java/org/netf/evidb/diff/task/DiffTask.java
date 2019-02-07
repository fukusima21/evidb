package org.netf.evidb.diff.task;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

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




	}

}
