package com.deamon.stockapi;

import java.net.URI;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

public class YahooFinanceApp {

	public JSONObject responseObject;
	public JSONArray buyArray;
	public JSONArray sellArray;

	public static final Map<String, Long> suffixes = new TreeMap<String, Long> ();
	static {
		suffixes.put("k", 1000L);
		suffixes.put("M", 1000000L);
		suffixes.put("G", 1000000000L);
		suffixes.put("B", 1000000000L);
		suffixes.put("T", 1000000000000L);
		suffixes.put("P", 1000000000000000L);
		suffixes.put("E", 1000000000000000000L);
	}

	//Hardcoded. Could be calculated from multiple algorithms and input sources
	final private double largePEBuy = 15;
	final private double largePESell = 17;
	final private double largefiftyWeekPercBuy = .25;
	final private double largefiftyWeekPercSell = .85;
	final private double largeYield = 2;

	final private double medPEBuy = 17;
	final private double medPESell = 20;
	final private double medfiftyWeekPercBuy = .50;
	final private double medfiftyWeekPercSell = .75;
	final private double medYield = 1.5;

	final private double smPEBuy = 17;
	final private double smPESell = 33;
	final private double smfiftyWeekPercBuy = .75;
	final private double smfiftyWeekPercSell = .65;
	final private double smYield = 1;

	/**
	 * Get a small amount of data pertaining to a list of stock symbolls
	 * @param symbolsToUse - comma separated string of symbols (ie. "sym1, sym2, sym3")
	 * @return Response object
	 */
	public Response getStockInfo(String symbolsToUse){
		String yahooStringFront = "http://finance.yahoo.com/webservice/v1/symbols/";
		String yahooStringBack = "/quote?format=json&view=detail";

		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(yahooStringFront+symbolsToUse+yahooStringBack);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
		Response response = invocationBuilder.get();

		responseObject = new JSONObject(response.readEntity(String.class));
		return response;

	}

	/**
	 * Use yahoo.finance.quotes REST service with 'limited' db query to retrieve detailed stock info
	 * The query is limited in that It can not just return all symbols, and can not perform math in the query
	 * @param stockArray - Array of stocks, in JSONObject form
	 * @return Response object
	 */
	public Response doStockQuery(JSONArray stockArray){

		String frontUrl = "http://query.yahooapis.com/v1/public/yql?q=";
		String endUrl = "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

		String query = "select * from yahoo.finance.quotes where symbol in (";

		//Parse symbols to ("YHOO","AAPL","GOOG","MSFT") form
		for(int a = 0; a < stockArray.length(); a++){

			if(a == stockArray.length()-1){
				query = query + "\""+stockArray.getJSONObject(a).getString("symbol")+"\") ";
			}
			else{
				query = query + "\""+stockArray.getJSONObject(a).getString("symbol")+"\",";
			}
		}

		URI uri = UriBuilder.fromUri(frontUrl)
				.replaceQuery("q="+query+endUrl)
				.build();


		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(uri);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
		Response response = invocationBuilder.get();

		responseObject = new JSONObject(response.readEntity(String.class));

		analyzeStocks(responseObject.getJSONObject("query").getJSONObject("results").getJSONArray("quote"));

		
		return response;

	}

	/**
	 * Anaylze given stock information against Finance Applications requirements to buy and sell.
	 * Could be refactored into multiple methods
	 * @param stockArray - JSONArray of stocks
	 */
	private void analyzeStocks(JSONArray stockArray){
		buyArray = new JSONArray();
		sellArray = new JSONArray();

		for(int a = 0; a < stockArray.length(); a++){

			// Some objects dont always have correct values
			if (!stockArray.getJSONObject(a).isNull("DividendYield") 
					&& !stockArray.getJSONObject(a).isNull("FiftydayMovingAverage")
					&& !stockArray.getJSONObject(a).isNull("Bid")
					&& !stockArray.getJSONObject(a).isNull("PERatio")){



				double fiftyDayAvg = stockArray.getJSONObject(a).getDouble("FiftydayMovingAverage");
				double bid = stockArray.getJSONObject(a).getDouble("Bid");
				double divYield = stockArray.getJSONObject(a).getDouble("DividendYield");
				double peRatio = stockArray.getJSONObject(a).getDouble("PERatio");

				//Parse Market Cap - form: 205B (int)+(string)
				String marketCap = stockArray.getJSONObject(a).getString("MarketCapitalization");
				String marketCapSuf = marketCap.substring(marketCap.length()-1);
				double marketCapAmt = Double.parseDouble(marketCap.substring(0, marketCap.length()-1));
				marketCapAmt = marketCapAmt*suffixes.get(marketCapSuf);

				//Large checks
				if(marketCapAmt >= (10*suffixes.get("B"))){
					if((bid < fiftyDayAvg*largefiftyWeekPercBuy)			//Buy
							&& (divYield > largeYield) 
							&& (peRatio < largePEBuy)){

						buyArray.put(stockArray.getJSONObject(a));
					}
					else if((bid > fiftyDayAvg*largefiftyWeekPercSell)		//Sell
							&& (peRatio > largePESell)){

						sellArray.put(stockArray.getJSONObject(a));
					}
				}

				//Medium checks
				else if(marketCapAmt >= (2*suffixes.get("B"))){	
					if((bid < fiftyDayAvg*medfiftyWeekPercBuy)				//Buy
							&& (divYield > medYield) 
							&& (peRatio < medPEBuy)){

						buyArray.put(stockArray.getJSONObject(a));
					}
					else if((bid > fiftyDayAvg*medfiftyWeekPercSell)		//Sell
							&& (peRatio > medPESell)){

						sellArray.put(stockArray.getJSONObject(a));
					}
				}
				
				//Small	checks
				else if(marketCapAmt >= (300*suffixes.get("M"))){				
					if((bid < fiftyDayAvg*smfiftyWeekPercBuy)				//Buy
							&& (divYield > smYield) 
							&& (peRatio < smPEBuy)){

						buyArray.put(stockArray.getJSONObject(a));
					}
					else if((bid > fiftyDayAvg*smfiftyWeekPercSell)			//Sell
							&& (peRatio > smPESell)){

						sellArray.put(stockArray.getJSONObject(a));
					}
				}
			}
		}
	}


}
