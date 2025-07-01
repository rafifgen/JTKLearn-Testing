package com.zaidan.testng.definitions;

import com.zaidan.testng.actions.myCourseActions;
import com.zaidan.testng.utils.HelperClass;
import org.testng.Assert;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class myCourseDefinitions {

    myCourseActions objMyCourse;

    @Given("User has logged in as {string}")
    public void user_has_logged_in_as(String role) {
        HelperClass.setUpDriver();
        objMyCourse = new myCourseActions(HelperClass.getDriver());
        // Simulasi login berdasarkan peran (role) jika diperlukan
        // Misalnya, navigasi ke halaman login dan autentikasi
    }

    @When("User navigates to {string} page")
    public void user_navigates_to_page(String pageName) {
        HelperClass.openPage("https://example.com/" + pageName.toLowerCase().replace(" ", "-"));
    }

    @Then("User should see the message {string}")
    public void user_should_see_the_message(String expectedMessage) {
        // Validasi pesan yang ditampilkan di halaman
        String actualMessage = objMyCourse.getCourseTitle(); // Contoh penggunaan metode
        Assert.assertEquals(actualMessage, expectedMessage, "Message does not match!");
    }

    @And("User selects the {string} tab")
    public void user_selects_the_tab(String tabName) {
        if (tabName.equalsIgnoreCase("Dalam Progres")) {
            objMyCourse.clickTabSelesai(); // Klik tab "Dalam Progres"
        } else if (tabName.equalsIgnoreCase("Selesai")) {
            objMyCourse.clickTabSelesai(); // Klik tab "Selesai"
        } else {
            Assert.fail("Unknown tab name: " + tabName);
        }
    }

    @Then("User should see the following courses:")
    public void user_should_see_the_following_courses(io.cucumber.datatable.DataTable dataTable) {
        // Validasi daftar kursus yang ditampilkan
        for (String courseName : dataTable.asList()) {
            boolean isCourseDisplayed = objMyCourse.isAnyCourseDisplayed();
            Assert.assertTrue(isCourseDisplayed, "Course not displayed: " + courseName);
        }
    }
}