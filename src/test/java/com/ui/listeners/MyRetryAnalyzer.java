package com.ui.listeners;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class MyRetryAnalyzer implements IRetryAnalyzer {

	private static final int MAX_ATTEMPTS=3;
	private static int currentAttempts;
	@Override
	public boolean retry(ITestResult result) {
		if(currentAttempts<MAX_ATTEMPTS)
		{
			currentAttempts++;
			return true;
		}
		else
		return false;
	}
}
