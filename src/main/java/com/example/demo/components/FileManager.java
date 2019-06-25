package com.example.demo.components;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(FileManager.class);
	
	/**
	 * Returns an input stream of the file. The user is responsible for
	 * closing the stream returned.
	 * 
	 * @param file the source file
	 * @return may return an empty optional if the file does not exist
	 */
	public Optional<InputStream> read(File file) {
		Optional<InputStream> stream = Optional.empty();
		if(file != null) {
			try {
				stream = Optional.ofNullable(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				LOG.error("File not found {}", file.getAbsolutePath());
			}
		}
		return stream;
	}
}
