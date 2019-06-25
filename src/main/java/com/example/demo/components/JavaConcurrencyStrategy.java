package com.example.demo.components;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.domain.LogSummary;
import com.example.demo.domain.Message;

/**
 * Java concurrency API implementation using {@link CompletableFuture}
 * 
 * @author Arthur Tolentino
 *
 */
public class JavaConcurrencyStrategy implements ProcessStrategy {
	
	private static final Logger LOG = LoggerFactory.getLogger(JavaConcurrencyStrategy.class);
	
	private final ExecutorService executor;
	
	public JavaConcurrencyStrategy(int threadCount) {
		executor = Executors.newFixedThreadPool(threadCount);
	}

	@Override
	public LogSummary process(List<String> lines) {
		try {
			return createCompletableFuture(lines).get();
		} catch (InterruptedException | ExecutionException e) {
			LOG.error("Failed to process asynchronously", e);
			if(e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
		}
		return null;
	}
	
	/**
	 * Creates a {@link CompletableFuture} that will execute asynchronous tasks in
	 * parallel to one another. Uses worker threads from {@link #executor}. 
	 * 
	 * After all the tasks have run by invoking {@link CompletableFuture#get()},
	 * it will consolidate the results of each task to build a {@link LogSummary}.
	 *  
	 * @param lines the log contents
	 * @return completable future that returns a log summary object
	 */
	private CompletableFuture<LogSummary> createCompletableFuture(List<String> lines) {
		// creates a {@link CompletableFuture} for each line of the log.
		// each task will return a {@link Message} as its result.
		List<CompletableFuture<Message>> messages = lines
				.stream()
				.map(this::toCompletableFuture)
				.collect(Collectors.toList());

		
		// each task will run in parallel to one another.
		return CompletableFuture
			.allOf(messages.toArray(new CompletableFuture[messages.size()]))	// run all future objects in parallel
			.thenApply(voidResult -> {											// allOf() method returns Completable<Void>
				return messages.stream()
						.map(CompletableFuture::join)							// collate the results when all futures are complete.
																				// will throw an unchecked exception when the underlying 
																				// future completes exceptionally.
						.filter(Objects::nonNull)
						.collect(Collectors.toList());							// returns List<Message>
			})
			.thenApply(this::buildSummary);										// convert into LogSummary
	}
	
	/**
	 * Creates a completable future that returns a {@link Message}
	 * 
	 * @param text the log line
	 * @return completable future
	 */
	private CompletableFuture<Message> toCompletableFuture(String text) {
		return CompletableFuture.supplyAsync(() -> new Message(text), executor)
				.exceptionally(throwable -> {
					LOG.error("Failed to convert string to Message. {}", text, throwable);
					return null;
				});
	}
	
	/**
	 * Creates a {@link LogSummary} by consolidating all {@link Message} objects
	 * 
	 * @param messages the result of all completable future objects
	 * @return log summary
	 */
	private LogSummary buildSummary(List<Message> messages) {
		LogSummary summary = new LogSummary();
		summary.setDebugCount(countMessages(messages, "debug"));
		summary.setInfoCount(countMessages(messages, "info"));
		summary.setErrorCount(countMessages(messages, "error"));
		return summary;
	}
	
	
	private Long countMessages(List<Message> messages, String level) {
		return messages
				.stream()
				.filter(message -> StringUtils.equalsIgnoreCase(level, message.getLevel()))
				.count();
	}
}
