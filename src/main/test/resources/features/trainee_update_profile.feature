Feature: Update trainee profile feature with success and failure outcome

  Scenario: Update trainee profile with success
    Given a user sends the request to the specified url with TraineeUpdateRequest dto
    When the system receives a request to update the trainee info and return the status 200
    Then the system should respond with a successful trainee update response

  Scenario: Update trainee profile with failure
    Given a user sends the put request to the specified url to update the trainee profile with the invalid data
    When the system receives a request to update the trainee profile and returns the status 400
    Then the system should respond with status 400 bad request