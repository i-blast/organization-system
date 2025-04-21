rootProject.name = "organization-system"

// Business domain
include(
    "services:user-service",
    "services:company-service",
)

// Infrastructure
include(
    "services:discovery-service",
    "services:gateway-service",

    "services:config-service",

    "e2e-tests",
)

// Shared
include(
    "common:shared-lib",
    "common:shared-test",
)
