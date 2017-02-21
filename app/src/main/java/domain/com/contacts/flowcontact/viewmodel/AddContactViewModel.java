package domain.com.contacts.flowcontact.viewmodel;

import android.Manifest;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import domain.com.contacts.database.ContactsManager;
import domain.com.contacts.event.AddContactEvent;
import domain.com.contacts.flowcontact.ContactsContract;
import domain.com.contacts.model.Contact;
import domain.com.contacts.net.RestClient;
import domain.com.contacts.utils.ConstantUtils;
import domain.com.contacts.utils.ImageUtils;
import domain.com.contacts.utils.RxBus;
import rx.Observable;
import rx.Observer;

import static domain.com.contacts.utils.RxUtils.buildIO;
import static domain.com.contacts.utils.RxUtils.getUploadToAWSObservable;
import static domain.com.contacts.utils.RxUtils.toObservable;
import static domain.com.contacts.utils.StringUtils.isValidEmail;
import static domain.com.contacts.utils.StringUtils.isValidName;
import static domain.com.contacts.utils.StringUtils.isValidPhoneNumber;

/**
 * Created by sumandas on 04/02/2017.
 */

public class AddContactViewModel implements ContactsContract.IAddContactModel {


    public ObservableField<String> firstName = new ObservableField<>();
    public ObservableField<String> lastName = new ObservableField<>();

    public ObservableField<String> phoneNumber = new ObservableField<>();
    public ObservableField<String> emailAddress = new ObservableField<>();

    public ObservableBoolean saveEnabled = new ObservableBoolean(false);

    public ObservableField<String> imageUrl = new ObservableField<>();

    public ContactsContract.IAddContactView mView;

    public RestClient mRestClient;

    public ContactsManager mContactManager;

    public File mContactPhoto;

    public String mCloudUrl= ConstantUtils.EMPTY;

    public RxBus mRxBus;

    @Inject
    public AddContactViewModel(RestClient restClient,RxBus rxBus,ContactsManager contactsManager) {
        mRestClient =restClient;
        mRxBus =rxBus;
        mContactManager= contactsManager;
        Observable.combineLatest(toObservable(firstName)
                , toObservable(lastName)
                , toObservable(phoneNumber)
                , toObservable(emailAddress), (firstName1, lastName1, phoneNumber1, email) -> {
                    boolean isFirstNameValid = isValidName(firstName1);
                    boolean isLastNameValid = isValidName(lastName1);
                    boolean isPhoneValid = isValidPhoneNumber(phoneNumber1);
                    boolean isEmailValid = isValidEmail(email);
                    return isFirstNameValid && isLastNameValid && isPhoneValid && isEmailValid;
                }).subscribe(isSaveEnabled -> {
            saveEnabled.set(isSaveEnabled);
        });
    }

    public void setView(ContactsContract.IAddContactView view) {
        mView = view;
    }

    public void onSaveClicked(View view) {
        buildIO(mContactManager.getNextId()).subscribe(nextId -> {
            String firstName1 = firstName.get();
            String lastName1 = lastName.get();
            String phoneNumber1 = phoneNumber.get();
            String email1 =emailAddress.get();

            Contact contact =Contact.createNewContact(nextId,firstName1,lastName1,
                    email1,phoneNumber1,mCloudUrl,false);
            mContactManager.saveContact(contact);
            mRxBus.postEvent(new AddContactEvent(contact));
            //post to server
            buildIO(mRestClient.addContact(contact))
                    .subscribe(new Observer<Contact>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Snackbar.make(view,ConstantUtils.ADD_FAILURE,
                                    Snackbar.LENGTH_SHORT);
                        }

                        @Override
                        public void onNext(Contact contact1) {
                            mView.finishView();
                        }
                    });
        });

    }


    public void onPhotoClick(View view) {
        mView.requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                ,Manifest.permission.CAMERA);
    }

    @Override
    public void onPermissionGranted() {
        try {
            mContactPhoto = ImageUtils.createImageFile();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mContactPhoto));
            mView.requestImage(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void uploadToCloud(String filePath) {
        Observable observable = getUploadToAWSObservable(filePath);
        buildIO(observable).subscribe(new Observer<URL>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(URL url) {
                if (url != null) {
                    mCloudUrl =url.toString();
                    imageUrl.set(mCloudUrl);
                }
            }
        });
    }

    @Override
    public void onCapturedImage() {
        buildIO(Observable.just(ImageUtils.compressImage(mContactPhoto))).subscribe(file1 -> {
            uploadToCloud(file1.getPath());
        });
    }
}
