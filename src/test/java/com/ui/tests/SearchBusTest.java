package com.ui.tests;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.constants.Browser;
import com.ui.pages.HomePage;
import com.ui.pages.SearchBusesPage;
import com.ui.pages.SelectSeatsPage;
import com.ui.pojo.BusData;
import com.ui.pojo.TestData;
import com.ui.dataproviders.*;

public class SearchBusTest {
	HomePage homePage;
	@BeforeMethod(description="load the homepage of website")
	public void setup()
	{
		homePage=new HomePage(Browser.CHROME);
		homePage.maximizeWindow();
	}
	
	@DataProvider(name="BusSearchData")
	public Object[][] busSearchData(){
		TestData testData=TestDataProvider.getUserData("testData");
		List<BusData> dataList=testData.getData();
		Object[][] provider=new Object[dataList.size()][6];
		for(int i=0;i<dataList.size();i++)
		{
			BusData record=dataList.get(i);
			provider[i][0]=record.getFilters();
			provider[i][1]=record.getSourceLocation();
			provider[i][2]=record.getDestinationLocation();
			provider[i][3]=record.getDate();
			provider[i][4]=record.getBusName();
			provider[i][5]=record.getBoardingTime();
		}
		return provider;
 	}
	
	@Test(dataProvider="BusSearchData",description="Verify if search bus functionality is working,filter is applied & view bus popup is opened",groups= {"e2e","sanity"})
	public void searchBus(List<String> filters,String sourceLocation,String destinationLocation,String date,String busName,String boardingTime) {
//		List<String> filterList = Arrays.asList("Primo Bus", "AC", "SLEEPER");
	    homePage.clickOnSourceOrDestinationField(homePage.getDriver())
	    .selectLocation(homePage.getDriver(),sourceLocation)
		.selectLocation(homePage.getDriver(), destinationLocation)
		.selectDate(date, homePage.getDriver())
		.clickOnSearchBusButton(homePage.getDriver())
		.selectFilter(homePage.getDriver(),filters)
		.clickOnViewSeatsButton(homePage.getDriver(), busName, boardingTime);
	}
	
	@Test(description="Verify the price after selecting seats & number of seats selected",groups= {"sanity"})
	public  void verifyPriceAfterSelectingSeats(List<String> filters,String sourceLocation,String destinationLocation,String date,String busName,String boardingTime)
	{
		List<String> filterList = Arrays.asList("Primo Bus", "AC", "SLEEPER");
		List<String> seats = Arrays.asList("15", "16");
		int countOfSeats=homePage.clickOnSourceOrDestinationField(homePage.getDriver())
				.selectLocation(homePage.getDriver(),"Mumbai")
		.selectLocation(homePage.getDriver(), "Pune").selectDate("26", homePage.getDriver())
		.clickOnSearchBusButton(homePage.getDriver())
		.selectFilter(homePage.getDriver(),filterList)
		.clickOnViewSeatsButton(homePage.getDriver(), "Dolphin travel house", "17:50")
		.selectSeats(seats, homePage.getDriver())
		.verifySelectedSeatsCount(homePage.getDriver());
		Assert.assertEquals(countOfSeats, seats.size());
	}

}
