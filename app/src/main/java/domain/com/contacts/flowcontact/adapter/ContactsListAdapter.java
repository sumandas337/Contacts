package domain.com.contacts.flowcontact.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

import domain.com.contacts.R;
import domain.com.contacts.model.Contact;
import domain.com.contacts.utils.FontTextView;
import domain.com.contacts.utils.ImageUtils;
import domain.com.contacts.utils.ViewUtils;

/**
 * Created by sumandas on 30/01/2017.
 */

public class ContactsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<Contact> mContacts;

    private Context mContext;

    private IContactsActionListener mListener;

    public ContactsListAdapter(Context context, @NonNull ArrayList<Contact> contacts, IContactsActionListener listener) {
        mContacts = contacts;
        mContext = context;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.item_contact_view, parent, false);
        return new ContactsViewHolder(mContext,rootView,mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Contact contact = mContacts.get(position);
        ((ContactsViewHolder) holder).mContact = contact;

        ViewUtils.setTint(mContext,((ContactsViewHolder) holder).mFavorite,R.color.grey,R.color.pink,contact.isFavorite);

        ((ContactsViewHolder) holder).mContactName.setText(contact.getFullName());

        ImageUtils.convertImageUrlToRoundedView(mContext,contact.mProfilePic,((ContactsViewHolder) holder).mProfilePic);
    }

    public void addContact(Contact contact){
        mContacts.add(0,contact);
        Collections.sort(mContacts,new Contact.NameAscendingComparator());
        notifyDataSetChanged();
    }

    public void updateContact(Contact contact){
        for(int i=0;i<mContacts.size();i++){
            if(contact.mId == mContacts.get(i).mId){
                mContacts.set(i,contact);
                notifyItemChanged(i);
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        public Contact mContact;
        public ImageView mFavorite;
        public ImageView mProfilePic;
        public FontTextView mContactName;
        public Context mContext;

        public ContactsViewHolder(Context context,View itemView, IContactsActionListener listener) {
            super(itemView);
            mFavorite = (ImageView) itemView.findViewById(R.id.favorite_check_box);
            mContactName = (FontTextView) itemView.findViewById(R.id.contact_name);
            mProfilePic =(ImageView) itemView.findViewById(R.id.profile_picture);
            mContext =context;
            itemView.setOnClickListener(view -> listener.onContactClicked(mContact));
            mFavorite.setOnClickListener(view -> {
                mContact.isFavorite=!mContact.isFavorite;
                listener.onContactMarkedFavorite(mContact);
                ViewUtils.setTint(mContext,mFavorite,R.color.grey,R.color.pink,mContact.isFavorite);
            });

        }

    }

    public ArrayList<Contact> getmContacts() {
        return mContacts;
    }

    public void setmContacts(ArrayList<Contact> mContacts) {
        this.mContacts = mContacts;
    }



    public interface IContactsActionListener {
        void onContactClicked(Contact contact);

        void onContactMarkedFavorite(Contact contact);

    }


}
