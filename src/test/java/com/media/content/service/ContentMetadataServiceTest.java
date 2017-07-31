package com.media.content.service;

import com.media.content.TestUtil;
import com.media.content.enums.Level;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

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

        Map<String, Object> expectedResult = TestUtil.getExpectedContentOutput("censored-content-op.json");
        assertContentMetadataResults(expectedResult, actualResult);
    }

    @Test
    public void shouldGetMetadataContentForUncensoredLevel() {
        Map<String, Object> actualResult =  service.getMetadataContentByLevel(Level.UNCENSORED);
        assertNotNull(actualResult);

        Map<String, Object> expectedResult = TestUtil.getExpectedContentOutput("uncensored-content-op.json");
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
}
