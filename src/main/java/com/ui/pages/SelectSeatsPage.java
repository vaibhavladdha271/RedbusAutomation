package com.ui.pages;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.utility.BrowserUtility;

public class SelectSeatsPage extends BrowserUtility{
	
	public SelectSeatsPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	private static By seatContainerLocator = By.xpath("//div[@data-autoid='seatContainer']");
	private static By seatLocator = By.xpath(".//span[contains(@class,'sleeper__ind-seat-styles')]");
	private static By soldSeatsLocator=By.xpath("./span[contains(@class,'sleeperSold')]");
	private static By seatPriceLocator=By.xpath("./span[contains(@class,'selectedSleeperPrice')]");
	private static By countOfSeatsLocator=By.xpath("//span[contains(@class,'seatCount')]");
	private static By priceWrapperLocator = By.xpath("//div[contains(@class,'priceWrap__ind-seat-styles')]");
	private static By priceButtonLocator = By.xpath("//div[contains(@class,'priceWrap') and @role='button']");
	private static By priceLocator = By.xpath("//span[contains(@class,'finalPrice')]");
	private static By priceBreakUpPopupLocator = By.xpath("//div[contains(@class,'bottomSheetContainer')]");
	private static By priceBreakupPopupCloseButtonLocator = By.xpath("//button[contains(@class,'actionButton')]");
	private static By selectBoardingOrDroppingPointLocator = By.xpath(
			"//div[contains(@class,'priceWrap__ind-seat-styles')]//button[contains(@class,'primaryButton')]");

	public SelectSeatsPage selectSeats(List<String> seats, WebDriver driver) {
		AtomicInteger totalPrice = new AtomicInteger(0);
		WebElement seatContainer = waitForVisibilityOfElementLocated(driver, seatContainerLocator);
		List<WebElement> seatsList = seatContainer.findElements(seatLocator);
		seatsList.stream()
				.filter(seat -> seat.findElements(soldSeatsLocator).isEmpty())
				.filter(seat -> seats.contains(seat.getAttribute("id"))).forEach(seat -> {
					System.out.println("Clicking on available seat" + seat.getAttribute("id"));
					seat.click();
					WebElement seatPrice = seat
							.findElement(seatPriceLocator);
					int price = Integer.parseInt(seatPrice.getText().replaceAll("[^0-9]", ""));
					totalPrice.addAndGet(price);
					System.out.println("Seat ID: " + seat.getAttribute("id") + "|" + "Seat price: " + price);
				});
		System.out.println("Total price for selected seats: " + totalPrice);
		return new SelectSeatsPage(getDriver());
	}
	
	public int verifySelectedSeatsCount(WebDriver driver) {
		int countOfSeat = Integer.parseInt(getSeatCount(driver));
		return countOfSeat;
	}
	public String getSeatCount(WebDriver driver) {
		WebElement seatCount = driver.findElement(countOfSeatsLocator);
		List<String> seatCountDetails = Arrays.asList(seatCount.getText().split(" "));
		return seatCountDetails.get(0);
	}
	
	public SelectSeatsPage clickOnSelectBoardingAndDroppingPointsButton(WebDriver driver) {
//		WebElement priceWrapperContainer = driver.findElement(priceWrapperLocator);
//		WebElement priceBreakUpButton =priceWrapperContainer.findElement(priceButtonLocator);
		WebElement selectBoardingOrDroppingPointButton = waitForElementToBeClickable(selectBoardingOrDroppingPointLocator);
		clickOnElementUsingActions(driver, selectBoardingOrDroppingPointButton);
		return new SelectSeatsPage(getDriver());
	}
	
	public String verifyPriceBreakPopup(WebDriver driver) {
		clickOnWebElement(driver, priceButtonLocator);
		WebElement priceBreakUpContainer = waitForVisibilityOfElementLocated(driver, priceBreakUpPopupLocator);
		String price = priceBreakUpContainer.findElement(priceLocator).getText();
		WebElement closeButton = priceBreakUpContainer.findElement(priceBreakupPopupCloseButtonLocator);
		clickOnElementUsingActions(driver, closeButton);
		return price;
	}
}
    