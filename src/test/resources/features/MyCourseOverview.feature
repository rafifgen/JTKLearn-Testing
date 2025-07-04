@CourseOverview
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

@InvalidEnrollmentKey
Scenario: Enroll in a course with an invalid enrollment key
    When User navigates to the course list and selects the unenrolled course "PABPMetagama"
    And User enters an enrollment key "kunci_salah_123"
    And User clicks the enroll button
    Then User should see an enrollment error message "Kode Pendaftaran yang Anda masukkan tidak valid. Silakan coba lagi!"
    And User should remain on the course information page

@NoMaterialContent
Scenario: Enroll in a course that has no material content
    When User navigates to the course list and selects the unenrolled course "PABPMetagama"
    And User enters an enrollment key "pabpmetagama"
    And User clicks the enroll button
    Then User should see an enrollment error message "Pendaftaran kursus gagal, kursus setidaknya memiliki minimal satu buah materi dan kuis."
    And User should remain on the course information page

  @NoQuizContent
  Scenario: Enroll in a course that has no material content
    When User navigates to the course list and selects the unenrolled course "PABPMetagama2"
    And User enters an enrollment key "pabpmetagama"
    And User clicks the enroll button
    Then User should see an enrollment error message "Pendaftaran kursus gagal, kursus setidaknya memiliki minimal satu buah materi dan kuis."
    And User should remain on the course information page