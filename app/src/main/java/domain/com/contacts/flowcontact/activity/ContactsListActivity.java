package domain.com.contacts.flowcontact.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import domain.com.contacts.R;
import domain.com.contacts.application.ContactsApplication;
import domain.com.contacts.event.AddContactEvent;
import domain.com.contacts.event.UpdateContactEvent;
import domain.com.contacts.flowcontact.ContactsContract;
import domain.com.contacts.flowcontact.adapter.ContactsListAdapter;
import domain.com.contacts.flowcontact.presenter.ContactsListPresenter;
import domain.com.contacts.model.Contact;
import domain.com.contacts.utils.Navigator;
import domain.com.contacts.utils.RxBus;
import rx.subscriptions.CompositeSubscription;

import static domain.com.contacts.utils.RxUtils.unSubscribe;


/**
 * Created by sumandas on 30/01/2017.
 */

public class ContactsListActivity extends AppCompatActivity implements ContactsContract.IContactsListView {

    private ContactsListAdapter mContactsAdapter;

    private CompositeSubscription mSubscription = new CompositeSubscription();

    private ProgressDialog mProgressDialog;

    @Inject
    ContactsListPresenter mContactsPresenter;

    @Inject
    RxBus mRxBus;

    @Inject
    Navigator mNavigator;


    @BindView(R.id.drawer_layout)
    View mRootView;
    @BindView(R.id.contacts_list_recycler)
    RecyclerView mContactsRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mContactsRecyclerView.setLayoutManager(layoutManager1);
        mContactsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            mToolbar.setNavigationOnClickListener(v -> finish());
        }


        mProgressDialog = new ProgressDialog(this);

        ((ContactsApplication)getApplication()).getmApplicationComponent().injectContactsListActivity(this);

        mContactsPresenter.create();

        mSubscription.add(mRxBus.toObservable().subscribe(event -> {
            if (event instanceof AddContactEvent) {
                Contact contactAdded = ((AddContactEvent) event).mContact;
                mContactsAdapter.addContact(contactAdded);
            } else if (event instanceof UpdateContactEvent) {
                Contact contactUpdated = ((UpdateContactEvent) event).mContact;
                mContactsAdapter.updateContact(contactUpdated);
            }
        }));

    }

    @Inject
    public void setUp() {
        mContactsPresenter.setMvpView(this);
    }



    @OnClick({R.id.fab})
    public void onAddClick() {
        mNavigator.navigateToAddActivity(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mContactsPresenter.destroy();
        unSubscribe(mSubscription);
    }

    @Override
    public void onContactLoadError() {
        Snackbar.make(mRootView, "Error in Loading Contacts", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onContactsLoaded(ArrayList<Contact> contacts) {
        mContactsAdapter = new ContactsListAdapter(this, contacts, mContactsPresenter);
        mContactsRecyclerView.setAdapter(mContactsAdapter);
        mContactsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onContactAdded() {

    }

    @Override
    public void onOpenContactDetails(Contact contact) {
        mNavigator.navigateToDetailsActivity(this, contact);
    }


    @Override
    public void showLoading() {
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        if (!isFinishing())
            mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

}
