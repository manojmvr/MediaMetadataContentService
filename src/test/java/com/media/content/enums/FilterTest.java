package com.media.content.enums;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Manoj Paramasivam
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class FilterTest {

    @Test
    public void shouldReturnFilterAsLowercase() {

        Assert.assertEquals(Filter.CENSORING.toString(), "censoring");
    }
}
