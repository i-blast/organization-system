rootProject.name = "organization-system"

include(
    "services:user-service",
    "services:company-service",

    "common:shared-lib",
    "common:shared-test",
)
