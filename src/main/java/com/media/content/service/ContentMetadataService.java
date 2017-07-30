package com.media.content.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.media.content.enums.Level;
import com.media.content.exceptions.MetadataContentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.media.content.enums.Level.CENSORED;
import static com.media.content.enums.Level.UNCENSORED;


/**
 * This is a service class which takes care of retrieving content content from the external source and helps to filter
 * contents based on levels.
 *
 * @author Manoj Paramasivam
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
@Slf4j
public class ContentMetadataService {

    @Value("${metadata.external.address}")
    private String metadataExternalAddress;

    // Keys
    private static final String PEG$CONTENT_CLASSIFICATION = "peg$contentClassification";
    private static final String MEDIA = "media";
    private static final String GUID = "guid";
    private static final String ENTRIES = "entries";
    private static final String CENSORED_ENDS_WITH_CHAR = "C";
    private static final String UNCLASSIFIED = "";


    //    public String expandUrl() throws IOException {
    //        URL url = new URL(metadataExternalAddress);
    //        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
    //        httpURLConnection.setInstanceFollowRedirects(false);
    //
    //        // extract location header containing the actual destination URL
    //        String expandedURL = httpURLConnection.getHeaderField("Location");
    //        httpURLConnection.disconnect();
    //
    //        return expandedURL;
    //    }

    //    public JSONObject getMetadataContentByLevel() throws UnirestException, IOException {
    //        HttpResponse<JsonNode> jsonObject = Unirest.get(expandUrl()).asJson();
    //        return jsonObject.getBody().getObject();
    //    }

    /***
     *  Gets the content content from the external source.
     *
     *  @param level The level filter
     *  @return responseMap The content retrieved from external source with level filter applied.
     */
    public Map<String, Object> getMetadataContentByLevel(Level level) {

        String metadataContent = readMetadataContentFromExternalSource();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap;
        try {
            responseMap = mapper.readValue(metadataContent, new TypeReference<Map<String, Object>>(){});
            applyLevelFilter(responseMap, level);
        }
        catch (IOException e) {
            final String errorMessage = "Error in Json content retrieved from External source";
            log.error(errorMessage, e);
            throw new MetadataContentException(errorMessage, e);
        }

        return responseMap;
    }

    /**
     * A helper method which helps to filter the content content by level.
     */
    private void applyLevelFilter(Map<String, Object> responseMap, Level requestedLevel) {

        List<Map<String, Object>> entries = (List<Map<String, Object>>) responseMap.get(ENTRIES);
        List<Map<String, Object>> filteredEntries = new ArrayList<>();

        entries.parallelStream().forEach(entry -> {
            final String censorLevel = ((String) entry.get(PEG$CONTENT_CLASSIFICATION));

            List<Map<String, Object>> mediaArray = (List<Map<String, Object>>) entry.get(MEDIA);
            List<Map<String, Object>> filteredMediaArray = new ArrayList<>();

            if (requestedLevel.equals(CENSORED)) {
                if (censorLevel.equalsIgnoreCase(CENSORED.toString())) {

                    mediaArray.parallelStream().forEach(media -> {
                        if (((String) media.get(GUID)).endsWith(CENSORED_ENDS_WITH_CHAR)) {
                            filteredMediaArray.add(media);
                        }
                    });

                } else if (censorLevel.equals(UNCLASSIFIED)) {

                    filteredEntries.add(entry);

                }

            } else if (requestedLevel.equals(UNCENSORED)) {
                if (censorLevel.equalsIgnoreCase(CENSORED.toString())) {

                    mediaArray.parallelStream().forEach(media -> {
                        if (!((String) media.get(GUID)).endsWith(CENSORED_ENDS_WITH_CHAR)) {
                            filteredMediaArray.add(media);
                        }
                    });

                } else if (censorLevel.equalsIgnoreCase(UNCENSORED.toString()) || censorLevel.equals(UNCLASSIFIED)) {

                    filteredEntries.add(entry);

                }
            }

            if (filteredMediaArray.size() > 0) {
                entry.put(MEDIA, filteredMediaArray);
                filteredEntries.add(entry);
            }

        });

        responseMap.put(ENTRIES, filteredEntries);
    }

    private String readMetadataContentFromExternalSource() {
        Resource resource = new ClassPathResource("content.json");
        InputStream resourceInputStream = null;
        try {
            resourceInputStream = resource.getInputStream();
        }
        catch (IOException e) {
            log.error("Error in reading input stream from content metadata", e);
            throw new MetadataContentException(e);
        }

        StringBuilder result = new StringBuilder("");
        Scanner scanner = new Scanner(resourceInputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            result.append(line).append("\n");
        }

        scanner.close();

        return result.toString();
    }
}
