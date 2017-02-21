package domain.com.contacts.event;

import domain.com.contacts.model.Contact;

/**
 * Created by sumandas on 07/02/2017.
 */

public class UpdateContactEvent {

    public Contact mContact;

    public UpdateContactEvent(Contact contact){
        mContact=contact;
    }
}
