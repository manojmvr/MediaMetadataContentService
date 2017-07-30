package com.media.content.enums;

import java.beans.PropertyEditorSupport;


/**
 * @author Manoj Paramasivam
 */
public class FilterEnumConverter extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {

        setValue(Filter.valueOf(text.toUpperCase()));

    }

}
