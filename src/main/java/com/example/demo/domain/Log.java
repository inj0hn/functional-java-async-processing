package com.example.demo.domain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cyclops.function.Memoize;

public class Log {
	
	private static final Logger LOG = LoggerFactory.getLogger(Log.class);

	private final Path filePath;

	/**
	 *  Initial implementation 'messages' field implementation.
	 *  
	 *  private volatile List<String> messages;
	 *  
	 *  public List<String> getMessages() {
	 *  	if(messages == null) {
	 *  		messages = load();
	 *  	}
	 *  	return messages;
	 *  }
	 */
	
	// caches the result of the supplier, making 'messages' immutable and thread-safe
	// The LazyImmutable object is designed to be shared across multiple threads
	private final Supplier<List<String>> messageSupplier = Memoize.memoizeSupplier(this::load);
	
	// this achieves the same thing as line 34
	// private final Eval<List<Message>> evalMessages = Eval.later(this::load);  
	

	/**
	 * 
	 * @param filePath log file path
	 */
	public Log(Path filePath) {
		this.filePath = filePath;
	}
	
	public List<String> getMessages() {
		// lazily load the messages
		// same as return evalMessages.get();
		return messageSupplier.get();
	}
	
	private List<String> load() {
		LOG.info("Loading {}", filePath);
		List<String> contents = null;
		try(BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
			contents = reader.lines().collect(Collectors.toList());
		} catch (IOException e) {
			LOG.error("Failed to load file",e);
			contents = new ArrayList<>();
		}
		return contents;
	}
}
