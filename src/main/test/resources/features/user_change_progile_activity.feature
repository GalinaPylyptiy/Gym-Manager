Feature: Operation for user to change profile activity with success and failure outcome

  Scenario: User changes profile activity with success
    Given a user sends a patch request to the specific url with ChangeActivityRequest body using valid data
    When a system receives the patch request and changes the profile activity for the specified user
    Then the response with the status code 200 is returned indicating the success in changing profile activity

  Scenario: User changes profile activity with failure
    Given a user sends a patch request to the specific url with ChangeActivityRequest body but pass empty activity status
    When a system receives the request and throws an exception after verifying that data is invalid
    Then the Exception is thrown with status 400 - Bad Request indicating that the profile activity was not changed


