package domain.com.contacts.application;

import domain.com.contacts.database.ContactsManager;
import domain.com.contacts.flowcontact.activity.ContactAddActivity;
import domain.com.contacts.flowcontact.activity.ContactDetailsActivity;
import domain.com.contacts.flowcontact.activity.ContactsListActivity;
import domain.com.contacts.net.RestClient;
import domain.com.contacts.utils.Navigator;
import domain.com.contacts.utils.RxBus;

/**
 * Created by sumandas on 29/01/2017.
 */


public interface IAppComponent {

    RestClient getRestClient();

    RxBus getRxBus();

    Navigator getNavigator();

    ContactsManager getContactManager();

    void injectContactsListActivity(ContactsListActivity contactsListActivity);

    void injectContactDetailsActivity(ContactDetailsActivity contactDetailsActivity);

    void injectContactAddActivity(ContactAddActivity contactAddActivity);


}


