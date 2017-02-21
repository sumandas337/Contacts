package domain.com.contacts.flowcontact.presenter;

import javax.inject.Inject;

import domain.com.contacts.base.IBaseView;
import domain.com.contacts.database.ContactsManager;
import domain.com.contacts.event.UpdateContactEvent;
import domain.com.contacts.flowcontact.ContactsContract;
import domain.com.contacts.model.Contact;
import domain.com.contacts.net.RestClient;
import domain.com.contacts.utils.RxBus;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sumandas on 02/02/2017.
 */

public class ContractDetailsPresenter implements ContactsContract.IContactDetailsPresenter {

    private ContactsContract.IContactsDetailsView mView;

    private RestClient mRestClient;

    private RxBus mRxbus;

    private ContactsManager mContactsManager;

    @Inject
    public ContractDetailsPresenter(RestClient restClient, RxBus rxBus,ContactsManager contactsManager){
        mRxbus=rxBus;
        mRestClient = restClient;
        mContactsManager =contactsManager;
    }


    @Override
    public void favoriteClicked(Contact contact) {
        contact.isFavorite=!contact.isFavorite;
        mContactsManager.updateContact(contact);

        Observable<Contact> observableUpdate =mRestClient.updateContact(contact);

        observableUpdate.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Contact>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Contact contact) {
                mRxbus.postEvent(new UpdateContactEvent(contact));
            }
        });

    }

    @Override
    public void setMvpView(IBaseView baseView) {
        mView= (ContactsContract.IContactsDetailsView) baseView;
    }

    @Override
    public void create() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public IBaseView getView() {
        return mView;
    }
}
