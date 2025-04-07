rootProject.name = "organization-system"

// Business domain
include(
    "services:user-service",
    "services:company-service",
)

// Infrastructure
include(
    "services:discovery-service",
)

// Shared
include(
    "common:shared-lib",
    "common:shared-test",
)
