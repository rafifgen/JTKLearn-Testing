@FR09
Feature: Quiz Result

  Background:
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "luthfipelajar2@example.com" and password "luthfipelajar2"
    And User clicks on the login button
    Given User session is initialized
    Then User is navigated to the dashboard page
    When User clicks on "Kursus Saya" navigation
    Given the user is on the courses page
    And the user clicks the "Contoh Kursus" course card
    Then the overview page for that course is displayed
    When the user clicks the "Lanjutkan" button on the overview page
    Then the course content page is displayed
    And the navigation title displays the course name from the database (column “nama_course”)

@TC21
  Scenario: Student answers all short answer questions correctly and gets score 100 (
    Given the user starts the quiz with all questions of type "short answer" with one correct key
    When the user answers all questions correctly
    And submits the quiz
    Then the system stores the quiz start time and answers in the database
    And the system calculates the quiz score as 100
    And the system stores the quiz score and end time in the database
    And the system marks the quiz as completed in the navigation
    And some navigation items have a green background (completed)
    And the learning achievement details match the database
    And the progress bar is partially filled
    And the progress percentage is between "1%" and "100%"
    And the system displays the last quiz result with:
      | answered_correctly | total_questions | score | status | feedback_message                                         |
      | n                  | n               | 100   | Lulus  | Sempurna kamu berhasil lulus kuis. Pertahankan usaha luar biasa ini. |
    And the quiz name, score, total questions, and correct answers match the database