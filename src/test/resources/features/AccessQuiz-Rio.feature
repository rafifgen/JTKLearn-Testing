@AccessQuiz
Feature: Access Quiz

Background:
    Given User has opened the browser
    And The user is on the application login page
    And User enters username "rio1@" and password "rio1"
    And User clicks on the login button
    And User is navigated to the dashboard page
    And User scrolls to course "FR09"
    And User clicks on course "FR09"
    And User is enrolled on the selected course
    And User clicks the lanjutkan kursus button

@TC-FR09-02
Scenario: Verify quiz result page with score = 80
    When User scrolls to the "TC-02" button on the sidebar
    And User clicks the "TC-02" button on the sidebar
    Then User should see the quiz title "TC-02"
    And User should see quiz result information with score "80"
    And User should see "Ulangi kuis" button
    And User should see "Tinjau Hasil Kuis" button
    And User should see next or previous button
    And User with email "rio1@" should see the quiz "TC-02" result information is the same as in the database

@TC-FR09-03
Scenario: Verify quiz result page with 80 < score < 100
    When User scrolls to the "TC-03" button on the sidebar
    And User clicks the "TC-03" button on the sidebar
    Then User should see the quiz title "TC-03"
    And User should see quiz result information with score "90"
    And User should see "Ulangi kuis" button
    And User should see "Tinjau Hasil Kuis" button
    And User should see next or previous button
    And User with email "rio1@" should see the quiz "TC-03" result information is the same as in the database

@TC-FR09-04
Scenario: Verify quiz result page with score = 100
    When User scrolls to the "TC-04" button on the sidebar
    And User clicks the "TC-04" button on the sidebar
    Then User should see the quiz title "TC-04"
    And User should see quiz result information with score "100"
    And User should see "Tinjau Hasil Kuis" button
    And User should see next or previous button
    And User with email "rio1@" should see the quiz "TC-04" result information is the same as in the database

@TC-FR09-05
Scenario: Verify quiz result page with score < 80
    When User scrolls to the "TC-05" button on the sidebar
    And User clicks the "TC-05" button on the sidebar
    Then User should see the quiz title "TC-05"
    And User should see quiz result information with score "60"
    And User should see "Ulangi kuis" button
    And User should see "Tinjau Hasil Kuis" button
    And User should see next or previous button
    And User with email "rio1@" should see the quiz "TC-05" result information is the same as in the database