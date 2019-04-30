package com.kiki.react.amap.search.cloud

import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.*
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener
import com.facebook.react.bridge.*
import com.kiki.react.amap.search.utils.AMapParse


class GeocodeModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    internal var reactContext: ReactContext? = null

    init {
        this.reactContext = reactContext
    }

    override fun getName(): String {
        return "AMapGeocode"
    }

    @ReactMethod
    fun AMapGeocodeSearch(options: ReadableMap, promise: Promise) {
        val geocoderSearch = GeocodeSearch(this.reactContext);

        val name = if (options.hasKey("name")) {
            options.getString("name")
        } else {
            promise.reject("参数错误", "请传入正确的 name")
            return
        }

        val code = if (options.hasKey("code")) {
            options.getString("code")
        } else {
            promise.reject("参数错误", "请传入正确的 code")
            return
        }

        val query = GeocodeQuery(name, code)

        geocoderSearch.setOnGeocodeSearchListener(object : OnGeocodeSearchListener {
            override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onGeocodeSearched(result: GeocodeResult, p1: Int) {
                promise.resolve(AMapParse.parserGeocodeResult(result.geocodeAddressList))
            }

        });

        geocoderSearch.getFromLocationNameAsyn(query)
    }


    @ReactMethod
    fun AMapRegeocodeSearch(options: ReadableMap, promise: Promise) {
        val geocoderSearch = GeocodeSearch(this.reactContext);

        val latLonPoint = if (options.hasKey("latLonPoint")) {
            options.getMap("latLonPoint")
        } else {
            promise.reject("参数错误", "请传入正确的 latLonPoint")
            return
        }

        val radius = if (options.hasKey("radius")) {
            options.getDouble("radius")
        } else {
            promise.reject("参数错误", "请传入正确的 radius")
            return
        }


        val coordinateSystem = if (options.hasKey("radius")) {
            options.getString("coordinateSystem")
        } else {
            GeocodeSearch.AMAP
        }

        val latlon = LatLonPoint(latLonPoint!!.getDouble("latitude"), latLonPoint.getDouble("longitude"))

        val query = RegeocodeQuery(latlon, radius.toFloat(), coordinateSystem)

        geocoderSearch.setOnGeocodeSearchListener(object : OnGeocodeSearchListener {
            override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onRegeocodeSearched(result: RegeocodeResult, p1: Int) {
                promise.resolve(AMapParse.parserRegeocodeAddress(result.regeocodeAddress))
            }

        });

        geocoderSearch.getFromLocationAsyn(query)
    }


}
