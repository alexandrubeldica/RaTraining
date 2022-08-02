package com.endava.petclinic;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class HomeworkRA {

    //Adaugare de animale owner-ului Beldica Florin Alexandru
    @Test
    public void postPetTest() {
        given().baseUri("http://api.petclinic.mywire.org")
                .basePath("/petclinic")
                .port(80)
                .contentType(ContentType.JSON)
                .body("\n" +
                        "{\n" +
                        "  \"birthDate\": \"2000/05/23\",\n" +
                        "  \"id\": null,\n" +
                        "  \"name\": \"Laky\",\n" +
                        "  \"owner\": {\n" +
                        "    \"address\": \"Primaverii\",\n" +
                        "    \"city\": \"Bucuresti\",\n" +
                        "    \"firstName\": \"Florin Alexandru\",\n" +
                        "    \"id\": 23,\n" +
                        "    \"lastName\": \"Beldica\",\n" +
                        "    \"pets\": [\n" +
                        "      null\n" +
                        "    ],\n" +
                        "    \"telephone\": \"9876543210\"\n" +
                        "  },\n" +
                        "  \"type\": {\n" +
                        "    \"id\": 2,\n" +
                        "    \"name\": \"Dog\"\n" +
                        "  },\n" +
                        "  \"visits\": [\n" +
                        "    {\n" +
                        "      \"date\": \"2022/03/10\",\n" +
                        "      \"description\": \"Annual Control\",\n" +
                        "      \"id\": null\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .when().log().all()
                .post("/api/pets/")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    //Test the get pet list API

    @Test

    public void getPetTest(){
        given().baseUri("http://api.petclinic.mywire.org")
                .basePath("/petclinic")
                .port(80)
                .when()
                .get("/api/pets")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    //Adaugare de vizita

    @Test
    public void postPetVisit(){
        given().baseUri("http://api.petclinic.mywire.org")
                .basePath("/petclinic")
                .port(80)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"date\": \"2022/08/10\",\n" +
                        "  \"description\": \"Vaccin\",\n" +
                        "  \"id\": null,\n" +
                        "  \"pet\": {\n" +
                        "    \"birthDate\": \"200/05/23\",\n" +
                        "    \"id\": 38,\n" +
                        "    \"name\": \"Laky\",\n" +
                        "    \"owner\": {\n" +
                        "      \"address\": \"Primaverii\",\n" +
                        "      \"city\": \"Bucuresti\",\n" +
                        "      \"firstName\": \"Florin Alexandru\",\n" +
                        "      \"id\": 95,\n" +
                        "      \"lastName\": \"Beldica\",\n" +
                        "      \"pets\": [\n" +
                        "        null\n" +
                        "      ],\n" +
                        "      \"telephone\": \"9876543210\"\n" +
                        "    },\n" +
                        "    \"type\": {\n" +
                        "      \"id\":2,\n" +
                        "      \"name\": \"Dog\"\n" +
                        "    },\n" +
                        "    \"visits\": [\n" +
                        "      null\n" +
                        "    ]\n" +
                        "  }\n" +
                        "}")
                .when().log().all()
                .post("/api/visits")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

    }
}
