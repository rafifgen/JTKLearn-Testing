@AccessMaterials
Feature: FR08-Access Materials

@wip
@VerifyVideo
Scenario: Verify video can be played
    TC-FR08-01
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pelajar1@example.com" and password "pelajar1"
    And User clicks on the login button
    And User clicks on course "Contoh Kursus"
    And User clicks on the continue button
    When User clicks on one of the video in the navigation bar
    Then User should be able to see the page title "Contoh Kursus"
    And User should be able to play the video
    And User should be able to see the next or previous button
    And The course name of id 3 and material name of id 4 should be the same as in the database

@wip
@VerifyPDF
Scenario: Verify PDF file can be opened and read
    TC-FR08-02
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pelajar1@example.com" and password "pelajar1"
    And User clicks on the login button
    And User clicks on course "Contoh Kursus"
    And User clicks on the continue button
    When User clicks on the example PDF material
    Then User should be able to read the PDF file with material id 5
    And User should be able to see the next or previous button
    And The course name of id 3 and material name of id 5 should be the same as in the database