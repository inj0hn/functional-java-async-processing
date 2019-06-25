package com.example.demo.components;

import java.util.List;

import com.example.demo.domain.LogSummary;

public interface ProcessStrategy {

	/**
	 * Creates a {@link LogSummary} from the log contents
	 * 
	 * @param lines the log contents
	 * @return log summary of the log contents
	 */
	LogSummary process(List<String> lines);
}
