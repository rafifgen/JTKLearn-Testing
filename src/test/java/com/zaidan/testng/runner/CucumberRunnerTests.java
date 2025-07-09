package com.zaidan.testng.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(tags = "@CourseDetails", features = "src/test/resources/features", glue = "com.zaidan.testng.definitions", plugin = {
        "pretty", "html:report_test.html", "json:target/cucumber/report_test.json",
        "html:target/report_tes.html" })

public class CucumberRunnerTests extends AbstractTestNGCucumberTests {
}
