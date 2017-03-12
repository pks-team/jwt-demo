# JWT-Demo

Run with: 

`gradlew bootRun`

Create a token with:

`http://localhost:8080/auth/{userId}`

Set a token in a header X-JWT-Token and call:

`http://localhost:8080/token`

This returns the whole token deserialized.



