package com.deamon.cucumber.cucumber_testing;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = {"Feature", "Feature2", "Feature3"}, 
		glue={"com.deamon.cucumber.stepDefinitions"})
public class CucumberTestRunner {
	
	
	
}


