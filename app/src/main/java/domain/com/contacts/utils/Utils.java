package domain.com.contacts.utils;

import java.util.Random;

/**
 * Created by sumandas on 06/02/2017.
 */

public class Utils {

    public static final int RandomNumberMaximumLimit = 100000;
    public static final int RandomNumberMinimumLimit = 10000;

    public static String createRandomNumber() {
        Random rand = new Random();
        int randomNum = rand.nextInt((RandomNumberMaximumLimit - RandomNumberMinimumLimit) + 1) + RandomNumberMinimumLimit;
        return String.valueOf(randomNum);
    }

    public static int rand() {
        Random rand = new Random();
        int randomNum = rand.nextInt((RandomNumberMaximumLimit - RandomNumberMinimumLimit) + 1) + RandomNumberMinimumLimit;
        return randomNum;
    }


}
