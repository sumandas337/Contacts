package domain.com.contacts.database;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

import domain.com.contacts.model.Contact;
import domain.com.contacts.model.Contact_Table;
import rx.Observable;

/**
 * Created by sumandas on 30/01/2017.
 */

public class  ContactsManager {

    private static volatile ContactsManager sContactManager;

    public static ContactsManager initializeManager() {
        if (sContactManager == null) {
            synchronized (ContactsManager.class) {
                if (sContactManager == null) {
                    sContactManager = new ContactsManager();
                }
            }
        }
        return sContactManager;
    }

    public static ContactsManager getInstance() {
        if (sContactManager == null) {
            throw new IllegalStateException("Manager not initialized . Call initializeManager");
        } else {
            return sContactManager;
        }
    }

    private ContactsManager(){

    }

    //returns the max id stored.
    public  Observable<Integer> getNextId() {
        return Observable.create(subscriber -> {
            List<Contact> contacts = SQLite.select()
                    .from(Contact.class)
                    .queryList();
            int maxId = -1;
            if (contacts != null) {
                for (Contact contact : contacts) {
                    if (contact.mId > maxId) {
                        maxId = contact.mId;
                    }
                }
            }
            subscriber.onNext(maxId + 1);
            subscriber.onCompleted();
        });
    }

    public  Observable<ArrayList<Contact>> getContacts() {
        return Observable.create(subscriber -> {
            List<Contact> contacts = SQLite.select()
                    .from(Contact.class)
                    .queryList();
            subscriber.onNext((ArrayList<Contact>) contacts);
            subscriber.onCompleted();
        });
    }

    public  Contact getContactById(int id) {
        return SQLite.select().from(Contact.class).where(Contact_Table.mId.eq(id)).querySingle();
    }

    public  void persistContactsFromServer(ArrayList<Contact> contacts) {
        for (Contact contact : contacts) {
            contact.save();
        }
    }

    public  void updateContact(Contact contact) {
        contact.update();
    }

    public  void saveContact(Contact contact) {
        contact.save();
    }

}
