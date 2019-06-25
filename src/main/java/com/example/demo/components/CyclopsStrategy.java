package com.example.demo.components;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.domain.LogSummary;
import com.example.demo.domain.Message;

import cyclops.control.Future;

/**
 * Cyclops library implementation. 
 * Use {@link cyclops.control.Future} instead of {@link CompletableFuture}.
 * 
 * https://medium.com/@johnmcclean/dysfunctional-programming-in-java-6-no-locks-or-synchronized-blocks-ea5a47644689
 * 
 * @author Arthur Tolentino
 *
 */
public class CyclopsStrategy implements ProcessStrategy {
	
	private static final Logger LOG = LoggerFactory.getLogger(CyclopsStrategy.class);
	
	private final ExecutorService executor;
	
	public CyclopsStrategy(int threadCount) {
		executor = Executors.newFixedThreadPool(threadCount);
	}

	@Override
	public LogSummary process(List<String> lines) {
		// create Future objects that will asynchronously convert each string into a Message
		List<Future<Message>> messages = lines
				.stream()
				.map(this::toFuture)
				.collect(Collectors.toList());
		
		// TODO Auto-generated method stub
		return null;
	}
	
	private Future<Message> toFuture(String text) {
		return Future.of(() ->  new Message(text), executor);
	}

}
