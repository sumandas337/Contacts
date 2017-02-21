package domain.com.contacts;


import javax.inject.Singleton;

import dagger.Component;
import domain.com.contacts.application.AppModule;
import domain.com.contacts.application.IAppComponent;

/**
 * Created by sumandas on 11/02/2017.
 */
@Singleton
@Component(modules = {AppModule.class,MockRestClient.class})
public interface TestComponent extends IAppComponent{

    void injectContactListTest(ContactListActivityTest contactListActivityTest);
    void injectContactDetailsTest(ContactDetailsActivityTest contactDetailsActivityTest);
    void injectContactAddTest(ContactAddActivityTest contactAddActivityTest);
}
