package com.pii.user_service.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceE2ETest extends BaseIntegrationTest {

   /* @BeforeEach
    void setUp() {
        System.setProperty("company-service.url", "http://localhost:8081/api/companies");
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    static Long companyId;

    @Test
    @Order(1)
    void shouldCreateCompanyInCompanyService() {
        var request = new CreateCompanyRequest("TestCorp", 5000L, List.of());

        companyId = given()
                .contentType("application/json")
                .body(request)
                .when()
                .post("http://localhost:8081/api/companies")
                .then()
                .statusCode(200)
                .extract().jsonPath().getLong("id");
    }

    record CreateCompanyRequest(

            @NotBlank(message = "Company name must not be blank")
            @Size(max = 255, message = "Company name must be at most 255 characters")
            String name,

            @NotNull(message = "Budget must not be null")
            @PositiveOrZero(message = "Budget must be zero or positive")
            Long budget, // Budget in USD cents.

            @NotNull(message = "Employees list must not be null")
            List<Long> employees
    ) {
    }


    @Test
    @Order(2)
    void shouldCreateUserAndGetCompanyInResponse() {
        var request = new CreateUserRequest("John", "Doe", "123456789", companyId);

        given().contentType("application/json")
                .body(request)
                .when()
                .post("/api/users")
                .then()
                .statusCode(200)
                .body("firstName", equalTo("John"))
                .body("company.id", equalTo(companyId.intValue()));
    }*/
}
