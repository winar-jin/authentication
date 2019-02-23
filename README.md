## Authentication 
> A simple authentication services by using spring security and oauth2.

#### How to use:
* `git clone https://github.com/winar-jin/authentication.git`
* go to the root of this repository: `cd authentication`
* run `./gradlew build` to build the project
* run `./gradlew bootRun` to run the project, when you see "* run `./gradlew bootRun` to run the project, when you see "Tomcat started on port(s): 8080 (http) with context path.", that means you run successful.

#### Api example
* Register a new user:
    * Request:
    ```
    POST /api/register HTTP/1.1
    Host: localhost:8080
    Content-Type: application/json
    {
        "userName":"test2",
        "passWord":"111",
        "phoneNumber": "110",
        "email":"123@126.com"
    }
    ```
    * Response:
    ```json
    {
        "id": 1,
        "userName": "test2",
        "passWord": "$2a$11$1ucbwvijeV6Iga0BhpY0xOTOuJm4N9KUtozeJAFKMCXiBujzauquG",
        "phoneNumber": "110",
        "email": "123@126.com"
    }
    ```
* Search user info by `userName`
    * Request:
    ```
    GET /api/users/test2 HTTP/1.1
    Host: localhost:8080
    ```
    * Response:
    ```json
    {
        "id": 1,
        "userName": "test2",
        "passWord": "$2a$11$1ucbwvijeV6Iga0BhpY0xOTOuJm4N9KUtozeJAFKMCXiBujzauquG",
        "phoneNumber": "110",
        "email": "123@126.com"
    }
    ```
* Get token by `username` and `password`
    * Request:
    ```
    GET /oauth/token?username=test2&amp; password=111&amp; grant_type=password&amp; scope=read&amp; client_id=web&amp; client_secret=123456 HTTP/1.1
    Host: localhost:8080
    ```
    * Response:
    ```json
    {
        "access_token": "816839ac-8e2b-4803-a0ab-e5c4d7baf5d8",
        "token_type": "bearer",
        "refresh_token": "a9fc2ab3-6c4b-4c77-914f-90013bb3e01a",
        "expires_in": 599,
        "scope": "read"
    }
    ```
* Get item by `itemId`, "/item/{id}" is a authorization api, you have to request with a valid access token.
    * Right Request:
    ```
    GET /item/23 HTTP/1.1
    Host: localhost:8080
    Authorization: Bearer 816839ac-8e2b-4803-a0ab-e5c4d7baf5d8
    ```
    * Right Response:
    ```
    With authentication, the item id is 23.
    ```
    * Wrong Request:
    ```
    GET /item/23 HTTP/1.1
    Host: localhost:8080
    Authorization: Bearer 816839ac-8e2b-4803-a0ab-e5c4d7baf
    ```
    * Wrong Response:
    ```json
    {
        "error": "invalid_token",
        "error_description": "Invalid access token: 816839ac-8e2b-4803-a0ab-e5c4d7baf"
    }
    ```
* Get user info by token.
    * Request:
    ```
    GET /api/user HTTP/1.1
    Host: localhost:8080
    Authorization: Bearer 816839ac-8e2b-4803-a0ab-e5c4d7baf5d8
    ```
    * Response:
    ```
    test2
    ```
* Get all valid token by `clientId`.
    * Request:
    ```
    GET /api/oauth/tokens?clientId=web HTTP/1.1
    Host: localhost:8080
    ```
    * Response:
    ```json
    [
        "816839ac-8e2b-4803-a0ab-e5c4d7baf5d8"
    ]
    ```
* Refresh token by `refresh_token`.
> When your access token expire, it will become a invalid token. Then you can use refresh token to get a new token. Usually the refresh token will live longer than access token. But after you logout the access token, the related refresh token will be invalid too.

    * Request:
    ```
    GET /oauth/token?grant_type=refresh_token&amp; refresh_token=a9fc2ab3-6c4b-4c77-914f-90013bb3e01a&amp; client_id=web&amp; client_secret=123456 HTTP/1.1
    Host: localhost:8080
    ```
    * Response:
    ```json
    {
        "access_token": "28a46478-fe66-4476-a59e-df6653dbd510",
        "token_type": "bearer",
        "refresh_token": "5faf880b-8d67-4fe1-8563-4ae148c40c88",
        "expires_in": 599,
        "scope": "read"
    }
    ```
* Logout by `token` and `clientId`.
> After you logout, the token will become invalid, then you can't use the invalid token to access to the authorization request.  

    * Request:
    ```
    GET /api/logout?clientId=web HTTP/1.1
    Host: localhost:8080
    Authorization: Bearer 816839ac-8e2b-4803-a0ab-e5c4d7baf5d8
    ``` 
    * Response:
    ```
    logout successful!
    ```

