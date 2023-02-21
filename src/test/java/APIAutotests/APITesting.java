package APIAutotests;


import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;
import java.util.stream.Collectors;


import static io.restassured.RestAssured.given;


public class APITesting {


    private PojoClass register;


    private LoginPojo login;

    private SoftAssert softAssert; //мягкая проверка

    public Map<String,Double> Change(String currency) {


        return given().get("https://open.er-api.com/v6/latest/"+currency)
                .then().extract().body().jsonPath().getMap("rates").entrySet().stream()
                .collect(Collectors.toMap(x->String.valueOf(x.getKey()), x->Double.parseDouble(String.valueOf(x.getValue())),
                        (e1, e2) -> e1, LinkedHashMap::new));


    }

    @Test(dataProvider = "CurrencyValue",dataProviderClass = DataForTests.class)
    public void SortByAlphabet(String v)  {

         softAssert = new SoftAssert();



    Change(v).entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(System.out::println);

    }



    @Test(dataProvider = "CurrencyValue",dataProviderClass = DataForTests.class)
    public void SortedByValueFromHighToLow(String v){


        Map<String, Double> sortedRatesHighToLow =
                Change(v).entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) //Сортировка от большего к меньшему
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));

        sortedRatesHighToLow.entrySet().stream().forEach(System.out::println);



    }

    @Test(dataProvider = "CurrencyValue",dataProviderClass = DataForTests.class)
    public void SortedFromLowToHighValue(String v){

        Map<String, Double> sortedRatesLowToHigh =
                Change(v).entrySet().stream()
                        .sorted(Map.Entry.comparingByValue()) //Сортировка от меньшего к большему
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));

        sortedRatesLowToHigh.entrySet().stream().forEach(System.out::println);

    }



    @Test(priority = 0)
    public void SuccessRegistration(){


        register = new PojoClass(DataForTests.RandomUsername(), DataForTests.RandomPassword());

            given().contentType(ContentType.JSON).body(register).when().
                post("http://85.192.34.140:8080/api/register").
                then().log().body().statusCode(201);



    }
    @Test(priority = 1)
    public void SuccessLogin(){

        login= new LoginPojo(register.getLogin(),register.getPass());

         Response response =given().contentType(ContentType.JSON).body(login).when().post("http://85.192.34.140:8080/api/login").
                then().log().body().extract().response();

        JsonPath jsonPath = response.jsonPath();

        String token = jsonPath.get("token");


        softAssert.assertTrue(token!=null);

        softAssert.assertAll("Tests failed!");
    }

}




