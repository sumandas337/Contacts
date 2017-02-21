package domain.com.contacts.utils;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;

import java.io.File;
import java.net.URL;
import java.util.Date;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;


/**
 * Created by sumandas on 29/01/2017.
 */

public class RxUtils {

    public static <T> Observable<T> buildIO(Observable<T> observable) {
        return observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public static <T> Observable<T> buildComputation(Observable<T> observable) {
        return observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }


    public static <T> Observable<T> buildTest(Observable<T> observable) {
        return observable
                .observeOn(Schedulers.immediate())
                .subscribeOn(Schedulers.immediate());
    }


    public static void unSubscribe(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed()) {
            return;
        }
        subscription.unsubscribe();
    }


    @NonNull
    public static <T> Observable<T> toObservable(@NonNull final ObservableField<T> field) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                subscriber.onNext(field.get());
                final android.databinding.Observable.OnPropertyChangedCallback callback = new android.databinding.Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        subscriber.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);
                subscriber.add(Subscriptions.create(() -> field.removeOnPropertyChangedCallback(callback)));
            }
        });
    }

    public static Observable getUploadToAWSObservable(String filePath) {
        Observable observable = Observable.create(subscriber -> {
            AmazonS3Client s3Client = AmazonCredentials.getAmazonImageUploadInstance();
            String randomNumber = Utils.createRandomNumber();
            Object obj = null;
            PutObjectRequest por = new PutObjectRequest(AmazonCredentials.AWSBaseBucketName, randomNumber + ".jpeg", new File(filePath));
            obj = s3Client.putObject(por);
            if(obj!=null){
                ResponseHeaderOverrides override = new ResponseHeaderOverrides();
                override.setContentType("image/jpeg");
                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(AmazonCredentials.AWSBaseBucketName, randomNumber + ".jpeg");
                urlRequest.setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 30 * 12 * 100));
                urlRequest.setResponseHeaders(override);
                URL url = s3Client.generatePresignedUrl(urlRequest);
                subscriber.onNext(url);
            }else{
                 subscriber.onNext(null);
            }
        });
        return observable;
    }
}
