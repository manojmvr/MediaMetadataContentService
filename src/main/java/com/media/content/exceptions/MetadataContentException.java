package com.media.content.exceptions;

/**
 * @author Manoj Paramasivam
 */
public class MetadataContentException extends RuntimeException {

    public MetadataContentException(String msg) {

        super(msg);
    }


    public MetadataContentException(String msg, Throwable throwable) {

        super(msg, throwable);
    }


    public MetadataContentException(Throwable throwable) {

        super(throwable);
    }

}
