package com.media.content;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.media.content.exceptions.MetadataContentException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Manoj Paramasivam
 */
public class TestUtil {

    public static Map<String, Object> getExpectedContentOutput(String fileName) {
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
