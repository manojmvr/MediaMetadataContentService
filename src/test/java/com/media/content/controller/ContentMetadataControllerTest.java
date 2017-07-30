package com.media.content.controller;

import com.media.content.enums.Level;
import com.media.content.service.ContentMetadataService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Manoj Paramasivam
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ControllerTestConfiguration.class)
public class ContentMetadataControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ContentMetadataService metadataContentService;

    @InjectMocks
    private ContentMetadataController target;

    @Before
    public void setup() throws IOException {

        mockMvc = MockMvcBuilders
                .standaloneSetup(target)
                .build();

    }

    @Test
    public void shouldFailIfWrongFilterProvided() throws Exception {

        this.mockMvc.perform(
                get("/media?filter=censoringggg&level=censored")).andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldFailIfWrongLevelProvided() throws Exception {

        this.mockMvc.perform(
                get("/media?filter=censoring&level=random")).andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldGetMetadataContentWithCensoredLevel() throws Exception {

        when(metadataContentService.getMetadataContentByLevel(eq(Level.CENSORED))).thenReturn(new HashMap<>());

        MvcResult result = this.mockMvc.perform(get("/media?filter=censoring&level=censored"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("{}", result.getResponse().getContentAsString());
    }

    @Test
    public void shouldGetMetadataContentWithUnCensoredLevel() throws Exception {

        when(metadataContentService.getMetadataContentByLevel(eq(Level.UNCENSORED))).thenReturn(new HashMap<>());

        MvcResult result = this.mockMvc.perform(get("/media?filter=censoring&level=uncensored"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("{}", result.getResponse().getContentAsString());
    }
}
