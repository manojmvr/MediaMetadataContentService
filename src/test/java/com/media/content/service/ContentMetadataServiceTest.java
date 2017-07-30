package com.media.content.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.media.content.enums.Level;
import com.media.content.exceptions.MetadataContentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Manoj Paramasivam
 */
@RunWith(MockitoJUnitRunner.class)
public class ContentMetadataServiceTest {

    @InjectMocks
    private ContentMetadataService service;

    @Test
    public void shouldGetMetadataContentForCensoredLevel() {
        Map<String, Object> actualResult =  service.getMetadataContentByLevel(Level.CENSORED);
        assertNotNull(actualResult);

        Map<String, Object> expectedResult = getExpectedContentOutput("censored-content-op.json");
        assertContentMetadataResults(expectedResult, actualResult);
    }

    @Test
    public void shouldGetMetadataContentForUncensoredLevel() {
        Map<String, Object> actualResult =  service.getMetadataContentByLevel(Level.UNCENSORED);
        assertNotNull(actualResult);

        Map<String, Object> expectedResult = getExpectedContentOutput("uncensored-content-op.json");
        assertContentMetadataResults(expectedResult, actualResult);
    }

    private void assertContentMetadataResults(Map<String, Object> expectedResult, Map<String, Object> actualResult) {
        assertEquals(expectedResult.size(), actualResult.size());

        List<Map<String, Object>> expectedEntries = (List<Map<String, Object>>) expectedResult.get("entries");
        List<Map<String, Object>> actualEntries = (List<Map<String, Object>>) actualResult.get("entries");
        assertEquals(expectedEntries.size(), actualEntries.size());


        List<Map<String, Object>> expectedMedia = (List<Map<String, Object>>) expectedEntries.get(0).get("media");
        List<Map<String, Object>> actualMedia = (List<Map<String, Object>>) expectedEntries.get(0).get("media");
        assertEquals(expectedMedia.size(), actualMedia.size());
        assertEquals(expectedMedia.get(0), actualMedia.get(0));
    }

    private Map<String, Object> getExpectedContentOutput(String fileName) {
        Resource resource = new ClassPathResource(fileName);
        InputStream resourceInputStream;

        try {
            resourceInputStream = resource.getInputStream();
        } catch (IOException e) {
            throw new MetadataContentException(e);
        }

        StringBuilder result = new StringBuilder("");
        Scanner scanner = new Scanner(resourceInputStream);

        while (scanner.hasNextLine()) {
            result.append(scanner.nextLine()).append("\n");
        }

        scanner.close();

        try {
            return new ObjectMapper().readValue(result.toString(), new TypeReference<Map<String, Object>>(){});
        } catch (IOException e) {
            throw new MetadataContentException(e);
        }
    }
}
