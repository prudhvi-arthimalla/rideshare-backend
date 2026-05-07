package com.rideshare.user.web.examples;

public class ResponseExample {

    public static final String LOGIN_RESPONSE = """
            {
                "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBlbWFpbC5jb20iLCJyb2xlIjoiUklERVIiLCJpZCI6MSwiaWF0IjoxNzQ2NjA0MjAwLCJleHAiOjE3NDY2OTA2MDB9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
            }
            """;

    public static final String USER_RESPONSE = """
            {
                "id": 1,
                "email": "john.doe@email.com",
                "firstName": "John",
                "lastName": "Doe",
                "phoneNumber": "+14155552671",
                "role": "RIDER",
                "createdAt": "2026-05-07T06:30:00Z",
                "updatedAt": "2026-05-07T06:30:00Z"
            }
            """;
}
