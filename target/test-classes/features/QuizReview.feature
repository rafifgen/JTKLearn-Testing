@QuizReview
Feature: FR09-Review Quiz Results

  Background:
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"

  @QuizReviewPositive
  Scenario: Verify quiz review functionality displays correct information from database for Memasak course
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
    When User clicks on "Akhiri Tinjauan" button
    Then User should be redirected to the course detail page

  @QuizReviewMultipleQuestions
  Scenario: Verify quiz review with multiple questions shows complete information for Memasak course
    When User enters username "pelajar1@example.com" and password "pelajar1"
    And User clicks on the login button
    And User navigates to course detail page
    And User selects quiz section
    And User clicks on "Tinjau Hasil Kuis" button
    Then User should see all questions in the quiz
    And Each question should display student answer
    And Each question should show answer status with appropriate color coding
    And Wrong answers should display correct answer key
    And Quiz should have exactly 2 questions with all correct answers
    When User clicks on "Akhiri Tinjauan" button
    Then User should be redirected to the course detail page
