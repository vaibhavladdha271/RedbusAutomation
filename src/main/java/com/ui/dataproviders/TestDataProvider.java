package com.ui.dataproviders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.DataProvider;

import com.google.gson.Gson;
import com.ui.pojo.BusData;
import com.ui.pojo.TestData;

public class TestDataProvider {

	public static TestData getUserData(String fileName) {
		Gson gson = new Gson();
		File testFile = new File(System.getProperty("user.dir") +"\\testData\\" + fileName + ".json");
		FileReader reader = null;
		try {
			reader = new FileReader(testFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TestData testData = gson.fromJson(reader, TestData.class);
		return testData;
	}
	
	@DataProvider(name="BusSearchData")
	public Object[][] busSearchData(){
		TestData testData=TestDataProvider.getUserData("testData");
		List<BusData> busDataList=testData.getData();
		List<Object[]> dataProviderList=new ArrayList<>();
		busDataList.forEach(busData->dataProviderList.add(new Object[] {busData}));
		return dataProviderList.toArray(new Object[0][]);
 	}
}
