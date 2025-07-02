Feature: Login to JTK Learn Application

Background:
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
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

