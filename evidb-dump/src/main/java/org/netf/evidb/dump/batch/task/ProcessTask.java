package org.netf.evidb.dump.batch.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.netf.evidb.dump.batch.dao.GenericDao;
import org.netf.evidb.dump.batch.model.Dump;
import org.netf.evidb.dump.batch.model.Item;
import org.netf.evidb.dump.batch.model.Settings;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProcessTask implements Tasklet {

	private final Dump dump;
	private final Settings settings;
	private final GenericDao genericDao;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		List<Item> items = settings.getItems();

		for (Item item : items) {
			outputFile(item);
		}

		return RepeatStatus.FINISHED;
	}

	/**
	 * 検索結果をファイルへ出力する。
	 *
	 * @param item
	 * @throws Exception
	 */
	private void outputFile(Item item) throws Exception {

		// 出力フォルダ作成
		File outputDir = new File(dump.getOutputDir(),
				DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()));
		outputDir.mkdirs();

		// 出力ファイルパス生成
		File outputFile = new File(outputDir, item.getTableName() + ".csv");

		try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(outputFile), "utf-8");
				CSVWriter csvWriter = new CSVWriter(osw)) {

			// BOM
			osw.write("\ufeff");

			// ヘッダー出力
			csvWriter.writeNext(item.getColumnName().keySet().toArray(new String[0]));

			// データ出力
			genericDao.selectAll(item.getQuery(), stream -> {
				stream.forEach(entity -> {
					String[] array = entity.values().stream().map(mapper -> {
						if (mapper != null) {
							return mapper.toString();
						}
						return dump.getNullValue();
					}).toArray(String[]::new);
					csvWriter.writeNext(array);
				});

				return null;
			});

			csvWriter.close();

		}

	}

}
