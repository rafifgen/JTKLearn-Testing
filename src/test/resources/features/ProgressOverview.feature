Feature: FR-12 Progress Overview

Scenario: Verify progress overview page with a student has not finished all materials and quizzes yet
    TC-FR12-08
    Given User has opened the browser
    And The user is on the application login page
    And User enters username "pengajar1@example.com" and password "pengajar1"
    And User clicks on the login button
    And User clicks on Pemantauan
    When User clicks on Progres button of Komputer Grafik course
    And Student tasks finish status should be set like these:
    | student_position | task_position | status      | score |
    | 1                | 1             | NOT_STARTED | 0     |
    | 1                | 2             | NOT_STARTED | 0     |
    | 1                | 3             | NOT_STARTED | 0     |
    | 2                | 1             | NOT_STARTED | 0     |
    | 2                | 2             | NOT_STARTED | 0     |
    | 2                | 3             | NOT_STARTED | 0     |
    | 3                | 1             | NOT_STARTED | 0     |
    | 3                | 2             | NOT_STARTED | 0     |
    | 3                | 3             | NOT_STARTED | 0     |
    And Student progress of "Komputer Grafik" course should be set like these:
    | student_position | percentage |
    | 1                | 0          |
    | 2                | 0          |
    | 3                | 0          |
    Then The page title should be "Pemantau Progres Belajar"
    And The page subtitle should be the same with "Komputer Grafik" course
    And The study progress of every student should be displayed

Scenario: Verify progress overview page with some students finished some materials and quizzes
    TC-FR12-09
    Given User has opened the browser
    And The user is on the application login page
    And User enters username "pengajar1@example.com" and password "pengajar1"
    And User clicks on the login button
    And User clicks on Pemantauan
    When User clicks on Progres button of Komputer Grafik course
    And Student tasks finish status should be set like these:
    | student_position | task_position | status      | score |
    | 1                | 1             | NOT_STARTED | 0     |
    | 1                | 2             | FINISHED    | 0     |
    | 1                | 3             | NOT_STARTED | 0     |
    | 2                | 1             | IN_PROGRESS | 0     |
    | 2                | 2             | NOT_STARTED | 0     |
    | 2                | 3             | FINISHED    | 95    |
    | 3                | 1             | NOT_STARTED | 0     |
    | 3                | 2             | NOT_STARTED | 0     |
    | 3                | 3             | NOT_STARTED | 0     |
    And Student progress of "Komputer Grafik" course should be set like these:
    | student_position | percentage |
    | 1                | 33.33      |
    | 2                | 33.33      |
    | 3                | 0          |
    Then The page title should be "Pemantau Progres Belajar"
    And The page subtitle should be the same with "Komputer Grafik" course
    And The study progress of every student should be displayed

@wip
Scenario: Verify progress overview page with all students finished all materials and quizzes
    TC-FR12-10
    Given User has opened the browser
    And The user is on the application login page
    And User enters username "pengajar1@example.com" and password "pengajar1"
    And User clicks on the login button
    And User clicks on Pemantauan
    When User clicks on Progres button of Komputer Grafik course
    And Student tasks finish status should be set like these:
    | student_position | task_position | status      | score |
    | 1                | 1             | FINISHED    | 0     |
    | 1                | 2             | FINISHED    | 0     |
    | 1                | 3             | FINISHED    | 90    |
    | 2                | 1             | FINISHED    | 0     |
    | 2                | 2             | FINISHED    | 0     |
    | 2                | 3             | FINISHED    | 95    |
    | 3                | 1             | FINISHED    | 0     |
    | 3                | 2             | FINISHED    | 0     |
    | 3                | 3             | FINISHED    | 85    |
    And Student progress of "Komputer Grafik" course should be set like these:
    | student_position | percentage |
    | 1                | 100        |
    | 2                | 100        |
    | 3                | 100        |
    Then The page title should be "Pemantau Progres Belajar"
    And The page subtitle should be the same with "Komputer Grafik" course
    And The study progress of every student should be displayed

Scenario: Verify progress overview page with three different student states;
          has not finished all, finished some materials, and finished all
    TC-FR12-11
    Given User has opened the browser
    And The user is on the application login page
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
    And The user is on the application login page
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
    And The user is on the application login page
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
    And The user is on the application login page
    And User enters username "pengajar1@example.com" and password "pengajar1"
    And User clicks on the login button
    And User clicks on Pemantauan
    When User clicks on Progres button of Komputer Grafik course
    And User clicks on "descending" sorting button
    Then The displayed names on Pemantau Progres Belajar page should be sorted in descending order