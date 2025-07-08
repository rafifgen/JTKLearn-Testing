@QuizResult
Feature: Pengerjaan Kuis

  Background:
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pelajar1@example.com" and password "pelajar1"
    And User clicks on the login button
    Then User is navigated to the dashboard page

  @AttemptQuizAndPass
  Scenario: Student attempts a quiz and gets a passing score of 80
    When User selects the course "Komdatjar" from the dashboard
    And User clicks the "Lanjutkan Kursus" button
    And User starts the quiz named "kursus nilai 80"
    And User clicks the "Ulangi Kuis" button
    And User answers the questions to get a score of 80
    And User submits the quiz
    Then The result page should show the title "Hasil Kuis"
    And The result page should confirm "4 dari 5 soal dengan benar" and a score of "80"
    And The result page should show the passing message "Kamu lulus!"