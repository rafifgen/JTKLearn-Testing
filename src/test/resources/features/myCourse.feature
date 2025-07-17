@MyCourse
Feature: My Course Functionality
  As a PELAJAR user
  I want to manage and view my course progress
  So that I can track my learning journey

  Background:
    Given User has opened the browser
    And The user is on the application login page
    And User enters username "luthfipelajar@example.com" and password "luthfipelajar"
    And User clicks on the login button
    Then User is navigated to the dashboard page

  @Positive @TCF-FRS-01
  Scenario: Verify empty 'Dalam Progress' tab
    When User clicks on "Kursus Saya" navigation
    Then System displays active tab "Dalam Progres"
    And System shows message "Belum ada kursus yang sedang dijalani."

  @Positive @TCF-FRS-05
  Scenario: Verify empty 'Selesai' tab
    When User clicks on "Kursus Saya" navigation
    And User switches to "Selesai" tab
    Then System displays active tab "Selesai"
    And System shows message "Belum ada kursus yang selesai"

  @Positive @TCF-FRS-07
  Scenario: Verify populated 'Selesai' tab
    Given User has completed courses in database
    And The user is on the application login page
    And User enters username "luthfipelajar2@example.com" and password "luthfipelajar2"
    And User clicks on the login button
    Then User is navigated to the dashboard page
    When User clicks on "Kursus Saya" navigation
    And User switches to "Selesai" tab
    Then System displays active tab "Selesai"
    And System shows course list with:
      | Component              | Verification               |
      | Course image           | Matches database           |
      | Course name            | Matches database           |
      | Instructor name        | Matches database           |
      | Progress bar           | Shows 100% completion      |
      | Completion percentage  | Shows 100%                 |
    Given User session is initialized
    And All course data matches database records