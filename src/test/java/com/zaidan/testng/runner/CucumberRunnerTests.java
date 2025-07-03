package com.zaidan.testng.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(tags = "@MyCourseOverview", features = "src/test/resources/features", glue = "com.zaidan.testng.definitions", plugin = {
        "pretty", "html:test-output-jtklearn.html", "json:target/cucumber/cucumber.json",
        "html:target/report_test_suit_fr_06.html" })

public class CucumberRunnerTests extends AbstractTestNGCucumberTests {
}
