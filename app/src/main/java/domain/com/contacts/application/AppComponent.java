package domain.com.contacts.application;

import javax.inject.Singleton;

import dagger.Component;
import domain.com.contacts.net.RestApiModule;

/**
 * Created by sumandas on 29/01/2017.
 */
@Singleton
@Component(modules = {AppModule.class, RestApiModule.class})
public interface AppComponent extends IAppComponent {
}
