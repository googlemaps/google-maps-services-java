package com.google.maps.model;

/**
 * StaticMapResult contains the photo for a PhotoReference.
 * <p>
 * <p>Please see <a href="https://developers.google.com/places/web-service/photos">Photos</a> for
 * more details.</p>
 */
public class StaticMapResult {

    /**
     * imageData is the byte array of returned image data from the Static Map API call.
     */
    public byte[] imageData;

    /**
     * contentType is the Content-Type header of the returned result.
     */
    public String contentType;
}
