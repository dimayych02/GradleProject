package APIAutotests;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;

public class DataForTests {
    @DataProvider
    public  Object[] CurrencyValue(){
        Object [] currencies={"EUR","USD","RUB","BYN"};
        return currencies;
    }


    public static String RandomUsername(){

        return  RandomStringUtils.randomAlphabetic(5);
    }

    public  static String RandomPassword(){

        return RandomStringUtils.randomAlphabetic(10);
    }
}
