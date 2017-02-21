package domain.com.contacts.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.text.SimpleDateFormat;
import java.util.Comparator;

import domain.com.contacts.database.ContactsDb;
import domain.com.contacts.utils.TimeUtils;

/**
 * Created by sumandas on 30/01/2017.
 */
@Table(database = ContactsDb.class)
public class Contact  extends BaseModel implements Parcelable{

    @SerializedName("id")
    @Column
    @PrimaryKey
    public int mId;

    @SerializedName("first_name")
    @Column
    public String mFirstName;

    @SerializedName("last_name")
    @Column
    public String mLastName;

    @SerializedName("email")
    @Column
    public String mEmail;

    @SerializedName("phone_number")
    @Column
    public String mPhoneNumber;


    @SerializedName("profile_pic")
    @Column
    public String mProfilePic;

    @SerializedName("favorite")
    @Column
    public boolean isFavorite;

    @SerializedName("created_at")
    @Column
    public String mCreatedAt;

    @SerializedName("updated_at")
    @Column
    public String mUpdatedAt;

    @Column
    public boolean isServerOutofSync;


    public String getFullName(){
        return mFirstName+" "+mLastName;
    }


    public Contact() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mFirstName);
        dest.writeString(this.mLastName);
        dest.writeString(this.mEmail);
        dest.writeString(this.mPhoneNumber);
        dest.writeString(this.mProfilePic);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
        dest.writeString(this.mCreatedAt);
        dest.writeString(this.mUpdatedAt);
        dest.writeByte(this.isServerOutofSync ? (byte) 1 : (byte) 0);
    }

    protected Contact(Parcel in) {
        this.mId = in.readInt();
        this.mFirstName = in.readString();
        this.mLastName = in.readString();
        this.mEmail = in.readString();
        this.mPhoneNumber = in.readString();
        this.mProfilePic = in.readString();
        this.isFavorite = in.readByte() != 0;
        this.mCreatedAt = in.readString();
        this.mUpdatedAt = in.readString();
        this.isServerOutofSync = in.readByte() != 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public static  Contact createNewContact(int id,String firstName,String lastName,String email,
                                   String phoneNumber,String photoUrl,boolean isFavorite){
        Contact contact =new Contact();
        contact.mId=id;
        contact.mFirstName =firstName;
        contact.mLastName = lastName;
        contact.mEmail = email ;
        contact.mPhoneNumber =phoneNumber;
        contact.mProfilePic = photoUrl;
        contact.isFavorite = isFavorite;
        String timeNow=TimeUtils.getTimeNow();
        contact.mCreatedAt = timeNow;
        contact.mUpdatedAt = timeNow;
        return contact;
    }


    public static class NameAscendingComparator implements Comparator<Contact>{
        @Override
        public int compare(Contact lhs, Contact rhs) {
            int firstCompare= lhs.mFirstName.trim().compareToIgnoreCase(rhs.mFirstName.trim());
            if(firstCompare == 0){
                return lhs.mLastName.trim().compareToIgnoreCase(rhs.mLastName.trim());
            }else{
                return firstCompare;
            }

        }

    }
}
