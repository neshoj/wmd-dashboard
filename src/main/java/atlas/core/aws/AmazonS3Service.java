package atlas.core.aws;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import atlas.core.mail.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

/** 
 * @category    AWS
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 * 
 */
@Component
public class AmazonS3Service {
    private final String accessKey;
    private final String secretKey;
    private final String bucketName;
    private final String s3BaseUrl;
    private AmazonS3 s3Client;

    private static final Logger logger = LoggerFactory.getLogger(AmazonS3Service.class);

    @Autowired
    public AmazonS3Service(@Value("${aws.access-key}") String accessKey,
                           @Value("${aws.secret-key}") String secretKey,
                           @Value("${aws.bucket}") String bucketName,
                           @Value("${aws.s3url}") String s3BaseUrl) {

        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
        this.s3BaseUrl = s3BaseUrl;

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion( Regions.US_EAST_1 )
                .withCredentials( new AWSStaticCredentialsProvider( awsCredentials ))
                .build();
    }

    public String uploadMultipart(MultipartFile file, String fileName) throws IOException {
        String fileOriginalName = file.getOriginalFilename();
        String directory = System.getProperty("java.io.tmpdir");
        String filepath = Paths.get(directory, fileOriginalName).toString();
        fileName = String.format("%s.png", fileName);

        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)))) {
            stream.write( file.getBytes());
        }

        /*Upload file*/
        File uploadedFile = new File(filepath);
        return  uploadFile( uploadedFile, fileName);
    }

    /**
     * When uploading a Base64 encoded image
     *
     * @param base64EncodedImage
     * @param fileName
     * @return String URL pointing to the uploaded file
     * @throws IOException
     */
    public String uploadMultipart(String base64EncodedImage, String fileName) throws IOException {
        byte[] decodedBytes = Base64.decode( base64EncodedImage );
        File file = new File( fileName );
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream( file ))) {
            stream.write( decodedBytes );
        }
        /*Upload file*/
        fileName = String.format("%s.png", fileName);
        return  uploadFile(file, fileName);
    }


    public static void main(String[] args) {

    }

    /**
     * Upload a file to a bucket
     *
     * @param file
     * @param fileName
     * @return String URL pointing to the uploaded file
     */
    public String uploadFile(File file, String fileName) {
        try {
            //Upload file
            if (s3Client.doesBucketExistV2( bucketName)) {
                s3Client.putObject(new PutObjectRequest(bucketName, fileName, file)
                        .withCannedAcl( CannedAccessControlList.PublicRead )
                );
            }
        } catch (AmazonServiceException e) {
            logger.error("File Request made it to Amazon Web Service but returned with an error:");
            logger.error("==================================================================");
            logger.error("Error Message:    " + e.getMessage());
            logger.error("HTTP Status Code: " + e.getStatusCode());
            logger.error("AWS Error Code:   " + e.getErrorCode());
            logger.error("Error Type:       " + e.getErrorType());
            logger.error("MailRequest ID:       " + e.getRequestId());
            logger.error("==================================================================");

        } catch (AmazonClientException ace) {
            logger.error("The client encountered an internal server error while performing this request:");
            logger.error("==================================================================");
            logger.error("Error Message: " + ace.getMessage());
            logger.error("==================================================================");
        }

        //Generate file url
        if( !org.springframework.util.StringUtils.isEmpty( fileName )){
            UriComponents uriComponents = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host( s3BaseUrl )
                    .path("/"+bucketName+"/"+fileName)
                    .build()
                    .encode();
            return uriComponents.toString();
        }
        else return "";
    }

    public boolean deleteFile(String filename){
        boolean deleted = false;
        try {
            //Delete file
            if (s3Client.doesBucketExistV2( bucketName )) {
                boolean exists = s3Client.doesObjectExist(bucketName, filename);
                if( exists ) s3Client.deleteObject( new DeleteObjectRequest(bucketName, filename));

                deleted = true;
            }
        } catch (AmazonServiceException ase) {
            logger.error("File Request made it to Amazon Web Service but returned with an error:");
            logger.error("==================================================================");
            logger.error("Error Message:    " + ase.getMessage());
            logger.error("HTTP Status Code: " + ase.getStatusCode());
            logger.error("AWS Error Code:   " + ase.getErrorCode());
            logger.error("Error Type:       " + ase.getErrorType());
            logger.error("MailRequest ID:       " + ase.getRequestId());
            logger.error("==================================================================");

        } catch (AmazonClientException ace) {
            logger.error("The client encountered an internal server error while performing this request:");
            logger.error("==================================================================");
            logger.error("Error Message: " + ace.getMessage());
            logger.error("==================================================================");
        }
        return deleted;
    }


}
