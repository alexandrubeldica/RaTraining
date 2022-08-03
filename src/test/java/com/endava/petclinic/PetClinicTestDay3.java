package com.endava.petclinic;

import com.endava.petclinic.controllers.OwnerController;
import com.endava.petclinic.controllers.PetController;
import com.endava.petclinic.models.Owner;
import com.endava.petclinic.models.Pet;
import com.endava.petclinic.models.PetType;
import com.endava.petclinic.models.User;
import com.endava.petclinic.utils.EnvReader;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PetClinicTestDay3 {
    //luam owner din pet
    //nu va fi unul deja existent, se va crea automat
    @Test
    public void postPet() {
        Pet pet = PetController.generateNewRandomPet();
//AICI CREAM OWNER
        ValidatableResponse createOwnerResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(pet.getOwner())
                .post("/api/owners")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        pet.getOwner().setId(createOwnerResponse.extract().jsonPath().getInt("id"));

//content type trebuie pus daca avem body, la fel si invers
        ValidatableResponse createPetTypeResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(pet.getType())
                .post("/api/pettypes")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        pet.getType().setId(createPetTypeResponse.extract().jsonPath().getInt("id"));

        ValidatableResponse createPetResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(pet)
                .post("/api/pets")
                .then()
                .statusCode(HttpStatus.SC_CREATED);


        pet.setId(createPetResponse.extract().jsonPath().getInt("id"));

        ValidatableResponse getPetResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .pathParam("petId", pet.getId())
                .get("/api/pets/{petId}")
                .then()
                .statusCode(HttpStatus.SC_OK);

        Pet actual = getPetResponse.extract().as(Pet.class);

        assertThat(actual, is(pet));
    }

    //ACUM FACEM PRIMA DATA UN OWNER
    //SI LUI II VOM CREA UN PET

    @Test
    public void createPetWithOwnerAndTypePet() {

        Owner owner = OwnerController.generateNewRandomOwner();
        ValidatableResponse createOwnerResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(owner)
                .post("/api/owners")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        owner.setId(createOwnerResponse.extract().jsonPath().getInt("id"));

        PetType petType = new PetType(new Faker().animal().name());

        ValidatableResponse createPetTypeResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(petType)
                .post("/api/pettypes")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        petType.setId(createPetTypeResponse.extract().jsonPath().getInt("id"));

        Pet pet = PetController.generateNewRandomPet();
        //chiar daca instanta vine cu owner si pettype generati nu sunt in BD
        //asa ca ii vom pune pe cei de mai sus

        pet.setOwner(owner);
        pet.setType(petType);

        ValidatableResponse createPetResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .contentType(ContentType.JSON)
                .body(pet)
                .post("/api/pets")
                .then()
                .statusCode(HttpStatus.SC_CREATED);


        pet.setId(createPetResponse.extract().jsonPath().getInt("id"));

        ValidatableResponse getPetResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPort())
                .pathParam("petId", pet.getId())
                .get("/api/pets/{petId}")
                .then()
                .statusCode(HttpStatus.SC_OK);

        Pet actual = getPetResponse.extract().as(Pet.class);

        assertThat(actual, is(pet));
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
                .port(EnvReader.getPortSecured())
                .auth()
                .preemptive() //cand apelam un ednpoint securizat, acesta ne raspunde si cere credidentiale apoi face call
                //trimite direct credidentiale, nu asteapta sa fie cerute, ajuta la optimizarea timpului
                .basic("admin", "admin") //se trec user+parola
                .contentType(ContentType.JSON)
                .when()
                .body(owner)
                .log().
                all()
                .post("/api/owners")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);


        owner.setId(response.extract().jsonPath().getInt("id"));
        ValidatableResponse getResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPortSecured())
                .auth()
                .preemptive()
                .basic("admin", "admin")
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
    public void createOwnerSecured() {

        Faker faker = new Faker();
        //instanta noua de User
        User user = new User(faker.name().username(), faker.internet().password(), "OWNER_ADMIN", "VET_ADMIN");

        given().basePath(EnvReader.getBasePath())
                .baseUri(EnvReader.getBaseUri())
                .port(EnvReader.getPortSecured())
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body(user)
                .post("/api/users")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        Owner owner = OwnerController.generateNewRandomOwner();
//randul de mai sus e similar cu cele comentate, doar ca acum am facut o clasa ca sa nu setam de fiecare data
        ValidatableResponse response = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPortSecured())
                .auth()
                .preemptive() //cand apelam un ednpoint securizat, acesta ne raspunde si cere credidentiale apoi face call
                //trimite direct credidentiale, nu asteapta sa fie cerute, ajuta la optimizarea timpului
                .basic(user.getUsername(), user.getPassword()) //se trec user+parola
                .contentType(ContentType.JSON)
                .when()
                .body(owner)
                .log()
                .all()
                .post("/api/owners")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_CREATED);


        owner.setId(response.extract().jsonPath().getInt("id"));
        ValidatableResponse getResponse = given().baseUri(EnvReader.getBaseUri())
                .basePath(EnvReader.getBasePath())
                .port(EnvReader.getPortSecured())
                .auth()
                .preemptive()
                .basic(user.getUsername(), user.getPassword())
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

}
