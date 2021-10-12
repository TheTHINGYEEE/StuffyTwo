package com.github.thethingyee.StuffyTwo.handlers.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AWSHandler {

    private final AmazonS3 bucket;
    private final String bucketName = "cloudstoragecdn";
    private final String prefix = "queues/";

    /**
     * Initialize the AWS bucket to get its info.
     */
    public AWSHandler() {
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials("accessKey", "secretKey")
        );

        bucket = AmazonS3ClientBuilder
                .standard()
                .withCredentials(awsCredentialsProvider)
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                "s3.ap-southeast-1.amazonaws.com", "ap-southeast-1"
                        )
                )
                .build();
    }

    /**
     * Generates a very short uuid using the first hex code before the first dash.
     *
     * @return Returns a short string uuid.
     */
    private String generateShortUUID() {
        String[] uuid = UUID.randomUUID().toString().split("-");
        return uuid[0];
    }

    /**
     * Saves the queue to a file given to it and can be uploaded to Amazon S3.
     *
     * @param queue The queue to be saved.
     * @return Returns a File object on where it is saved.
     * @throws IOException File can be created or not created.
     */
    public File savePublicQueue(List<AudioTrack> queue) throws IOException {
        List<String> queueNames = getPublicQueues();
        String generated = generateShortUUID();
        while(queueNames.contains(generated)) {
            generated = generateShortUUID();
            if(!queueNames.contains(generated)) break;
        }

        JSONObject parent = new JSONObject();
        parent.put("queueName", generated);

        ArrayList<String> urls = new ArrayList<>();
        queue.forEach(audioTrack -> {
            urls.add(audioTrack.getInfo().uri);
        });
        parent.put("songsInSort", urls);

        File folder = new File(".\\temp");
        folder.mkdirs();

        File f = new File(".\\temp\\" + generated + ".json");
        f.createNewFile();

        FileWriter fWriter = new FileWriter(f);
        fWriter.write(parent.toString(2));
        fWriter.flush();
        fWriter.close();

        return f;
    }

    /**
     * Get the list of queues uploaded to the bucket.
     *
     * @return Returns the list of keys in a prefix.
     */
    public List<String> getPublicQueues() {

        ListObjectsV2Request req = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix);

        ListObjectsV2Result result = bucket.listObjectsV2(req);
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        List<String> stuff = new ArrayList<>();
        objects.stream().forEach(s3ObjectSummary -> {
            stuff.add(s3ObjectSummary.getKey().replace(prefix, ""));
        });

        return stuff;
    }

    /**
     * Uploads a file (mainly a queue), and deletes it.
     *
     * @param file File input and this must be a JSON file.
     * @param altURL An alternative URL which you can put if the method gets the URL of the resource.
     * @param useAltUrl A boolean if you really want to use an alternative URL.
     * @return The URL for the uploaded resource.
     */
    public String uploadFile(File file, String altURL, boolean useAltUrl) {

        if(altURL != null) {
            useAltUrl = true;
        }

        try {
            InputStream is = new FileInputStream(file);
            byte[] bytes = IOUtils.toByteArray(is);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/json");
            metadata.setContentLength(bytes.length);
            bucket.putObject(new PutObjectRequest(bucketName, prefix + file.getName(), new ByteArrayInputStream(bytes), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.delete();
        if(useAltUrl) {
            return altURL + "/" + prefix + file.getName();
        }
        return bucket.getUrl(bucketName, prefix + file.getName()).toString();
    }
}
