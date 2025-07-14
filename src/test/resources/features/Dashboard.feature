@Dashboard
Feature: FR03-View Dashboard

Background:
    Given User has opened the browser
    And The user is on the application login page

@UnjoinedCourse
Scenario: Verify dashboard page for a student who hasn't registered for any courses
    When User enters username "pelajar3@example.com" and password "pelajar3"
    And User clicks on the login button
    Then User should see the welcome message for email "pelajar3@example.com"
    And User should see the page title "Kursus"
    And User should see all courses matching with the database records

@JoinedCourse
Scenario: Verify dashboard page for a student who registered in courses from different lecturer
    When User enters username "pelajar1@example.com" and password "pelajar1"
    And User clicks on the login button
    Then User should see the welcome message for email "pelajar1@example.com"
    And User should see the page title "Kursus"
    And User should see the joined courses matching with the database records with email "pelajar1@example.com"