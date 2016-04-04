#Author: Deagan Monahan

#Given a hard coded list of symbols
#Check that the yahoo stock API returns the correct results

@tag
Feature: Check Yahoo API with hardcoded data
	I want to check Yahoo API results for Symbols
	
Scenario: Successfully retrieve correct stock info for given symbols
	Given The valid stock symbol: <symbol>
  | GOOG |
  | GSK |
  | AAPL |
	When Valid Yahoo results are returned
	And Returned count match given count
	And Returned values match given values
	Then Print Success
	
	
