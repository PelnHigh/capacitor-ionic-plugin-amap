package com.capacitor.plugin.amap;

import com.getcapacitor.JSObject;

public class CapacitorAmapLocation {
    public float getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(float accuracy) {
        Accuracy = accuracy;
    }

    public JSObject getJsObject() {
        return JsObject;
    }

    public void setJsObject(JSObject jsObject) {
        JsObject = jsObject;
    }

    private float Accuracy;
    private JSObject JsObject;
}