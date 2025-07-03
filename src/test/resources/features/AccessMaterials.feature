@AccessMaterials
Feature: FR08-Access Materials

@VerifyVideo
Scenario: Verify video can be played
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pelajar1@example.com" and password "pelajar1"
    And User clicks on the login button
    And User clicks on course "Contoh Kursus"
    When User clicks on one of the video in the navigation bar
    Then User should be able to see the page title that contains the course name "Contoh Kursus"
    And User should be able to play the video
    And User should be able to see the next or previous button