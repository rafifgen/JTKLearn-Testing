@AccessQuiz
Feature: Access Quiz

Background:
    Given User has opened the browser
    And The user is on the application login page

@TC-FR09-02
Scenario: Verify quiz result page with score = 80
    When User enters username "rio1@" and password "rio1"
    And User clicks on the login button
    And User is navigated to the dashboard page
    And User scrolls to course "FR09"
    And User clicks on course "FR09"
    And User is enrolled on the selected course
    And User clicks the lanjutkan kursus button
    And User scrolls to the "TC-02" button on the sidebar
    And User clicks the "TC-02" button on the sidebar
    Then User should see the quiz title "TC-02"
    And User should see quiz result information with score "80"
    And User should see "Ulangi kuis" button
    And User should see "Tinjau Hasil Kuis" button
    And User should see next or previous button
    And User with email "rio1@" should see the quiz "TC-02" result information is the same as in the database

@TC-FR09-03
Scenario: Verify quiz result page with 80 < score < 100
    When User enters username "rio1@" and password "rio1"
    And User clicks on the login button
    And User is navigated to the dashboard page
    And User scrolls to course "FR09"
    And User clicks on course "FR09"
    And User is enrolled on the selected course
    And User clicks the lanjutkan kursus button
    And User scrolls to the "TC-03" button on the sidebar
    And User clicks the "TC-03" button on the sidebar
    Then User should see the quiz title "TC-03"
    And User should see quiz result information with score "90"
    And User should see "Ulangi kuis" button
    And User should see "Tinjau Hasil Kuis" button
    And User should see next or previous button
    And User with email "rio1@" should see the quiz "TC-03" result information is the same as in the database

@TC-FR09-04
Scenario: Verify quiz result page with score = 100
    When User enters username "rio1@" and password "rio1"
    And User clicks on the login button
    And User is navigated to the dashboard page
    And User scrolls to course "FR09"
    And User clicks on course "FR09"
    And User is enrolled on the selected course
    And User clicks the lanjutkan kursus button
    And User scrolls to the "TC-04" button on the sidebar
    And User clicks the "TC-04" button on the sidebar
    Then User should see the quiz title "TC-04"
    And User should see quiz result information with score "100"
    And User should see "Tinjau Hasil Kuis" button
    And User should see next or previous button
    And User with email "rio1@" should see the quiz "TC-04" result information is the same as in the database

@TC-FR09-05
Scenario: Verify quiz result page with score < 80
    When User enters username "rio1@" and password "rio1"
    And User clicks on the login button
    And User is navigated to the dashboard page
    And User scrolls to course "FR09"
    And User clicks on course "FR09"
    And User is enrolled on the selected course
    And User clicks the lanjutkan kursus button
    And User scrolls to the "TC-05" button on the sidebar
    And User clicks the "TC-05" button on the sidebar
    Then User should see the quiz title "TC-05"
    And User should see quiz result information with score "60"
    And User should see "Ulangi kuis" button
    And User should see "Tinjau Hasil Kuis" button
    And User should see next or previous button
    And User with email "rio1@" should see the quiz "TC-05" result information is the same as in the database

@QuizReviewComplete
Scenario: Verify complete quiz review functionality displays correct information from database for Memasak course
    When User enters username "pelajar1@example.com" and password "pelajar1"
    And User clicks on the login button
    And User navigates to course detail page
    And User selects quiz section
    And User clicks on "Tinjau Hasil Kuis" button
    Then User should see quiz review page with URL containing "mode=review"
    And User should see all quiz questions displayed
    And User should see student answers for each question
    And User should see answer status for each question with correct styling
    And User should see correct answer key for wrong answers
    And For multiple choice questions with wrong answers, the correct answer should have green border
    And For correct answers, the status should be displayed in green font color
    And For wrong answers, the status should be displayed in red font color
    And All quiz information should match with database records including:
      | Question content    |
      | Student answers     |
      | Answer status       |
      | Correct answer keys |
    When User clicks on "See Result" button
    Then User should be redirected to the course detail page