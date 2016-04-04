package com.deamon.cucumber.stepDefinitions;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import com.deamon.stockapi.YahooFinanceApp;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import junit.framework.Assert;


public class StepDefs2 {

	JSONArray stockArray = new JSONArray();
	String symbolsToUse;
	YahooFinanceApp testApp;
	List<Buy> buyCriteria;
	List<Sell> sellCriteria;

	/**
	 * A cucumber test that reads and parses a .csv file and maps it to an iterable datastructure.
	 * @throws Throwable
	 */
	@Given("^A csv of stock data$")
	public void a_csv_of_stock_data() throws Throwable {

		//Read in CSV
		Reader in = new FileReader("CSV/stocks.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);

		//Parse header, and other rows
		ArrayList<String> headerList = new ArrayList<String>();
		boolean header = true;
		String headers;
		//Loop through the csv rows
		for (CSVRecord record : records) {
			if(header){		//Header parsing
				headers = record.toString().substring(record.toString().indexOf("values=[")+8, record.toString().indexOf("]]")) ;

				while(headers.contains(",")){
					headerList.add(headers.substring(0, headers.indexOf(",")));
					headers = headers.substring(headers.indexOf(", ")+2);
				}

				headerList.add(headers.substring(0));
				header = false;
			}
			else{			//Normal row parsing

				String row = record.toString().substring(record.toString().indexOf("values=[")+8, record.toString().indexOf("]]"));
				//Parse String into JSON
				JSONObject tempRow = new JSONObject();
				for(int b = 0; b < headerList.size()-1; b++){
					tempRow.put(headerList.get(b), row.substring(0, row.indexOf(",")));
					row = row.substring(row.indexOf(", ")+2);
				}

				//Add JSONObject to JSONArray
				tempRow.put(headerList.get(headerList.size()-1), row);
				stockArray.put(tempRow);


				//Update symbol list for REST call

				symbolsToUse = symbolsToUse + ", " +tempRow.get("symbol");

			}

		}

		//Now we have a JSONArray of stock info -> stockArray

		//Instantiate an instance of our Stock App for testing.
		testApp = new YahooFinanceApp();

	}

	/**
	 * Verify return code
	 * @throws Throwable
	 */
	@When("^Valid REST results are returned$")
	public void valid_REST_results_are_returned() throws Throwable {

		Response response = testApp.getStockInfo(symbolsToUse);
		Assert.assertTrue(response.getStatus() == 200);

	}

	/**
	 * Check that stock counts match
	 * @throws Throwable
	 */
	@When("^Returned symbol count matches csv symbol count$")
	public void returned_symbol_count_matches_csv_symbol_count() throws Throwable {
		int count = testApp.responseObject.getJSONObject("list").getJSONObject("meta").getInt("count");
		Assert.assertEquals(count, stockArray.length());
	}

	/**
	 * Loop through both arrays and check that symbols match
	 * @throws Throwable
	 */
	@When("^Returned symbols match csv symbols$")
	public void returned_symbols_match_csv_symbols() throws Throwable {
		JSONArray returnedArray = testApp.responseObject.getJSONObject("list").getJSONArray("resources");
		for(int a = 0; a < returnedArray.length(); a++){
			String sym = returnedArray.getJSONObject(a).getJSONObject("resource").getJSONObject("fields").getString("symbol");
			Assert.assertEquals(sym, stockArray.getJSONObject(a).getString("symbol"));
		}
	}

	/**
	 * Check that issuer names match
	 * @throws Throwable
	 */
	@When("^Returned issuer name matches csv issuer name$")
	public void returned_issuer_name_matches_csv_issuer_name() throws Throwable {
		JSONArray returnedArray = testApp.responseObject.getJSONObject("list").getJSONArray("resources");
		for(int a = 0; a < returnedArray.length(); a++){
			String sym = returnedArray.getJSONObject(a).getJSONObject("resource").getJSONObject("fields").getString("issuer_name");
			Assert.assertEquals(sym, stockArray.getJSONObject(a).getString("issuer_name"));
		}
	}

	/**
	 * Check that stock types match
	 * @throws Throwable
	 */
	@When("^Returned type matches csv type$")
	public void returned_type_matches_csv_type() throws Throwable {
		JSONArray returnedArray = testApp.responseObject.getJSONObject("list").getJSONArray("resources");
		for(int a = 0; a < returnedArray.length(); a++){
			String sym = returnedArray.getJSONObject(a).getJSONObject("resource").getJSONObject("fields").getString("type");
			Assert.assertEquals(sym, stockArray.getJSONObject(a).getString("type"));
		}
	}

	@Then("^Print csv success$")
	public void print_csv_success() throws Throwable {
		System.out.println("\nSuccess with csv symbols");
	}


	//----- Criteria Tests ------//

	@Given("^A list of buy requirements$")
	public void a_list_of_buy_requirements(List<Buy> criteria) throws Throwable {

		buyCriteria = criteria;
		for(Buy item : buyCriteria){
			item.analyze();
		}
	}


	@Given("^A list of sell requirements$")
	public void a_list_of_sell_requirements(List<Sell> criteria) throws Throwable {

		sellCriteria = criteria;
		for(Sell item : sellCriteria){
			item.analyze();
		}
	}


	@When("^Valid Query results are returned$")
	public void valid_Query_results_are_returned() throws Throwable {

		Response response = testApp.doStockQuery(stockArray);
		Assert.assertTrue(response.getStatus() == 200);

	}

	
	@When("^Stock is large cap and meets buy criteria$")
	public void stock_is_large_cap_and_meets_buy_criteria() throws Throwable {

		String priceComp = buyCriteria.get(0).getlPriceComp();
		String yieldComp = buyCriteria.get(0).getlYieldComp();
		String peComp = buyCriteria.get(0).getlPEComp();

		double priceDub = buyCriteria.get(0).getLargePricedub();
		double yieldDub = buyCriteria.get(0).getLargeYielddub();
		double peDub = buyCriteria.get(0).getLargePEdub();

		Assert.assertTrue(buyStock(priceComp, yieldComp, peComp, 
				priceDub, yieldDub, peDub, 
				10*testApp.suffixes.get("B"), 
				1000*testApp.suffixes.get("B")));


	}

	@When("^Stock is med cap and meets buy criteria$")
	public void stock_is_med_cap_and_meets_buy_criteria() throws Throwable {

		String priceComp = buyCriteria.get(0).getmPriceComp();
		String yieldComp = buyCriteria.get(0).getmYieldComp();
		String peComp = buyCriteria.get(0).getmPEComp();

		double priceDub = buyCriteria.get(0).getMedPricedub();
		double yieldDub = buyCriteria.get(0).getMedYielddub();
		double peDub = buyCriteria.get(0).getMedPEdub();

		Assert.assertTrue(buyStock(priceComp, yieldComp, peComp, 
				priceDub, yieldDub, peDub, 
				2*testApp.suffixes.get("B"), 
				10*testApp.suffixes.get("B")));

	}

	@When("^Stock is small cap and meets buy criteria$")
	public void stock_is_small_cap_and_meets_buy_criteria() throws Throwable {

		String priceComp = buyCriteria.get(0).getsPriceComp();
		String yieldComp = buyCriteria.get(0).getsYieldComp();
		String peComp = buyCriteria.get(0).getsPEComp();

		double priceDub = buyCriteria.get(0).getSmPricedub();
		double yieldDub = buyCriteria.get(0).getSmYielddub();
		double peDub = buyCriteria.get(0).getSmPEdub();

		Assert.assertTrue(buyStock(priceComp, yieldComp, peComp, 
				priceDub, yieldDub, peDub, 
				10*testApp.suffixes.get("B"), 
				1000*testApp.suffixes.get("B")));

	}

	
	@When("^Stock is large cap and meets sell criteria$")
	public void stock_is_large_cap_and_meets_sell_criteria() throws Throwable {
		
		String priceComp = sellCriteria.get(0).getlPriceComp();
		String peComp = sellCriteria.get(0).getlPEComp();

		double priceDub = sellCriteria.get(0).getLargePricedub();
		double peDub = sellCriteria.get(0).getLargePEdub();
		
		Assert.assertTrue(sellStock(priceComp, peComp, 
				priceDub, peDub, 
				2*testApp.suffixes.get("B"), 
				10*testApp.suffixes.get("B")));
	}

	@When("^Stock is med cap and meets sell criteria$")
	public void stock_is_med_cap_and_meets_sell_criteria() throws Throwable {
		
		String priceComp = sellCriteria.get(0).getmPriceComp();
		String peComp = sellCriteria.get(0).getmPEComp();

		double priceDub = sellCriteria.get(0).getMedPricedub();
		double peDub = sellCriteria.get(0).getMedPEdub();
		
		Assert.assertTrue(sellStock(priceComp, peComp, 
				priceDub, peDub, 
				200*testApp.suffixes.get("M"), 
				2*testApp.suffixes.get("B")));
		
	}

	@When("^Stock is small cap and meets sell criteria$")
	public void stock_is_small_cap_and_meets_sell_criteria() throws Throwable {
		
		String priceComp = sellCriteria.get(0).getsPriceComp();
		String peComp = sellCriteria.get(0).getsPEComp();

		double priceDub = sellCriteria.get(0).getSmPricedub();
		double peDub = sellCriteria.get(0).getSmPEdub();
		
		Assert.assertTrue(sellStock(priceComp, peComp, 
				priceDub, peDub, 
				200*testApp.suffixes.get("M"), 
				2*testApp.suffixes.get("B")));
		
	}
	

	@Then("^Print criteria success$")
	public void print_criteria_success() throws Throwable {
		System.out.println("\nSuccess with criteria");
	}
	
	
	
	/**
	 * Method to check buy criteria against saved stock information
	 * @param priceComp
	 * @param yieldComp
	 * @param peComp
	 * @param priceDub
	 * @param yieldDub
	 * @param peDub
	 * @param marketCapStart
	 * @param marketCapEnd
	 * @return boolean
	 */
	private boolean buyStock(String priceComp, 
			String yieldComp, 
			String peComp,
			double priceDub,
			double yieldDub,
			double peDub,
			double marketCapStart,
			double marketCapEnd){

		boolean result = true;

		for(int a = 0; a < testApp.buyArray.length(); a++){

			double fiftyDayAvg = stockArray.getJSONObject(a).getDouble("FiftydayMovingAverage");
			double bid = stockArray.getJSONObject(a).getDouble("Bid");
			double divYield = stockArray.getJSONObject(a).getDouble("DividendYield");
			double peRatio = stockArray.getJSONObject(a).getDouble("PERatio");

			//Parse Market Cap - form: 205B (int)+(string)
			String marketCap = stockArray.getJSONObject(a).getString("MarketCapitalization");
			String marketCapSuf = marketCap.substring(marketCap.length()-1);
			double marketCapAmt = Double.parseDouble(marketCap.substring(0, marketCap.length()-1));
			marketCapAmt = marketCapAmt*testApp.suffixes.get(marketCapSuf);


			if(marketCapAmt >= marketCapStart && marketCapAmt < marketCapEnd){

				if(priceComp.equalsIgnoreCase("g") && (bid > fiftyDayAvg*priceDub)){}
				else if(priceComp.equalsIgnoreCase("l") && (bid < fiftyDayAvg*priceDub)){}
				else if(priceComp.equalsIgnoreCase("e") && (bid == fiftyDayAvg*priceDub)){}
				else {
					System.out.println("Failed price");
					result = false;
				}

				if(yieldComp.equalsIgnoreCase("g") && (divYield > yieldDub)){}
				else if(yieldComp.equalsIgnoreCase("l") && (divYield < yieldDub)){}
				else if(yieldComp.equalsIgnoreCase("e") && (divYield == yieldDub)){}
				else {
					System.out.println("Failed yield");
					result = false;
				}

				if(peComp.equalsIgnoreCase("g") && (peRatio > peDub)){}
				else if(peComp.equalsIgnoreCase("l") && (peRatio < peDub)){}
				else if(peComp.equalsIgnoreCase("e") && (peRatio == peDub)){}
				else {
					System.out.println("Failed pe");
					result = false;
				}

			}
			else
				result = true;

		}

		return result;

	}


	/**
	 * Method to check sell criteria against saved stock information
	 * @param priceComp - String Comparator
	 * @param peComp - String Comparator
	 * @param priceDub - double percent of 50 Week Avg
	 * @param peDub - double PE Ratio
	 * @param marketCapStart - Market Cap Start Boundary
	 * @param marketCapEnd - Market Cap End Boundary
	 * @return boolean 
	 */
	private boolean sellStock(String priceComp,
			String peComp,
			double priceDub,
			double peDub,
			double marketCapStart,
			double marketCapEnd){

		boolean result = true;

		for(int a = 0; a < testApp.buyArray.length(); a++){

			double fiftyDayAvg = stockArray.getJSONObject(a).getDouble("FiftydayMovingAverage");
			double bid = stockArray.getJSONObject(a).getDouble("Bid");
			double peRatio = stockArray.getJSONObject(a).getDouble("PERatio");

			//Parse Market Cap - form: 205B (int)+(string)
			String marketCap = stockArray.getJSONObject(a).getString("MarketCapitalization");
			String marketCapSuf = marketCap.substring(marketCap.length()-1);
			double marketCapAmt = Double.parseDouble(marketCap.substring(0, marketCap.length()-1));
			marketCapAmt = marketCapAmt*testApp.suffixes.get(marketCapSuf);


			if(marketCapAmt >= marketCapStart && marketCapAmt < marketCapEnd){

				if(priceComp.equalsIgnoreCase("g") && (bid > fiftyDayAvg*priceDub)){}
				else if(priceComp.equalsIgnoreCase("l") && (bid < fiftyDayAvg*priceDub)){}
				else if(priceComp.equalsIgnoreCase("e") && (bid == fiftyDayAvg*priceDub)){}
				else {
					System.out.println("Failed price");
					result = false;
				}

				if(peComp.equalsIgnoreCase("g") && (peRatio > peDub)){}
				else if(peComp.equalsIgnoreCase("l") && (peRatio < peDub)){}
				else if(peComp.equalsIgnoreCase("e") && (peRatio == peDub)){}
				else {
					System.out.println("Failed pe");
					result = false;
				}

			}
			else
				result = true;

		}

		return result;

	}

	
	
	

}
