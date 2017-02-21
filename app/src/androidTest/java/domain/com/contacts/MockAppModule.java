package domain.com.contacts;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import domain.com.contacts.database.ContactsManager;
import domain.com.contacts.utils.Navigator;
import domain.com.contacts.utils.RxBus;

import static org.mockito.Mockito.mock;

/**
 * Created by sumandas on 12/02/2017.
 */
@Module
public class MockAppModule {

    @Singleton
    @Provides
    RxBus providesRxBus(){
        return mock(RxBus.class);
    }

    @Singleton
    @Provides
    Navigator providesNavigator() {
        return mock(Navigator.class);
    }


    @Singleton
    @Provides
    ContactsManager providesContactManager(){
        return mock(ContactsManager.class);
    }
}
