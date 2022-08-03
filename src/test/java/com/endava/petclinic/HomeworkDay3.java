package com.endava.petclinic;

import com.endava.petclinic.utils.EnvReader;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;

public class HomeworkDay3 {

    @Test
    public void postTwitter(){

        given().basePath(EnvReader.getBasePathTwitter())
                .baseUri(EnvReader.getBaseUriTwitter())
                .auth()
                .oauth(EnvReader.getApiKey(), EnvReader.getApiKeySecret(), EnvReader.getAccessToken(), EnvReader.getAccessTokenSecret())
                .queryParam("status", "Alexxx")
                .when()
                .log()
                .all()
                .post("https://api.twitter.com/1.1/statuses/update.json")
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    public void getAllTwitter(){

        given().basePath(EnvReader.getBasePathTwitter())
                .baseUri(EnvReader.getBaseUriTwitter())
                .auth()
                .oauth(EnvReader.getApiKey(), EnvReader.getApiKeySecret(), EnvReader.getAccessToken(), EnvReader.getAccessTokenSecret())
                .queryParam("id", "278136668")
                .when()
                .log()
                .all()
                .get("https://api.twitter.com/1.1/statuses/user_timeline.json")
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
