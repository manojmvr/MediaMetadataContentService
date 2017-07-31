package com.media.content.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.media.content.TestUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Map;

/**
 * @author Manoj Paramasivam
 */
@RunWith(MockitoJUnitRunner.class)
public class DriveUtilTest {

    private Map<String, Object> getMap(String content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(content, new TypeReference<Map<String, Object>>(){});
    }

    @Test
    public void shouldGetContentFromExternalSource() throws IOException {
        String actualResponse = DriveUtil.getMetadataContentFromExternalSource();
        Map<String, Object> actualResult = getMap(actualResponse);
        Map<String, Object> expectedResult = TestUtil.getExpectedContentOutput("content.json");
        Assert.assertEquals(actualResult.size(), expectedResult.size());
    }
}
