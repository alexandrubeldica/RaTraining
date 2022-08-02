package com.endava.petclinic;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PetClinicTest {

    @Test

    public void getOwnerByID() {
        given().baseUri("http://api.petclinic.mywire.org")
                .basePath("/petclinic")
                .port(80)
                .pathParam("ownerId", 1)
                //pana la when nu conteaza ordinea, sunt doar niste preconditii
                .when()//.log().all()
                .get("/api/owners/{ownerId}")
                .prettyPeek() //afisare in consola, nu trebuie neaparat pus, la fel si .log().all()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", is(1))
                //Verificare ca firstName este fix George
                .body("firstName", is("George"))
                //firstName continte org
                .body("firstName", containsString("org"))
                //lastName incepe cu Fr
                .body("lastName", startsWith("Fr"))
                //Comapra orasul si nu conteaza cum sunt scrise literele
                .body("city", equalToIgnoringCase("madiSon"))
                //Are lungimea de 10
                .body("telephone", hasLength(10));
    }

    @Test

    public void postOwnersTest(){
        ValidatableResponse response = given().baseUri("http://api.petclinic.mywire.org")
                .basePath("/petclinic")
                .port(80)
                .contentType(ContentType.JSON)
                //body trebuie pus in preconditii
                .body("{\n" +
                        "  \"address\": \"Primaverii\",\n" +
                        "  \"city\": \"Bucuresti\",\n" +
                        "  \"firstName\": \"Florin Alexandru\",\n" +
                        "  \"id\": null,\n" +
                        "  \"lastName\": \"Beldica\",\n" +
                        "  \"telephone\": \"9876543210\"\n" +
                        "}")
                .when().log().all()
                .post("/api/owners")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        Integer ownerdId = response.extract().jsonPath().getInt("id");

        given().baseUri("http://api.petclinic.mywire.org")
                .basePath("/petclinic")
                .port(80)
                .pathParam("ownerId", ownerdId)
                .when()
                .get("/api/owners/{ownerId}")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", is(ownerdId));
    }
}
