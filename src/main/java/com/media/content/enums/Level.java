package com.media.content.enums;

/**
 * @author Manoj Paramasivam
 */
public enum Level {

    CENSORED,
    UNCENSORED;


    @Override
    public String toString() {

        return this.name().toLowerCase();
    }

}
