Feature: Operation to update trainee trainers list with success and failure scenarios


  Scenario: Update Trainee's Trainers List with success
    Given a user sends the request to the specified url to update the Trainee`s trainers list with the Trainee username and list of trainers usernames
    When the system receives a request to update the trainee's trainers list and return the status 200
    Then the system should respond with a list of updated trainers

  Scenario: Update Trainee's Trainers List with failure
    Given a user sends the put request to the specified url to update the Trainee`s trainers list with empty trainers list
    When the system receives a request and throws an HttpClientErrorException.BadRequest exception
    Then the system should respond with Bad Request status 400