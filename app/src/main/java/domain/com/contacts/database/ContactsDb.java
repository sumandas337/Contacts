package domain.com.contacts.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by sumandas on 30/01/2017.
 */
@Database(name = ContactsDb.NAME, version = ContactsDb.VERSION)
public class ContactsDb {
    public static final String NAME = "ContactsDb";
    public static final int VERSION = 1;
}
