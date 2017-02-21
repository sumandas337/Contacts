package domain.com.contacts.utils;

import android.text.TextUtils;

import java.util.ArrayList;

import domain.com.contacts.model.Contact;

/**
 * Created by sumandas on 03/02/2017.
 */

public class DummyUtils {


    public static String giveDefaultNumber(String number){
        if(TextUtils.isEmpty(number)){
            return "+91-9742136538";
        }else{
            return number;
        }
    }

    public static String giveDefaultEmail(String email){
        if(TextUtils.isEmpty(email)){
            return "sumandas@treebohotels.com";
        }else{
            return email;
        }
    }


    public static Contact getDummyContact(){
        Contact contact =new Contact();
        contact.mId = Utils.rand();
        contact.mFirstName ="hello";
        contact.mLastName = "world";
        contact.isFavorite =false;
        contact.mPhoneNumber ="111111111111";
        contact.mEmail = "abc@gmail.com";
        contact.mProfilePic = ConstantUtils.EMPTY;
        contact.mCreatedAt =ConstantUtils.EMPTY;
        contact.mUpdatedAt =ConstantUtils.EMPTY;
        return contact;

    }

    public static ArrayList<Contact>  getDummyList(){
        ArrayList<Contact> contacts =new ArrayList<>();
        Contact contact =getDummyContact();
        contact.mFirstName = "abc";
        contact.mLastName = "def";
        contacts.add(contact);
        contact =getDummyContact();
        contact.mFirstName = "abc";
        contact.mLastName = "xyz";
        contacts.add(contact);

        contact =getDummyContact();
        contact.mFirstName = "def";
        contact.mLastName = "mnc";
        contacts.add(contact);

        contact =getDummyContact();
        contact.mFirstName = "lmn";
        contact.mLastName = "xyz";
        contacts.add(contact);

        return contacts;

    }

}
