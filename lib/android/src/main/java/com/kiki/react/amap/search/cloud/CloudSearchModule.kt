package com.kiki.react.amap.search.cloud

import android.util.Log
import com.amap.api.services.core.LatLonPoint
import com.facebook.react.bridge.*
import com.kiki.react.amap.search.utils.AMapParse
import com.amap.api.services.cloud.CloudItemDetail as AMapCloudItemDetail
import com.amap.api.services.cloud.CloudResult as AMapCloudResult
import com.amap.api.services.cloud.CloudSearch as AMapCloudSearch


val TAG = "kiki"

class CloudSearchModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    internal var reactContext: ReactContext? = null

    init {
        this.reactContext = reactContext

    }

    override fun getName(): String {
        return "AMapCloudSearch"
    }

    @ReactMethod
    fun AMapCloudSearch(params: ReadableMap, promise: Promise) {

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

        val bound = AMapCloudSearch.SearchBound(latLonPoint, radius)

        val mQuery = AMapCloudSearch.Query(tableId, keyword, bound);

        mCloudSearch.setOnCloudSearchListener(object : AMapCloudSearch.OnCloudSearchListener {
            override fun onCloudItemDetailSearched(cloudItemDetail: AMapCloudItemDetail?, count: Int) {
                Log.d(TAG, "test1 ==> $count")
                Log.d(TAG, "cloudSear ->> " + cloudItemDetail)
            }

            override fun onCloudSearched(cloudResult: AMapCloudResult, count: Int) {
                Log.d(TAG, "cloudSear ->> res" + cloudResult)
                promise.resolve(toWriteArray(cloudResult))
            }

        })

        mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
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
