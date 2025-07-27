package com.ui.tests;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.constants.Browser;
import com.ui.pages.HomePage;
import com.ui.pojo.BusData;
import com.utility.LoggerUtility;

@Listeners(com.ui.listeners.TestListener.class)
public class HomePageTest extends TestBase {
	
	@Test(dataProviderClass=com.ui.dataproviders.TestDataProvider.class,dataProvider="BusSearchData",description="Verify if search bus functionality is working,filter is applied & view bus popup is opened",groups= {"e2e","sanity"})
	public void searchBusUsingData(BusData busData) {
//		List<String> filterList = Arrays.asList("Primo Bus", "AC", "SLEEPER");
		logger.info("Started red bus application");
	    homePage.clickOnSourceOrDestinationField()
	    .selectLocation(busData.getSourceLocation())
		.selectLocation(busData.getDestinationLocation())
		.selectDate(busData.getDate())
		.clickOnSearchBusButton()
		.selectFilter(busData.getFilters())
		.clickOnViewSeatsButton(busData.getBusName(), busData.getBoardingTime());
	    logger.info("View seats button is clicked");
	}
	@Test(description="Verify the price after selecting seats & number of seats selected",groups= {"sanity"},dataProviderClass=com.ui.dataproviders.TestDataProvider.class,dataProvider="BusSearchData")
	public  void verifyPriceAfterSelectingSeats(BusData busData)
	{
		List<String> filterList = Arrays.asList("Primo Bus", "AC", "SLEEPER");
		List<String> seats = Arrays.asList("15", "16");
		int countOfSeats=homePage.clickOnSourceOrDestinationField()
				.selectLocation(busData.getSourceLocation())
		.selectLocation(busData.getDestinationLocation()).selectDate(busData.getDate())
		.clickOnSearchBusButton()
		.selectFilter(busData.getFilters())
		.clickOnViewSeatsButton(busData.getBusName(), busData.getBoardingTime())
		.selectSeats(busData.getSeats())
		.verifySelectedSeatsCount();
		Assert.assertEquals(countOfSeats, seats.size());
	}

}
