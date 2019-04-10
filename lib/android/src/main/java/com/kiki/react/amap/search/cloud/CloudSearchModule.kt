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
    fun AMapCloudSearch(options: ReadableMap, promise: Promise) {

        if (options.getString("tableId") == null || "".equals(options.getString("tableId"))) {
            promise.reject("参数错误", "请传入正确的 tableId")
            return
        }

        if (!options.hasKey("searchBoundParams")) {
            promise.reject("参数错误", "请传入 searchBoundParams")
            return
        }

        if (!options.hasKey("searchBoundType")) {
            promise.reject("参数错误", "请传入 searchBoundType")
            return
        }

        val keyword = if (options.hasKey("keyword")) {
            options.getString("keyword")
        } else {
            ""
        }

        val radius = if (options.hasKey("radius")) {
            options.getInt("radius")
        } else {
            1000
        }
        val tableId = options.getString("tableId")
        val searchBoundType = options.getString("searchBoundType")

        val bound: AMapCloudSearch.SearchBound = if ("Bound".equals(searchBoundType)) {

            val bound = options.getMap("searchBoundParams")

            val latitude = bound.getDouble("latitude")
            val longitude = bound.getDouble("longitude")

            val latLonPoint = LatLonPoint(latitude, longitude)

            AMapCloudSearch.SearchBound(latLonPoint, radius)

        } else if ("Rectangle".equals(searchBoundType)) {

            val bound = options.getArray("searchBoundParams")

            val point1 = bound.getMap(1)
            val point2 = bound.getMap(2)

            val latlon1 = LatLonPoint(point1.getDouble("latitude"), point1.getDouble("longitude"))
            val latlon2 = LatLonPoint(point2.getDouble("latitude"), point2.getDouble("longitude"))

            AMapCloudSearch.SearchBound(latlon1, latlon2)

        } else if ("Polygon".equals(searchBoundType)) {
            val bound = options.getArray("searchBoundParams")


            val latlons = (0..bound.size() - 1).map {
                val point = bound.getMap(it)
                val latitude = point.getDouble("latitude")
                val longitude = point.getDouble("longitude")

                LatLonPoint(latitude, longitude)
            }

            AMapCloudSearch.SearchBound(latlons)
        } else if ("Local".equals(searchBoundType)) {

            val city = options.getString("searchBoundParams")
            AMapCloudSearch.SearchBound(city)
        } else {
            promise.reject("参数错误", "请传入正确的 searchBoundType, " +
                    "searchBoundType 只能为: Bound Polygon Rectangle Local 之一")
            return
        }

        val mCloudSearch = AMapCloudSearch(reactContext)

        val mQuery = AMapCloudSearch.Query(tableId, keyword, bound);

        mCloudSearch.setOnCloudSearchListener(object : AMapCloudSearch.OnCloudSearchListener {
            override fun onCloudItemDetailSearched(cloudItemDetail: AMapCloudItemDetail?, count: Int) {
                Log.d(TAG, "onCloudItemDetailSearched ->> " + cloudItemDetail)
            }

            override fun onCloudSearched(cloudResult: AMapCloudResult, count: Int) {
                Log.d(TAG, "onCloudSearched ->> " + cloudResult)
                promise.resolve(toWriteArray(cloudResult))
            }
        })

        mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
    }

    private fun toWriteArray(cloudResult: AMapCloudResult): WritableMap? {
        val map = Arguments.createMap()

        map.putMap("query", AMapParse.parseQuery(cloudResult.query))

        map.putMap("bound", AMapParse.parserSearchBound(cloudResult.bound))

        map.putInt("pageCount", cloudResult.pageCount)
        map.putInt("totalCount", cloudResult.totalCount)
        map.putArray("clouds", AMapParse.parserCloudItem(cloudResult.clouds))

        return map
    }

}
