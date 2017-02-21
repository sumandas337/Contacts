package domain.com.contacts;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import domain.com.contacts.application.ContactsApplication;
import domain.com.contacts.database.ContactsManager;
import domain.com.contacts.flowcontact.activity.ContactAddActivity;
import domain.com.contacts.flowcontact.viewmodel.AddContactViewModel;
import domain.com.contacts.net.RestClient;
import domain.com.contacts.utils.Navigator;
import domain.com.contacts.utils.RxBus;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;


/**
 * Created by sumandas on 13/02/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ContactAddActivityTest {

    @Inject
    AddContactViewModel mViewModel;

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
    public IntentsTestRule<ContactAddActivity> mActivityTestRule =
            new IntentsTestRule<>(ContactAddActivity.class, true,     // initialTouchMode
                    false);

    @Before
    public void setUp() {
        Instrumentation instrumentation = getInstrumentation();
        ContactsApplication app
                = (ContactsApplication) getTargetContext().getApplicationContext();
        ((TestComponent) app.getmApplicationComponent()).injectContactAddTest(this);
        mContext = instrumentation.getTargetContext().getApplicationContext();
    }

    @Before
    public void grantCameraStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.CAMERA");
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.ACCESS_NETWORK_STATE");
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.WRITE_EXTERNAL_STORAGE");
            getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + getTargetContext().getPackageName()
                            + " android.permission.READ_EXTERNAL_STORAGE");
        }
    }


    @Test
    public void verify_add_button_enabled_on_valid_fields() {
        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        onView(withId(R.id.add_contact_details))
                .check(matches(not(isEnabled())));

        onView(withId(R.id.contact_first_name_tv))
                .perform(click())
                .perform(typeText("hello"));

        onView(withId(R.id.contact_last_name_tv))
                .perform(click())
                .perform(typeText("world"));

        onView(withId(R.id.contact_phone_number_textview))
                .perform(click())
                .perform(typeText("111111111111"));

        onView(withId(R.id.add_contact_details))
                .check(matches(isEnabled()));

    }

    @Test
    public void verify_add_button_disabled_on_invalid_fields() {
        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);

        onView(withId(R.id.add_contact_details))
                .check(matches(not(isEnabled())));

        onView(withId(R.id.contact_first_name_tv))
                .perform(click())
                .perform(typeText("he"));

        onView(withId(R.id.contact_last_name_tv))
                .perform(click())
                .perform(typeText("world"));

        onView(withId(R.id.contact_phone_number_textview))
                .perform(click())
                .perform(typeText("111111111111"));

        //verify with first name not valid
        onView(withId(R.id.add_contact_details))
                .check(matches(not(isEnabled())));

        onView(withId(R.id.contact_phone_number_textview))
                .perform(clearText())
                .perform(click())
                .perform(typeText("1111"));

        onView(withId(R.id.contact_first_name_tv))
                .perform(click())
                .perform(typeText("hello"));

        //verify with phone not valid
        onView(withId(R.id.add_contact_details))
                .check(matches(not(isEnabled())));


        onView(withId(R.id.contact_phone_number_textview))
                .perform(clearText())
                .perform(click())
                .perform(typeText("111111111111"));

        onView(withId(R.id.contact_last_name_tv))
                .perform(clearText())
                .perform(click())
                .perform(typeText("w"));

        //verify with last name not valid
        onView(withId(R.id.add_contact_details))
                .check(matches(not(isEnabled())));
    }


    @Test
    public void verify_camera_opened_on_photo_click() {
        Intent intent = new Intent();
        mActivityTestRule.launchActivity(intent);
        onView(withId(R.id.add_profile_picture))
                .perform(click());

        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));

    }

}
