package domain.com.contacts;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import domain.com.contacts.database.ContactsManager;
import domain.com.contacts.flowcontact.ContactsContract;
import domain.com.contacts.flowcontact.presenter.ContactsListPresenter;
import domain.com.contacts.model.Contact;
import domain.com.contacts.net.RestClient;
import domain.com.contacts.rules.ImmediateSchedulerRule;
import domain.com.contacts.rules.TestSchedulerRule;
import domain.com.contacts.utils.DummyUtils;
import rx.Observable;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;


/**
 * Created by sumandas on 09/02/2017.
 */
public class ContactsListPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ImmediateSchedulerRule testSchedulerRule = new ImmediateSchedulerRule();

    @Mock
    ContactsContract.IContactsListView mView;

    @Mock
    RestClient mRestClient;

    @Mock
    ContactsManager mContactsManager;

    @InjectMocks
    ContactsListPresenter mContactsListPresenter;

    ContactsListPresenter mSpyPresenter;

    @Before
    public void setUp() {
        spy(ContactsManager.class);
        mContactsListPresenter = new ContactsListPresenter(mRestClient,mContactsManager);
        mContactsListPresenter.setMvpView(mView);
        mSpyPresenter = Mockito.spy(mContactsListPresenter);
    }


    @Test
    public void verify_on_load_called_in_create() {
        ArrayList<Contact> contactsList = new ArrayList<>();
        contactsList.add(DummyUtils.getDummyContact());
        contactsList.add(DummyUtils.getDummyContact());
        Observable<ArrayList<Contact>> contact = Observable.create(subscriber -> {
            subscriber.onNext(contactsList);
            subscriber.onCompleted();
        });
        when(mContactsManager.getContacts()).thenReturn(contact);
        when(mRestClient.getContacts()).thenReturn(contact);
        mSpyPresenter.create();
        verify(mSpyPresenter).loadContacts();
    }

    //TODO figure out the null pointer crash

/*    @Test
    public void verify_network_call_non_empty_db_empty() {
        ArrayList<Contact> contactsList = new ArrayList<>();
        contactsList.add(DummyUtils.getDummyContact());
        contactsList.add(DummyUtils.getDummyContact());

        ArrayList<Contact> emptyList = new ArrayList<>();

        Observable<ArrayList<Contact>> emptyContacts =Observable.just(emptyList);
        Observable<ArrayList<Contact>> someContacts =Observable.just(contactsList).delay(2, TimeUnit.SECONDS);

        when(mContactsManager.getContacts()).thenReturn(emptyContacts);
        when(mRestClient.getContacts()).thenReturn(someContacts);

        mSpyPresenter.loadContacts();

        verify(mView).onContactsLoaded(contactsList);

    }*/


}
