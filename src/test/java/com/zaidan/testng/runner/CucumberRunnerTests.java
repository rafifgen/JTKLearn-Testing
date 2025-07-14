package com.zaidan.testng.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.CucumberOptions.SnippetType;

@CucumberOptions(
        tags = "@VerifyVideo",
        features = "src/test/resources/features",
        glue = "com.zaidan.testng.definitions",
        plugin = {
                "pretty",
                "html:report_test_suit_so_far.html",
                "json:target/cucumber/report_test_suit_so_far.json",
                "html:target/report_test_suit_so_far.html",
        },
        snippets = SnippetType.CAMELCASE)

public class CucumberRunnerTests extends AbstractTestNGCucumberTests {
}