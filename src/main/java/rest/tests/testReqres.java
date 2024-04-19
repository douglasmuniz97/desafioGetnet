package rest.tests;

import org.hamcrest.Matchers;
import org.junit.Test;
import rest.core.BaseTest;
import rest.pojo.RegisterLoginSuccessful;
import rest.pojo.RegisterLoginUnsuccessful;
import rest.pojo.Usuario;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static io.restassured.RestAssured.given;

public class testReqres extends BaseTest {

    @Test
    public void test01_ListUsers(){
        given()
        .when()
                .get("/users?page=2")
        .then()
                .statusCode(200)
                .body("page", Matchers.is(2))
                .body("per_page", Matchers.is(6))
                .body("total", Matchers.is(12))
                .body("total_pages", Matchers.is(2))
                .body("data[0].id", Matchers.is(7))
                .body("data[0].email", Matchers.is("michael.lawson@reqres.in"))
                .body("data[0].first_name", Matchers.is("Michael"))
                .body("data[0].last_name", Matchers.is("Lawson"));
    }

    @Test
    public void test02_SingleUser(){
        given()
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .body("data.id", Matchers.is(2))
                .body("data.email", Matchers.is("janet.weaver@reqres.in"))
                .body("data.first_name", Matchers.is("Janet"))
                .body("data.last_name", Matchers.is("Weaver"));
    }

    @Test
    public void test03_SingleUserNotFound(){
        given()
                .when()
                .get("/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    public void test04_ListResource(){
        given()
                .when()
                .get("/unknown")
                .then()
                .statusCode(200)
                .body("data[0].id", Matchers.is(1))
                .body("data[0].name", Matchers.is("cerulean"))
                .body("data[0].year", Matchers.is(2000))
                .body("data[0].color", Matchers.is("#98B2D1"));
    }

    @Test
    public void test05_SingleResource(){
        given()
                .when()
                .get("/unknown/2")
                .then()
                .statusCode(200);
    }

    @Test
    public void test06_SingleResourceNotFound(){
        given()
                .when()
                .get("/unknown/23")
                .then()
                .statusCode(404);
    }

    @Test
    public void test07_CreateUser(){
        Usuario usuario = new Usuario();
        usuario.setName("douglas.muniz");
        usuario.setJob("qa");
        given()
                .body(usuario)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", Matchers.is("douglas.muniz"))
                .body("job", Matchers.is("qa"));
    }

    @Test
    public void test08_UpdatePutUser(){
        Usuario usuario = new Usuario();
        usuario.setName("douglas.muniz");
        usuario.setJob("qa lead");
        given()
                .body(usuario)
                .when()
                .put("/users/23")
                .then()
                .statusCode(200)
                .body("name", Matchers.is("douglas.muniz"))
                .body("job", Matchers.is("qa lead"));
    }

    @Test
    public void test09_UpdatePatchUser(){
        Usuario usuario = new Usuario();
        usuario.setName("douglas.muniz");
        usuario.setJob("lead");
        given()
                .body(usuario)
                .when()
                .put("/users/23")
                .then()
                .statusCode(200)
                .body("name", Matchers.is("douglas.muniz"))
                .body("job", Matchers.is("lead"));
    }

    @Test
    public void test10_DeleteUser(){
        given()
                .when()
                .put("/users/999")
                .then()
                .statusCode(200);
    }

    @Test
    public void test11_RegisterSuccessful(){
        RegisterLoginSuccessful registerSuccessful = new RegisterLoginSuccessful();
        registerSuccessful.setEmail("rachel.howell@reqres.in");
        registerSuccessful.setPassword("doug123");
        given()
                .body(registerSuccessful)
                .when()
                .post("/register")
                .then()
                .statusCode(200);
    }

    @Test
    public void test12_RegisterUnsuccessful(){
        RegisterLoginUnsuccessful registerUnsuccessful = new RegisterLoginUnsuccessful();
        registerUnsuccessful.setEmail("sydney@fife");
        given()
                .body(registerUnsuccessful)
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .body("error", Matchers.is("Missing password"));
    }

    @Test
    public void test13_LoginSuccessful(){
        RegisterLoginSuccessful loginSuccessful = new RegisterLoginSuccessful();
        loginSuccessful.setEmail("rachel.howell@reqres.in");
        loginSuccessful.setPassword("doug123");
        given()
                .body(loginSuccessful)
                .when()
                .post("/login")
                .then()
                .statusCode(200);
    }

    @Test
    public void test14_LoginUnsuccessful(){
        RegisterLoginUnsuccessful loginUnsuccessful = new RegisterLoginUnsuccessful();
        loginUnsuccessful.setEmail("sydney@fife");
        given()
                .body(loginUnsuccessful)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .body("error", Matchers.is("Missing password"));
    }

    @Test
    public void test15_DelayedResponse(){
        given()

                .when()
                .get("/users?delay=3")
                .then()
                .statusCode(200);
    }

///---CENÁRIOS COM CONTRATO---

    @Test
    public void test16_ContratoCreateUser(){
        Usuario usuario = new Usuario();
        usuario.setName("douglas.muniz");
        usuario.setJob("qa");
        given()
                .body(usuario)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/createUser.json"));
    }

    @Test
    public void test17_ContratoUpdatePutUser(){
        Usuario usuario = new Usuario();
        usuario.setName("douglas.muniz");
        usuario.setJob("qa lead");
        given()
                .body(usuario)
                .when()
                .put("/users/23")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/updatePutUser.json"));
    }

    @Test
    public void test18_ContratoUpdatePatchUser(){
        Usuario usuario = new Usuario();
        usuario.setName("douglas.muniz");
        usuario.setJob("lead");
        given()
                .body(usuario)
                .when()
                .put("/users/23")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/updatePatchUser.json"));
    }

    @Test
    public void test19_ContratoRegisterUnsuccessful(){
        RegisterLoginUnsuccessful registerUnsuccessful = new RegisterLoginUnsuccessful();
        registerUnsuccessful.setEmail("sydney@fife");
        given()
                .body(registerUnsuccessful)
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("schemas/registerUnsuccessful.json"));
    }

    @Test
    public void test20_ContratoLoginUnsuccessful(){
        RegisterLoginUnsuccessful loginUnsuccessful = new RegisterLoginUnsuccessful();
        loginUnsuccessful.setEmail("sydney@fife");
        given()
                .body(loginUnsuccessful)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("schemas/loginUnsuccessful.json"));
    }
}
