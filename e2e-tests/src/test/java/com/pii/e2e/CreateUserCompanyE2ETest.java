package com.pii.e2e;

import com.pii.shared.dto.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Disabled("Fix stability")
class CreateUserCompanyE2ETest extends BaseE2ETest {

    private String userServiceUrl;
    private String companyServiceUrl;

    @BeforeAll
    static void beforeAll() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void setUp() {
        userServiceUrl = getServiceUrl("user-service", 8081) + "/api/users";
        companyServiceUrl = getServiceUrl("company-service", 8082) + "/api/companies";

        await().atMost(40, SECONDS).untilAsserted(() -> {
            RestAssured.get(getServiceUrl("user-service", 8081) + "/actuator/health")
                    .then().statusCode(200);
            RestAssured.get(getServiceUrl("company-service", 8082) + "/actuator/health")
                    .then().statusCode(200);
        });
    }

    @Test
    void shouldCreateCompanyAndUserAndAssign() {

        // Create company
        CreateCompanyRequest createCompanyRequest = new CreateCompanyRequest(
                "Очень серьёзная организация",
                1000000000L,
                List.of()
        );
        CompanyDto company = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createCompanyRequest)
                .post(companyServiceUrl)
                .then()
                .statusCode(200)
                .extract()
                .as(CompanyDto.class);

        // Create user
        CreateUserRequest createUserRequest = new CreateUserRequest(
                "ilYa",
                "hello",
                "1234567890",
                company.getId()
        );
        UserDto user = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createUserRequest)
                .post(userServiceUrl)
                .then()
                .statusCode(200)
                .extract()
                .as(UserDto.class);

        // Assert user contains company
        assertThat(user.getCompany()).isNotNull();
        assertThat(user.getCompany().id()).isEqualTo(company.getId());

        // Assert updated company contains user
        CompanyDto updatedCompany = RestAssured.given()
                .get(companyServiceUrl + "/" + company.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(CompanyDto.class);
        assertThat(updatedCompany.getEmployees())
                .extracting(UserShortDto::id)
                .containsExactly(user.getId());
    }
}
