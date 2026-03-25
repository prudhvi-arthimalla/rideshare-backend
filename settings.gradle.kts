rootProject.name = "rideshare-backend"

include("user-service")
project(":user-service").projectDir = file("services/user-service")
