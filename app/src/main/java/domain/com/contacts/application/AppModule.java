package domain.com.contacts.application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import domain.com.contacts.database.ContactsManager;
import domain.com.contacts.utils.Navigator;
import domain.com.contacts.utils.RxBus;

/**
 * Created by sumandas on 29/01/2017.
 */
@Module
public class AppModule {

    @Singleton
    @Provides
    RxBus providesRxBus(){
        return RxBus.getInstance();
    }

    @Singleton
    @Provides
    Navigator providesNavigator() {
        return new Navigator();
    }


    @Singleton
    @Provides
    ContactsManager providesContactManager(){
        return ContactsManager.initializeManager();
    }
}
