Feature: FR-12 Progress Overview

Scenario: Verify progress overview page with a student has not finished all materials and quizzes yet
    TC-FR12-08
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pengajar1@example.com" and password "pengajar1"
    And User clicks on the login button
    And User clicks on Pemantauan
    And Student of id 1 has not started yet material of id 9
    And Student of id 1 has not started yet material of id 10
    And Student of id 1 has not started yet quiz of id 7
    And The progress of student with id 1 is set to 0
    And Student of id 4 has not started yet material of id 9
    And Student of id 4 has not started yet material of id 10
    And Student of id 4 has not started yet quiz of id 7
    And The progress of student with id 4 is set to 0
    And Student of id 12 has not started yet material of id 9
    And Student of id 12 has not started yet material of id 10
    And Student of id 12 has not started yet quiz of id 7
    And The progress of student with id 12 is set to 0
    When User clicks on Progres button of Komputer Grafik course
    Then The page title should be "Pemantau Progres Belajar"
    And The page subtitle should be the same with course with id 4
    And The study progress of every student should be displayed

Scenario: Verify progress overview page with some students finished some materials and quizzes
    TC-FR12-09
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pengajar1@example.com" and password "pengajar1"
    And User clicks on the login button
    And User clicks on Pemantauan
    And Student of id 1 has not started yet material of id 9
    And Student of id 1 finished material of id 10
    And Student of id 1 has not started yet quiz of id 7
    And The progress of student with id 1 is set to 33.333
    And Student of id 4 is still on progress in material of id 9 
    And Student of id 4 has not started yet material of id 10
    And Student of id 4 finished quiz of id 7 with score 95
    And The progress of student with id 4 is set to 33.333
    And Student of id 12 has not started yet material of id 9
    And Student of id 12 has not started yet material of id 10
    And Student of id 12 has not started yet quiz of id 7
    And The progress of student with id 12 is set to 0
    When User clicks on Progres button of Komputer Grafik course
    Then The page title should be "Pemantau Progres Belajar"
    And The page subtitle should be the same with course with id 4
    And The study progress of every student should be displayed

Scenario: Verify progress overview page with all students finished all materials and quizzes
    TC-FR12-10
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pengajar1@example.com" and password "pengajar1"
    And User clicks on the login button
    And User clicks on Pemantauan
    And Student of id 1 finished material of id 9
    And Student of id 1 finished material of id 10
    And Student of id 1 finished quiz of id 7 with score 90
    And The progress of student with id 1 is set to 100
    And Student of id 4 finished material of id 9
    And Student of id 4 finished material of id 10
    And Student of id 4 finished quiz of id 7 with score 95
    And The progress of student with id 4 is set to 100
    And Student of id 12 finished material of id 9
    And Student of id 12 finished material of id 10
    And Student of id 12 finished quiz of id 7 with score 85
    And The progress of student with id 12 is set to 100
    When User clicks on Progres button of Komputer Grafik course
    Then The page title should be "Pemantau Progres Belajar"
    And The page subtitle should be the same with course with id 4
    And The study progress of every student should be displayed

Scenario: Verify progress overview page with three different student states;
          has not finished all, finished some materials, and finished all
    TC-FR12-11
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pengajar1@example.com" and password "pengajar1"
    And User clicks on the login button
    And User clicks on Pemantauan
    And Student of id 1 has not started yet material of id 9
    And Student of id 1 has not started yet material of id 10
    And Student of id 1 has not started yet quiz of id 7
    And The progress of student with id 1 is set to 0
    And Student of id 4 finished material of id 9
    And Student of id 4 is still on progress in material of id 10
    And Student of id 4 has not started yet quiz of id 7
    And The progress of student with id 4 is set to 33.333
    And Student of id 12 finished material of id 9
    And Student of id 12 finished material of id 10
    And Student of id 12 finished quiz of id 7 with score 100
    And The progress of student with id 12 is set to 100
    When User clicks on Progres button of Komputer Grafik course
    Then The page title should be "Pemantau Progres Belajar"
    And The page subtitle should be the same with course with id 4
    And The study progress of every student should be displayed

Scenario: Verify ascending sorting operation on Riwayat Kuis page
    TC-FR12-23
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pengajar1@example.com" and password "pengajar1"
    And User clicks on the login button
    And User clicks on Pemantauan
    And Student of id 1 finished quiz of id 7 with score 100
    And Student of id 4 finished quiz of id 7 with score 90
    And Student of id 12 finished quiz of id 7 with score 85
    When User clicks on Detail Kuis button of Komputer Grafik course
    And User clicks on Lihat Hasil button of Tes Progres quiz
    And User clicks on "ascending" sorting button
    Then The displayed names should be sorted in "ascending" order

Scenario: Verify ascending sorting operation on Riwayat Kuis page
    TC-FR12-24
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pengajar1@example.com" and password "pengajar1"
    And User clicks on the login button
    And User clicks on Pemantauan
    And Student of id 1 finished quiz of id 7 with score 100
    And Student of id 4 finished quiz of id 7 with score 90
    And Student of id 12 finished quiz of id 7 with score 85
    When User clicks on Detail Kuis button of Komputer Grafik course
    And User clicks on Lihat Hasil button of Tes Progres quiz
    And User clicks on "descending" sorting button
    Then The displayed names should be sorted in "descending" order

Scenario: Verify descending sorting operation on Pemantau Progres Belajar page
    TC-FR12-26
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "pengajar1@example.com" and password "pengajar1"
    And User clicks on the login button
    And User clicks on Pemantauan
    When User clicks on Progres button of Komputer Grafik course
    And User clicks on "descending" sorting button
    Then The displayed names on Pemantau Progres Belajar page should be sorted in descending order