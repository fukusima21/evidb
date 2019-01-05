package org.netf.evidb.dump.batch.task;

import java.util.List;
import java.util.Map;

import org.netf.evidb.dump.batch.dao.GenericDao;
import org.netf.evidb.dump.batch.model.Dump;
import org.netf.evidb.dump.batch.model.Item;
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

	private final Dump dump;
	private final Settings settings;
	private final GenericDao genericDao;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		List<Item> items = settings.getItems();

		List<Map<String, Object>> results = genericDao.selectAll(items.get(0).getQuery());

		return RepeatStatus.FINISHED;
	}

}
