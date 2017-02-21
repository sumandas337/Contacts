package domain.com.contacts.net;

import java.util.ArrayList;

import domain.com.contacts.model.Contact;
import rx.Observable;

/**
 * Created by sumandas on 04/12/2016.
 */

public class RestClient {

    private RestApi mRestApi;

    public RestClient(RestApi restApi){
        mRestApi=restApi;
    }

    public Observable<ArrayList<Contact>> getContacts() {
        return mRestApi.getContacts();
    }

    public Observable<Contact> addContact(Contact contact){
        return mRestApi.addContact(contact);
    }

    public Observable<Contact> updateContact(Contact contact){
        return mRestApi.updateContact(contact.mId,contact);
    }


}
