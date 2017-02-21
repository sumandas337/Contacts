package domain.com.contacts;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import domain.com.contacts.net.RestApi;
import domain.com.contacts.net.RestClient;
import domain.com.contacts.net.RxErrorHandlingCallAdapterFactory;
import domain.com.contacts.utils.TimeUtils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.mockito.Mockito.mock;

/**
 * Created by sumandas on 10/02/2017.
 */
@Module
public class MockRestClient {

    public static final String API_ROOT = "http://" +"gojek-contacts-app.herokuapp.com/";

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .connectTimeout(TimeUtils.TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor providesLoginInterceptor(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return  loggingInterceptor;
    }

    @Provides
    @Singleton
    GsonConverterFactory providesGsonConverterFactory(){
        return GsonConverterFactory.create();
    }


    @Provides
    @Singleton
    CallAdapter.Factory providesRxJavaCallAdapterFactory() {
        return RxErrorHandlingCallAdapterFactory.create();
    }


    @Provides
    @Singleton
    RestApi providesRestAdapter(OkHttpClient client,
                                GsonConverterFactory gsonConverterFactory,
                                CallAdapter.Factory rxErrorHandlingCallAdapterFactory) {

        Retrofit sRetrofit = new Retrofit.Builder()
                .baseUrl(API_ROOT)
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxErrorHandlingCallAdapterFactory)
                .build();
        return sRetrofit.create(RestApi.class);
    }

    @Provides
    @Singleton
    RestClient providesRestClient(RestApi restApi){
        return mock(RestClient.class);
    }


}
