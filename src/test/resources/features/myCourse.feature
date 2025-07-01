Feature: Kursus Saya

  @TC-FR5-01
  Scenario: Pelajar belum mendaftar kursus
    Given User has logged in as "Pelajar"
    When User navigates to "Kursus Saya" page
    Then User should see the message "Belum ada kursus"

  @TC-FR5-05
  Scenario Outline: Pelajar mendaftar kursus namun belum menyelesaikan
    Given User has logged in as "Pelajar"
    When User navigates to "Kursus Saya" page
    And User selects the "Dalam Progres" tab
    Then User should see the following courses:
      | courseName       |
      | <courseName>     |

    Examples:
      | courseName       |
      | Pemrograman Java |
      | Desain Web       |

  @TC-FR5-10
  Scenario Outline: Pelajar menyelesaikan kursus
    Given User has logged in as "Pelajar"
    When User navigates to "Kursus Saya" page
    And User selects the "Selesai" tab
    Then User should see the following courses:
      | courseName       |
      | <courseName>     |

    Examples:
      | courseName       |
      | Pemrograman Java |
      | Desain Web       |