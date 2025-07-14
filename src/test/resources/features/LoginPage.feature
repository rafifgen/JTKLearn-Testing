@Authentication
Feature: FR-01 Authentication

Background:
    Given User has opened the browser
    And The user is on the application login page
    And User enters username "pelajar1@example.com" and password "pelajar1"
    And User clicks on the login button

@ValidCredentials
Scenario: Login with valid credentials
    Then User is navigated to the dashboard page
    And User should be able to see navigation bar for pelajar
    And User should be able to see photo and username

@VerificationGoToHomepage
Scenario:User navigates to Beranda through Kursus Saya and sees course list
    When User clicks on Kursus Saya navigation
    And User clicks on Beranda navigation
    Then Page title should be displayed
    And Course list created by the instructor should be visible

@VerificationSubMenuOnUserProfile
Scenario:User click on Username Menu and sub menu is displayed
    When User clicks on username
    Then Sub menu keluar is displayed

