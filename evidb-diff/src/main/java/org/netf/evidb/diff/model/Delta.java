package org.netf.evidb.diff.model;

import lombok.Data;

@Data
public class Delta {
	private String name;
	private int before;
	private int after;
	private int create;
	private int update;
	private int delete;
}
