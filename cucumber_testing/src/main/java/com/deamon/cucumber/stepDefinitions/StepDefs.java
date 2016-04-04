package com.deamon.cucumber.stepDefinitions;

import java.util.List;

import javax.ws.rs.core.Response;

import org.json.JSONArray;
import com.deamon.stockapi.YahooFinanceApp;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import junit.framework.Assert;


public class StepDefs {
	
	
	List<String> symbolList;
	String symbolsToUse;
	YahooFinanceApp testApp;
    
	
	/**
	 * 
	 * @param arg1
	 * @throws Throwable
	 */
	@Given("^The valid stock symbol: <symbol>$")
	public void the_valid_stock_symbol_symbol(DataTable arg1) throws Throwable {
		
		symbolList = arg1.asList(String.class);
		symbolsToUse = symbolList.toString();
		symbolsToUse = symbolsToUse.substring(1, symbolsToUse.length()-1);
		testApp = new YahooFinanceApp();
	}

	@When("^Valid Yahoo results are returned$")
	public void valid_Yahoo_results_are_returned() throws Throwable {
		
		Response response = testApp.getStockInfo(symbolsToUse);
		Assert.assertTrue(response.getStatus() == 200);
		
	}

	@When("^Returned count match given count$")
	public void returned_count_match_given_count() throws Throwable {
		Assert.assertEquals(testApp.responseObject.getJSONObject("list").getJSONObject("meta").get("count"), symbolList.size());
	}
	
	@When("^Returned values match given values$")
	public void returned_values_match_given_values() throws Throwable {
		JSONArray resultArray = testApp.responseObject.getJSONObject("list").getJSONArray("resources");
		
		for(int a = 0; a < symbolList.size(); a++){
			Assert.assertEquals(symbolList.get(a), 
					resultArray.getJSONObject(a).getJSONObject("resource").getJSONObject("fields").get("symbol"));
		}
	}

	@Then("^Print Success$")
	public void print_Success() throws Throwable {
		System.out.println("Success with preset symbols");
	}
	
	
	
}
