package domain.com.contacts.flowcontact;

import android.content.Intent;

import java.util.ArrayList;

import domain.com.contacts.base.IBasePresenter;
import domain.com.contacts.base.IBaseView;
import domain.com.contacts.model.Contact;

/**
 * Created by sumandas on 30/01/2017.
 */

public interface ContactsContract {

    interface IContactsListView extends IBaseView {

        void onContactsLoaded(ArrayList<Contact> contacts);

        void onContactLoadError();

        void onContactAdded();

        void onOpenContactDetails(Contact contact);


    }

    interface IContactsListPresenter extends IBasePresenter {

        void loadContacts();

    }

    interface IContactsDetailsView extends IBaseView {
        void onPhoneNumberClicked();
        void onEmailClicked();
        void onFavoriteClicked();
    }

    interface IContactDetailsPresenter extends IBasePresenter {
        void favoriteClicked(Contact contact);
    }


    interface IAddContactModel {
        void onPermissionGranted();

        void onCapturedImage();
    }

    interface IAddContactView {
        void requestImage(Intent intent);
        void requestPermissions(String... permissions);
        void finishView();
    }

}
