package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PostsApiTests {
    @BeforeMethod
    public void setUP() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/";
    }

    @Test
    public void testGet() {
        given().log().uri()
                .expect().statusCode(200)
                .when().get("/posts")
                .then().log().status()
                .body("id", Matchers.hasSize(100));
    }

    @Test
    public void testGetPost() {
        given().log().uri()
                .expect().statusCode(200)
                .when().get("/posts/5")
                .then().body("id", equalTo(5))
                .and().body("title", Matchers.notNullValue());
    }

    @Test
    public void testPostNewPost() {
        given().log().uri().contentType(ContentType.JSON)
                .body("{\"userId\": 1,\n" +
                        "\"title\": \"banana\",\n" +
                        "\"body\": \"1234\"}")
                .when().post("/posts")
                .then().log().body().statusCode(201)
                .body("id", Matchers.notNullValue());
    }

    @Test
    public void testPutEditPost() {
        given().log().uri().contentType(ContentType.JSON)
                .body("{\"userId\": 1,\n" +
                        "\"title\": \"banana\",\n" +
                        "\"body\": \"1234\"}")
                .when().put("/posts/1")
                .then().log().body().statusCode(200)
                .body("title", equalTo("banana"))
                .and().body("body", equalTo("1234"));
    }

    @Test
    public void testPutNonExistIdFails() {
        given().log().uri().contentType(ContentType.JSON)
                .body("{\"userId\": 1,\n" +
                        "\"title\": \"banana\",\n" +
                        "\"body\": \"1234\"}")
                .when().put("/posts/101")
                .then().log().status().statusCode(500);
    }

    @Test
    public void testDeletePost() {
        given().log().uri()
                .when().delete("/posts/5")
                .then().log().status().statusCode(200);
    }
}
