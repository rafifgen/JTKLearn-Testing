@QuizHistory
Feature: TC FR-11 Quiz Results History

Background:
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"


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