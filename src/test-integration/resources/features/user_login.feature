Feature: Operation for user login to the system with success and failure outcome

  Scenario: User logs in with success
    Given a user sends a post request to the specific url using valid login and password
    When a system receives the request and confirms that the credentials are valid
    Then the response with valid jwt token is sent to the user and the status code is 200

  Scenario: User logs in with failure
    Given a user sends a post request to the specific url for logging in but using invalid login
    When a system receives the request and throws an exception for invalid credentials
    Then the HttpClientErrorException exception is thrown with status 400 - Bad Request


