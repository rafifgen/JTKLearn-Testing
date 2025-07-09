Feature: FR-12 Progress Overview

Scenario: Verify progress overview page with a student has not finished all materials and quizzes yet
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pengajar1@example.com" and password "pengajar1"
    And User clicks on the login button
    And User clicks on Pemantauan
    And The progress of student id 1 and 4 for all materials should be reset to zero
    When User clicks on Progres button of Komputer Grafik course
    Then The page title should be "Pemantau Progres Belajar"
    And The page subtitle should be the same with course with id 4
    And The study progress of every student should be displayed

@wip
Scenario: Verify progress overview page with some students finished some materials and quizzez
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pengajar1@example.com" and password "pengajar1"
    And User clicks on the login button
    And User clicks on Pemantauan
    And Student of id 1 finished material of id 10
    And Student of id 4 is still on progress in material of id 9 
    And Student of id 4 finished quiz of id 7
    And The page should be reloaded first
    When User clicks on Progres button of Komputer Grafik course
    Then The page title should be "Pemantau Progres Belajar"
    And The page subtitle should be the same with course with id 4
    And The study progress of every student should be displayed