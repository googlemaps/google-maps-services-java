package com.google.maps;

import com.google.maps.internal.ApiConfig;
import com.google.maps.model.StaticMapResult;

/**
 * Request for the Static Map API.
 */

public class StaticMapApiRequest extends PendingResultBase<StaticMapResult, StaticMapApiRequest, StaticMapApi.Response> {

    private static final ApiConfig API_CONFIG = new ApiConfig("/maps/api/staticmap");

    public StaticMapApiRequest(GeoApiContext context) {
        super(context, API_CONFIG, StaticMapApi.Response.class);
    }

    @Override
    protected void validateRequest() {
        if (!params().containsKey("size")) {
            throw new IllegalArgumentException("Request must contain 'size'");
        }
    }

    /**
     * Defines the rectangular dimensions of the map image. This parameter takes a string of the
     * form {horizontal_value}x{vertical_value}. For example, 500x400 defines a map 500 pixels wide
     * by 400 pixels high. Maps smaller than 180 pixels in width will display a reduced-size Google
     * logo. This parameter is affected by the scale parameter, described below; the final output
     * size is the product of the size and scale values.
     *
     * @param width  Width (in pixels) of the map image.
     * @param height Height (in pixels) of the map image.
     */
    public StaticMapApiRequest size(int width, int height) {
        if (width < 0) {
            throw new IllegalArgumentException("Width must be greater than 0.");
        }
        if (height < 0) {
            throw new IllegalArgumentException("Height must be greater than 0.");
        }
        return param("size", width + "x" + height);
    }

    public StaticMapApiRequest scale(int scale) {
        if (scale != 1 && scale != 2 && scale != 4) {
            throw new IllegalArgumentException("Accepted values are 1, 2, and 4.");
        }
        return param("scale", String.valueOf(scale));
    }
}
