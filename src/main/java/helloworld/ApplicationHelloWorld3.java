/*
Copyright (c) 2010 eBay, Inc.

This program is licensed under the terms of the eBay Common Development and 
Distribution License (CDDL) Version 1.0 (the "License") and any subsequent 
version thereof released by eBay.  The then-current version of the License 
can be found at https://www.codebase.ebay.com/Licenses.html and in the 
eBaySDKLicense file that is under the eBay SDK install directory.
 */

package helloworld;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.helper.ConsoleUtil;
import com.ebay.sdk.call.GetItemCall;
import com.ebay.sdk.call.GeteBayOfficialTimeCall;
import com.ebay.soap.eBLBaseComponents.ItemType;

/**
 * A Hello World-like sample, 
 * showing how to call eBay API using eBay SDK.
 *  
 * @author boyang
 *
 */
public class ApplicationHelloWorld3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println("starting at ........ " + (new Date()).toString());
			ApiContext apiContext = getApiContext();

			for (int i = 0; i < 4; i++)
				System.out.println("Official eBay Time : " + 
						(new GeteBayOfficialTimeCall(apiContext)).geteBayOfficialTime().getTime().toString());


			for (int i = 0; i < 10; i++){

				String itemID = "131313531183";

				GetItemCall api = new GetItemCall(apiContext);
				ItemType it = api.getItem(itemID);
				System.out.println("starting at ........ " + (new Date()).toString());


				//			try{ System.out.println("  1 " + it.getConditionDefinition()); 							}catch(Exception e){}
				try{ System.out.println("  2 " + it.getConditionDescription()); 							}catch(Exception e){}
				//			try{ System.out.println("  3 " + it.getConditionDisplayName()); 							}catch(Exception e){}
				try{ System.out.println("  4 " + it.getDescription().length()); 							}catch(Exception e){}
				//			try{ System.out.println("  5 " + it.getEBayNotes()); 							}catch(Exception e){}
				try{ System.out.println("  6 " + it.getListingDuration()); 							}catch(Exception e){}
				try{ System.out.println("  7 " + it.getSubTitle()); 							}catch(Exception e){}
				try{ System.out.println("  8 " + it.getTitle()); 							}catch(Exception e){}
				try{ System.out.println("  9 " + it.getWatchCount()); 							}catch(Exception e){}
				try{ System.out.println("  10 " + it.getAttributeArray().getAttributeLength()); 							}catch(Exception e){}
				try{ System.out.println("  11 " + it.getListingDetails().getMinimumBestOfferMessage().toString()); 							}catch(Exception e){}
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
		ApiCredential cred = apiContext.getApiCredential();
		String token = "AgAAAA**AQAAAA**aAAAAA**xiQnVA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AGmISkDpWLpQSdj6x9nY+seQ**838CAA**AAMAAA**cycAV0aDG5B03I1yGae5xRgLpejJkdo+eRTCq/nWFgJxjBsYmlLbOemCxqvs0a+lQIjztv3/qOA6Y8t8l4LsJCoFx2S+9nqfZLK7X70cVvOdwJG3UdcSr4s7qIAfT5Kxao2xS3nna8PJ+7ZsVIUDr0dIYtdMeF+QxWbVOJY/NUbsKdygE37GZsYAR7A2nzugEeLPCHMQxWNYyTFc6U9K1du+Ikj4s62aY1MI75XlrIRiwT8GGfI2FnsV+PPTWlKNhG2KrQDVHMtKnFIe+ypICaIRJU3yZKSDX1YpgxyLvu2z84MlhrIBKaxi7wDmGZRrZgTC54/Rv4z/f2MMp1xX4U5+5HKW6Div0/rBJGMvVgfjLSy1tPl0lDgTtQ10DM7rBQ14LSkuUPo2IeMp81IwxOvbJUGc4XzEs/zYNA869d7kxaOcMJa1eWgrXg4S3nufMWegwW7DjkbMzXOxq0vvQIPUQz9EtbMbPiS5b07aGJeGiiFYEicnuSU8gVoSl18VqfW+vkOgUssZQdo8rcWoUCFVaVQ2eip3DO/xXh9e79FUAze9eQIbDIv1qYHS8F9XlSQgA/GfBk0IpBqwV/JDesLGxvlzCYtr0kPGbAL4O3zm7gzjuCmDRKJ4S1pWF+StxJeGOVZhZ0xP8yx7jaUBV/zl+A0zGG/QoPsP9PaaYy7lcgaYxial1tA26oGXmeoyN+aOJ+s2pqJzPlGzHb7bb9nDJWujY130IazVfc5mVNGZsDRDryZXWy++kZBdojD3";
		String server = "https://api.ebay.com/wsapi";
		cred.seteBayToken(token);	//input
		apiContext.setApiServerUrl(server); //input
		return apiContext;
	}

}