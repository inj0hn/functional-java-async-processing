package com.example.demo.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class Message {
	private final Date timestamp;
	private final String body;
	private final String level;

	public Message(String text) {
		String[] fields = Arrays.stream(StringUtils.split(text, "|"))
			.map(StringUtils::strip)
			.toArray(String[]::new);
		level = fields[0];
		body = fields[4];
		timestamp = Date.from(LocalDateTime.from(DateTimeFormatter.ISO_DATE_TIME.parse(fields[1]))
				.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public String getBody() {
		return body;
	}
	
	public String getLevel() {
		return level;
	}

}
