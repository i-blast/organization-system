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

    "config",
    "services:config-service",

    "e2e-tests",
)

// Shared
include(
    "common:shared-lib",
    "common:shared-test",
)
