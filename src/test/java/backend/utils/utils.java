package backend.utils;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.*;

import static backend.tests.BaseTest.BASE_URL;
import static backend.tests.BaseTest.FILMS;

public class utils {

    public static Map filmsWithPlanetsCount(List<String> films)
    {
        Map map=new HashMap();
        for (String film : films) {
            Response response = RestAssured.given()
                    .filter(new AllureRestAssured())
                    .when()
                    .get(film)
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .response();

            map.put(response.jsonPath().getString("title"), response.jsonPath().getList("planets").size());
        }
        return map;
    }

    public static boolean checkIfStarshipInFilm(String starshipURL, String filmName)
    {
        Response film = RestAssured.given()
                .filter(new AllureRestAssured())
                .queryParam("search", filmName)
                .when()
                .get(BASE_URL + FILMS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        return film.jsonPath().getList("results[0].starships").contains(starshipURL);
    }

    public static List getAllObjectsFromEndpointThatHaveRequiredNumberOfEntriesInCollection(String endpoint, String collection, int size) {
        Response response;
        String url = BASE_URL + endpoint;
        boolean hasNext = false;
        List<LinkedHashMap> objects = new ArrayList<>();

        do {
            response = RestAssured.given()
                    .filter(new AllureRestAssured())
                    .when()
                    .get(url)
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .response();

            for (int i=0; i<response.jsonPath().getList("results").size(); i++)
            {
                if(response.jsonPath().getList("results[" + i + "]." + collection).size()==size)
                {
                    objects.add(response.jsonPath().get("results[" + i + "]"));
                }
            }

            hasNext = response.jsonPath().getString("next") != null;
            if (hasNext)
                url = response.jsonPath().getString("next");
        } while (hasNext);
        return objects;
    }
}
