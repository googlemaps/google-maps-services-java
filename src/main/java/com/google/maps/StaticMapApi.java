package com.google.maps;

import com.google.gson.FieldNamingPolicy;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiConfig;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.StaticMapResult;

import java.util.logging.Logger;

/**
 * Created by bhalchandrawadekar on 24/03/2017.
 */

public class StaticMapApi {

    private static final String API_BASE_URL = "https://maps.googleapis.com";
    private static final Logger LOG = Logger.getLogger(StaticMapApi.class.getName());

    static final ApiConfig STATIC_MAP_API_CONFIG = new ApiConfig("/maps/api/staticmap")
            .hostName(API_BASE_URL)
            .supportsClientId(false)
            .fieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .requestVerb("GET");

    public static StaticMapApiRequest staticMap(GeoApiContext context, int width, int height) {
        StaticMapApiRequest request = new StaticMapApiRequest(context);
        request.size(width, height);
        return request;
    }

    public static class Response implements ApiResponse<StaticMapResult> {

        public int code = 200;
        public String message = "OK";
        public byte[] imageData;
        public String reason = null;

        @Override
        public boolean successful() {
            return code == 200;
        }

        @Override
        public StaticMapResult getResult() {
            StaticMapResult result = new StaticMapResult();
            result.imageData = imageData;
            return result;
        }

        @Override
        public ApiException getError() {
            if (successful())
                return null;
            return ApiException.from(message, reason);
        }
    }
}
