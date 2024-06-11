package backend.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;

public class BaseTest {
    public static final String BASE_URL = "https://swapi.dev/api/";
    public static final String FILMS = "films";
    public static final String PEOPLE = "people";

    protected static RequestSpecification requestSpecification;

    @BeforeTest
    public void setup() {

        RestAssured.useRelaxedHTTPSValidation();
        requestSpecification = new RequestSpecBuilder()
                .addFilter(new AllureRestAssured())
                .build();
    }
}
