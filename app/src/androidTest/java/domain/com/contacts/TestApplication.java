package domain.com.contacts;

import domain.com.contacts.application.ContactsApplication;
import domain.com.contacts.application.DaggerAppComponent;


/**
 * Created by sumandas on 11/02/2017.
 */

public class TestApplication extends ContactsApplication {


    @Override
    public void initAppComponent(){
        mAppComponent = DaggerTestComponent.builder().build();
    }
}
