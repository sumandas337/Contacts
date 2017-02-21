package domain.com.contacts.flowcontact.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import domain.com.contacts.base.IBaseView;
import domain.com.contacts.database.ContactsManager;
import domain.com.contacts.flowcontact.ContactsContract;
import domain.com.contacts.flowcontact.adapter.ContactsListAdapter;
import domain.com.contacts.model.Contact;
import domain.com.contacts.net.RestClient;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sumandas on 30/01/2017.
 */

public class ContactsListPresenter implements ContactsContract.IContactsListPresenter,
        ContactsListAdapter.IContactsActionListener {

    private ContactsContract.IContactsListView mView;

    private RestClient mRestClient;

    private ContactsManager mContactManager;


    @Inject
    public ContactsListPresenter(RestClient restClient, ContactsManager contactsManager) {
        mRestClient = restClient;
        mContactManager = contactsManager;
    }


    @Override
    public void loadContacts() {

        mView.showLoading();
        Observable<ArrayList<Contact>> networkAndSave = mRestClient.getContacts()
                .doOnNext(contacts -> {
                    mContactManager.persistContactsFromServer(contacts);
                });
        Observable<ArrayList<Contact>> db = mContactManager.getContacts();

        Observable<ArrayList<Contact>> concatObservable=getConcatObservable(networkAndSave, db);

                concatObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Contact>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onContactLoadError();
                        mView.hideLoading();
                    }

                    @Override
                    public void onNext(ArrayList<Contact> contacts) {
                        Collections.sort(contacts, new Contact.NameAscendingComparator());
                        mView.onContactsLoaded(contacts);
                        mView.hideLoading();
                    }
                });
    }

    public Observable<ArrayList<Contact>> getConcatObservable(Observable<ArrayList<Contact>> network, Observable<ArrayList<Contact>> db) {
        return Observable.concat(db, network).first(contacts -> {
            if (contacts == null || contacts.isEmpty()) {
                return false;
            } else {
                return true;
            }
        });
    }


    @Override
    public void setMvpView(IBaseView baseView) {
        mView = (ContactsContract.IContactsListView) baseView;
    }

    @Override
    public void create() {
        loadContacts();
    }

    @Override
    public void destroy() {

    }

    @Override
    public IBaseView getView() {
        return mView;
    }


    @Override
    public void onContactClicked(Contact contact) {
        mView.onOpenContactDetails(contact);
    }

    @Override
    public void onContactMarkedFavorite(Contact contact) {
        mContactManager.updateContact(contact);
        mRestClient.updateContact(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Contact>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Contact contact) {

            }
        });
    }


}
