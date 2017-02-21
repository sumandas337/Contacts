package domain.com.contacts.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import domain.com.contacts.flowcontact.activity.ContactAddActivity;
import domain.com.contacts.flowcontact.activity.ContactDetailsActivity;
import domain.com.contacts.model.Contact;

/**
 * Created by sumandas on 02/02/2017.
 */

public class Navigator {

    public void navigateToEmail(Context context, String email) {
        if(!TextUtils.isEmpty(email)){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", email, null));
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }
    }

    public void navigateToPhoneDailer(Context context,@NonNull String phone){
        if(!TextUtils.isEmpty(phone)){
            String uri = "tel:" + phone.trim() ;
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(uri));
            context.startActivity(intent);
        }
    }


    public void navigateToDetailsActivity(Activity activity, @NonNull Contact contact){
        Intent intent = new Intent(activity, ContactDetailsActivity.class);
        intent.putExtra(ConstantUtils.CONTACT_OBJECT,contact);
        activity.startActivityForResult(intent, ConstantUtils.SHOW_DETAILS_CODE);
    }

    public void navigateToAddActivity(Activity activity){
        Intent intent = new Intent(activity, ContactAddActivity.class);
        activity.startActivityForResult(intent,ConstantUtils.ADD_REQUEST_CODE);
    }

}
