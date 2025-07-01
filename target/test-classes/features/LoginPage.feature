Feature: Login to JTK Learn Application

Background:
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"

@ValidCredentials
Scenario Outline: Login with valid credentials
    When User enters username "<username>" and password "<password>"
    And User clicks on the login button
    Then User is navigated to the dashboard page
    And User should be able to see navigation bar for pelajar
    And User should be able to see photo and username

    Examples:
    | username   | password   |
    | pelajar1@example.com | pelajar1 |
#    | admin@example.com  | admin   |
#    | pengajar1@example.com  | pengajar1   |


@InvalidCredentials
Scenario Outline: Login with invalid credentials
    When User enters username "<username>" and password "<password>"
    And User clicks on the login button
    Then User should be able to see "Kesalahan!" notification message

    Examples:
    | username | password   |
    | reqi     | admin111   |



@VerificationGoToHomepage
Scenario Outline:User navigates to Beranda through Kursus Saya and sees course list
    When User enters username "<username>" and password "<password>"
    And User clicks on the login button
    And User clicks on Kursus Saya navigation
    And User clicks on Beranda navigation
    Then Page title should be displayed
    And Course list created by the instructor should be visible

    Examples:
        |  username | password |
        | pelajar1@example.com | pelajar1 |


@VerificationSubMenuOnUserProfile
Scenario Outline:User navigates to Beranda through Kursus Saya and sees course list
    When User enters username "<username>" and password "<password>"
    And User clicks on the login button
    And User clicks on username
    Then Sub menu keluar is displayed

    Examples:
        |  username | password |
        | pelajar1@example.com | pelajar1 |