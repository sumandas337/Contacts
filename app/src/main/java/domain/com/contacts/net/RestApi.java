package domain.com.contacts.net;

import java.util.ArrayList;

import domain.com.contacts.model.Contact;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by sumandas on 04/12/2016.
 */

public interface RestApi {

    @GET("/contacts.json")
    Observable<ArrayList<Contact>> getContacts();

    @POST("/contacts.json")
    Observable<Contact> addContact(@Body Contact contact);

    @PUT("contacts/{id}.json")
    Observable<Contact> updateContact(@Path("id") int id,@Body Contact contact);

}
