package com.endava.petclinic;

import com.endava.petclinic.controllers.OwnerController;
import com.endava.petclinic.models.Owner;
import com.endava.petclinic.utils.EnvReader;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PetClinicTestDay2 {
    //CAND FACEM POST AVEM NEVOIE DE BODY SI CONTENT TYPE
    @Test
    public void postOwnerTest() {
//pt serializare trebuie adaugat jackson-databind
        HashMap<String, String> owner = new HashMap<>();
        owner.put("id", null);
        owner.put("firstName", "George");
        owner.put("lastName", "Ionescu");
        owner.put("address", "Tineretului 71");
        owner.put("city", "Bucharest");
        owner.put("telephone", "0720049199");

        ValidatableResponse response = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(owner)
                .post("/api/owners")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        Integer id = response.extract().jsonPath().getInt("id");

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .pathParam("ownerId", id)
                .when()
                .get("/api/owners/{ownerId}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", is(id));

    }

    @Test
    public void postOwnerTestWithObject() {
        Faker faker = new Faker();
//        Owner owner = new Owner(faker.name().firstName(),
//                faker.name().lastName(),
//                faker.address().streetAddress(),
//                faker.address().city(),
//                faker.number().digits(10));

        Owner owner = OwnerController.generateNewRandomOwner();
//randul de mai sus e similar cu cele comentate, doar ca acum am facut o clasa ca sa nu setam de fiecare data
        ValidatableResponse response = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .when()
                .body(owner).log().all()
                .post("/api/owners")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);


        owner.setId(response.extract().jsonPath().getInt("id"));
        ValidatableResponse getResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .pathParam("ownerId", owner.getId())
                .when()
                .get("/api/owners/{ownerId}")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK);
//in as punem clasa in care avem atributele
        Owner ownerFromGetResponse = getResponse.extract().as(Owner.class);
//owner initial si cel de mai sus sunt egale
//compara toate campurile, nu doar id-urile
        assertThat(ownerFromGetResponse, is(owner));

    }

    @Test
    public void putOwnerTest() {
        //CAND FACEM UN PUT TREBUIE SA:
        //Facem post pt a crea un utilizator
        //II modificam anumite campuri
        //Facem put-ul
        //Facem un get (vine owner-ul cu datele date la put), apoi un assert pentru a verifica ca datele s-au actualizat
        Faker faker = new Faker();
        Owner owner = OwnerController.generateNewRandomOwner();

        ValidatableResponse postResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(owner)
                .when().log().all()
                .post("/api/owners")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        //extragem id-ul
        owner.setId(postResponse.extract().jsonPath().getInt("id"));
        //modificam city si telefon
        owner.setCity(faker.address().city());
        owner.setTelephone(faker.number().digits(10));

        given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .pathParam("ownerId", owner.getId())
                .body(owner).log().all()
                .put("/api/owners/{ownerId}")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        ValidatableResponse getResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .pathParam("ownerId", owner.getId())
                .get("/api/owners/{ownerId}")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK);

        Owner acctualOwner = getResponse.extract().as(Owner.class);

        assertThat(acctualOwner, is(owner));


    }
}
