

<img src="http://jhipster.github.io/img/svg/cucumber.svg" width="30" height="30"/> 

# CucumberYahooStockTests



A set of Cucumber tests that check the results of Yahoo stock web-service calls.


### Test 1
##### Scenario: Successfully retrieve correct stock info for given symbols via REST API
    Given The valid stock symbols:
    	| GOOG |
    	| GSK  |
    	| AAPL |
    When Valid Yahoo results are returned
    And Returned count matches given count
    And Returned values matches given values
    Then Print Success

### Test 2
##### Scenario: Successfully retrieve correct stock info from csv symbols via Yahoo REST API
	Given A csv of stock data
	When Valid REST results are returned
	And Returned symbol count matches csv symbol count
	And Returned symbols match csv symbols
	And Returned issuer name matches csv issuer name
	And Returned type matches csv type
	Then Print csv success
	
### Test 3
##### Scenario: Successfully give buy/sell recommendation based on given criteria
    Given A csv of stock data
    Given A list of buy requirements
    	| largePE | largePrice | largeYield | medPE  | medPrice | medYield | smPE | smPrice | smYield |
    	| l15     | l25        | g2         | l17    | l50      | g1.5     | l25  | l75     | g1      |
    Given A list of sell requirements
    	| largePE | largePrice | medPE | medPrice | smPE | smPrice |
    	| g17     | g85        | g20   | l75      | l33  | l65     |
	When Valid Query results are returned
	When Stock is large cap and meets buy criteria
	When Stock is med cap and meets buy criteria
	When Stock is small cap and meets buy criteria
	When Stock is large cap and meets sell criteria
	When Stock is med cap and meets sell criteria
	When Stock is small cap and meets sell criteria
	Then Print criteria success
