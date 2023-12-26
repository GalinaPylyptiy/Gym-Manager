Feature: Trainee get profile positive scenario with good credentials and negative scenario with bad credentials

  Scenario: get trainee profile with correct login and password
    Given a user sends the get request to the specified url with username and password to fetch the trainee profile
    When the system receives the get request to get the trainee profile and return the status 200
    Then the system should respond with the trainee profile response

  Scenario: Get trainee profile with bad credentials
    Given a user logs in success and sends the get request to the specified url with bad credentials to fetch the trainee profile
    When the system receives a request to get the trainee profile and return throws an exception
    Then the system should respond with the status 400 - Bad Credentials