package domain.com.contacts.flowcontact.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import domain.com.contacts.R;
import domain.com.contacts.application.ContactsApplication;
import domain.com.contacts.application.DaggerAppComponent;
import domain.com.contacts.application.IAppComponent;
import domain.com.contacts.databinding.ActivityContactAddBinding;
import domain.com.contacts.flowcontact.ContactsContract;
import domain.com.contacts.flowcontact.viewmodel.AddContactViewModel;
import domain.com.contacts.utils.ConstantUtils;
import domain.com.contacts.utils.NetworkUtil;

/**
 * Created by sumandas on 03/02/2017.
 */

public class ContactAddActivity extends AppCompatActivity implements ContactsContract.IAddContactView {

    @Inject
    AddContactViewModel mAddContactViewModel;

    RxPermissions mRxPermissions;

    @BindView(R.id.toolbar)
    Toolbar mToolBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((ContactsApplication)getApplication()).getmApplicationComponent().
                injectContactAddActivity(this);

        ActivityContactAddBinding activityContactAddBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_contact_add);
        activityContactAddBinding.setContactaddmodel(mAddContactViewModel);

        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.contact_add));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            mToolBar.setNavigationOnClickListener(v -> finish());
        }
        mRxPermissions = new RxPermissions(this);

    }

    @Inject
    public void setUp() {
        mAddContactViewModel.setView(this);
    }

    @Override
    public void requestImage(Intent intent) {
        startActivityForResult(intent, ConstantUtils.PIC_PHOTO_CODE);
    }


    @Override
    public void requestPermissions(String... permissions) {
        mRxPermissions.request(permissions).subscribe(granted -> {
            if (granted) {
                mAddContactViewModel.onPermissionGranted();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantUtils.PIC_PHOTO_CODE && resultCode == RESULT_OK) {
            if (mAddContactViewModel != null && NetworkUtil.isConnected(this)) {
                mAddContactViewModel.onCapturedImage();
            }
        }
    }

    @Override
    public void finishView() {
        finish();
    }


}
