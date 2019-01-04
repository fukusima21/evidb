package org.netf.evidb.dump.batch.task;

import java.util.List;

import org.netf.evidb.dump.batch.model.ItemDto;
import org.netf.evidb.dump.batch.model.Settings;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class ProcessTask implements Tasklet {

	private final Settings settings;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		log.info("Hello!");

		List<ItemDto> items = settings.getItems();

		return RepeatStatus.FINISHED;
	}

}
