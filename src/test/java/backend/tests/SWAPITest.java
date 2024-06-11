package backend.tests;

import backend.utils.utils;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;


public class SWAPITest extends BaseTest {

    @Test
    public void swapiTest() {

        Response vader = given()
                .filter(new AllureRestAssured())
                .queryParam("search", "Vader")
                .when()
                .get(BASE_URL + PEOPLE)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        System.out.println("Darth Vader url to people api is " + vader.jsonPath().getString("results[0].url"));
        Assert.assertEquals(vader.jsonPath().getString("results[0].name"), "Darth Vader");

        List<String> vaderFilms = vader.jsonPath().getList("results[0].films");
        Map vaderFilmsWithPlanets = utils.filmsWithPlanetsCount(vaderFilms);
        String vaderFilmWithLessPlanets = (String) Collections.min(vaderFilmsWithPlanets.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println("Film with Vader and less planets is " + vaderFilmWithLessPlanets);
        Assert.assertEquals(vaderFilmWithLessPlanets, "A New Hope");

        String vaderStarshipURL = (String) vader.jsonPath().getList("results[0].starships").get(0);
        System.out.println(String.format("Is Vader starship in %s? - ", vaderFilmWithLessPlanets)+ utils.checkIfStarshipInFilm(vaderStarshipURL, vaderFilmWithLessPlanets));
        Assert.assertTrue(utils.checkIfStarshipInFilm(vaderStarshipURL, vaderFilmWithLessPlanets));
    }

    @Test
    public void getOldestCharacterThatAppeardInEveryFilmTest() {
        // Get number of films
        Response response = given()
                .filter(new AllureRestAssured())
                .when()
                .get(BASE_URL + FILMS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        // Get all characters that appeard in every film
        List<LinkedHashMap> objects = utils.getAllObjectsFromEndpointThatHaveRequiredNumberOfEntriesInCollection(PEOPLE, "films", Integer.parseInt(response.jsonPath().getString("count")));

        // Find the oldest character from list
        String oldestCharacterFromEveryFilm = "";
        double oldestCharacterFromEveryFilmNumber = 0.0;
        String oldestCharacterFromEveryFilmEra = "";
        for (LinkedHashMap object: objects)
        {
            String characterAge = (String) object.get("birth_year");
            String ageNumber = characterAge.substring(0, characterAge.length() - 3);
            String ageEra = characterAge.substring(characterAge.length() - 3);
            if (oldestCharacterFromEveryFilmEra.compareTo(ageEra) <= 0)
            {
                if (oldestCharacterFromEveryFilmNumber < Double.parseDouble(ageNumber))
                {
                    oldestCharacterFromEveryFilmEra = ageEra;
                    oldestCharacterFromEveryFilmNumber = Double.parseDouble(ageNumber);
                    oldestCharacterFromEveryFilm = (String) object.get("name");
                }
            }
        }
        System.out.println("Oldest character that appeard in every film is " + oldestCharacterFromEveryFilm);
        Assert.assertEquals(oldestCharacterFromEveryFilm, "C-3PO");
    }

    @Test
    public void schemaValidationPeopleAPITest() {
        given()
                .filter(new AllureRestAssured())
                .when()
                .get(BASE_URL + PEOPLE + "/1")
                .then().assertThat()
                .body(JsonSchemaValidator.
                        matchesJsonSchema(new File("src/main/resources/peopleApiJsonSchema.json")))
                .statusCode(SC_OK);
    }
}
