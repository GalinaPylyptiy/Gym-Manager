Feature: Operation for user to change password with success and failure outcome

  Scenario: User changes password with success
    Given a user sends a patch request to the specific url with ChangePasswordRequest body using valid data
    When a system receives the patch request and changes the password for the specified user
    Then the response with the status code 200 Ok is returned indicating success in changing password

  Scenario: User changes password with failure
    Given a user sends a patch request to the specific url with ChangePasswordRequest body but pass empty new password data
    When a system receives the request and throws an exception for bad request due to validation error
    Then the Exception is thrown with status 400 - Bad Request indicating that the password was not changed


