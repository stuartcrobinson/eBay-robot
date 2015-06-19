package helloworld;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class test2 {
	public static void main2(String[] args) {

		WebDriver driver = new HtmlUnitDriver();

		List<String> words = new ArrayList<String>();

		System.out.println(getAmazonPrice(driver, words));
		System.out.println("WRONG PROGRAM");

	}

	public static String getAmazonPrice(WebDriver driver, List<String> keywords){


		//		String url = "http://www.amazon.com/s/keywords="; // Apple \"iPhone+5c\"  Verizon 16GB ";
		//		for (String word : keywords){
		//			url += " \"" + word + "\" ";
		//		}

		//		String url = "http://www.amazon.com/s/keywords= \"Apple\"  \"iPhone 5\" \"Verizon\"  \"16GB\"";
//		WebDriver driver = new HtmlUnitDriver();
		String url = "http://www.amazon.com/s/keywords=%20%22Apple%22%20%22iPhone%205%22%20%22Verizon%22%20%2216GB%22";
		System.out.println(url);

		driver.get(url);
		System.out.println(driver.getCurrentUrl());
		boolean found = false;
		List<WebElement> wes = null;
		while (!found){ 

//			List<WebElement> wes = driver.findElements(By.xpath("//*[contains(@id,'result')]/div/div/div/div[2]"));
//			List<WebElement> wes = driver.findElements(By.cssSelector(".a-col-right"));
			driver = new HtmlUnitDriver();
			driver.get(url);
			wes = driver.findElements(By.cssSelector(".a-col-right"));
			System.out.println(wes.size() + " elements found.");
			if (wes.size() > 0)
				found = true;
//			else
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {}
		}

		String patternString = "(.*)\\$([\\d+\\.]*)used\\((\\d+) offer(.*)";

		for (WebElement we : wes){

			String text = we.getText();
//			String title =  we.findElement(By.xpath("div/a")).getText();
			String title =  we.findElement(By.cssSelector(".s-access-title")).getText();
			//.s-access-title
			System.out.println(title + "\n____\n" + text + "\n-----\n\n");
			//			boolean skipThis = false;
			//			for (String keyword : keywords){
			//				if (!title.contains(keyword)){
			//					skipThis = true;
			//					break;
			//				}
			//			}
			//			if (skipThis) continue;
			Pattern pattern = Pattern.compile(patternString);
			Matcher m = pattern.matcher(text);
			System.out.println();
			//			System.out.println("Title:      " + title);
			if (m.find( ) ){
				System.out.println("Used price: " + m.group(2) );
				System.out.println("Quantity:   " + m.group(3) );
				return m.group(2);
			}else 
				System.out.println("NO MATCH.");
		}

		return null;


	}


}

//
//
//
//words = new ArrayList<String>();
//words.add("Apple");
//words.add("iPhone 5c");
//words.add("Verizon");
//words.add("16GB");
//
//System.out.println(getAmazonPrice(driver, words));
//
//words = new ArrayList<String>();
//words.add("Apple");
//words.add("iPhone 5s");
//words.add("Verizon");
//words.add("16GB");
//
//System.out.println(getAmazonPrice(driver, words));
//
//words = new ArrayList<String>();
//words.add("Apple");
//words.add("iPhone 5");
//words.add("Verizon");
//words.add("32GB");
//
//System.out.println(getAmazonPrice(driver, words));
//
//
//words = new ArrayList<String>();
//words.add("Apple");
//words.add("iPhone 5c");
//words.add("AT&T");
//words.add("32GB");
//
//System.out.println(getAmazonPrice(driver, words));
//
//words = new ArrayList<String>();
//words.add("Apple");
//words.add("iPhone 5c");
//words.add("Verizon");
//words.add("32GB");
//
//System.out.println(getAmazonPrice(driver, words));
//
//
//words = new ArrayList<String>();
//words.add("Apple");
//words.add("iPhone 5c");
//words.add("Verizon");
//words.add("64GB");
//
//System.out.println(getAmazonPrice(driver, words));
//
//
//words = new ArrayList<String>();
//words.add("Apple");
//words.add("iPhone 5c");
//words.add("AT&T");
//words.add("16GB");
//
//System.out.println(getAmazonPrice(driver, words));
//
//words = new ArrayList<String>();
//words.add("Apple");
//words.add("iPhone 5s");
//words.add("Verizon");
//words.add("32GB");
//
//System.out.println(getAmazonPrice(driver, words));
//
//words = new ArrayList<String>();
//words.add("Apple");
//words.add("iPhone 6");
//words.add("AT&T");
//words.add("32GB");
//
//System.out.println(getAmazonPrice(driver, words));
//
//words = new ArrayList<String>();
//words.add("Apple");
//words.add("iPhone 6");
//words.add("Verizon");
//words.add("32GB");
//
//System.out.println(getAmazonPrice(driver, words));
