package domain.com.contacts.flowcontact.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import domain.com.contacts.R;
import domain.com.contacts.application.ContactsApplication;
import domain.com.contacts.application.DaggerAppComponent;
import domain.com.contacts.application.IAppComponent;
import domain.com.contacts.flowcontact.ContactsContract;
import domain.com.contacts.flowcontact.presenter.ContractDetailsPresenter;
import domain.com.contacts.model.Contact;
import domain.com.contacts.utils.ConstantUtils;
import domain.com.contacts.utils.DummyUtils;
import domain.com.contacts.utils.FontTextView;
import domain.com.contacts.utils.ImageUtils;
import domain.com.contacts.utils.Navigator;
import domain.com.contacts.utils.ViewUtils;

/**
 * Created by sumandas on 02/02/2017.
 */

public class ContactDetailsActivity extends AppCompatActivity implements ContactsContract.IContactsDetailsView {

    @Inject
    public ContractDetailsPresenter mDetailsPresenter;

    @Inject
    public Navigator mNavigator;


    @BindView(R.id.contact_name)
    FontTextView mContactName;

    @BindView(R.id.favorite_button)
    ImageView mFavorite;

    @BindView(R.id.contact_phone_number)
    FontTextView mContactPhone;

    @BindView(R.id.contact_email)
    FontTextView mContactEmail;

    @BindView(R.id.profile_picture)
    ImageView mImageView;

    @BindView(R.id.contact_root_view)
    View mRootView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private Contact mContact;

    private RxPermissions mRxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_details);

        ButterKnife.bind(this);

        ((ContactsApplication)getApplication()).getmApplicationComponent().injectContactDetailsActivity(this);

        if (savedInstanceState == null) {
            mContact = getIntent().getParcelableExtra(ConstantUtils.CONTACT_OBJECT);
        } else {
            mContact = savedInstanceState.getParcelable(ConstantUtils.CONTACT_OBJECT);
        }

        mRxPermissions = new RxPermissions(this);

        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.contact_details));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            mToolbar.setNavigationOnClickListener(v -> finish());
        }

        populateViewElements();

    }

    @Inject
    public void setUp() {
        mDetailsPresenter.setMvpView(this);
    }

    @OnClick({R.id.contact_phone_number, R.id.phone_image})
    @Override
    public void onPhoneNumberClicked() {
        mRxPermissions.request(Manifest.permission.CALL_PHONE)
                .subscribe(granted -> {
                    if (granted) {
                        mNavigator.navigateToPhoneDailer(ContactDetailsActivity.this, mContact.mPhoneNumber);
                    } else {
                        Snackbar.make(mRootView, "Call Phone Permissions not available. Please enable to call"
                                , Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick({R.id.contact_email, R.id.email_image})
    @Override
    public void onEmailClicked() {
        mNavigator.navigateToEmail(this, mContact.mEmail);
    }

    @OnClick({R.id.favorite_button})
    @Override
    public void onFavoriteClicked() {
        mDetailsPresenter.favoriteClicked(mContact);
        ViewUtils.setTint(this,mFavorite,R.color.grey,R.color.pink,mContact.isFavorite);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private void populateViewElements() {
        mContactName.setText(mContact.getFullName());
        mContactPhone.setText(DummyUtils.giveDefaultNumber(mContact.mPhoneNumber));
        mContactEmail.setText(DummyUtils.giveDefaultEmail(mContact.mEmail));
        ViewUtils.setTint(this,mFavorite,R.color.grey,R.color.pink,mContact.isFavorite);
        ImageUtils.convertImageUrlToRoundedView(this, mContact.mProfilePic, mImageView);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ConstantUtils.CONTACT_OBJECT, mContact);
    }


}
