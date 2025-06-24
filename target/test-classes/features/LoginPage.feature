Feature: Login to Zaidan Educare School Application

Background:
    Given User has opened the browser
    And User has navigated to the login page of Education Fund Payment Management System for Zaidan Educare School app "http://ptbsp.ddns.net:6882"

@ValidCredentials
Scenario Outline: Login with valid credentials
    When User enters username "<username>" and password "<password>"
    And User clicks on the login button
    Then User is navigated to the dashboard page
    And User should be able to see navigation bar for bendahara

    Examples:
    | username   | password   |
    | bendahara  | admin123   |

@InvalidCredentials
Scenario Outline: Login with invalid credentials
    When User enters username "<username>" and password "<password>"
    And User clicks on the login button
    Then User should be able to see "Incorrect username or password, please try again!" notification message

    Examples:
    | username | password   |
    | indra    | admin123   |
    | reqi     | admin111   |
