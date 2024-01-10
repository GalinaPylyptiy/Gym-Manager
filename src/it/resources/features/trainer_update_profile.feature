Feature: Update trainer profile feature with success and failure outcome

  Scenario: Update trainer profile with success
    Given a user sends the put request to the specified url with TrainerUpdateRequest
    When the system receives a request to update the trainer profile and returns the status 200
    Then the system should respond with a TrainerUpdateResponse

  Scenario: Update trainer profile with failure
    Given a user sends the put request to the specified url to update the trainer profile but leave the username blank
    When the system receives a request to update the trainer profile and throws an exception for invalid data
    Then the system should respond with status 400 bad request for invalid request data to update trainer