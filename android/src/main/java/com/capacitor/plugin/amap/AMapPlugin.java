package com.capacitor.plugin.amap;

import android.Manifest;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(name = "AMap", permissions = {
        @Permission(alias = "capacitorAMapLocation", strings = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET
        })
})
public class AMapPlugin extends Plugin {
    private static final String TAG = "CapacitorAMap";
    private AMapLocationClient locationClient;
    private CapacitorAMapLocationListener locationListener;
    public boolean isInLocation = false;
    private boolean isInited = false;


    @Override
    public void load() {
        super.load();

    }

    @PermissionCallback
    public void LocationPermissionCallback(PluginCall call) {
        if (getPermissionState("capacitorAMapLocation") != PermissionState.GRANTED) {
            call.reject("用户拒绝授予权限");
        } else {
            this.locateWhenPermitted(call);
        }
    }

    @PluginMethod
    public void init(PluginCall call) {
        if (this.isInited) {
            return;
        }
        try {
            String appKey = getConfig().getString("androidKey", "");
            AMapLocationClient.setApiKey(appKey);
            AMapLocationClient.updatePrivacyShow(getContext(), true, true);
            AMapLocationClient.updatePrivacyAgree(getContext(), true);
            this.locationListener = new CapacitorAMapLocationListener(this);
            locationClient = new AMapLocationClient(getContext().getApplicationContext());
            locationClient.setLocationListener(this.locationListener);
            this.isInited = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PluginMethod
    public void locate(PluginCall call) {
        if (getPermissionState("capacitorAMapLocation") != PermissionState.GRANTED) {
            requestPermissionForAlias("capacitorAMapLocation", call, "LocationPermissionCallback");
        } else {
            this.locateWhenPermitted(call);
        }

    }

    private void locateWhenPermitted(PluginCall call) {
        this.locationListener.addPluginCall(call);
        if (!this.isInLocation) {
            this.isInLocation = true;
            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            option.setNeedAddress(true);
            option.setOnceLocation(true);
            locationClient.setLocationOption(option);
            locationClient.startLocation();
        }
    }

    @PluginMethod
    public void weather(PluginCall call) {
        if (this.locationListener.isInWeather()) {
            call.reject("已有正在执行中的天气查询任务");
            return;
        }
        try {
            WeatherSearchQuery weatherSearchQuery = new WeatherSearchQuery(call.getString("adCode"), WeatherSearchQuery.WEATHER_TYPE_LIVE);
            WeatherSearch weatherSearch = new WeatherSearch(getContext());
            weatherSearch.setOnWeatherSearchListener(this.locationListener);
            this.locationListener.setPluginCall(call);
            weatherSearch.setQuery(weatherSearchQuery);
            weatherSearch.searchWeatherAsyn();
        } catch (Exception e) {
            e.printStackTrace();
            call.reject(e.getMessage());
        }
    }

    @PluginMethod
    public void calculate(PluginCall call) {
        try {
            DPoint startPoint = new DPoint(call.getDouble("startLatitude"), call.getDouble("startLongitude"));
            DPoint endPoint = new DPoint(call.getDouble("endLatitude"), call.getDouble("endLongitude"));
            float v = CoordinateConverter.calculateLineDistance(startPoint, endPoint);
            JSObject jsObject = new JSObject();
            jsObject.put("distance", v);
            call.resolve(jsObject);
        } catch (Exception e) {
            e.printStackTrace();
            call.reject(e.getMessage());
        }
    }

    @PluginMethod
    public void doStopLocation(PluginCall call) {
        stopLocation();
        call.resolve();
    }

    public void stopLocation() {
        this.locationClient.stopLocation();
        this.locationListener.clearLocationHistory();
        this.isInLocation = false;

    }
}
