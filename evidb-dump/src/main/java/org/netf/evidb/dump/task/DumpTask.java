package org.netf.evidb.dump.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.netf.evidb.core.exception.ApplicationException;
import org.netf.evidb.dump.model.Item;
import org.netf.evidb.dump.model.Settings;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.opencsv.CSVWriter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DumpTask extends Task {

	/** DB driver */
	private String driver;

	/** DB url */
	private String url;

	/** User name */
	private String user;

	/** Password */
	private String password;

	/** outputDir */
	private String outputDir;

	/** null value */
	private String nullValue = "\u200B(NULL)\u200B";

	/* (非 Javadoc)
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException {

	}

	@SuppressWarnings("unchecked")
	private Settings getSettings() {

		Yaml yaml = new Yaml(new Constructor());

		Settings settings = new Settings();

		settings.setItems(new ArrayList<Item>());

		try (InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("sqlgen.yml");
				InputStreamReader reader = new InputStreamReader(in, "utf-8")) {

			Map<String, Object> yamlMap = yaml.load(in);

			Map<String, Object> settingsMap = (Map<String, Object>) yamlMap.get("settings");

			for (Map<String, Object> itemMap : (List<Map<String, Object>>) settingsMap.get("items")) {
				Item item = new Item();
				BeanUtils.copyProperty(item, "tableName", itemMap.get("table-name"));
				BeanUtils.copyProperty(item, "tableComment", itemMap.get("table-comment"));
				BeanUtils.copyProperty(item, "columnName", itemMap.get("column-name"));
				BeanUtils.copyProperty(item, "query", itemMap.get("query"));
				settings.getItems().add(item);
			}

		} catch (IOException | IllegalAccessException | InvocationTargetException e) {
			throw new ApplicationException(e.getMessage(), e);
		}

		return settings;

	}

	/**
	 * ＤＢコネクション取得
	 * @return
	 */
	private Connection getConnection() {

		try {
			Class.forName(driver);

			return DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException e) {
			throw new ApplicationException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new ApplicationException(e.getMessage(), e);
		}

	}

	/**
	 * 検索結果をファイルへ出力する。
	 *
	 * @param item
	 * @throws Exception
	 */
	private void outputFile(Item item) throws Exception {

		// 出力フォルダ作成
		File outputDir = new File(getOutputDir(),
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

			//			// データ出力
			//			genericDao.selectAll(item.getQuery(), stream -> {
			//				stream.forEach(entity -> {
			//					String[] array = entity.values().stream().map(mapper -> {
			//						if (mapper != null) {
			//							return mapper.toString();
			//						}
			//						return dump.getNullValue();
			//					}).toArray(String[]::new);
			//					csvWriter.writeNext(array);
			//				});
			//
			//				return null;
			//			});

			csvWriter.close();

		}

	}

}
