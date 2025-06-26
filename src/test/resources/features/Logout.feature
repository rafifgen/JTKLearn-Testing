#Feature: Logout from Zaidan Educare School Application
#
#Background:
#    Given User has successfully logged in as "admin@example.com" with password "admin"
#    And User is on the Dasbor page Education Fund Payment Management System for Zaidan Educare School app "https://polban-space.cloudias79.com/jtk-learn/"
#
#    @ValidCredentials
#    Scenario: Check logout is successful with valid credentials as role "bendahara"
#        When User clicks on logout button
#        And User clicks yes button on logout confirmation pop up
#        Then User should be redirected to the login page