@QuizHistory
Feature: TC FR-11 Quiz Results History

Background:
    Given User has opened the browser
    And The user is on the application login page


@EmptyCourseHistory
Scenario: View quiz history page when no courses are enrolled
  When User enters username "rapelajar@example.com" and password "rapelajar"
  And User clicks on the login button
  Then User is navigated to the dashboard page
  And User navigates to the Quiz History page
  Then The Quiz History page should display the title "Riwayat Kuis"
  And The Quiz History page should display a message "Belum ada kursus yang diikuti."

@QuizHistoryForEnrolledCourse
Scenario: View quiz history for an enrolled course with no quiz attempts
    When User enters username "reci@example.com" and password "recipelajar"
    And User clicks on the login button
    Then User is navigated to the dashboard page
    And User is on the Quiz History page
    And User clicks the detail button for the course "Komdatjar"
    Then The Sub Quiz History page should display the title "Riwayat Hasil Kuis"
    And The subtitle for the course "Komdatjar" should be visible
    And The history for "Komdatjar" should show a message "Belum ada kuis yang diselesaikan."

@QuizHistoryWithSomeAttempts
Scenario: View quiz history for an enrolled course with some quiz attempts
  When User enters username "sa@example.com" and password "sapelajar"
  And User clicks on the login button
  Then User is navigated to the dashboard page
  And User is on the Quiz History page
  And User clicks the detail button for the course "Komdatjar"
  Then The system should display a list of quizzes for "Komdatjar"
  And The quiz details displayed on the UI for student "sa@example.com" and course "Komdatjar" should match the database records

  @QuizHistoryWithFullAttempts
  Scenario: View quiz history for an enrolled course with quiz attempts completely
    When User enters username "full@example.com" and password "fullpelajar"
    And User clicks on the login button
    Then User is navigated to the dashboard page
    And User is on the Quiz History page
    And User clicks the detail button for the course "Komdatjar"
    Then The system should display a list of quizzes for "Komdatjar"
    And The quiz details displayed on the UI for student "full@example.com" and course "Komdatjar" should match the database records

  @SearchQuizHistory
  Scenario: Search for a specific quiz in the history
    When User enters username "sa@example.com" and password "sapelajar"
    And User clicks on the login button
    Then User is navigated to the dashboard page
    And User is on the Quiz History page
    And User clicks the detail button for the course "Komdatjar"
    And User searches for the quiz "Kuis Pengantar"
    Then The system should display a filtered list containing "Kuis Pengantar"
    And The filtered quiz details for student "sa@example.com" should match the database records for course "Komdatjar" and search term "Kuis Pengantar"