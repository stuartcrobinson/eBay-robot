package helloworld;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.ebay.services.finding.ItemFilterType;

public class Helper {

	public static String cellPhoneCategory = "9355";
	public static String hardDrivesCategory = "158830";
	public static String CpusProcessorsCat = "164";
	public static String motherboardsCat = "1244";
	public static String micsCat = "29946";
	public static String smartphoneNegators = "iphone (-water, \"no water\") -description -blocked -locked -blacklisted -list -crack -parts -\"not work\"";// -bad -blacklisted -broken -crack -damage -doesn't -photos -poor -work -parts -read -list -repair"; //does

	public static List<AvoidanceTerm> setIphoneAvoidanceTerms(){

		String[] mediumRedeemers = {
				"no",
				"not",
				"never",
				"tiny",
				"slight",
				"tiny",
				"one",
				"a",
				"hairline",
				"hair line",
				"small",
				"a small",	//TODO redundant
				"one small",
				"light",
				"lite",
				"minor",
		"nothing"};

		String[] evilRedeemers = {
				"free of",
				"free from",
				"no",
				"not",
				"never",
		"nothing"};

		String[] isnEmeiRedeemers = {
				"good",
				"clean",
				"great",
		"verified"};

		String[] repairedReplacedRedeemers = {
				"i",
				"they",
				"new",
				"we",
				"apple",
				"was",
				"has been",
		"i"};

		String[] boxRedeemers = {
				"original",
				"in",
				"w/",
				"w/ original",
				"with original",
				"with"
		};

		List<AvoidanceTerm> terms = new ArrayList<AvoidanceTerm>();

		terms.add(new AvoidanceTerm("activate", new String[]{"ready to", "can", "to"}));
		terms.add(new AvoidanceTerm("can't be activated",  null));
		terms.add(new AvoidanceTerm("unable to be activated",  null));
		terms.add(new AvoidanceTerm("unable to activate",  null));
		terms.add(new AvoidanceTerm("bad", null));
		terms.add(new AvoidanceTerm("be replaced", null));
		terms.add(new AvoidanceTerm("blacklisted", evilRedeemers));
		terms.add(new AvoidanceTerm("blemish", null));
		terms.add(new AvoidanceTerm("block", ArrayUtils.addAll(evilRedeemers, "will")));
		terms.add(new AvoidanceTerm("blocked", evilRedeemers));
		terms.add(new AvoidanceTerm("box", boxRedeemers));
		terms.add(new AvoidanceTerm("broke", evilRedeemers));
		terms.add(new AvoidanceTerm("broken", ArrayUtils.addAll(evilRedeemers, "the")));
		terms.add(new AvoidanceTerm("cloud", ArrayUtils.addAll(evilRedeemers, isnEmeiRedeemers)));
		terms.add(new AvoidanceTerm("crack", mediumRedeemers));
		terms.add(new AvoidanceTerm("cracked", evilRedeemers));
		terms.add(new AvoidanceTerm("cracks", evilRedeemers));
		terms.add(new AvoidanceTerm("damage", evilRedeemers));
		terms.add(new AvoidanceTerm("damaged", ArrayUtils.addAll(evilRedeemers, "arrives")));
		terms.add(new AvoidanceTerm("dark", null));
		terms.add(new AvoidanceTerm("defective", null));
		terms.add(new AvoidanceTerm("dent", mediumRedeemers));
		terms.add(new AvoidanceTerm("dents", mediumRedeemers));
		terms.add(new AvoidanceTerm("died", null));
		terms.add(new AvoidanceTerm("dies", null));
		terms.add(new AvoidanceTerm("disable", null));
		terms.add(new AvoidanceTerm("disabled", null));
		terms.add(new AvoidanceTerm("does not", null));
		terms.add(new AvoidanceTerm("does not turn on", null));
		terms.add(new AvoidanceTerm("does not work", null));
		terms.add(new AvoidanceTerm("doesn't", null));
		terms.add(new AvoidanceTerm("doesn't work", null));
		terms.add(new AvoidanceTerm("doesnt", null));
		terms.add(new AvoidanceTerm("doesnt work", null));
		terms.add(new AvoidanceTerm("don't know", null));
		terms.add(new AvoidanceTerm("drop", evilRedeemers));
		terms.add(new AvoidanceTerm("dropped", evilRedeemers));
		terms.add(new AvoidanceTerm("esn", isnEmeiRedeemers));
		terms.add(new AvoidanceTerm("fails", null));
		terms.add(new AvoidanceTerm("faulty", null));
		terms.add(new AvoidanceTerm("fixable", null));
		terms.add(new AvoidanceTerm("for parts", null));
		terms.add(new AvoidanceTerm("fracture", evilRedeemers));
		terms.add(new AvoidanceTerm("fractured", evilRedeemers));
		terms.add(new AvoidanceTerm("fractures", evilRedeemers));
		terms.add(new AvoidanceTerm("icloud lock", evilRedeemers));
		terms.add(new AvoidanceTerm("imei", isnEmeiRedeemers));
		terms.add(new AvoidanceTerm("inconsistent", null));
		terms.add(new AvoidanceTerm("l0ck", null));
		terms.add(new AvoidanceTerm("lines", null));
		terms.add(new AvoidanceTerm("list", evilRedeemers));
		terms.add(new AvoidanceTerm("listed", evilRedeemers));
		terms.add(new AvoidanceTerm("lock", evilRedeemers));
		terms.add(new AvoidanceTerm("locked", evilRedeemers));
		terms.add(new AvoidanceTerm("loud", null));
		terms.add(new AvoidanceTerm("malfuntion", null));
		terms.add(new AvoidanceTerm("messed up", null));
		terms.add(new AvoidanceTerm("needs", new String[]{"modern"}));
		terms.add(new AvoidanceTerm("need", null));
		terms.add(new AvoidanceTerm("not clean", null));
		terms.add(new AvoidanceTerm("not working", null));
		terms.add(new AvoidanceTerm("password", null));
		//terms.add(new AvoidanceTerm("photos", null));//TODO not sure about these ... clean phones say look at pictures too.
		//terms.add(new AvoidanceTerm("pictures", null));
		terms.add(new AvoidanceTerm("please look", null));
		terms.add(new AvoidanceTerm("please read", null));
		terms.add(new AvoidanceTerm("poor", null));
		terms.add(new AvoidanceTerm("run over", null));
		terms.add(new AvoidanceTerm("ran over", null));
		terms.add(new AvoidanceTerm("read description ", null));
		terms.add(new AvoidanceTerm("registered", new String[]{"inc", "your"}));		//Siri are trademarks of Apple Inc., registered in the U.S. an
		terms.add(new AvoidanceTerm("repaired", repairedReplacedRedeemers));
		terms.add(new AvoidanceTerm("repairs", repairedReplacedRedeemers));
		terms.add(new AvoidanceTerm("replaced", repairedReplacedRedeemers));
		terms.add(new AvoidanceTerm("shadow", null));
		terms.add(new AvoidanceTerm("shadows", null));
		terms.add(new AvoidanceTerm("shattered", null));
		terms.add(new AvoidanceTerm("slow", null));
		terms.add(new AvoidanceTerm("spot", null));
		terms.add(new AvoidanceTerm("spots", null));
		terms.add(new AvoidanceTerm("stuck", null));
		terms.add(new AvoidanceTerm("this screen", null));
		terms.add(new AvoidanceTerm("to replace", null));
		terms.add(new AvoidanceTerm("turn on", null));
		terms.add(new AvoidanceTerm("unresponsive", null));
		terms.add(new AvoidanceTerm("water damage", evilRedeemers));
		terms.add(new AvoidanceTerm("went black", null));

		return terms;
	}

	public static List<It3m> getIt3ms(int spread) throws InterruptedException, IOException{


		List<It3m> it3ms = new ArrayList<It3m>();

		List<AvoidanceTerm> iphoneAvoidanceTerms = Helper.setIphoneAvoidanceTerms();
		//String iPhoneKeywordAvoidanceAppendage = Helper.getAvoidanceKeywordAppendage(iphoneAvoidanceTerms);

		List<IFilter> iFiltersList = Arrays.asList(
				new IFilter[]{
						new IFilter(ItemFilterType.LISTING_TYPE, new String[]{"Auction", "AuctionWithBIN"}),
						new IFilter(ItemFilterType.LOCATED_IN, "US"),
						new IFilter(ItemFilterType.CONDITION, new String[]{"1000", "1500", "1750", "2000", "2500", "3000", "4000", "5000"})
				});

		it3ms.add(new It3m("Samsung 840 EVO 500gb", "", 		Helper.hardDrivesCategory, iphoneAvoidanceTerms, iFiltersList, null));
		it3ms.add(new It3m("Crucial MX100 512GB", "", 			Helper.hardDrivesCategory, iphoneAvoidanceTerms, iFiltersList, null));
		it3ms.add(new It3m("i7-4790k", "", 						Helper.CpusProcessorsCat, iphoneAvoidanceTerms, iFiltersList, null));
		it3ms.add(new It3m("i5-3570K", "", 						Helper.CpusProcessorsCat, iphoneAvoidanceTerms, iFiltersList, null));
		it3ms.add(new It3m("i7-3930K", "", 						Helper.CpusProcessorsCat, iphoneAvoidanceTerms, iFiltersList, null));
		it3ms.add(new It3m("Asus Rampage IV black", "", 		Helper.motherboardsCat, iphoneAvoidanceTerms, iFiltersList, null));
		it3ms.add(new It3m("Asus Rampage IV extreme", "", 		Helper.motherboardsCat, iphoneAvoidanceTerms, iFiltersList, null));
		it3ms.add(new It3m("akg c214", "", 						Helper.micsCat, iphoneAvoidanceTerms, iFiltersList, null));
		it3ms.add(new It3m("blue microphones baby bottle", "", 	Helper.micsCat, iphoneAvoidanceTerms, iFiltersList, null));
		it3ms.add(new It3m("blue microphones bluebird", "", 		Helper.micsCat, iphoneAvoidanceTerms, iFiltersList, null));
		it3ms.add(new It3m("Neumann TLM-103", "", 				Helper.micsCat, iphoneAvoidanceTerms, iFiltersList, null));
		it3ms.add(new It3m("rode ntg2", "", 					Helper.micsCat, iphoneAvoidanceTerms, iFiltersList, null));
		it3ms.add(new It3m("shure sm7b", "", 					Helper.micsCat, iphoneAvoidanceTerms, iFiltersList, null));

		It3m iphone = new It3m("iphone", Helper.smartphoneNegators, Helper.cellPhoneCategory, iphoneAvoidanceTerms, iFiltersList, null);

		it3ms.addAll(iphone.listOfIt3msExtendedFromTemplate(
				new String[][]{ 
						new String[]{"Model", "iPhone 4s"},
						new String[]{"Carrier", "AT&T", "Verizon", "Sprint", "Unlocked"},
						new String[]{"Color", "", "Black", "White"},
						new String[]{"Storage Capacity", "16GB", "32GB", "64GB"}}));
		it3ms.addAll(iphone.listOfIt3msExtendedFromTemplate(
				new String[][]{ 
						new String[]{"Model", "iPhone 5"},
						new String[]{"Carrier", "AT&T", "Verizon", "Sprint", "Unlocked"},
						new String[]{"Color", "", "Black", "White"},
						new String[]{"Storage Capacity", "", "16GB", "32GB", "64GB"}}));
		it3ms.addAll(iphone.listOfIt3msExtendedFromTemplate(
				new String[][]{ 
						new String[]{"Model", "iPhone 5c"},
						new String[]{"Carrier", "AT&T", "Verizon", "Sprint", "Unlocked"},
						//new String[]{"Color", "", "Blue", "Pink", "Green", "Yellow", "White"},
						new String[]{"Storage Capacity", "", "16GB", "32GB"}}));
		it3ms.addAll(iphone.listOfIt3msExtendedFromTemplate(
				new String[][]{ 
						new String[]{"Model", "iPhone 5s"},
						new String[]{"Carrier", "AT&T", "Verizon", "Sprint", "Unlocked"},
						//new String[]{"Color", "", "Space Gray", "Silver", "Gold" },
						new String[]{"Storage Capacity", "", "16GB", "32GB", "64GB"}}));
		it3ms.addAll(iphone.listOfIt3msExtendedFromTemplate(
				new String[][]{ 
						new String[]{"Model", "iPhone 6", "iPhone 6 Plus"},
						new String[]{"Carrier", "AT&T", "Verizon", "Sprint", "Unlocked"},
						new String[]{"Color", "", "Space Gray", "Silver", "Gold" },
						new String[]{"Storage Capacity", "", "16GB", "64GB", "128GB"}}));

		It3m galaxy = new It3m("galaxy", Helper.smartphoneNegators, Helper.cellPhoneCategory, iphoneAvoidanceTerms, iFiltersList, null);

		it3ms.addAll(galaxy.listOfIt3msExtendedFromTemplate(
				new String[][]{ 
						new String[]{"Model", "galaxy s4", "galaxy s5"},
						new String[]{"Carrier", "AT&T", "Verizon", "Sprint", "Unlocked"},
						new String[]{"Color", "", "White", "Black", "Blue" },
						new String[]{"Storage Capacity", "16GB", "32GB"}}));



		//File f = new File(filePathString);
		//if(f.exists() && !f.isDirectory()) { /* do something */ }

		File f = new File("c:/temp/amazonpricefile.txt");

		//if no file, then get new prices



		if (!f.exists()){
			System.out.println("starting driver...");
			WebDriver driver = new HtmlUnitDriver();

			//TODO see if this helps	
			//	driver.manage().timeouts().
			//	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

			StringBuilder sb = new StringBuilder();
			for (It3m it3m : it3ms){
				it3m.set_amazon_low_price(driver);
				sb.append(it3m.getID());
				sb.append(", ");
				sb.append(it3m.amazon_low_price);
				sb.append("\n");
			}
			//write to file
			Files.write(Paths.get(f.getPath()), sb.toString().getBytes());

		} else{
			//else, read prices from file.	

			List<String> lines = Files.readAllLines(Paths.get(f.getPath()), Charset.defaultCharset());

			Map<String, String> map = new HashMap<String, String>();
			for (String line : lines){
				String[] lineAr = line.split(", ");
				map.put(lineAr[0],  lineAr[1]);
			}
			for (It3m it3m : it3ms){
				it3m.amazon_low_price = map.get(it3m.getID());
			}
		}

		int nullCount = 0;
		for (int i = 0; true; i++){
			if (i == it3ms.size())
				break;
			try{
				Double.parseDouble(it3ms.get(i).amazon_low_price.replaceAll(",", ""));
			}catch (Exception e){ 
				it3ms.remove(i--);
				nullCount++;
			}
		}
		System.out.println(nullCount + " nullCount");

		for (It3m it3m : it3ms){
			//	System.out.println(it3m.amazon_low_price + " " + it3m.getID());
			try{
				it3m.ebay_max_price = Double.parseDouble(it3m.amazon_low_price.replaceAll(",", "")) - spread;
			}catch (Exception e){ 
				it3ms.remove(it3m);
			}
		}



		System.out.println("finished here");
		return it3ms;
	}

	//
	//	public static String getAmazonPrice(WebDriver driver, List<String> keywords){
	//
	//
	//String url = it3m.getAmazonSearchURL();
	//
	//"http://www.amazon.com/s/keywords="; // Apple \"iPhone+5c\"  Verizon 16GB ";
	//for (String word : keywords){
	//	url += " \"" + word + "\" ";
	//}
	//System.out.println();
	//System.out.println(url);
	//
	//driver.get(url);
	//List<WebElement> wes = driver.findElements(By.xpath("//*[contains(@id,'result')]/div/div/div/div[2]"));
	//
	//String patternString = "(.*)\\$([\\d+\\.]*)used\\((\\d+) offer(.*)";
	//
	//for (WebElement we : wes){
	//
	//	String text = we.getText();
	//	String title =  we.findElement(By.xpath("div/a")).getText();
	//	boolean skipThis = false;
	//	for (String keyword : keywords){
	//if (!title.contains(keyword)){
	//	skipThis = true;
	//	break;
	//}
	//	}
	//	if (skipThis) continue;
	//	Pattern pattern = Pattern.compile(patternString);
	//	Matcher m = pattern.matcher(text);
	//	System.out.println("Title:      " + title);
	//	if (m.find( ) ){
	//System.out.println("Used price: " + m.group(2) );
	//System.out.println("Quantity:   " + m.group(3) );
	//return m.group(2);
	//	}else 
	//System.out.println("NO MATCH.");
	//
	//}
	//
	//return null;
	//	}
}
