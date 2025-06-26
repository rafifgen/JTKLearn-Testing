Feature: Login to JTK Learn Application

Background:
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"

@ValidCredentials
Scenario Outline: Login with valid credentials
    When User enters username "<username>" and password "<password>"
    And User clicks on the login button
    Then User is navigated to the dashboard page
#    And User should be able to see navigation bar for bendahara

    Examples:
    | username   | password   |
    | admin@example.com  | admin   |
    | pelajar1@example.com | pelajar1 |
    | pengajar1@example.com  | pengajar1   |


@InvalidCredentials
Scenario Outline: Login with invalid credentials
    When User enters username "<username>" and password "<password>"
    And User clicks on the login button
    Then User should be able to see "Kesalahan!" notification message

    Examples:
    | username | password   |
    | reqi     | admin111   |
