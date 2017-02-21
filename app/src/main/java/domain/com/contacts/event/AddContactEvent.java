package domain.com.contacts.event;

import domain.com.contacts.model.Contact;

/**
 * Created by sumandas on 07/02/2017.
 */

public class AddContactEvent {

    public Contact mContact;

    public AddContactEvent(Contact contact){
        mContact=contact;
    }
}
