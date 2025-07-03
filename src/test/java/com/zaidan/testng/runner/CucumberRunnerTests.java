package com.zaidan.testng.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(tags = "@Dashboard", features = "src/test/resources/features", glue = "com.zaidan.testng.definitions", plugin = {
        "pretty", "html:test-output-jtklearn.html", "json:target/cucumber/cucumber.json",
        "html:target/cucumber-html-report-jtklearn.html" })

public class CucumberRunnerTests extends AbstractTestNGCucumberTests {
}
