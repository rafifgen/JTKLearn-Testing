Feature: View Dashboard

  Background:
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    When User enters username "pelajar1@example.com" and password "pelajar1"
    And User clicks on the login button

@Dashboard
Scenario: Verify dashboard page for a student who hasn't registered for any courses
  Then User should see the welcome message "Hai, Andi Pelajar!"
  And User should see the page title "Kursus"
  And User should see the following unjoined courses:
    | courseName         | instructorName  |
    | Contoh Kursus      | Budi Pengajar   |
    | Pemrograman Web    | Budi Pengajar   |
    | Pemrograman Mobile | Budi Pengajar   |
  And the displayed course information should match the database records
