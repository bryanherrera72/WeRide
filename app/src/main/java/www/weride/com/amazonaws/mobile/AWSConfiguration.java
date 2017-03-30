
package www.weride.com.amazonaws.mobile;


import com.amazonaws.regions.Regions;
import com.weride.www.awsmobilehelper.config.AWSMobileHelperConfiguration;

/**
 * This class defines constants for the developer's resource
 * identifiers and API keys. This configuration should not
 * be shared or posted to any public source code repository.
 */
public class AWSConfiguration {
    // AWS MobileHub user agent string
    public static final String AWS_MOBILEHUB_USER_AGENT =
        "MobileHub a579f09b-7fc8-4eec-9b4a-4298d2b8300c aws-my-sample-app-android-v0.16";
    // AMAZON COGNITO
    public static final Regions AMAZON_COGNITO_REGION =
      Regions.fromName("us-west-2");
    public static final String  AMAZON_COGNITO_IDENTITY_POOL_ID =
        "us-west-2:2bc2a21c-dfe4-4b81-9a91-a254c1a79592";
    // Google Client ID for Web application
    public static final String GOOGLE_CLIENT_ID =
        "735715552598-pv2fkt18catfl1fpiv27j60vjlff1s63.apps.googleusercontent.com";
    // GOOGLE CLOUD MESSAGING SENDER ID
    public static final String GOOGLE_CLOUD_MESSAGING_SENDER_ID =
        "735715552598";
    // SNS PLATFORM APPLICATION ARN
    public static final String AMAZON_SNS_PLATFORM_APPLICATION_ARN =
        "arn:aws:sns:us-west-1:375656310812:app/GCM/weride_MOBILEHUB_833431439";
    public static final Regions AMAZON_SNS_REGION =
         Regions.fromName("us-west-1");
    // SNS DEFAULT TOPIC ARN
    public static final String AMAZON_SNS_DEFAULT_TOPIC_ARN =
        "arn:aws:sns:us-west-1:375656310812:weride_alldevices_MOBILEHUB_833431439";
    // SNS PLATFORM TOPIC ARNS
    public static final String[] AMAZON_SNS_TOPIC_ARNS =
        {};
    // S3 BUCKET
    public static final String AMAZON_S3_USER_FILES_BUCKET =
        "weride-userfiles-mobilehub-833431439";
    // S3 BUCKET REGION
    public static final Regions AMAZON_S3_USER_FILES_BUCKET_REGION =
        Regions.fromName("us-west-1");
    public static final Regions AMAZON_DYNAMODB_REGION =
       Regions.fromName("us-west-1");
    public static final String AMAZON_COGNITO_USER_POOL_ID =
        "us-west-2_uxJm521WH";
    public static final String AMAZON_COGNITO_USER_POOL_CLIENT_ID =
        "6vkaivck5v9pvg54sdee3gfs1j";
    public static final String AMAZON_COGNITO_USER_POOL_CLIENT_SECRET =
        "ij3ncnb0epf17fu14ss1oe7auvu1jlvk58a732tpu4sgo4umu79";

    //GroupDO TABLE NAME
    public static final String AMAZON_DYNAMODB_TABLENAME_GROUP= "weride-mobilehub-833431439-Conversation";
    //ConversationDO TABLE NAME
    public static final String AMAZON_DYNAMODB_TABLENAME_CONVERSATION = "weride-mobilehub-833431439-Groups";
    //UserProfileDO TABLE NAME
    public static final String AMAZON_DYNAMODB_TABLENAME_USERPROFILE = "weride-mobilehub-833431439-UserProfile";

    private static final AWSMobileHelperConfiguration helperConfiguration = new AWSMobileHelperConfiguration.Builder()
        .withCognitoRegion(AMAZON_COGNITO_REGION)
        .withCognitoIdentityPoolId(AMAZON_COGNITO_IDENTITY_POOL_ID)
        .withCognitoUserPool(AMAZON_COGNITO_USER_POOL_ID,
            AMAZON_COGNITO_USER_POOL_CLIENT_ID, AMAZON_COGNITO_USER_POOL_CLIENT_SECRET)
        .build();
    /**
     * @return the configuration for AWSKit.
     */
    public static AWSMobileHelperConfiguration getAWSMobileHelperConfiguration() {
        return helperConfiguration;
    }
}
