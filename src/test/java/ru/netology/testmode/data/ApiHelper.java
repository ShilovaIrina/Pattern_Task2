package ru.netology.testmode.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ApiHelper {
    //Спецификация
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private ApiHelper() {
    }

    //Метод для отправки User
    static DataGenerator.RegistrationDto sendRequest(DataGenerator.RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when().log().all() //методы позволяющие увидеть полную информацию об отправленом запросе
                .post("/api/system/users")
                .then().log().all() //И его ответе
                .statusCode(200);
        return user;
    }
}
