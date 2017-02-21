package domain.com.contacts.utils;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Created by sumandas on 06/02/2017.
 */

public class AmazonCredentials {

    //TODO please  fill your own keys to use the upload feature
    public static final  String AWSBaseBucketName="";
    private static final String CLIENT_ACCESS_KEY= "";
    private static final String CLIENT_SECRECT_KEY= "";

    public  static AmazonS3Client getAmazonImageUploadInstance(){
        AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(CLIENT_ACCESS_KEY, CLIENT_SECRECT_KEY));
        return s3Client;
    }

}
