Feature: View Dashboard

Background:
    Given User has opened the browser
    And User has navigated to the login page of JTK Learn app "https://polban-space.cloudias79.com/jtk-learn/"
    And User has logged in as student
    And User has accessed the Dashboard

Scenario Outline: Verifikasi halaman dasbor dengan status pelajar belum mendaftar kursus satu pun
    When User sees the dashboard appearance
    Then User should be able to see text "Hai, Nama Pengguna"
    And User should be able to see the page title: Kursus
    And User should be able to see the courses that haven't been joined
