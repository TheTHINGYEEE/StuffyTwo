package com.github.thethingyee.StuffyTwo.handlers.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.github.thethingyee.StuffyTwo.handlers.ConfigurationHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
    Huge thanks to Kody Simpson's video for this.
    https://youtu.be/wRt2iKERUws
 */
public class AWSHandler {

    private final AmazonS3 bucket;
    private final String bucketName = ConfigurationHandler.getInstance().getProperty("aws.bucketName");
    private final String prefix = ConfigurationHandler.getInstance().getProperty("aws.bucketPrefix") + "//";

    /**
     * Initialize the AWS bucket to get its info.
     */
    public AWSHandler() {
        ConfigurationHandler shortConfigHandler = ConfigurationHandler.getInstance();
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(shortConfigHandler.getProperty("aws.accessKey"), shortConfigHandler.
                        getProperty("aws.secretKey"))
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

        boolean useBackslash = ConfigurationHandler.getInstance().useBackslashes();

        File folder = new File((useBackslash ? "temp" : "temp"));
        folder.mkdirs();

        File f = new File((useBackslash ? "temp\\" : "temp//") + generated + ".json");
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
     * @return The URL for the uploaded resource and the json (string) contents of the file.
     */
    public String[] uploadFile(File file) throws IOException {

        String altURL = ConfigurationHandler.getInstance().getProperty("aws.embed.alternateURL");
        boolean useAltURL = ConfigurationHandler.getInstance().useAlternateURL();

        // array 0 for url of uploaded file
        // array 1 for string contents of json file
        String[] response = new String[2];

        if(altURL != null) useAltURL = true;

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
        response[1] = new String(Files.readAllBytes(Paths.get(file.toURI())));

        file.delete();

        response[0] = (useAltURL ? altURL + "/" + prefix + file.getName() :
                bucket.getUrl(bucketName, prefix + file.getName()).toString());

        return response;
    }

    public boolean bucketIsNull() {
        return bucket == null;
    }
}
