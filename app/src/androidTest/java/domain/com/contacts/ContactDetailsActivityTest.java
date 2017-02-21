package domain.com.contacts;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import domain.com.contacts.application.ContactsApplication;
import domain.com.contacts.database.ContactsManager;
import domain.com.contacts.flowcontact.activity.ContactDetailsActivity;
import domain.com.contacts.flowcontact.presenter.ContractDetailsPresenter;
import domain.com.contacts.model.Contact;
import domain.com.contacts.net.RestClient;
import domain.com.contacts.utils.ConstantUtils;
import domain.com.contacts.utils.DummyUtils;
import domain.com.contacts.utils.Navigator;
import domain.com.contacts.utils.RxBus;
import rx.Observable;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static domain.com.contacts.TestUtils.withTint;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


/**
 * Created by sumandas on 12/02/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ContactDetailsActivityTest {

    @Inject
    ContractDetailsPresenter mContactsPresenter;

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
    public IntentsTestRule<ContactDetailsActivity> mActivityTestRule =
            new IntentsTestRule<>(ContactDetailsActivity.class,true,     // initialTouchMode
                    false);

    @Before
    public void setUp() {
        Instrumentation instrumentation = getInstrumentation();
        ContactsApplication app
                = (ContactsApplication) getTargetContext().getApplicationContext();
        ((TestComponent)app.getmApplicationComponent()).injectContactDetailsTest(this);
        mContext =  instrumentation.getTargetContext().getApplicationContext();
    }

    @Before
    public void grantPhonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.CALL_PHONE");
        }
    }

    @Test
    public void verify_email_phone_name_shown() {
        Contact contact = DummyUtils.getDummyContact();
        Intent intent = new Intent();
        intent.putExtra(ConstantUtils.CONTACT_OBJECT, contact);
        mActivityTestRule.launchActivity(intent);
        onView(withId(R.id.contact_name))
                .check(matches(withText(contact.getFullName())))
                .check(matches(isDisplayed()));
        onView(withId(R.id.contact_phone_number))
                .check(matches(withText(contact.mPhoneNumber)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.contact_email))
                .check(matches(withText(contact.mEmail)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void verify_favorite_button_clicked_tint_change(){
        Contact contact = DummyUtils.getDummyContact();
        Intent intent = new Intent();
        intent.putExtra(ConstantUtils.CONTACT_OBJECT, contact);
        Observable<Contact> updateObservable = TestUtils.getContactObservable(contact);
        when(mRestClient.updateContact(any(Contact.class))).thenReturn(updateObservable);
        mActivityTestRule.launchActivity(intent);
        onView(withId(R.id.favorite_button))
                .perform(click());
        onView(withId(R.id.favorite_button))
                .check(matches(withTint(mContext,R.color.pink)));
        onView(withId(R.id.favorite_button))
                .perform(click());
        onView(withId(R.id.favorite_button))
                .check(matches(withTint(mContext,R.color.grey)));

    }

    @Test
    public void verify_phone_click_phone_opened(){
        Contact contact = DummyUtils.getDummyContact();
        Intent intent = new Intent();
        intent.putExtra(ConstantUtils.CONTACT_OBJECT, contact);
        mActivityTestRule.launchActivity(intent);
        onView(withId(R.id.contact_phone_number))
                .perform(click());
        intended(allOf(toPackage("com.android.server.telecom")
                ,hasData(Uri.parse("tel:"+contact.mPhoneNumber.trim()))));

    }

    @Test
    public void verify_phone_icon_click_phone_opened(){
        Contact contact = DummyUtils.getDummyContact();
        Intent intent = new Intent();
        intent.putExtra(ConstantUtils.CONTACT_OBJECT, contact);

        mActivityTestRule.launchActivity(intent);

        onView(withId(R.id.phone_image))
                .perform(click());
        intended(allOf(toPackage("com.android.server.telecom")
                ,hasData(Uri.parse("tel:"+contact.mPhoneNumber.trim()))));

    }

    @Test
    public void verify_email_click_email_opened(){
        Contact contact = DummyUtils.getDummyContact();
        Intent intent = new Intent();
        intent.putExtra(ConstantUtils.CONTACT_OBJECT, contact);
        mActivityTestRule.launchActivity(intent);
        onView(withId(R.id.contact_email))
                .perform(click());
        intended(hasAction(Intent.ACTION_CHOOSER));

    }

    @Test
    public void verify_email_image_click_email_opened(){
        Contact contact = DummyUtils.getDummyContact();
        Intent intent = new Intent();
        intent.putExtra(ConstantUtils.CONTACT_OBJECT, contact);
        mActivityTestRule.launchActivity(intent);
        onView(withId(R.id.email_image))
                .perform(click());
        intended(hasAction(Intent.ACTION_CHOOSER));

    }


}
