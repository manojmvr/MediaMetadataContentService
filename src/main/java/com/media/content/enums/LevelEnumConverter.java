package com.media.content.enums;

import java.beans.PropertyEditorSupport;


/**
 * @author Manoj Paramasivam
 */
public class LevelEnumConverter extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {

        setValue(Level.valueOf(text.toUpperCase()));

    }

}
