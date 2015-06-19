/*
Copyright (c) 2013 eBay, Inc.
This program is licensed under the terms of the eBay Common Development and
Distribution License (CDDL) Version 1.0 (the "License") and any subsequent  version 
thereof released by eBay.  The then-current version of the License can be found 
at http://www.opensource.org/licenses/cddl1.php and in the eBaySDKLicense file that 
is under the eBay SDK ../docs directory.
 */

package helloworld;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.GeteBayOfficialTimeCall;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.PaginationInput;
import com.ebay.services.finding.SearchItem;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.ItemType;

/**
 * A Hello World-like sample, 
 * showing how to call eBay API using eBay SDK.
 *  
 * @author boyang
 *
 */
public class ApplicationHelloWorld {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {

//			WebDriver driver = new FirefoxDriver();
//			WebDriverWait wait = new WebDriverWait(driver, 30);
//			driver.get("https://signin.ebay.com/ws/eBayISAPI.dll");
//			driver.findElement(By.id("userid")).sendKeys("stuart.clifford@gmail.com");
//			driver.findElement(By.id("pass")).sendKeys("qwe123QWE!@#");
//			driver.findElement(By.id("sgnBt")).click();
			
//			
//			String myUrl = "http://www.ebay.com/itm/Apple-iPhone-5s-16GB-Gold-AT-T-Excellent-Condition-No-contract-ios8-/400780349252?pt=Cell_Phones&hash=item5d505ecf44";
//			driver.get(myUrl);
//			driver.findElement(By.id("bidBtn_btn")).click();
//			driver.findElement(By.className("inpVal")).sendKeys("2");
//			driver.findElement(By.className("inpVal")).sendKeys(Keys.RETURN);
//			System.exit(0);
//			
//			String completedURL = "http://www.ebay.com/itm/Apple-iPhone-5s-Latest-Model-16GB-Space-Gray-AT-T-Smartphone-/291248339511?pt=Cell_Phones&hash=item43cfc0fa37";//"http://cgi.ebay.com/ws/eBayISAPI.dll?ViewItem&item=231334346661&ssPageName=ADME:B:EOIBSA:US:1120";
//			driver.get(completedURL);
//			String s = driver.getPageSource();
//			//System.out.println(s);
//			Boolean b = s.contains("You won this auction");
//			System.out.println(b);
//			System.exit(0);
			
					
					//You won this auction.
					//"You won this auction. "
			
			
			System.out.print("\n");
			System.out.print("+++++++++++++++++++++++++++++++++++++++\n");
			System.out.print("+ Welcome to eBay SDK for Java Sample +\n");
			System.out.print("+  - HelloWorld                   +\n");
			System.out.print("+++++++++++++++++++++++++++++++++++++++\n");
			System.out.print("\n");

			// [Step 1] Initialize eBay ApiContext object
			System.out.println("===== [1] Account Information ====");
			ApiContext apiContext = getApiContext();

			// [Step 2] Create call object and execute the call
			GeteBayOfficialTimeCall apiCall = new GeteBayOfficialTimeCall(apiContext);
			System.out.println("Begin to call eBay API, please wait ... ");
			Calendar cal = apiCall.geteBayOfficialTime();
			System.out.println("End to call eBay API, show call result ...");

			// [Setp 3] Handle the result returned
			System.out.println("Official eBay Time : " + cal.getTime().toString());


			//now get description

			GetItemCall api = new GetItemCall(apiContext);

			DetailLevelCodeType[] detailLevels = new DetailLevelCodeType[] {
					DetailLevelCodeType.RETURN_ALL,
					DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES,
					DetailLevelCodeType.ITEM_RETURN_DESCRIPTION
			};
			api.setDetailLevel(detailLevels);

//			String itemID = "111471257732";
//			itemID = itemID.trim();
//			ItemType itemAPI = api.getItem(itemID);
//
//			System.out.println(itemAPI.getTitle());
//			System.out.println(itemAPI.getSellingStatus().getCurrentPrice().getValue());
//			System.out.println(itemAPI.getDescription());
			
//			String itemID2 = "291248339511";
//
//			GetAllBiddersCall asdf = new GetAllBiddersCall(apiContext);
//			asdf.setItemID(itemID2);
//			System.out.println("highest bidder: " + asdf.getReturnedHighBidder());

//			System.exit(0);
			
			ClientConfig config = new ClientConfig();
			config.setApplicationId("stuartro-dd31-4a26-881e-600984744b93");
			FindingServicePortType serviceClient = FindingServiceClientFactory.getServiceClient(config);
			FindItemsAdvancedRequest request = new FindItemsAdvancedRequest();
			request.setKeywords("Apple iPhone 5 - 16GB - MD639LL/A White/Silver AT&T Smartphone iCloud Issue");


			ItemFilter filter5 = new ItemFilter();
			filter5.setName(ItemFilterType.LISTING_TYPE);
			filter5.getValue().add("Auction");	
			filter5.getValue().add("AuctionWithBIN");	
			request.getItemFilter().add(filter5);

			PaginationInput pi = new PaginationInput();
			pi.setEntriesPerPage(6);
			request.setPaginationInput(pi);

			//call service
			System.out.println("before call");
			//            FindItemsByKeywordsResponse result = serviceClient.findItemsByKeywords(request);
			FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);
			System.out.println("after call");

			//            result.getSearchResult().getItem().

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

			//output result
			System.out.println("Ack = "+result.getAck());
			System.out.println("Find " + result.getSearchResult().getCount() + " items." );
			List<SearchItem> items = result.getSearchResult().getItem();




			for(SearchItem item : items) {



				System.out.println(item.getTitle());
				System.out.println("  " + item.getLocation());
				System.out.println("  " + sdf.format(item.getListingInfo().getEndTime().getTime()));

				System.out.println("  " + item.getPrimaryCategory().getCategoryName());
				System.out.println("  " + item.getPrimaryCategory().getCategoryId());
				//            	System.out.println("  " + item.getSecondaryCategory().getCategoryName());
				System.out.println("  " + (item.getProductId() == null ? "NA" : item.getProductId().getValue()));
				//            	System.out.println("  " + item.getGalleryURL());
				System.out.println("  " + item.getViewItemURL());
				System.out.println("  " + (item.getShippingInfo().getShippingServiceCost() == null ? "NA shipping" : item.getShippingInfo().getShippingServiceCost().getValue()));

				System.out.println("  " + item.getShippingInfo().getShippingType());
				//            	System.out.println("  " + item.getListingInfo().getEndTime());
				//            	System.out.println("  " + (item.getListingInfo().getBuyItNowPrice() == null ? "NA" : item.getListingInfo().getBuyItNowPrice().getValue()) );
				//            	System.out.println("  " + item.getListingInfo().getListingType());
				//            	System.out.println("  pics: " + item.getGalleryPlusPictureURL().size());
				System.out.println("  " + item.getCondition().getConditionDisplayName());
				//            	System.out.println("  " + item.getSellingStatus().getConvertedCurrentPrice().getValue());
				System.out.println("  " + item.getSellingStatus().getCurrentPrice().getValue());
				System.out.println("  " + item.getSellingStatus().getTimeLeft().toString());
				System.out.println("  " + 
						item.getSellingStatus().getTimeLeft().getHours() + ":" +
						item.getSellingStatus().getTimeLeft().getMinutes()+ ":" + 
						item.getSellingStatus().getTimeLeft().getSeconds());



				ItemType itemAPI_2 = api.getItem(item.getItemId().trim());

				System.out.println(itemAPI_2.getTitle());
				System.out.println(itemAPI_2.getSellingStatus().getCurrentPrice().getValue());
//				System.out.println(itemAPI_2.getDescription());
				System.out.println(itemAPI_2.getPictureDetails().getGalleryURL());
				System.out.println(itemAPI_2.getPictureDetails().getExternalPictureURLLength());
//				System.out.println(itemAPI_2.getPictureDetails().getPictureURL());


				for (String url : itemAPI_2.getPictureDetails().getExternalPictureURL()) {
					System.out.println("IMAGE URL: " + url);
				}
				
				//this one might work
				for (String url : itemAPI_2.getPictureDetails().getPictureURL()) {
					System.out.println("IMAGE2 URL: " + url);
				}

				int i = 0;
				for (String url : itemAPI_2.getPictureDetails().getPictureURL()) {
					i++;
					BufferedImage image =null;
					try{
						image = ImageIO.read(new URL(url));
						ImageIO.write(image, "jpg",new File("C:\\test" + "\\" + i + ".jpg"));
					}catch(IOException e){e.printStackTrace();}
				}

//				GetAllBiddersCall asdf = new GetAllBiddersCall(apiContext);
//				asdf.setItemID(item.getItemId().trim());
//				System.out.println("highest bidder: " + asdf.getReturnedHighBidder());

				System.out.println("\n");
			}


		}
		catch(Exception e) {
			System.out.println("Fail to get eBay official time.");
			e.printStackTrace();
		}

	}

	/**
	 * Populate eBay SDK ApiContext object with data input from user
	 * @return ApiContext object
	 */
	private static ApiContext getApiContext() throws IOException {

		ApiContext apiContext = new ApiContext();

		//set Api Token to access eBay Api Server
		ApiCredential cred = apiContext.getApiCredential();
		//	      input = ConsoleUtil.readString("Enter your eBay Authentication Token: ");

		String token = "AgAAAA**AQAAAA**aAAAAA**xiQnVA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AGmISkDpWLpQSdj6x9nY+seQ**838CAA**AAMAAA**cycAV0aDG5B03I1yGae5xRgLpejJkdo+eRTCq/nWFgJxjBsYmlLbOemCxqvs0a+lQIjztv3/qOA6Y8t8l4LsJCoFx2S+9nqfZLK7X70cVvOdwJG3UdcSr4s7qIAfT5Kxao2xS3nna8PJ+7ZsVIUDr0dIYtdMeF+QxWbVOJY/NUbsKdygE37GZsYAR7A2nzugEeLPCHMQxWNYyTFc6U9K1du+Ikj4s62aY1MI75XlrIRiwT8GGfI2FnsV+PPTWlKNhG2KrQDVHMtKnFIe+ypICaIRJU3yZKSDX1YpgxyLvu2z84MlhrIBKaxi7wDmGZRrZgTC54/Rv4z/f2MMp1xX4U5+5HKW6Div0/rBJGMvVgfjLSy1tPl0lDgTtQ10DM7rBQ14LSkuUPo2IeMp81IwxOvbJUGc4XzEs/zYNA869d7kxaOcMJa1eWgrXg4S3nufMWegwW7DjkbMzXOxq0vvQIPUQz9EtbMbPiS5b07aGJeGiiFYEicnuSU8gVoSl18VqfW+vkOgUssZQdo8rcWoUCFVaVQ2eip3DO/xXh9e79FUAze9eQIbDIv1qYHS8F9XlSQgA/GfBk0IpBqwV/JDesLGxvlzCYtr0kPGbAL4O3zm7gzjuCmDRKJ4S1pWF+StxJeGOVZhZ0xP8yx7jaUBV/zl+A0zGG/QoPsP9PaaYy7lcgaYxial1tA26oGXmeoyN+aOJ+s2pqJzPlGzHb7bb9nDJWujY130IazVfc5mVNGZsDRDryZXWy++kZBdojD3";
		String server = "https://api.ebay.com/wsapi";

		cred.seteBayToken(token);	//input

		//set Api Server Url
		//	     input = ConsoleUtil.readString("Enter eBay SOAP server URL (e.g., https://api.sandbox.ebay.com/wsapi): ");

		apiContext.setApiServerUrl(server); //input

		return apiContext;
	}

}

//  
//  AgAAAA**AQAAAA**aAAAAA**VyEnVA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AGkIejAJaHpg2dj6x9nY+seQ**838CAA**AAMAAA**BQ15hokOx5RSIGzIAHNn1vmE6QDQSDGaa+MZJHvCq9FQ30cBoVTjZM4yAdqaGfcgrd4p9jek5knHl5GsLM9Hq9MeHKzcO4v97HYy8tMheHrdkId4IL6XyCw+OCwsLrYYiV+i+CXvsyhXP+2K4+/HF55/FzsnpSXrhdsJZCiwifqmHOSXC4xYYmgN7I7zzpdc5X4tkji/ZTwkTKZkYVc53oQLyZoFZzWGePqlMkDhDgp4cP2xAV6JYp6O/BNJnfszbZtJ/efvyyeUy90A6n29GNWm1rXZZTvEYOMJE2Tx4qSFVf468vb4YknQcpykGTT52BCN2ZGgC1ZYmkas+SByx5vv0KhGF1uQR5hFls3FhemM1VGBb6ulzYY82zNlsRvmx1N7gONmLHz4zqx6un1qTKYLKwyoF8SKhscDiKBEfoHSrCg8efjCxbaJ0yvhZv2v3oykgaMa6Oc08AY3P/VMl0dZjjDFm1I3cuNlUJ0+xez6Dix+MIk0g+uWavVAX1Mdx+2OU0TLOwmdy9yToUvODpi2sY8eaA64SByWdqq7zhMlPlBVHP6YnL7pjER4B0/pPXeFHN40Eka2Ri84Jj9B62O2AcGyRtZz16AJSoHEFzUnZJunLRIerS2RUsI/jDBZawnssr6CWYApK93SZJXDX1FrqVH1o4AfAPSRiwkNAXcpvoyiGSuHxOPRG3D0hJI4SWlJljox8CEMbtv2vmpbaxuBDSs5vfADD30bifVe68WCo8yE4X56ZnyNelQBjGxf

//this one worked 9/27/2014 5"14 pm
//  AgAAAA**AQAAAA**aAAAAA**xiQnVA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AGmISkDpWLpQSdj6x9nY+seQ**838CAA**AAMAAA**cycAV0aDG5B03I1yGae5xRgLpejJkdo+eRTCq/nWFgJxjBsYmlLbOemCxqvs0a+lQIjztv3/qOA6Y8t8l4LsJCoFx2S+9nqfZLK7X70cVvOdwJG3UdcSr4s7qIAfT5Kxao2xS3nna8PJ+7ZsVIUDr0dIYtdMeF+QxWbVOJY/NUbsKdygE37GZsYAR7A2nzugEeLPCHMQxWNYyTFc6U9K1du+Ikj4s62aY1MI75XlrIRiwT8GGfI2FnsV+PPTWlKNhG2KrQDVHMtKnFIe+ypICaIRJU3yZKSDX1YpgxyLvu2z84MlhrIBKaxi7wDmGZRrZgTC54/Rv4z/f2MMp1xX4U5+5HKW6Div0/rBJGMvVgfjLSy1tPl0lDgTtQ10DM7rBQ14LSkuUPo2IeMp81IwxOvbJUGc4XzEs/zYNA869d7kxaOcMJa1eWgrXg4S3nufMWegwW7DjkbMzXOxq0vvQIPUQz9EtbMbPiS5b07aGJeGiiFYEicnuSU8gVoSl18VqfW+vkOgUssZQdo8rcWoUCFVaVQ2eip3DO/xXh9e79FUAze9eQIbDIv1qYHS8F9XlSQgA/GfBk0IpBqwV/JDesLGxvlzCYtr0kPGbAL4O3zm7gzjuCmDRKJ4S1pWF+StxJeGOVZhZ0xP8yx7jaUBV/zl+A0zGG/QoPsP9PaaYy7lcgaYxial1tA26oGXmeoyN+aOJ+s2pqJzPlGzHb7bb9nDJWujY130IazVfc5mVNGZsDRDryZXWy++kZBdojD3
//  https://api.ebay.com/wsapi
//  https://api.ebay.com/wsapi

//SANDBOX:
// AgAAAA**AQAAAA**aAAAAA**AycnVA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFk4GhDJeKpAudj6x9nY+seQ**HA4DAA**AAMAAA**fJHxc4ozuEx+wOtVeGFiQ86ptc8/ootCpQ4n+CbybjKua43LZdtdFhtsbvueCLnqG7NR+VnEbINsb1BHsxwB5+urveHgwTMQpdrXLa3bR7RIal6UkjZ7oi4NUJshC/0IdiLTzxk6pX+nk2ZJCtOXc/rnBdTatUigcBeP8mWT7P8yJHJbOuXsEAk/8r++j2mHJGI1A9Tm2Q2SqWuo7PcbKX1XL1JOvISZ6N3hu70sQd5kL8QzPvSm0gsJUjMlwJ+rTZ4BBTL9nHwZz609ZIw0tWj9wAA0XroyDeIbPCKePjSuEXy2aTs3yUaxO/oQ2pkSiglqTMmiSn1RRDkFtdoJGsNXQhD8x1nT/aT6aRIWFIDGZjukkv8ygkDzVt6aKVMK807AXQDozaaoasC1FFnf/nx9Pky73tK8+uwhvh2LnjbQOkbu+trgV18rYyVF1GhU9fPactpIm4jaH4bo2crmP2QMB4F2hpwsGvO2jMa1BshpFAEF8B0bLA9HN/imLwr4CIsuneaVdjCJCqJ8CI0c5A+1OD0nctdKoCl/0bcxNgIDgDgj/guR95f1s8SPDpBzLEYf3XMci/xJsZ4odH97inZyf0F0+zvGAKCtvwF77yStPtQ9pNK+/kT4RFRmv4OEw0u17lKq/jKRftzUvEAEYQK4EDOhAStSjyJBnJMH7EgAMTiL0BXZAq2NqfsLhsqnUCwU1Dshce6sep+gF95xGlxssr/w3MBvY2p2+y1dMQ8nHlgJICI6UvpFlox6YSwZ