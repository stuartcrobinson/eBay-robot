/*
Copyright (c) 2011 eBay, Inc.

This program is licensed under the terms of the eBay Common Development and 
Distribution License (CDDL) Version 1.0 (the "License") and any subsequent 
version thereof released by eBay.  The then-current version of the License 
can be found at https://www.codebase.ebay.com/Licenses.html and in the 
eBaySDKLicense file that is under the eBay SDK install directory.
 */
package helloworld;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.ItemFilter;
import com.ebay.services.finding.ItemFilterType;
import com.ebay.services.finding.PaginationInput;
import com.ebay.services.finding.SearchItem;
import com.ebay.services.finding.SortOrderType;


/**
 * A sample to show eBay Finding servcie call using the simplied interface 
 * provided by the findingKit.
 * @author boyang
 */
public class FindItem2 {
	public static  void main(String[] args) {
		try {


			ClientConfig config = new ClientConfig();
			config.setApplicationId("stuartro-dd31-4a26-881e-600984744b93");
			FindingServicePortType serviceClient = FindingServiceClientFactory.getServiceClient(config);

			//NOTE:  excluding terms in the keyword only excludes things in title.  NOT description.  wait maybe not...
			// :( :( :( :(  it DOES search description but NOT SELLERS NOTES :(
			It3m it3m = new It3m("iphone 5", "9355",
					new String[]{"1000", "1500", "1750", "2000", "2500", "3000", "4000", "5000"}, 
					new String[]{"broken", "scratched"},
					"1", "0", "275", "US");

			FindItemsAdvancedRequest request = it3m.getRequest(60, 100);
//			request.isDescriptionSearch();
			request.setDescriptionSearch(true);
//			request.
//			request.
//			request.setSortOrder(SortOrderType.BEST_MATCH);
			
			FindItemsAdvancedResponse result = serviceClient.findItemsAdvanced(request);


			SimpleDateFormat mysdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss Z XXX z");

			System.out.println("Ack = "+result.getAck());
			System.out.println("Find " + result.getSearchResult().getCount() + " items." );
			List<SearchItem> items = result.getSearchResult().getItem();


			for(SearchItem item : items) {


				Calendar endTimeCal = item.getListingInfo().getEndTime();

				System.out.println(item.getTitle());

				try{ System.out.println("  1 " + item.getSubtitle()); }catch(Exception e){}
				try{ System.out.println("  2 " + item.getCompatibility()); }catch(Exception e){}
				try{ System.out.println("  3 " + item.getSecondaryCategory().getCategoryName()); }catch(Exception e){}
				try{ System.out.println("  4 " + item.getDelimiter()); }catch(Exception e){}
				try{ System.out.println("  5 " + item.getListingInfo().getListingType()); }catch(Exception e){}
				try{ System.out.println("  6 " + item.getListingInfo().getBuyItNowPrice().getValue()); }catch(Exception e){}




				System.out.println("  " + item.getLocation());
				System.out.println("  " + mysdf.format(item.getListingInfo().getEndTime().getTime()));

				System.out.println("  " + item.getPrimaryCategory().getCategoryName());
				System.out.println("  " + item.getPrimaryCategory().getCategoryId());
				System.out.println("  " + (item.getProductId() == null ? "NA" : item.getProductId().getValue()));
				System.out.println("  " + item.getViewItemURL());
				System.out.println("  " + (item.getShippingInfo().getShippingServiceCost() == null ? "NA shipping" : item.getShippingInfo().getShippingServiceCost().getValue()));
				System.out.println("  " + item.getShippingInfo().getShippingType());
				System.out.println("  " + item.getCondition().getConditionDisplayName());
				System.out.println("  " + item.getSellingStatus().getCurrentPrice().getValue());
				System.out.println("  " + item.getSellingStatus().getTimeLeft().toString());
				
				
				System.out.println("  " + 
						item.getSellingStatus().getTimeLeft().getHours() + ":" +
						item.getSellingStatus().getTimeLeft().getMinutes()+ ":" + 
						item.getSellingStatus().getTimeLeft().getSeconds());
			}

		} catch (Exception ex) {ex.printStackTrace(); }
	}

}

