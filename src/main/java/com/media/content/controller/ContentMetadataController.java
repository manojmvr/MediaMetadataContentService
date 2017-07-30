package com.media.content.controller;

import com.media.content.enums.Filter;
import com.media.content.enums.FilterEnumConverter;
import com.media.content.enums.Level;
import com.media.content.enums.LevelEnumConverter;
import com.media.content.service.ContentMetadataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * The Metadata Content Rest API
 *
 * @author Manoj Paramasivam
 */
@RestController
@Api(value = "content content", description = "Retrieve content content")
public class ContentMetadataController {

    @Autowired
    private ContentMetadataService metadataContentService;

    @RequestMapping(value = "/media", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get content contents api", notes = "Gets the content content from external source and filters "
            + "content based on the given level.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filter", value = "The censoring filter",
                    required = true, paramType = "query"),
            @ApiImplicitParam(name = "level", value = "The level of classification of censored and uncensored movies",
                    required = true, paramType = "query"),
    })
    public @ResponseBody Map<String, Object> getMetadataContent(
            @RequestParam(value = "filter") Filter filter,
            @RequestParam(value = "level") Level level) {

        return metadataContentService.getMetadataContentByLevel(level);

    }


    /**
     * Binder for enum classes @{@link Filter} and @{@link Level} which converts the incoming to convert to
     * enum regardless of any case.
     */
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        dataBinder.registerCustomEditor(Filter.class, new FilterEnumConverter());
        dataBinder.registerCustomEditor(Level.class, new LevelEnumConverter());

    }
}
