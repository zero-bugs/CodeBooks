// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0 (For details, see https://github.com/awsdocs/amazon-s3-developer-guide/blob/master/LICENSE-SAMPLECODE.)

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

/**
 * -Dcom.amazonaws.sdk.disableCertChecking=true
 * -Djavax.net.debug=ssl
 * dd if=/dev/zero of=tmp.1G bs=1G count=1
 */
public class AmazonS3Test {

    private static final int concurrentThreadNumber = 20;
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(concurrentThreadNumber);

    private static String clientRegion = "eu-america";
    private static String bucketName = "bucketxx01";
    private static String objKeyName = "obj01";

    private static String fileObjKeyName = "tmp.1G";


    public static void main(String[] args) throws IOException {

        try {
            AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration("127.0.0.1", clientRegion);
            ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setClientExecutionTimeout(580);
            clientConfiguration.setSocketTimeout(100);

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider())
                .withEndpointConfiguration(endpointConfiguration)
                    .withClientConfiguration(clientConfiguration)
//                    .withPathStyleAccessEnabled(true)
                    .withPayloadSigningEnabled(true)
                    .withChunkedEncodingDisabled(false)
                    .build();

//            s3Client.setPathStyleAccessEnabled(true);

            //list user buckets
            System.out.println("---------------list user buckets ---------------------");
//            List<Bucket> listRes = s3Client.listBuckets();
//            System.out.println(listRes);

            //create buckets
            System.out.println("---------------create user buckets ---------------------");
            CreateBucketRequest request = new CreateBucketRequest(bucketName);
            request.setRegion(clientRegion);
//            System.out.println(s3Client.createBucket(request));


            //get object
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objKeyName);
            s3Client.getObject(getObjectRequest);

//            List<Future> resList = new ArrayList<Future>();
//            for (int i = 0;i<concurrentThreadNumber;++i)
//            {
//                PutObjectRequest putObjectRequest =
//                        new PutObjectRequest(bucketName, objKeyName + i, new File(fileObjKeyName));
//
//                GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objKeyName);
//
//                java.util.concurrent.Future<S3Object> res =
//                        fixedThreadPool.submit(new Callable<S3Object>()
//                        {
//                            /**
//                             * Computes a result, or throws an exception if unable to do so.
//                             *
//                             * @return computed result
//                             * @throws Exception if unable to compute a result
//                             */
//                            @Override public S3Object call() throws Exception
//                            {
//                                //put object use chunked Upload
//                                System.out.println("---------------upload object to bucket ------------------");
//
//                                return s3Client.getObject(getObjectRequest);
//                            }
//                        });
//                resList.add(res);
//            }
//
//            for(Future f : resList)
//            {
//                System.out.println(f.get());
//            }



//            System.out.println(s3Client.);


            // Upload a text string as a new object.
//            s3Client.putObject(bucketName, stringObjKeyName, "Uploaded String Object");
            
            // Upload a file as a new object with ContentType and title specified.
//            PutObjectRequest request = new PutObjectRequest(bucketName, fileObjKeyName, new File(fileName));
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentType("plain/text");
//            metadata.addUserMetadata("x-amz-meta-title", "someTitle");
//            request.setMetadata(metadata);
//            s3Client.putObject(request);
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        finally
        {
            System.exit(0);
        }
    }
}
