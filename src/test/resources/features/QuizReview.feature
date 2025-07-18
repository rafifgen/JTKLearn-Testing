@QuizReview
Feature: FR09-Review Quiz Results

  Background:
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"

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
    And For correct answers, the status should be displayed in blue font color
    And For wrong answers, the status should be displayed in red font color
    And All quiz information should match with database records including:
      | Question content    |
      | Student answers     |
      | Answer status       |
      | Correct answer keys |
    When User clicks on "See Result" button
    Then User should be redirected to the course detail page
