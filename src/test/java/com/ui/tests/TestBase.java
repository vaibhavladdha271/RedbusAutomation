package com.ui.tests;

import static com.constants.Browser.*;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;

import com.constants.Browser;
import com.ui.pages.HomePage;
import com.utility.BrowserUtility;
import com.utility.LambdaTestUtility;
import com.utility.LoggerUtility;

public class TestBase {

	Logger logger=LoggerUtility.getLogger(this.getClass());
	public HomePage homePage;
	private boolean isLambdaTest=false;
	private boolean isHeadless=false;
	
	@Parameters({"browser","isLambdaTest","isHeadless"})
	@BeforeMethod(description="load the homepage of website")
	public void setup(@Optional("chrome") String browser,@Optional("false") boolean isLambdaTest,@Optional("false")boolean isHeadless, ITestResult result)
	{
		this.isLambdaTest=isLambdaTest;
		this.isHeadless=isHeadless;
		WebDriver lambdaDriver;
		if(isLambdaTest)
		{
			lambdaDriver=LambdaTestUtility.initializeLambdaTestSession("chrome", result.getMethod().getMethodName());
			homePage=new HomePage(lambdaDriver);
		}
		else
		{
		logger.info("Load redbus homepage");
		homePage=new HomePage(Browser.valueOf(browser.toUpperCase()),isHeadless);
		homePage.maximizeWindow();
		}
	}
	
	public BrowserUtility getInstance() {
		return homePage;
	}
	
	@AfterMethod
	public void tearDown() {
		if(isLambdaTest) {
			LambdaTestUtility.quitSession();
		}
		else
		{
			homePage.quitSession();
		}
	}
}
