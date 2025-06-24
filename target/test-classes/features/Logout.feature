Feature: Logout from Zaidan Educare School Application

Background:
    Given User has successfully logged in as "bendahara" with password "admin123"
    And User is on the Dasbor page Education Fund Payment Management System for Zaidan Educare School app "http://ptbsp.ddns.net:6882"

    @ValidCredentials
    Scenario: Check logout is successful with valid credentials as role "bendahara"
        When User clicks on logout button
        And User clicks yes button on logout confirmation pop up
        Then User should be redirected to the login page