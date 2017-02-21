package domain.com.contacts.net;

import android.support.annotation.IntDef;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by sumandas on 04/12/2016.
 */
public class RetrofitException extends RuntimeException {

    /**
     * An {@link IOException} occurred while communicating to the server.
     */
    public static final int NETWORK = 0;

    /**
     * A non-200 HTTP status code was received from the server.
     */

    public static final int HTTP = 1;

    /**
     * An internal error occurred while attempting to execute a request. It is best practice to
     * re-throw this exception so your application crashes.
     */
    public static final int UNEXPECTED = 2;

    /**
     * Identifies the event kind which triggered a {@link RetrofitException}.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NETWORK, HTTP, UNEXPECTED})
    public @interface Kind {
    }

    private final String mUrl;
    private final Response mResponse;
    @Kind
    private final int mKind;
    private final Retrofit mRetrofit;

    RetrofitException(String message, String url, Response response, @Kind int kind, Throwable exception, Retrofit retrofit) {
        super(message, exception);
        this.mUrl = url;
        this.mResponse = response;
        this.mKind = kind;
        this.mRetrofit = retrofit;
    }


    public static RetrofitException httpError(String url, Response response, Retrofit retrofit) {
        String message = String.format(Locale.US, "%s %s", response.code(), response.message());
        return new RetrofitException(message, url, response, HTTP, null, retrofit);
    }

    public static RetrofitException networkError(IOException exception) {
        return new RetrofitException(exception.getMessage(), null, null, NETWORK, exception, null);
    }

    public static RetrofitException unexpectedError(Throwable exception) {
        return new RetrofitException(exception.getMessage(), null, null, UNEXPECTED, exception, null);
    }


    /**
     * The request URL which produced the error.
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * Response object containing status code, headers, body, etc.
     */
    public Response getResponse() {
        return mResponse;
    }

    /**
     * The event kind which triggered this error.
     */
    @Kind
    public int getKind() {
        return mKind;
    }

    /**
     * The Retrofit this request was executed on
     */
    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    /**
     * HTTP response body converted to specified {@code type}. {@code null} if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified {@code type}.
     */
    public <T> T getErrorBodyAs(Class<T> type) throws IOException {
        if (mResponse == null || mResponse.errorBody() == null) {
            return null;
        }
        Converter<ResponseBody, T> converter = mRetrofit.responseBodyConverter(type, new Annotation[0]);
        return converter.convert(mResponse.errorBody());
    }
}
