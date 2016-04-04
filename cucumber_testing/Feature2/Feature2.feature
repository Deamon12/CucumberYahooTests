
#Read, parse, eval CSV
@tag
Feature: Check Yahoo API results when compared to CSV results
	

Scenario: Successfully retrieve correct stock info from csv symbols
	Given A csv of stock data
	When Valid REST results are returned
	And Returned symbol count matches csv symbol count
	And Returned symbols match csv symbols
	And Returned issuer name matches csv issuer name
	And Returned type matches csv type
	Then Print csv success