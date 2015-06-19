package helloworld;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.ebay.services.finding.AspectFilter;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.PaginationInput;
import com.ebay.services.finding.SortOrderType;
import com.google.common.collect.Lists;

public class It3m{



	public String originalKeywords;
	public String keywords;
	public String keywordNegators;
	public String categoryCode;
	public List<AvoidanceTerm> avoidanceTerms;
	public List<IFilter> itemFilters; 
	public List<AFilter> aspectFilters;

	public List<String> amazonKeywords;
	public String ebayKeywords;

	public String amazon_low_price;
	public double ebay_max_price;
	public String ebayApiPrice;

	public It3m(){}

	public It3m(String keywords, String keywordNegators, String categoryCode, List<AvoidanceTerm> avoidanceTerms,
			List<IFilter> itemFilters, List<AFilter> aspectFilters){
		if (itemFilters == null) itemFilters = new ArrayList<IFilter>();
		if (aspectFilters == null) aspectFilters = new ArrayList<AFilter>();

		keywords = keywords.toLowerCase();
		keywordNegators = keywordNegators.toLowerCase();

		this.itemFilters = itemFilters;
		this.aspectFilters = aspectFilters;

		this.avoidanceTerms = avoidanceTerms;

		ebayKeywords = "";
		amazonKeywords = new ArrayList<String>();

		if (!(this.aspectFilters == null || this.aspectFilters.isEmpty())){
			boolean gbSet = false;
			for (AFilter af : this.aspectFilters){
				ebayKeywords += " \"" + af.value + "\" ";
				amazonKeywords.add(af.value.toLowerCase());		
				if (af.name.equals("Storage Capacity"))
					gbSet = true;
			}
			if (!gbSet)
				amazonKeywords.add("16gb");		//because if you don't search for specific gb in amazon, sometimes the better more expensive option is listed first
		}
		else{
			amazonKeywords = new ArrayList<String>(Arrays.asList(keywords.split(" ")));
		}

		this.originalKeywords = keywords;
		this.keywordNegators = keywordNegators;

		this.keywords = ebayKeywords + " " + keywords;
		this.categoryCode = categoryCode;

		//		System.out.println("#############################<");
		//		System.out.println(Arrays.asList(keywords.split(" ")));
		//		System.out.println(this.amazonKeywords);
		//		System.out.println(this.originalKeywords);
		//		System.out.println(this.keywordNegators);
		//		System.out.println(this.keywords);
		//		System.out.println("#############################>");

	}



	//	new String[][]{ 
	//			new String[]{"Model", "iPhone 4s"},
	//			new String[]{"Carrier", "AT&T", "Verizon"},
	//			new String[]{"Color", "", "Black", "White"},
	//			new String[]{"Storage Capacity", "16GB", "32GB", "64GB"}}
	//	));
	//  http://stackoverflow.com/questions/15868914/how-to-get-2d-array-possible-combinations
	//  https://community.oracle.com/message/5244006
	public List<It3m> listOfIt3msExtendedFromTemplate(String[][] twoDimStringArray){

		List<It3m> returner = new ArrayList<It3m>();

		int sizeArray[] = new int[twoDimStringArray.length];

		int counterArray[] = new int[twoDimStringArray.length];

		for (int i = 0; i < counterArray.length; i++)
			counterArray[i] = 1;

		int totalCombinationCount = 1;
		for(int i = 0; i < twoDimStringArray.length; ++i) {
			sizeArray[i] = twoDimStringArray[i].length;
			totalCombinationCount *= (twoDimStringArray[i].length - 1);
		}

		for (int countdown = totalCombinationCount; countdown > 0; --countdown) {
			List<AFilter> it3m_aspectFilters = new ArrayList<AFilter>(this.aspectFilters); // = new ArrayList<AFilter>(this.aspectFilters);

			for(int i = 0; i < twoDimStringArray.length; ++i) {
				if (! twoDimStringArray[i][counterArray[i]].equals("")){
					it3m_aspectFilters.add(new AFilter(twoDimStringArray[i][0], twoDimStringArray[i][counterArray[i]]));
				}
			}

			String it3m_keywords = this.keywords;
			String it3m_keywordNegators = this.keywordNegators;
			String it3m_categoryCode = this.categoryCode;
			List<AvoidanceTerm> it3m_avoidanceTerms =  new ArrayList<AvoidanceTerm>(this.avoidanceTerms);
			List<IFilter> it3m_itemFilters = new ArrayList<IFilter>(this.itemFilters);

			It3m it3m = new It3m(it3m_keywords, it3m_keywordNegators, it3m_categoryCode, it3m_avoidanceTerms, it3m_itemFilters, it3m_aspectFilters);
			//			System.out.println("herehere");	//TODO
			//			System.out.println(it3m.ebayKeywords);
			//			System.exit(0);

			returner.add(it3m);

			for(int incIndex = twoDimStringArray.length - 1; incIndex >= 0; --incIndex) {
				if(counterArray[incIndex] + 1 < sizeArray[incIndex]) {
					++counterArray[incIndex];
					break;
				}
				counterArray[incIndex] = 1;
			}
		}
		//		System.out.println("hereherehere");	//TODO
		//		System.out.println(returner.size());	//TODO
		//		System.exit(0);
		return returner;
	}

	public String getAmazonSearchURL() {

		String url = "http://www.amazon.com/s/keywords="; // Apple \"iPhone+5c\"  Verizon 16GB ";
		for (String word : amazonKeywords){
			if (word.contains(" "))
				url += " \"" + word.replace("&", "") + "\" ";	//%26 ??
			else
				url += " " + word.replace("&", "") + " ";	//%26 ??

		}

		return url;
	}
	public ItemFilter get_filter_end_time(int minutesFromNow){


		TimeZone timeZone = TimeZone.getTimeZone("GMT");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		sdf.setTimeZone(timeZone);
		Calendar cal = new GregorianCalendar(timeZone);
		cal.add(Calendar.MINUTE, minutesFromNow);

		ItemFilter filter_end_time = new ItemFilter();
		filter_end_time.setName(ItemFilterType.END_TIME_TO);
		filter_end_time.getValue().add(sdf.format(cal.getTime()));	//GMT is EST + 4 

		return filter_end_time;
	}

	public FindItemsAdvancedRequest getRequest(int minutesFromNow_auctionEnd, int pagination){

		FindItemsAdvancedRequest request = new FindItemsAdvancedRequest();

		request.setDescriptionSearch(true);


		request.setKeywords(this.keywords);//Helper.getAvoidanceKeywordAppendage(avoidanceTerms));

		request.getCategoryId().add(this.categoryCode);  //9355 - cells and smartphones


		//		for (AFilter afilter : this.aspectFilters){
		//			try {
		//				AspectFilter af1 = new AspectFilter();
		//				af1.setAspectName(afilter.name);
		//				af1.getAspectValueName().add(afilter.value);
		//				request.getAspectFilter().add(af1);
		//			}catch (Exception e){System.out.println("aspectfilter exception"); break;}
		//		}
		for (IFilter ifilter : this.itemFilters){
			try {
				ItemFilter if1 = new ItemFilter();
				if1.setName(ifilter.name);
				for (String value : ifilter.values){
					if1.getValue().add(value);
				}
				request.getItemFilter().add(if1);
			}catch (Exception e){System.out.println("aspectfilter exception"); break;}
		}

		ItemFilter if1 = new ItemFilter();
		if1.setName(ItemFilterType.MAX_PRICE);
		if1.getValue().add(String.format("%.2f", this.ebay_max_price));
		request.getItemFilter().add(if1);


		request.getItemFilter().add(this.get_filter_end_time(minutesFromNow_auctionEnd));

		PaginationInput pi = new PaginationInput();
		pi.setEntriesPerPage(pagination);
		request.setPaginationInput(pi);

		request.setSortOrder(SortOrderType.END_TIME_SOONEST);

		return request;
	}

	public boolean hasCleanDescription(String description, String title) {

		try {
			if (description.equals(null) || description.isEmpty())
				return true;
		}catch(Exception e2){return true;}

		boolean isTitle = false;
		if (description.equals(title))
			isTitle = true;

		title = title.toLowerCase();
		description = description.toLowerCase();
		description = description.replaceAll("&nbsp;", " ");
		description = description.replaceAll("<body>", "BODYHTMLSTARTBODY");
		description = description.replaceAll("</body>", "BODYHTMLENDBODY");	//doesn't work :(
		description = description.replaceAll("'", "");
		description = description.replaceAll("\\W", "_");
		description = description.replaceAll("_", " ");
		description = description.trim();
		while (description.contains("  ")){
			description = description.replaceAll("  ", " ");
		}

		description = description.replaceAll("(.*)BODYHTMLSTARTBODY", "");
		description = description.replaceAll("BODYHTMLENDBODY(.*)", "");	//doesn't work :(
		//		System.out.println("!prepared description!");
		//		System.out.println(description);
		String[] words = description.split("\\W");

		//		System.out.println("!words!");
		//		for (String word : words){
		//			System.out.print("*"+word + "* ");
		//		}
		//		System.out.println();

		if (isTitle){
			String[] titleBadWords = new String[]{
					"please", "read", "description", "photo", "pic", "picture", "broken", 
					" locked", "crack", "water", "t work", "work ", "does",
			"damage"};
			for (String word : titleBadWords){

				if (description.contains(word)){

					Tobor.out.println("found bad word " + word + " in title");
					return false;
				}
			}
		}

		boolean returner = true;
		for (int i = 0; i < words.length; i++){
			for (AvoidanceTerm term : avoidanceTerms){

				if (words[i].equals(term.badWord)){
					Tobor.out.println("found bad word " + words[i]);

					Boolean cancelItem = true;

					//					if ((term.badWord.equals("box") || term.badWord.equals("case")) && (title.contains("with") || title.contains("w\""))){
					//						Tobor.out.println("redeemed by with box/case case" );
					//						cancelItem = false;
					//						continue;
					//					}
					if ((term.badWord.equals("box") || term.badWord.equals("case")) && !isTitle){
						Tobor.out.println("redeemed by with box/case in nonTitle description" );
						cancelItem = false;
						continue;
					}

					try {
						if (!isTitle && (term.badWord.equals("imei") || term.badWord.equals("esn")) && 
								( 
										words[i+2].equals("clean") || words[i+2].equals("clear") ||
										words[i+3].equals("clean") || words[i+3].equals("clear") ||
										words[i+4].equals("clean") || words[i+4].equals("clear") ||
										words[i+5].equals("clean") || words[i+5].equals("clear") ||
										words[i+6].equals("clean") || words[i+6].equals("clear") ||
										words[i+7].equals("clean") || words[i+7].equals("clear") ||
										words[i+8].equals("clean") || words[i+8].equals("clear") ||
										words[i+9].equals("clean") || words[i+9].equals("clear") ||
										words[i+10].equals("clean") || words[i+10].equals("clear") ||
										words[i+11].equals("clean") || words[i+11].equals("clear") 
										)
								){
							Tobor.out.println("redeemed by clean/clear AFTER imei/esn" );
							cancelItem = false;
							continue;
						}
					}catch (Exception e){}



					try {
						for (String redeem : term.redemptionWords){
							if (words[i-1].equals(redeem)){
								Tobor.out.println("redeemed 1 by " + redeem );
								cancelItem = false;
								continue;
							}
							try {
								if (words[i-2].equals(redeem)){
									Tobor.out.println("redeemed 2 by " + redeem );
									cancelItem = false;
									continue;
								}
								try {
									if (words[i-3].equals(redeem)){
										Tobor.out.println("redeemed 3 by " + redeem );
										cancelItem = false;
										continue;
									}
									try {
										if (words[i-4].equals(redeem)){
											Tobor.out.println("redeemed 4 by " + redeem );
											cancelItem = false;
											continue;
										}

									}catch(Exception ee){}

								}catch(Exception ee){}

							}catch(Exception ee){}
						}
					} catch(Exception e){
						Tobor.out.println("not redeemed!");
						returner = false;
					}
					if (cancelItem){
						Tobor.out.println("not redeemed!");
						returner = false;
					}
				}

			}

		}


		return returner;


	}

	public void set_amazon_low_price(WebDriver driver) throws InterruptedException {

		//TODO -- needs to get CHEAPEST price on results.  sometimes PINK will come first and be more expensive
		//TODO -- iphone 6 - THERE IS A SPACE HERE "16 GB"
		//TODO -- if NO USED item listed, get CHEAPEST NEW ITEM litsed

		//TODO -- get CHEAPER of NEW and USED prices!  sometimes "USED" is $500 more than new!!!




		String url = getAmazonSearchURL();


		System.out.println("#############################<");
		System.out.println(Arrays.asList(keywords.split(" ")));
		System.out.println(this.amazonKeywords);
		System.out.println(this.originalKeywords);
		System.out.println(this.keywordNegators);
		System.out.println(this.keywords);
		System.out.println(url);
		System.out.println("#############################>");

		boolean found = false;
		List<WebElement> wes = null;
		while (!found){ 
			//			List<WebElement> wes = driver.findElements(By.xpath("//*[contains(@id,'result')]/div/div/div/div[2]"));
			
			//fails here!!  need to wrap in try/catch
			driver.get(url);
			System.out.println();
			System.out.println(driver.getCurrentUrl());
			wes = driver.findElements(By.cssSelector(".a-col-right"));
			System.out.println(wes.size() + " elements found.");
			if (wes.size() > 0)
				found = true;
			else{
				driver = new HtmlUnitDriver();
			}
		}
//		String patternString = "(.*)\\$([\\d+\\.\\,]*)used\\((\\d+) offer(.*)";
//		String patternStringNew = "(.*)\\$([\\d+\\.\\,]*)new\\((\\d+) offer(.*)";
		String patternString = "(.*)\\$([\\d+\\.\\,]*)used(.*)";
		String patternStringNew = "(.*)\\$([\\d+\\.\\,]*)new(.*)";

		System.out.println(url);
		for (WebElement we : wes){

			String text = we.getText();
			System.out.println(driver.getCurrentUrl());
			System.out.println(this.amazonKeywords);
			System.out.println("___________________________\n" + text + "\n---------------------------\n");
			String title =  we.findElement(By.cssSelector(".s-access-title")).getText();
			title = title.toLowerCase();
			title = title.replace(" gb", "gb");
			title = title.replace(" tb", "tb");
			boolean skipThis = false;
			for (String keyword : amazonKeywords){
				if (!title.contains(keyword) ||
						(title.contains("unlocked") && !amazonKeywords.contains("unlocked")) ||
						(title.contains("mini") && !amazonKeywords.contains("mini"))){
					if (!title.contains(keyword))
						System.out.println(keyword);
					skipThis = true;
					break;
				}
			}
			System.out.println("skipThis: " + skipThis);
			if (skipThis) continue;
			Pattern pattern = Pattern.compile(patternString);
			Pattern patternNew = Pattern.compile(patternStringNew);
			Matcher m = pattern.matcher(text);
			Matcher mNew = patternNew.matcher(text);
			System.out.println("Title:      " + title);
			String usedPrice = "", newPrice = "";
			double usedPriceD = 0, newPriceD = 0;
			if (m.find( ) ){
				System.out.println("Used price: " + m.group(2) );
				System.out.println("Quantity:   " + m.group(3) );
				usedPrice  = m.group(2);
				usedPriceD = Double.parseDouble(usedPrice.replaceAll(",", ""));
			}
			if (mNew.find()){
				System.out.println("New price: " + mNew.group(2) );
				System.out.println("Quantity:   " + mNew.group(3) );
				//				prices.add(Double.parseDouble(mNew.group(2)));
				newPrice  = mNew.group(2);
				newPriceD = Double.parseDouble(newPrice.replaceAll(",", ""));
			}

			if (usedPriceD > 0 && newPriceD > 0){
				amazon_low_price = String.format("%.2f", Math.min(usedPriceD, newPriceD));
				System.out.println("ebay low:   " + amazon_low_price);
				return;
			}else if (usedPriceD > 0){
				amazon_low_price = usedPrice;
				System.out.println("ebay low:   " + amazon_low_price);
				return;
			}else if (newPriceD > 0){
				amazon_low_price = newPrice;
				System.out.println("ebay low:   " + amazon_low_price);
				return;
			}else {
				System.out.println("NO MATCH.");
			}
		}

		amazon_low_price = null;
		System.out.println("ebay low:   " + amazon_low_price);
	}

	public String getID() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.originalKeywords);				//TODO use new items! remove quotes in ID?
		for (AFilter af : this.aspectFilters){
			sb.append(af.name);
			sb.append(af.value);
		}
		return sb.toString();
	}

}

//			ItemFilter filter1 = new ItemFilter();
//			filter1.setName(ItemFilterType.CONDITION);
//			filter1.getValue().add("1000"); filter1.getValue().add("1500"); filter1.getValue().add("6000");
//			filter1.getValue().add("1750"); filter1.getValue().add("2000"); filter1.getValue().add("2500"); 
//			filter1.getValue().add("3000"); filter1.getValue().add("4000"); filter1.getValue().add("5000"); 
//			request.getItemFilter().add(filter1);
//
//
//			ItemFilter filter2 = new ItemFilter();
//			filter2.setName(ItemFilterType.MAX_PRICE);
//			filter2.getValue().add("275.50");
//			request.getItemFilter().add(filter2);

//			ItemFilter filter3 = new ItemFilter();
//			filter3.setName(ItemFilterType.LOCATED_IN);
//			filter3.getValue().add("US");
//			request.getItemFilter().add(filter3);



//			ItemFilter filter4 = new ItemFilter();
//			filter4.setName(ItemFilterType.END_TIME_TO);
//			filter4.getValue().add("2014-09-27T20:50:00.513Z");	//GMT is EST + 4 
//			request.getItemFilter().add(filter4);


//ItemFilter filter5 = new ItemFilter();
//filter5.setName(ItemFilterType.LISTING_TYPE);
//filter5.getValue().add("Auction");	
//filter5.getValue().add("AuctionWithBIN");	
//request.getItemFilter().add(filter5);