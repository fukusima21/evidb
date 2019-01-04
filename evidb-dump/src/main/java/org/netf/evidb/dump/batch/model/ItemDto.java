package org.netf.evidb.dump.batch.model;

import java.util.Map;

import lombok.Data;

@Data
public class ItemDto {
	private String tableName;
	private String tableComment;
	private Map<String, String> columnName;
	private String query;
}
