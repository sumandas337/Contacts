package domain.com.contacts.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by sumandas on 02/02/2017.
 */

public class ConstantUtils {

    public static final String CONTACT_OBJECT="contact_object";
    public static final String SUCCESS="success";

    public static final String EMPTY="";

    public static final int ADD_REQUEST_CODE=1001;

    public static final int SHOW_DETAILS_CODE=1002;

    public static final int PIC_PHOTO_CODE = 10344;

    public static final String DIRECTORY_NAME = "Contacts";

    public static final String ROOT_DIRECTORY = Environment.getExternalStorageDirectory()
            + File.separator + DIRECTORY_NAME;

    public static final String IMAGE_DIRECTORY_NAME = "Images";

    public static final String IMAGE_DIRECTORY = ROOT_DIRECTORY + File.separator
            + IMAGE_DIRECTORY_NAME;

    public static final String ADD_SUCCESS = "Added contact to server successfully";

    public static final String ADD_FAILURE = "Adding contact to server failed";

}
