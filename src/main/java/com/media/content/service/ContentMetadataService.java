package com.media.content.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.media.content.enums.Level;
import com.media.content.exceptions.MetadataContentException;
import com.media.content.utils.DriveUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    // Keys
    private static final String PEG$CONTENT_CLASSIFICATION = "peg$contentClassification";
    private static final String MEDIA = "media";
    private static final String GUID = "guid";
    private static final String ENTRIES = "entries";
    private static final String CENSORED_ENDS_WITH_CHAR = "C";
    private static final String UNCLASSIFIED = "";

    /***
     *  Gets the content content from the external source.
     *
     *  @param level The level filter
     *  @return responseMap The content retrieved from external source with level filter applied.
     */
    public Map<String, Object> getMetadataContentByLevel(Level level) {

        String metadataContent = DriveUtil.getMetadataContentFromExternalSource();

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
}
