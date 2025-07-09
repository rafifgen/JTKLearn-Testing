@AccessMaterials
Feature: FR08-Access Materials

@VerifyVideo
Scenario: Verify video can be played
    TC-FR08-01
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pelajar1@example.com" and password "pelajar1"
    And User clicks on the login button
    And User clicks on course "Komputer Grafik"
    And User clicks on the continue button
    When User clicks on one of the video in the navigation bar
    Then User should be able to see the page title "Komputer Grafik"
    And User should be able to play the video
    And User should be able to see the next or previous button
    And The course name of id 4 and material name of id 9 should be the same as in the database

@VerifyPDF
Scenario: Verify PDF file can be opened and read
    TC-FR08-02
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pelajar1@example.com" and password "pelajar1"
    And User clicks on the login button
    And User clicks on course "Komputer Grafik"
    And User clicks on the continue button
    When User clicks on the example PDF material
    Then User should be able to read the PDF file with material id 10
    And User should be able to see the next or previous button
    And The course name of id 4 and material name of id 10 should be the same as in the database

# TODO: solve the finish time
# @wip
@ResetMaterialState
Scenario Outline: Verify the time of finishing video material
    TC-FR08-09, TC-FR08-10, TC-FR08-11
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pelajar1@example.com" and password "pelajar1"
    And User clicks on the login button
    And User clicks on course "Komputer Grafik"
    And User clicks on the continue button
    And The initial progress is tracked
    When User clicks on one of the video in the navigation bar
    And User should be able to play the video
    And User moves to the next page right after <duration> minutes
    Then The system correctly tracks video material completion for <duration> minutes

    Examples:
    | duration |
    | 5        |
    | 6        |
    | 7        |

# TODO: solve the finish time
# @wip
@ResetMaterialState
Scenario Outline: Verify the time of finishing PDF material
    TC-FR08-14, TC-FR08-15, TC-FR08-16
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pelajar1@example.com" and password "pelajar1"
    And User clicks on the login button
    And User clicks on course "Komputer Grafik"
    And User clicks on the continue button
    And The initial progress is tracked
    When User clicks on the example PDF material
    And User should be able to read the PDF file with material id 10
    And User moves to the next page right after <duration> minutes
    Then The system correctly tracks PDF material completion for <duration> minutes


    Examples:
    | duration |
    | 2        |
    | 3        |
    | 4        |