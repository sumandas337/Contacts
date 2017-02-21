package domain.com.contacts;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import domain.com.contacts.database.ContactsManager;
import domain.com.contacts.flowcontact.ContactsContract;
import domain.com.contacts.flowcontact.presenter.ContractDetailsPresenter;
import domain.com.contacts.model.Contact;
import domain.com.contacts.net.RestClient;
import domain.com.contacts.rules.ImmediateSchedulerRule;
import domain.com.contacts.utils.DummyUtils;
import domain.com.contacts.utils.RxBus;
import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by sumandas on 10/02/2017.
 */
public class ContactDetailsPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule public ImmediateSchedulerRule immediateSchedulerRule = new ImmediateSchedulerRule();

    @Mock
    ContactsContract.IContactsDetailsView mView;

    @Mock
    RestClient mRestClient;

    @Mock
    RxBus mRxBus;

    @Mock ContactsManager mContactsManager;

    @InjectMocks
    ContractDetailsPresenter mContactsDetailPresenter;

    @Before
    public void setUp(){
        mContactsDetailPresenter.setMvpView(mView);
    }

    @Test
    public void verify_db_updated_on_favorite_click(){
        Contact contact = DummyUtils.getDummyContact();

        Observable<Contact> observableContact =Observable.just(contact);

        when(mRestClient.updateContact(any(Contact.class))).thenReturn(observableContact);

        mContactsDetailPresenter.favoriteClicked(contact);

        verify(mContactsManager).updateContact(contact);
        verify(mRestClient).updateContact(contact);

    }
}
