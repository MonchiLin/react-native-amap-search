package com.kiki.react.amap.search.cloud

import android.util.Log
import com.amap.api.services.core.LatLonPoint

import com.facebook.react.bridge.*
import com.kiki.react.amap.search.utils.AMapParse
import com.amap.api.services.cloud.CloudSearch as AMapCloudSearch
import com.amap.api.services.cloud.CloudResult as AMapCloudResult
import com.amap.api.services.cloud.CloudItemDetail as AMapCloudItemDetail


val TAG = "kiki"

class CloudSearchModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), AMapCloudSearch.OnCloudSearchListener {

    internal var reactContext: ReactContext? = null
    internal var searchResultPromise: Promise? = null

    init {
        this.reactContext = reactContext
    }

    override fun getName(): String {
        return "AMapCloudSearch"
    }

    @ReactMethod
    fun CloudSearch(params: ReadableMap, promise: Promise) {
        searchResultPromise = promise

        if (!params.hasKey("tableId") || "".equals(params.getString("tableId"))) {
            promise.reject("tableId", "请传入正确的 table id")
            return
        }

        if (!params.hasKey("longitude")) {
            promise.reject("longitude", "请传入正确的 longitude")
            return
        }

        if (!params.hasKey("latitude")) {
            promise.reject("latitude", "请传入正确的 latitude")
            return
        }

        val radius = if (params.hasKey("radius")) {
            params.getInt("radius")
        } else {
            4000
        }

        val keyword = if (params.hasKey("keyword")) {
            params.getString("keyword")
        } else {
            ""
        }

        val tableId = params.getString("tableId")
        val latitude = params.getDouble("latitude")
        val longitude = params.getDouble("longitude")


        val latLonPoint = LatLonPoint(latitude, longitude)

        val mCloudSearch = AMapCloudSearch(reactContext)
        mCloudSearch.setOnCloudSearchListener(this)


        val bound = AMapCloudSearch.SearchBound(latLonPoint, radius)

        val mQuery = AMapCloudSearch.Query(tableId, keyword, bound);

        mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
    }

    override fun onCloudSearched(cloudResult: AMapCloudResult, i: Int) {
        Log.d(TAG, "cloudSear ->> res" + cloudResult)
        searchResultPromise?.resolve(toWriteArray(cloudResult))
    }

    override fun onCloudItemDetailSearched(cloudItemDetail: AMapCloudItemDetail, i: Int) {
        Log.d(TAG, "cloudSear ->> " + cloudItemDetail)
    }

    private fun toWriteArray(cloudResult: AMapCloudResult): WritableMap? {
        val map = Arguments.createMap()

        map.putMap("query", AMapParse.parseQuery(cloudResult.query))

        map.putMap("bound", AMapParse.parseSearchBound(cloudResult.bound))

        map.putInt("pageCount", cloudResult.pageCount)
        map.putInt("totalCount", cloudResult.totalCount)
        map.putArray("clouds", AMapParse.parseCloudItem(cloudResult.clouds))

        return map
    }


}
