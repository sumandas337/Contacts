package domain.com.contacts;

/**
 * Created by sumandas on 10/02/2017.
 */

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;

import javax.inject.Inject;

import domain.com.contacts.application.ContactsApplication;
import domain.com.contacts.database.ContactsManager;
import domain.com.contacts.flowcontact.activity.ContactAddActivity;
import domain.com.contacts.flowcontact.activity.ContactDetailsActivity;
import domain.com.contacts.flowcontact.activity.ContactsListActivity;
import domain.com.contacts.flowcontact.presenter.ContactsListPresenter;
import domain.com.contacts.model.Contact;
import domain.com.contacts.net.RestClient;
import domain.com.contacts.utils.DummyUtils;
import domain.com.contacts.utils.Navigator;
import domain.com.contacts.utils.RxBus;
import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static domain.com.contacts.TestUtils.isSortedAlphabetically;
import static domain.com.contacts.TestUtils.withRecyclerView;
import static domain.com.contacts.TestUtils.withTint;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ContactListActivityTest {

    @Inject
    ContactsListPresenter mContactsPresenter;

    @Inject
    RxBus mRxBus;

    @Inject
    Navigator mNavigator;

    @Inject
    RestClient mRestClient;

    @Inject
    ContactsManager mContactManager;

    private Context mContext;


    @Rule
    public IntentsTestRule<ContactsListActivity> mActivityTestRule =
            new IntentsTestRule<>(ContactsListActivity.class, true,     // initialTouchMode
                    false);

    @Before
    public void setUp() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        ContactsApplication app
                = (ContactsApplication) instrumentation.getTargetContext().getApplicationContext();
        ((TestComponent) app.getmApplicationComponent()).injectContactListTest(this);
        mContext = instrumentation.getTargetContext().getApplicationContext();
    }

    @Test
    public void show_list_contacts_not_empty() {

        ArrayList<Contact> contactsList = new ArrayList<>();
        Contact contact = DummyUtils.getDummyContact();
        contactsList.add(contact);
        Observable<ArrayList<Contact>> contactObservable = TestUtils.getContactsListObservable(contactsList);
        when(mRestClient.getContacts()).thenReturn(contactObservable);

        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.contact_name))
                .check(matches(withText("hello world")))
                .check(matches(isDisplayed()));

    }

    @Test
    public void is_sorted_alphabetically() {

        ArrayList<Contact> contactsList = DummyUtils.getDummyList();

        Observable<ArrayList<Contact>> contactObservable = TestUtils.getContactsListObservable(contactsList);
        when(mRestClient.getContacts()).thenReturn(contactObservable);

        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.contacts_list_recycler))
                .check(matches(isSortedAlphabetically()));
    }


    @Test
    public void is_item_favorite_color_toggled_on_click() {
        ArrayList<Contact> contactsList = DummyUtils.getDummyList();

        Observable<ArrayList<Contact>> contactObservable = TestUtils.getContactsListObservable(contactsList);
        Mockito.when(mRestClient.getContacts()).thenReturn(contactObservable);


        Observable<Contact> updateObservable = TestUtils.getContactObservable(contactsList.get(0));
        when(mRestClient.updateContact(contactsList.get(0))).thenReturn(updateObservable);

        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.contacts_list_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        ViewActionUtils.clickChildViewWithId(R.id.favorite_check_box)));


        onView(withRecyclerView(R.id.contacts_list_recycler)
                .atPositionOnView(0, R.id.favorite_check_box))
                .check(matches(withTint(mContext, R.color.pink)));

        onView(withId(R.id.contacts_list_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        ViewActionUtils.clickChildViewWithId(R.id.favorite_check_box)));


        onView(withRecyclerView(R.id.contacts_list_recycler)
                .atPositionOnView(0, R.id.favorite_check_box))
                .check(matches(withTint(mContext, R.color.grey)));

    }

    @Test
    public void launch_add_details_in_fab_click() {

        ArrayList<Contact> contactsList = new ArrayList<>();
        Contact contact = DummyUtils.getDummyContact();
        contactsList.add(contact);
        Observable<ArrayList<Contact>> contactObservable = TestUtils.getContactsListObservable(contactsList);
        when(mRestClient.getContacts()).thenReturn(contactObservable);

        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.fab)).perform(click());

        intended(hasComponent(new ComponentName(mContext, ContactAddActivity.class)));

    }

    @Test
    public void launch_details_in_item_click() {

        ArrayList<Contact> contactsList = new ArrayList<>();
        Contact contact = DummyUtils.getDummyContact();
        contactsList.add(contact);
        Observable<ArrayList<Contact>> contactObservable = TestUtils.getContactsListObservable(contactsList);
        when(mRestClient.getContacts()).thenReturn(contactObservable);

        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.contacts_list_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(new ComponentName(mContext, ContactDetailsActivity.class)));
    }

}
