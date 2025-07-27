package com.ui.listeners;

import java.util.Arrays;

import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.ui.tests.TestBase;
import com.utility.BrowserUtility;
import com.utility.ExtentReporterUtility;
import com.utility.LoggerUtility;

public class TestListener implements ITestListener {
	
	Logger logger=LoggerUtility.getLogger(this.getClass());
	
	public void onTestStart(ITestResult result) {
		logger.info(result.getMethod().getMethodName());
		logger.info(result.getMethod().getDescription());
		logger.info(Arrays.toString(result.getMethod().getGroups()));
		ExtentReporterUtility.createExtentTest(result.getMethod().getMethodName());
	}

	public void onTestSuccess(ITestResult result) {
		logger.info(result.getMethod().getMethodName()+ " "+"Passed");
		ExtentReporterUtility.getTest().log(Status.PASS,result.getMethod().getMethodName());
	}

	public void onTestFailure(ITestResult result) {
		logger.error(result.getMethod().getMethodName()+ " "+"FAILED");
		logger.error(result.getThrowable().getMessage());
		ExtentReporterUtility.getTest().log(Status.FAIL,result.getMethod().getMethodName());
		ExtentReporterUtility.getTest().log(Status.FAIL,result.getThrowable().getMessage());
		Object testClass=result.getInstance();
		BrowserUtility browserUtility=((TestBase)testClass).getInstance();
		logger.info("Cpaturing screenshot for failed test");
		String screenshotPath=browserUtility.takeScreenshot(result.getMethod().getMethodName());
		logger.info("Attaching screenshot to HTML file");
		ExtentReporterUtility.getTest().addScreenCaptureFromBase64String(screenshotPath);
	}

	public void onTestSkipped(ITestResult result) {
		logger.warn(result.getMethod().getMethodName());
		ExtentReporterUtility.getTest().log(Status.SKIP ,result.getMethod().getMethodName());
	}

	public void onStart(ITestContext context) {
		logger.info("Test suite started");
		ExtentReporterUtility.setupExtentReports("report");
	}

	public void onFinish(ITestContext context) {
		logger.info("Test suite completed");
		ExtentReporterUtility.flushReport();
	}
}
