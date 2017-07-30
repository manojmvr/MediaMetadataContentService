package com.media.content.enums;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Manoj Paramasivam
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class LevelTest {

    @Test
    public void shouldReturnLevelAsLowerCase() {

        assertEquals(Level.CENSORED.toString(), "censored");
        assertEquals(Level.UNCENSORED.toString(), "uncensored");
    }

}
