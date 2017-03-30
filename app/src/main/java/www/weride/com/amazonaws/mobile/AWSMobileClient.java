package www.weride.com.amazonaws.mobile;


import android.content.Context;
import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import www.weride.com.amazonaws.mobile.content.UserFileManager;
import www.weride.com.amazonaws.mobile.push.GCMTokenHelper;
import www.weride.com.amazonaws.mobile.push.PushManager;
import www.weride.com.amazonaws.mobile.user.signin.FacebookSignInProvider;
import www.weride.com.amazonaws.mobile.user.signin.GoogleSignInProvider;
import com.weride.www.awsmobilehelper.auth.IdentityManager;
import com.weride.www.awsmobilehelper.auth.signin.CognitoUserPoolsSignInProvider;

/**
 * The AWS Mobile Client bootstraps the application to make calls to AWS 
 * services. It creates clients which can be used to call services backing the
 * features you selected in your project.
 */
public class AWSMobileClient {

    private static final String LOG_TAG = AWSMobileClient.class.getSimpleName();

    private static volatile AWSMobileClient instance;

    private ClientConfiguration clientConfiguration;
    private IdentityManager identityManager;
    private GCMTokenHelper gcmTokenHelper;
    private PushManager pushManager;
    private AmazonDynamoDBClient dynamoDBClient;
    private DynamoDBMapper dynamoDBMapper;

    /**
     * Build class used to create the AWS mobile client.
     */
    public static class Builder {

        private Context applicationContext;
        private String  cognitoIdentityPoolID;
        private Regions cognitoRegion;
        private ClientConfiguration clientConfiguration;
        private IdentityManager identityManager;

	/**
	 * Constructor.
	 * @param context Android context.
	 */
        public Builder(final Context context) {
            this.applicationContext = context.getApplicationContext();
        };

	/**
	 * Provides the Amazon Cognito Identity Pool ID.
	 * @param cognitoIdentityPoolID identity pool ID
	 * @return builder
	 */
        public Builder withCognitoIdentityPoolID(final String cognitoIdentityPoolID) {
            this.cognitoIdentityPoolID = cognitoIdentityPoolID;
            return this;
        };
        
	/**
	 * Provides the Amazon Cognito service region.
	 * @param cognitoRegion service region
	 * @return builder
	 */
        public Builder withCognitoRegion(final Regions cognitoRegion) {
            this.cognitoRegion = cognitoRegion;
            return this;
        }

        /**
         * Provides the identity manager.
	 * @param identityManager identity manager
	 * @return builder
	 */
        public Builder withIdentityManager(final IdentityManager identityManager) {
            this.identityManager = identityManager;
            return this;
        }

        /**
         * Provides the client configuration
         * @param clientConfiguration client configuration
         * @return builder
         */
        public Builder withClientConfiguration(final ClientConfiguration clientConfiguration) {
            this.clientConfiguration = clientConfiguration;
            return this;
        }

	/**
	 * Creates the AWS mobile client instance and initializes it.
	 * @return AWS mobile client
	 */
        public AWSMobileClient build() {
            return
                new AWSMobileClient(applicationContext,
                                    cognitoIdentityPoolID,
                                    cognitoRegion,
                                    identityManager,
                                    clientConfiguration);
        }
    }

    private AWSMobileClient(final Context context,
                            final String  cognitoIdentityPoolID,
                            final Regions cognitoRegion,
                            final IdentityManager identityManager,
                            final ClientConfiguration clientConfiguration) {

        this.identityManager = identityManager;
        this.clientConfiguration = clientConfiguration;

        this.gcmTokenHelper = new GCMTokenHelper(context, AWSConfiguration.GOOGLE_CLOUD_MESSAGING_SENDER_ID);
        this.pushManager =
            new PushManager(context,
                            gcmTokenHelper,
                            identityManager.getCredentialsProvider(),
                            AWSConfiguration.AMAZON_SNS_PLATFORM_APPLICATION_ARN,
                            clientConfiguration,
                            AWSConfiguration.AMAZON_SNS_DEFAULT_TOPIC_ARN,
                            AWSConfiguration.AMAZON_SNS_TOPIC_ARNS,
                            AWSConfiguration.AMAZON_SNS_REGION);
        gcmTokenHelper.init();

        this.dynamoDBClient = new AmazonDynamoDBClient(identityManager.getCredentialsProvider(), clientConfiguration);
        this.dynamoDBClient.setRegion(Region.getRegion(AWSConfiguration.AMAZON_DYNAMODB_REGION));
        this.dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
    }

    /**
     * Sets the singleton instance of the AWS mobile client.
     * @param client client instance
     */
    public static void setDefaultMobileClient(AWSMobileClient client) {
        instance = client;
    }

    /**
     * Gets the default singleton instance of the AWS mobile client.
     * @return client
     */
    public static AWSMobileClient defaultMobileClient() {
        return instance;
    }

    /**
     * Gets the identity manager.
     * @return identity manager
     */
    public IdentityManager getIdentityManager() {
        return this.identityManager;
    }

    /**
     * Gets the push notifications manager.
     * @return push manager
     */
    public PushManager getPushManager() {
        return this.pushManager;
    }

    /**
     * Gets the Amazon Cognito Sync Manager, which is responsible for saving and
     * loading user profile data, such as game state or user settings.
     * @return sync manager
     */
    public CognitoSyncManager getSyncManager() {
        return identityManager.getSyncManager();
    }


    private static void addSignInProviders(final Context context, final IdentityManager identityManager) {
        // Add Facebook as an Identity Provider.
        identityManager.addIdentityProvider(FacebookSignInProvider.class);

        // Add Google as an Identity Provider.
        identityManager.addIdentityProvider(GoogleSignInProvider.class);

        // Add Cognito User Pools as an Identity Provider.
        identityManager.addIdentityProvider(CognitoUserPoolsSignInProvider.class);
    }

    /**
     * Creates and initialize the default AWSMobileClient if it doesn't already
     * exist using configuration constants from {@link AWSConfiguration}.
     *
     * @param context an application context.
     */
    public static void initializeMobileClientIfNecessary(final Context context) {
        if (AWSMobileClient.defaultMobileClient() == null) {
            Log.d(LOG_TAG, "Initializing AWS Mobile Client...");
            final ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setUserAgent(AWSConfiguration.AWS_MOBILEHUB_USER_AGENT);
            final IdentityManager identityManager = new IdentityManager(context, clientConfiguration,
                AWSConfiguration.getAWSMobileHelperConfiguration());

            addSignInProviders(context, identityManager);

            final AWSMobileClient awsClient =
                new AWSMobileClient.Builder(context)
                    .withCognitoRegion(AWSConfiguration.AMAZON_COGNITO_REGION)
                    .withCognitoIdentityPoolID(AWSConfiguration.AMAZON_COGNITO_IDENTITY_POOL_ID)
                    .withIdentityManager(identityManager)
                    .withClientConfiguration(clientConfiguration)
                    .build();

            AWSMobileClient.setDefaultMobileClient(awsClient);
        }
        Log.d(LOG_TAG, "AWS Mobile Client is OK");
    }

    /**
     * Gets the DynamoDB Client, which allows accessing Amazon DynamoDB tables.
     * @return the DynamoDB client instance.
     */
    public AmazonDynamoDBClient getDynamoDBClient() {
        return dynamoDBClient;
    }

    /**
     * Gets the Dynamo DB Object Mapper, which allows accessing DynamoDB tables using annotated
     * data object classes to represent your data using POJOs (Plain Old Java Objects).
     * @return the DynamoDB Object Mapper instance.
     */
    public DynamoDBMapper getDynamoDBMapper() {
        return dynamoDBMapper;
    }

    /**
     * Creates a User File Manager instance, which facilitates file transfers
     * between the device and the specified Amazon S3 (Simple Storage Service) bucket.
     *
     * @param context context.
     * @param s3Bucket Amazon S3 bucket
     * @param s3FolderPrefix Folder pre-fix for files affected by this user file
     *                       manager instance
     * @param resultHandler handles the resulting UserFileManager instance
     */
    public void createUserFileManager(final Context context,
                                      final String s3Bucket,
                                      final String s3FolderPrefix,
                                      final Regions region,
                                      final UserFileManager.BuilderResultHandler resultHandler) {

        new UserFileManager.Builder().withContext(context)
            .withIdentityManager(getIdentityManager())
            .withS3Bucket(s3Bucket)
            .withS3ObjectDirPrefix(s3FolderPrefix)
            .withLocalBasePath(context.getFilesDir().getAbsolutePath())
            .withClientConfiguration(clientConfiguration)
            .withRegion(region)
            .build(resultHandler);
    }

}
