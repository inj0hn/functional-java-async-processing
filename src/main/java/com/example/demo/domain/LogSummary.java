package com.example.demo.domain;

public class LogSummary {

	private long debugCount;
	private long infoCount;
	private long errorCount;
	
	public long getDebugCount() {
		return debugCount;
	}
	public void setDebugCount(long debugCount) {
		this.debugCount = debugCount;
	}
	public long getInfoCount() {
		return infoCount;
	}
	public void setInfoCount(long infoCount) {
		this.infoCount = infoCount;
	}
	public long getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(long errorCount) {
		this.errorCount = errorCount;
	}
	
}
