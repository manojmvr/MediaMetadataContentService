package com.media.content.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.media.content.exceptions.MetadataContentException;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


/**
 * Utility class to extract the bitly url and download content from respective redirected google drive source.
 *
 * @author Manoj Paramasivam
 */
@UtilityClass
public class DriveUtil {

    private static final String METADATA_EXTERNAL_ADDRESS = "http://bit.ly/javaassignmentsrc";

    private static final String DRIVE_FILE_DOWNLOAD_ADDRESS = "https://drive.google.com/uc?export=download";

    /**
     * Util method to find the expanded url from the given shortened url
     *
     * @param link shortened or redirectable input url
     * @return expandedURL destination url
     */
    private static String expandUrl(String link) throws IOException {
        URL url = new URL(link);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
        httpURLConnection.setInstanceFollowRedirects(false);

        // extract location header containing the actual destination URL
        String expandedURL = httpURLConnection.getHeaderField("Location");
        httpURLConnection.disconnect();

        return expandedURL;
    }


    /**
     * Utility method to extract shortened url and find the source from the expanded url.
     *
     * @return response Metadata content response from external source.
     */
    public static String getMetadataContentFromExternalSource() {

        HttpResponse<JsonNode> response;
        try {
            String driveUrl = expandUrl(METADATA_EXTERNAL_ADDRESS);
            URI driveFileUri = new URI(driveUrl);
            String driveDownloadUrl = expandUrl(DRIVE_FILE_DOWNLOAD_ADDRESS
                    + "&" + driveFileUri.getQuery());
            response = Unirest.get(driveDownloadUrl).asJson();
        }
        catch (UnirestException | URISyntaxException | IOException e) {
            throw new MetadataContentException("Error in reading file from external source", e);
        }
        return response.getBody().getObject().toString();
    }
}
