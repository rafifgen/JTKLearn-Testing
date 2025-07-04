@MyCourseOverview
Feature:FR-06 Course Overview

  Background:
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pelajar1@example.com" and password "pelajar1"
    And User clicks on the login button
    Then User is navigated to the dashboard page

  @VerifyCourseDetailsAndProgress
  Scenario: Student verifies the enrolled course details and progress against the database
    When User navigates to the "Dashboard" page and selects the course named "Komdatjar"
    Then The course details page should display the correct information for "Komdatjar"
    And The displayed course progress should be greater than 0 and less than 100 percent
    And The displayed course information for student "pelajar1@example.com" should match the database records