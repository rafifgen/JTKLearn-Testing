@CourseDetails
Feature: Verifikasi tampilan konten kursus berdasarkan progres pelajar

  Background:
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User enters username "luthfipelajar2@example.com" and password "luthfipelajar2"
    And User clicks on the login button
    Given User session is initialized
    Then User is navigated to the dashboard page
    When User clicks on "Kursus Saya" navigation

  @TC1
  Scenario: Pelajar belum memulai pembelajaran
    Given the user is on the courses page
    And the first course card shows a progress of "0%"
    Then the overview page for that course is displayed

    When the user clicks the "Lanjutkan Kursus" button on the overview page
    Then the course content page is displayed

  # Verifikasi Judul & Progress
    And the navigation title displays the course name from the database (column “nama_course”)
    And the progress bar is empty
    And the progress percentage is "0%"

  # Verifikasi Informasi Capaian
    And the learning achievement details match the database

  # Verifikasi Menu Navigasi
    And the navigation menu lists materials first, then quizzes
    And all navigation items have a white background
    And each material and quiz name matches the database

  @TC2
  Scenario: Pelajar sudah memulai pembelajaran, tetapi belum menyelesaikan
    Given the user is on the courses page
    And the first course card shows a progress between "1%" and "99%"
    Then the overview page for that course is displayed

    When the user clicks the "Lanjutkan Kursus" button on the overview page
    Then the course content page is displayed

  # Verifikasi Judul & Progress
    And the navigation title displays the course name from the database (column “nama_course”)
    And the progress bar is partially filled
    And the progress percentage is between "1%" and "99%"

  # Verifikasi Informasi Capaian
    And the learning achievement details match the database

  # Verifikasi Menu Navigasi
    And the navigation menu lists materials first, then quizzes
    And some navigation items have a white background (incomplete)
    And some navigation items have a green background (completed)
    And each material and quiz name matches the database
    And Completed items in the navigation should match the database

  @TC3
  Scenario: Halaman konten kursus - Pelajar sudah menyelesaikan kursus (TC3)
    Given User has completed courses in database
    And User switches to "Selesai" tab
    Then System displays active tab "Selesai"

    And the first course card shows a progress of "100%"
    When the user clicks the "Lihat Kursus" button on the overview page
    Then the course content page is displayed
    And the navigation title displays the course name from the database (column “nama_course”)
    And the progress bar is full
    And the progress percentage is "100%"
    And the learning achievement details match the database
    And each material and quiz name matches the database
    And the navigation menu lists materials first, then quizzes
    And all navigation items have a green background (completed)
    And Completed items in the navigation should match the database
