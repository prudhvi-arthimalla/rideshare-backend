package com.rideshare.user.web.examples;

public class RequestExample {

    public static final String REGISTER_REQUEST = """
            {
                "email": "john.doe@email.com",
                "password": "password123",
                "firstName": "John",
                "lastName": "Doe",
                "phoneNumber": "+14155552671",
                "role": "RIDER"
            }
            """;

    public static final String LOGIN_REQUEST = """
            {
                "email": "john.doe@email.com",
                "password": "password123"
            }
            """;
}
