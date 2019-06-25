package com.example.demo.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.domain.LogSummary;


@Component
public class LogProcessor {
	
	@Autowired
	private ProcessStrategy processStrategy;
	
	
	/**
	 * Returns a {@link LogSummary}. Creates the contents
	 * of the object from the input string list.
	 * 
	 * @param lines the contents of the log
	 * @return log summary object
	 */
	public LogSummary process(List<String> lines) {
		return processStrategy.process(lines);
		
	}
}
