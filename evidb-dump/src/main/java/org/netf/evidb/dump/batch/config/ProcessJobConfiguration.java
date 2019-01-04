package org.netf.evidb.dump.batch.config;

import org.netf.evidb.dump.batch.task.ProcessTask;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProcessJobConfiguration {

	private final JobBuilderFactory jobs;
	private final StepBuilderFactory steps;
	private final ProcessTask processTask;

	@Bean(name = "dumpJob")
	public Job dumpJob(Step dumpStep) {

		return jobs.get("dumpJob")
				.incrementer(incrementer())
				.start(dumpStep)
				.build();
	}

	@Bean(name = "dumpStep")
	public Step dumpStep() {

		return steps.get("dumpStep")
				.tasklet(processTask)
				.build();

	}

	private JobParametersIncrementer incrementer() {
		return new RunIdIncrementer();
	}

}
