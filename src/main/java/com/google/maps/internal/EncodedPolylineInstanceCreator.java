package com.google.maps.internal;

import com.google.gson.InstanceCreator;
import com.google.maps.model.EncodedPolyline;

import java.lang.reflect.Type;

public class EncodedPolylineInstanceCreator implements InstanceCreator<EncodedPolyline> {
    private String points;

    public  EncodedPolylineInstanceCreator(String points)
    {
        this.points = points;
    }
    @Override
    public EncodedPolyline createInstance(Type type) {
        return new EncodedPolyline(points);
    }
}
