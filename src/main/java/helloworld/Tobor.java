package helloworld;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.call.GetApiAccessRulesCall;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.GeteBayOfficialTimeCall;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.SearchItem;
import com.ebay.soap.eBLBaseComponents.ApiAccessRuleType;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.google.common.collect.Lists;

public class Tobor {


	/**
	 * 
	 * every X1 minutes:
	 * 	- search for items (FindItem w/ filters)
	 * 	- if PRICE < X2 then BID for this X3 sec prior to auction end
	 *  
	 * 
	 * BID:  first, ensure that price is still < X2
	 * 		second, read description - cancel items like "cracked, damaged, water, esn, blacklist, black list" etc
	 * 		get images urls
	 * 		-- if WIN? then save data: images, xml, title, description, etc.  to folder
	 * 
	 * 
	 * X1:  period of repeating schedule for items search
	 * X2:  maximum item price
	 * x3:  num seconds before auction close to start bidding
	 * 
	 * ..........
	 * .....
	 * .
	 * ........
	 * 
	 * i think this scheduled executor stuff is a bad idea.  right now its using a single thread pool....
	 * so if it sets a timer for something in 10 minutes, but then finds an item closing in 2 minutes, it will never 
	 * get to bid on the 2-min-away thing.  
	 * 
	 * i think.
	 * 
	 * alternative:  search for items in different repeating threads. put items in arraylist.
	 * continually sort arraylist to get soonest-ending items at the front
	 *  -- main program be constantly waiting until top item is ready for bidding
	 *  
	 *  question:  how use list used by multiple threads and that gets sorted? 
	 *  
	 *  TODO get upc for products. 
	 *  
	 *  
	 *  //TODO - how to balance FIND vs API calls????
	 *  // 1.  amazon realiability -- accurate and cheapest price
	 *  // 2.  ebay reliability - accurate product discovery.   -- make sure title text matches ebay filters result?
	 *  //  use SELLER filter item for updating price on a specific item?  maybe FIND has no max?
	 *  
	 *  FIND goes faster w/ category!
	 *  
	 *  BIG PROBLEM - ebay search is turning up the wrong phones.  need to cross-reference the AspectFilter searches with the title text
	 *  
	 *  ASPECT FILTERS DON"T WORK :( for storage capacity ... lets make sure
	 *  - the problem is that generic searches (with lower amazon prices) override specific searches (with higher prices)
	 *  
	 *  - also, ppl put wrong data in product fields.  go by title first, then details.  so .... ignore aspect filter?
	 *  
	 *  
1. 0'0:0:33    Apple iPhone 6 (Latest Model) - 4.7" - 64GB - Gold (Sprint) - USED
                  http://www.ebay.com/itm/Apple-iPhone-6-Latest-Model-4-7-64GB-Gold-Sprint-USED-/251684662507?pt=Cell_Phones
                  BrandAppleModeliPhone 6CarrierSprint
                  http://www.amazon.com/s/keywords= "apple"  "iphone 6"  "sprint"  "16gb" 
                  615.00
                  495.0
2. 0'0:1:49    Iphone 5 64gb AT&T 
                  http://www.ebay.com/itm/Iphone-5-64gb-AT-T-/251677635518?pt=Cell_Phones
                  BrandAppleModeliPhone 5CarrierUnlockedStorage Capacity64GB
                  http://www.amazon.com/s/keywords= "apple"  "iphone 5"  "unlocked"  "64gb" 
                  298.00
                  180.5
61. 0'0:53:51    Apple iPhone 5 - 32GB - Black & Slate (Verizon) Smartphone
                  http://www.ebay.com/itm/Apple-iPhone-5-32GB-Black-Slate-Verizon-Smartphone-/261632152984?pt=Cell_Phones
                  BrandAppleModeliPhone 5CarrierVerizon
                  http://www.amazon.com/s/keywords= "apple"  "iphone 5"  "verizon"  "16gb" 

                  //this one was listed as 128gb in the product details
60. 0'0:53:19    Apple iPhone 6 Plus 5.5" 64GB Gold (Unlocked, Never activated, sealed, in-hand)
                  http://www.ebay.com/itm/Apple-iPhone-6-Plus-5-5-64GB-Gold-Unlocked-Never-activated-sealed-in-hand-/181564080780?pt=Cell_Phones
                  BrandAppleModeliPhone 6 PlusCarrierUnlockedStorage Capacity128GB
                  http://www.amazon.com/s/keywords= "apple"  "iphone 6 plus"  "unlocked"  "128gb" 
                  1,176.29
                  1017.0

                  WTF THE EBAY PRICE IS TOTALLY WRONG

1. 0'0:0:13    Apple iPhone 5 - 16GB - Black & Slate (Verizon) Smartphone
                  http://www.ebay.com/itm/Apple-iPhone-5-16GB-Black-Slate-Verizon-Smartphone-/181560760883?pt=Cell_Phones
                  ModeliPhone 5CarrierVerizonStorage Capacity16GB
                  http://www.amazon.com/s/keywords= "iphone 5"  "verizon"  "16gb" 
                  ebayKeywords:  "iPhone 5"  "Verizon"  "16GB" 
                  230.00
                  138.0

                  TODO - sometimes ACK fails!  see if the same item is failing repeatedly

                  unfortunately, the FINDING API item prices are often wrong :( the api prices seem to be always? correct
http://developer.ebay.com/Devzone/guides/ebayfeatures/Development/Desc-ItemCondition.html:
GetItem and related calls in the Trading API, and GetSingleItem and GetMultipleItems in the Shopping API return the ConditionDescription field.

The Finding API and other search calls do not return condition descriptions. eBay's search engine does not search text within ConditionDescription.

TODO samsung galaxy prices are wrong :(

Wed Oct 22 22:01:32 EDT 2014
Samsung Galaxy S5 SM-G900A - 16GB - Shimmery White (FACTORY UNLOCKED) CLEAN IMEI
http://www.ebay.com/itm/Samsung-Galaxy-S5-SM-G900A-16GB-Shimmery-White-FACTORY-UNLOCKED-CLEAN-IMEI-/400792974114?pt=Cell_Phones
amazon:               449.99
ebay max:             399.99
shopping list price:  337.99
curr api price:       355.0

TODO -- why did this fail title???

Wed Oct 22 22:06:42 EDT 2014
CALLED API!
Apple Iphone 5C Pink / White 16 Gb Sprint Ready To Use
http://www.ebay.com/itm/Apple-Iphone-5C-Pink-White-16-Gb-Sprint-Ready-Use-/111490190408?pt=Cell_Phones
checking desc...
found bad word lock
redeemed 2 by no
found bad word lock
redeemed 2 by no
found bad word box
redeemed by with box/case in nonTitle description
checking cond desc...
found bad word lock
redeemed 2 by no
found bad word lock
redeemed 2 by no
found bad word box
redeemed by with box/case in nonTitle description
checking title...
found bad word [Ljava.lang.String;@60a12c13 in title
checking subtitle...

(price, max, amzon): (203.0, 148.87, 198.87)
passPrice:false
passDesc:true
passCDsc:true
passTitl:false
passSubt:true


TODO - must include model number with samsung galaxy! sm-g900h != sm-g900f!!!!

TODO - clean up / sreamline logging.  put in spreadsheet!  not stupid text files

TODO - pass if says: 
IMEI:013966000448683

TODO - how deal with "not unlocked":

APPLE IPHONE 6 PLUS-16GB-SILVER *NOT UNLOCKED* BRAND NEW IN BOX AT&T SMARTPHONE
http://www.ebay.com/itm/APPLE-IPHONE-6-PLUS-16GB-SILVER-NOT-UNLOCKED-BRAND-NEW-BOX-AT-T-SMARTPHONE-/111491650518?pt=Cell_Phones
	 * 
	 */


	public static WebDriver driver;
	public static WebDriverWait wait;
	public static GetItemCall api;
	public static FindingServicePortType serviceClient;
	public static List<ItemSet> shoppingList;
	public static PrintWriter out;
	public static ScheduledExecutorService webStuffExecutorService;
	/**
	 * @param args
	 * @throws Exception 
	 * @throws SdkException 
	 * @throws ApiException 
	 */
	//http://www.amazon.com/s/keywords=%22Apple%22+%22iPhone+5c%22+%22Verizon%22+%2216GB%22
	//www.amazon.com/s/keywords="Apple"+"iPhone+5c"+"Verizon"+"16GB"
	public static void main(String[] args) throws ApiException, SdkException, Exception {
		File purchaseLogFile = new File("C:\\temp\\ebayFakePurchases.txt");
		File callsLogFile = new File("C:\\temp\\ebayAPIcalls.txt");
		int spread = 80;

		FileUtils.writeStringToFile(purchaseLogFile, "\n_______________________________________________\n" +
				"spread = " + spread +"\n", true);
		FileUtils.writeStringToFile(callsLogFile, "\n_______________________________________________\n", true);


		//TODO there is galaxy s4/s5 MINI!!!!!!! and regular

		List<It3m> it3ms = Helper.getIt3ms(spread);

		int k = 1;
		for (It3m it3m : it3ms){
			System.out.println(k++);
			System.out.println("amazon price: " + it3m.amazon_low_price);
			System.out.println("ebay max: " + it3m.ebay_max_price);
			System.out.println(it3m.getAmazonSearchURL());
			for (AFilter af: it3m.aspectFilters){
				System.out.println(af.name + ": " + af.value);
			}
			System.out.println(" ");
		}
//								System.exit(0);


		//		driver = new FirefoxDriver();
		//		wait = new WebDriverWait(driver, 5);
		//		driver.get("https://signin.ebay.com/ws/eBayISAPI.dll");
		//		driver.findElement(By.id("userid")).sendKeys("stuart.clifford@gmail.com");
		//		driver.findElement(By.id("pass")).sendKeys("qwe123QWE!@#");
		//		driver.findElement(By.id("sgnBt")).click();

		System.out.println("===== [1] Account Information ====");
		ApiContext apiContext = getApiContext();


		//		GetApiAccessRulesCall api2 = new GetApiAccessRulesCall(apiContext);
		//		api2.getApiAccessRules();
		//		ApiAccessRuleType[] rules = api2.getReturnedApiAccessRules();
		//		for (ApiAccessRuleType rule : rules){
		//			System.out.println(rule.getCallName());
		//			System.out.println(rule.getDailyHardLimit());
		//			System.out.println(rule.getDailySoftLimit());
		//			System.out.println(rule.getDailyUsage());
		//			System.out.println(rule.getHourlyHardLimit());
		//			System.out.println(rule.getHourlySoftLimit());
		//			System.out.println(rule.getHourlyUsage());
		//			System.out.println(rule.getCallName());
		//			System.out.println();
		//		}
		//		System.exit(0);

		//		GeteBayOfficialTimeCall apiCall = new GeteBayOfficialTimeCall(apiContext);
		//		Calendar cal = apiCall.geteBayOfficialTime();
		//		System.out.println("Official eBay Time : " + cal.getTime().toString());



		api = new GetItemCall(apiContext);
		DetailLevelCodeType[] detailLevels = new DetailLevelCodeType[] {
				DetailLevelCodeType.RETURN_ALL,
				DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES,
				DetailLevelCodeType.ITEM_RETURN_DESCRIPTION
		};
		api.setDetailLevel(detailLevels);

		ClientConfig config = new ClientConfig();
		config.setApplicationId("stuartro-dd31-4a26-881e-600984744b93");
		serviceClient = FindingServiceClientFactory.getServiceClient(config);

		//		System.exit(0);
		shoppingList =  Collections.synchronizedList(new ArrayList<ItemSet>());

		ScheduledExecutorService scheduledExecutorServiceSearch =	Executors.newScheduledThreadPool(30);

		for (int i = 0; i < it3ms.size(); i++){	
			System.out.println("Scheduling: " + i + " " + it3ms.get(i).getAmazonSearchURL());
			scheduledExecutorServiceSearch.scheduleAtFixedRate(	new Search(it3ms.get(i)), i, 5*60*1000, TimeUnit.MILLISECONDS);
		}

		while(true){

			//	TODO -- problem -- HOW TO UPDATE PRICES OF ITEMS IN SHOPPING LIST!!!!!
			//	currently, items wont get deleted unless a NEW item is found in a Search thread with the same URL
			//	so if the price goes up, so the new search doesn't catch it, it won't get modified.  just stays in shopping list.
			//this whiletrue loop needs to use the FIND function to get the current price of the object.  use the Seller filter.  search by title
			// when to get seller?
			// - just don't search until the last minute.  like every 5 minutes.  only look at stuff ending w/in 5 min

			System.out.println(shoppingList.size() +" items in list");

			for (int i = 0; i < shoppingList.size(); i++){		//remove closed auctions
				ItemSet itemSet = shoppingList.get(i);
				SearchItem item = itemSet.item;

				Calendar endTimeCal = item.getListingInfo().getEndTime();
				long durationToEnd = endTimeCal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
				if (durationToEnd < 3*1000)
					shoppingList.remove(i--);
			}
			if (shoppingList.size() == 0) {
				//				System.out.println("0 items in list");
				Thread.sleep(1000);		//TODO should be 1000
				continue;
			}
			Collections.sort(shoppingList, new CustomComparator());

			int j = 1;
			try {
				for (ItemSet itemSet : shoppingList){
					System.out.println(j++ + ". " + itemSet.toString());

				}
			}catch(ConcurrentModificationException e){}
			//			System.exit(0);
			System.out.println();

			ItemSet itemSet = shoppingList.get(0);
			It3m it3m = itemSet.it3m;
			SearchItem item = itemSet.item;

			if (itemSet.timeLeftMillis() > 12*1000){
				Thread.sleep(2000);
				continue;
			}
			//			Thread.sleep(Math.max(itemSet.timeLeftMillis() - 12*1000, 0));
			Thread.sleep(Math.max(itemSet.timeLeftMillis() + 5000, 5000));			//TODO for fake purchases only!

			//TODO THIS LINE IS HOGGING API CALLS!
			//DON'T CHECK IT UNTIL IT'S REALLY CLOSE TO CLOSING AND THE PRICE IS LOW
			//actually no, idk :(
			if (clearedForPurchase(itemSet)){
				FileUtils.writeStringToFile(purchaseLogFile, 
						"\n" +
								(new Date()).toString() + "\n" + 
								item.getTitle() + "\n" +
								item.getViewItemURL() + "\n" + 
								"amazon:               " + it3m.amazon_low_price  + "\n" + 
								"ebay max:             " + it3m.ebay_max_price + "\n" + 
								"shopping list price:  " + item.getSellingStatus().getCurrentPrice().getValue() + "\n" +
								"curr api price:       " + it3m.ebayApiPrice + "\n", true);
				//				if (itemSet.timeLeftMillis() < 2*1000){
				//					shoppingList.remove(0);
				//					continue;
				//				}
				//				Tobor.driver.get("http://offer.ebay.com/ws/eBayISAPI.dll?MfcISAPICommand=MakeBid&user=stuarrobin&item="
				//						+ itemID +"&maxbid=" + String.format("%.2f", it3m.ebay_max_price));	//TODO set ebay max price
				//
				//				if (itemSet.timeLeftMillis() < 1*1000){
				//					shoppingList.remove(0);
				//					continue;
				//				}
				//				Thread.sleep(Math.max(itemSet.timeLeftMillis() - 200,  0));
				//
				//				try {
				//					//					Tobor.driver.findElement(By.name("confirmbid")).click();		//TODO testing//				
				//					Tobor.wait.until(ExpectedConditions.elementToBeClickable(By.name("confirmbid")));
				//					System.out.println(driver.getCurrentUrl());
				//					System.out.println("confirm bid button found! fake click confirm");
				//					
				//				}catch(Exception e){
				//					System.out.println("confirm button not found :(");
				//				}
				//
				//				Tobor.driver.get(item.getViewItemURL());
				//				Tobor.wait.until(ExpectedConditions.elementToBeClickable(By.linkText("See original listing")));
				//
				//				if (Tobor.driver.getPageSource().contains("You won this auction")){
				//					System.out.println("\ni won!\n");
				//
				//					String itemDirStr = "C:\\Users\\User\\Documents\\ebayAmazon\\" + itemID;
				//					File itemDir = new File(itemDirStr);
				//					itemDir.mkdir();
				//					ItemType itemAPI_2 = Tobor.api.getItem(itemID);
				//
				//					Files.write(Paths.get(itemDirStr + "\\description"), itemAPI_2.getDescription().getBytes());
				//					Files.write(Paths.get(itemDirStr + "\\title"), item.getTitle().getBytes());
				//					Files.write(Paths.get(itemDirStr + "\\url"), item.getViewItemURL().getBytes());
				//					Files.write(Paths.get(itemDirStr + "\\condition"), item.getCondition().getConditionDisplayName().getBytes());
				//					Files.write(Paths.get(itemDirStr + "\\description"), itemAPI_2.getDescription().getBytes());
				//					Files.write(Paths.get(itemDirStr + "\\price"), Double.toString(itemAPI_2.getSellingStatus().getCurrentPrice().getValue()).getBytes());
				//
				//					int i = 0;
				//					for (String url : itemAPI_2.getPictureDetails().getPictureURL()) {
				//						i++;
				//						BufferedImage image =null;
				//						try{
				//							image = ImageIO.read(new URL(url));
				//							ImageIO.write(image, "jpg",new File(itemDirStr + "\\" + i + ".jpg"));
				//						}catch(IOException e){e.printStackTrace();}
				//					}
				//				}
			}
			else {
				System.out.println("failed clearedforpurchase: " + item.getTitle());
				System.out.println("      "+ item.getViewItemURL());
			}
			//			System.exit(0);

			shoppingList.remove(0);
		}

	}

	static boolean clearedForPurchase(ItemSet itemSet) throws ApiException, SdkException, Exception {

		out = new PrintWriter(new BufferedWriter(new FileWriter("C:\\temp\\ebayAPIcalls.txt", true)));
		out.println("\n" + (new Date()).toString());

		It3m it3m = itemSet.it3m;
		SearchItem item = itemSet.item;
		String itemID = item.getItemId().trim();

		System.out.println("CALLING API!");
		ItemType itemAPI_2 = Tobor.api.getItem(itemID);
		System.out.println("CALLED API!");
		System.out.println(item.getTitle());
		System.out.println(item.getViewItemURL());

		out.println("CALLED API!");
		out.println(item.getTitle());
		out.println(item.getViewItemURL());
		boolean passPrice = itemAPI_2.getSellingStatus().getCurrentPrice().getValue() < it3m.ebay_max_price;
		out.println("checking desc...");
		boolean passDesc = it3m.hasCleanDescription(itemAPI_2.getDescription(), item.getTitle());
		out.println("checking cond desc...");
		boolean passCDsc = it3m.hasCleanDescription(itemAPI_2.getConditionDescription(), item.getTitle());
		out.println("checking title...");
		boolean passTitl = it3m.hasCleanDescription(item.getTitle(), item.getTitle());
		out.println("checking subtitle...");
		boolean passSubt = it3m.hasCleanDescription(item.getSubtitle(), item.getTitle());

		out.println();
		out.println("(price, max, amzon): (" + itemAPI_2.getSellingStatus().getCurrentPrice().getValue() + ", " + it3m.ebay_max_price + ", " + it3m.amazon_low_price + ")");
		out.println("passPrice:" + passPrice);
		out.println("passDesc:" + passDesc );
		out.println("passCDsc:" + passCDsc );
		out.println("passTitl:" + passTitl );
		out.println("passSubt:" + passSubt );		
		out.close();

		it3m.ebayApiPrice = itemAPI_2.getSellingStatus().getCurrentPrice().getValue() +"";

		return (passPrice && passDesc && passCDsc && passTitl && passSubt);

	}

	private static ApiContext getApiContext() throws IOException {
		ApiContext apiContext = new ApiContext();
		ApiCredential cred = apiContext.getApiCredential();
		//1st set: 
		//String token = "AgAAAA**AQAAAA**aAAAAA**xiQnVA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AGmISkDpWLpQSdj6x9nY+seQ**838CAA**AAMAAA**cycAV0aDG5B03I1yGae5xRgLpejJkdo+eRTCq/nWFgJxjBsYmlLbOemCxqvs0a+lQIjztv3/qOA6Y8t8l4LsJCoFx2S+9nqfZLK7X70cVvOdwJG3UdcSr4s7qIAfT5Kxao2xS3nna8PJ+7ZsVIUDr0dIYtdMeF+QxWbVOJY/NUbsKdygE37GZsYAR7A2nzugEeLPCHMQxWNYyTFc6U9K1du+Ikj4s62aY1MI75XlrIRiwT8GGfI2FnsV+PPTWlKNhG2KrQDVHMtKnFIe+ypICaIRJU3yZKSDX1YpgxyLvu2z84MlhrIBKaxi7wDmGZRrZgTC54/Rv4z/f2MMp1xX4U5+5HKW6Div0/rBJGMvVgfjLSy1tPl0lDgTtQ10DM7rBQ14LSkuUPo2IeMp81IwxOvbJUGc4XzEs/zYNA869d7kxaOcMJa1eWgrXg4S3nufMWegwW7DjkbMzXOxq0vvQIPUQz9EtbMbPiS5b07aGJeGiiFYEicnuSU8gVoSl18VqfW+vkOgUssZQdo8rcWoUCFVaVQ2eip3DO/xXh9e79FUAze9eQIbDIv1qYHS8F9XlSQgA/GfBk0IpBqwV/JDesLGxvlzCYtr0kPGbAL4O3zm7gzjuCmDRKJ4S1pWF+StxJeGOVZhZ0xP8yx7jaUBV/zl+A0zGG/QoPsP9PaaYy7lcgaYxial1tA26oGXmeoyN+aOJ+s2pqJzPlGzHb7bb9nDJWujY130IazVfc5mVNGZsDRDryZXWy++kZBdojD3";
		//2nd set:
		String token = "AgAAAA**AQAAAA**aAAAAA**hnREVA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AGkIejAJaHpg2dj6x9nY+seQ**xIgCAA**AAMAAA**b1wJ8ZYGoZYspGP8W00hjCSedsFYh6fw33SwjZ3hsRMima4eM7mSIE+eXcY8YHwW2HSu3Vfaiy9b/jc2isn5/pcT2B2/M9PpG0nnB52BeMMdYa6OpFdRxG2idQqjdE/5nONmYvGCveeX6m68poIbd4z2mfhHmq3KZ2oVSMhsoKT4Y1clj2zjWgjWknc1QetgAjh1WtqXpxoZmASEA/JU0u0wF6n2kc7w9ULyXzpXewrL9aC9D6+shU8Ob1xsV46TZnF12hhtqMbwf8vnmcrAVNDxZK9O6iS6GPo5ytKpnXHQqExXiFBSSABxCJSdDAVvk0l3FC558C6DXfrUN1+Ri+IkY30Ap/0Ij0M8/gI9c9VTKlbyoKdMiBdhYa7EznK0n2QX3AYRoOb+jdrSS2F+D0A0LncSN+OcGl5PyIJUYqSeyFyvXVJUZ0N/NpeAkf9WTQ2//NiB/57g530Xm1ctVjeXxsPCYEypjewn/vXKjmC0BMpJg8p7728AL2+/eW6yGGRnkJSLFRD/qW0rdhFfCrEk409j1Hoi/kRhnUGgj0XEHhVrVZV2mlaQcDUxFsdxmLfx2oYHlekuTzaJhZ8M/PFMma/93jEjyyMSIiVP2D+cMpU7A90/hur9e6L7hFcnxz5CoasDx/aL7SYGBEDedpLaR3ErK56LAXS2q8U2N89DbH3VEC5SSmcy5FJMyXcvrvwOstPhjRO1zTrQp+yAjltZ37J8pQJ5dbhw2cruMv15x93cqYPnwFVmH25X1o8S";
		String server = "https://api.ebay.com/wsapi";
		cred.seteBayToken(token);	//input
		apiContext.setApiServerUrl(server); //input
		return apiContext;


		//1st set:
		//stuartc842 - stuart.clifford@gmail.com
		//app key:     stuartro-dd31-4a26-881e-600984744b93
		//user token:  AgAAAA**AQAAAA**aAAAAA**xiQnVA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AGmISkDpWLpQSdj6x9nY+seQ**838CAA**AAMAAA**cycAV0aDG5B03I1yGae5xRgLpejJkdo+eRTCq/nWFgJxjBsYmlLbOemCxqvs0a+lQIjztv3/qOA6Y8t8l4LsJCoFx2S+9nqfZLK7X70cVvOdwJG3UdcSr4s7qIAfT5Kxao2xS3nna8PJ+7ZsVIUDr0dIYtdMeF+QxWbVOJY/NUbsKdygE37GZsYAR7A2nzugEeLPCHMQxWNYyTFc6U9K1du+Ikj4s62aY1MI75XlrIRiwT8GGfI2FnsV+PPTWlKNhG2KrQDVHMtKnFIe+ypICaIRJU3yZKSDX1YpgxyLvu2z84MlhrIBKaxi7wDmGZRrZgTC54/Rv4z/f2MMp1xX4U5+5HKW6Div0/rBJGMvVgfjLSy1tPl0lDgTtQ10DM7rBQ14LSkuUPo2IeMp81IwxOvbJUGc4XzEs/zYNA869d7kxaOcMJa1eWgrXg4S3nufMWegwW7DjkbMzXOxq0vvQIPUQz9EtbMbPiS5b07aGJeGiiFYEicnuSU8gVoSl18VqfW+vkOgUssZQdo8rcWoUCFVaVQ2eip3DO/xXh9e79FUAze9eQIbDIv1qYHS8F9XlSQgA/GfBk0IpBqwV/JDesLGxvlzCYtr0kPGbAL4O3zm7gzjuCmDRKJ4S1pWF+StxJeGOVZhZ0xP8yx7jaUBV/zl+A0zGG/QoPsP9PaaYy7lcgaYxial1tA26oGXmeoyN+aOJ+s2pqJzPlGzHb7bb9nDJWujY130IazVfc5mVNGZsDRDryZXWy++kZBdojD3


		//2nd set:
		//stuartc843 - strobinso@hotmail.com
		//dev key:     26ecfe49-0227-4180-8fc3-d109e951b967
		//app key:     stuartro-f527-43c2-9df9-325f8c7ea1ee
		//cert key:    f27ffd71-071a-4165-b0a7-e6b31a6aea1f
		//user token:  AgAAAA**AQAAAA**aAAAAA**hnREVA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AGkIejAJaHpg2dj6x9nY+seQ**xIgCAA**AAMAAA**b1wJ8ZYGoZYspGP8W00hjCSedsFYh6fw33SwjZ3hsRMima4eM7mSIE+eXcY8YHwW2HSu3Vfaiy9b/jc2isn5/pcT2B2/M9PpG0nnB52BeMMdYa6OpFdRxG2idQqjdE/5nONmYvGCveeX6m68poIbd4z2mfhHmq3KZ2oVSMhsoKT4Y1clj2zjWgjWknc1QetgAjh1WtqXpxoZmASEA/JU0u0wF6n2kc7w9ULyXzpXewrL9aC9D6+shU8Ob1xsV46TZnF12hhtqMbwf8vnmcrAVNDxZK9O6iS6GPo5ytKpnXHQqExXiFBSSABxCJSdDAVvk0l3FC558C6DXfrUN1+Ri+IkY30Ap/0Ij0M8/gI9c9VTKlbyoKdMiBdhYa7EznK0n2QX3AYRoOb+jdrSS2F+D0A0LncSN+OcGl5PyIJUYqSeyFyvXVJUZ0N/NpeAkf9WTQ2//NiB/57g530Xm1ctVjeXxsPCYEypjewn/vXKjmC0BMpJg8p7728AL2+/eW6yGGRnkJSLFRD/qW0rdhFfCrEk409j1Hoi/kRhnUGgj0XEHhVrVZV2mlaQcDUxFsdxmLfx2oYHlekuTzaJhZ8M/PFMma/93jEjyyMSIiVP2D+cMpU7A90/hur9e6L7hFcnxz5CoasDx/aL7SYGBEDedpLaR3ErK56LAXS2q8U2N89DbH3VEC5SSmcy5FJMyXcvrvwOstPhjRO1zTrQp+yAjltZ37J8pQJ5dbhw2cruMv15x93cqYPnwFVmH25X1o8S
	}

}

class Search implements Runnable{
	private It3m it3m;
	public Search(It3m it3m){
		this.it3m = it3m;
	}
	public void run(){

		boolean succeeded = false;
		//		while (!succeeded){
		try {
			FindItemsAdvancedRequest request = it3m.getRequest(5, 100);
			FindItemsAdvancedResponse result = Tobor.serviceClient.findItemsAdvanced(request);

			System.out.println("Ack = "+result.getAck());
			System.out.println("Find " + result.getSearchResult().getCount() + " items for " + it3m.keywords);

			List<SearchItem> items = result.getSearchResult().getItem();

			//remove old ent
			for(SearchItem new_item : items) {
				boolean dontAddItem = false;
				for(int i = 0; i < Tobor.shoppingList.size(); i++){
					if (Tobor.shoppingList.get(i).item.getViewItemURL().equals(new_item.getViewItemURL())){

						if (Tobor.shoppingList.get(i).it3m.ebay_max_price <= it3m.ebay_max_price)
							Tobor.shoppingList.remove(i);
						else 
							dontAddItem = true;
						//						break;
					}
				}

				if (!dontAddItem){
					boolean titleHasAllTerms = true;
					for (AFilter af : it3m.aspectFilters){
						String term = af.value.toLowerCase();
						if (!new_item.getTitle().toLowerCase().replace(" gb", "gb").contains(term)){
							if (!(term.equals("at&t") && new_item.getTitle().toLowerCase().contains("att"))){
								titleHasAllTerms = false;
								break;
							}
						}
					}
					if (titleHasAllTerms)
						Tobor.shoppingList.add(new ItemSet(it3m, new_item));
				}
			}
			succeeded = true;
		} 
		catch (Exception ex) {ex.printStackTrace(); }
		//		}
	}
}

class ItemSet {
	public It3m it3m;
	public SearchItem item;

	public ItemSet(It3m it3m, SearchItem item){
		this.it3m = it3m;
		this.item = item;
	}

	public long timeLeftMillis() {
		Calendar endTimeCal = item.getListingInfo().getEndTime();
		return endTimeCal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
	}

	public String toString(){
		long millis = timeLeftMillis();
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;
		long days = (millis / (1000 * 60 * 60 * 24));

		return days + "'" + hour + ":" + minute + ":" + second +  "    " +
		item.getTitle() + "\n                  " + item.getViewItemURL() 
		+ "\n                  " + it3m.getID()
		+ "\n                  " + it3m.getAmazonSearchURL()
		+ "\n                  " + "ebayKeywords: " + it3m.ebayKeywords
		+ "\n                  " + it3m.amazon_low_price
		//		+ "\n                  " + item.getSellingStatus().getCurrentPrice().getValue()
		+ "\n                  " + item.getSellingStatus().getConvertedCurrentPrice().getValue();			
	}
}

class CustomComparator implements Comparator<ItemSet> {
	@Override
	public int compare(ItemSet o1, ItemSet o2) {
		return o1.item.getListingInfo().getEndTime()
				.compareTo( o2.item.getListingInfo().getEndTime() );
	}
}


class AvoidanceTerm {
	public String badWord;
	public String[] redemptionWords;
	public  AvoidanceTerm(String badWord, String[] redeemers){
		this.badWord = badWord;
		this.redemptionWords = redeemers;
	}
	//	public AvoidanceTerm(String badWord2, String[] empty) {
	//		// TODO Auto-generated constructor stub
	//	}

}

class IFilter {
	public ItemFilterType name;
	String[] values;
	public IFilter(ItemFilterType name, String[] values){
		this.name = name;
		this.values = values;
	}
	public IFilter(ItemFilterType name, String value){
		this.name = name;
		this.values = new String[1];
		values[0] = value;
	}
}
class AFilter {
	public String name;
	public String value;
	//	public AFilter(String name, String[] values){
	//		this.name = name;
	//		this.values = values;
	//	}
	public AFilter(String name, String value){
		this.name = name;
		this.value = value;
	}
}


//		GetApiAccessRulesCall api = new GetApiAccessRulesCall(apiContext);
//		api.getApiAccessRules();
//		ApiAccessRuleType[] rules = api.getReturnedApiAccessRules();
//		for (ApiAccessRuleType rule : rules){
//			System.out.println(rule.getCallName());
//			System.out.println(rule.getDailyHardLimit());
//			System.out.println(rule.getDailySoftLimit());
//			System.out.println(rule.getDailyUsage());
//			System.out.println(rule.getHourlyHardLimit());
//			System.out.println(rule.getHourlySoftLimit());
//			System.out.println(rule.getHourlyUsage());
//			System.out.println(rule.getCallName());
//			System.out.println();
//		}
