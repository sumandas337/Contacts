package domain.com.contacts;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.test.espresso.core.deps.guava.collect.Ordering;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.List;

import domain.com.contacts.flowcontact.adapter.ContactsListAdapter;
import domain.com.contacts.model.Contact;
import rx.Observable;

/**
 * Created by sumandas on 11/02/2017.
 */

public class TestUtils {

    public static Observable<ArrayList<Contact>> getContactsListObservable(ArrayList<Contact> contactsList){
        return Observable.create(subscriber -> {
            subscriber.onNext(contactsList);
            subscriber.onCompleted();
        });
    }


    public static Observable<Contact> getContactObservable(Contact contact){
        return Observable.create(subscriber -> {
            subscriber.onNext(contact);
            subscriber.onCompleted();
        });
    }


    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static Matcher<View> isSortedAlphabetically() {
        return new TypeSafeMatcher<View>() {

            private final List<String> contacts = new ArrayList<>();

            @Override
            protected boolean matchesSafely(View item) {
                RecyclerView recyclerView = (RecyclerView) item;
                ContactsListAdapter contactsListAdapter = (ContactsListAdapter) recyclerView.getAdapter();
                ArrayList<Contact> contactsFromView = contactsListAdapter.getmContacts();
                for(Contact contact : contactsFromView){
                    contacts.add(contact.getFullName());
                }
                return Ordering.natural().isOrdered(contacts);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has items sorted alphabetically: " + contacts);
            }
        };
    }

    public static Matcher<View> withTint(Context context,int colorId){
        return new BoundedMatcher<View,ImageView>(ImageView.class) {
            private PorterDuffColorFilter mColorFilterToMatch;
            private  PorterDuffColorFilter mColorFilter;
            @Override
            public void describeTo(Description description) {
                mColorFilter =new PorterDuffColorFilter(ContextCompat.getColor(context,colorId), PorterDuff.Mode.SRC_ATOP);
                description.appendText("tint expected: " + mColorFilter.hashCode());
            }

            @Override
            protected boolean matchesSafely(ImageView item) {
                mColorFilter =new PorterDuffColorFilter(ContextCompat.getColor(context,colorId), PorterDuff.Mode.SRC_ATOP);
                mColorFilterToMatch = (PorterDuffColorFilter)item.getColorFilter();
                if(mColorFilterToMatch.hashCode() == mColorFilter.hashCode()){
                    return true;
                }else{
                    return false;
                }
            }
        };
    }

}
