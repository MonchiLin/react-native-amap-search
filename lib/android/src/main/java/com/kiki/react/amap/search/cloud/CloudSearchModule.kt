package com.kiki.react.amap.search.cloud

import android.util.Log
import com.amap.api.services.cloud.CloudItemDetail
import com.amap.api.services.cloud.CloudResult
import com.amap.api.services.cloud.CloudSearch
import com.amap.api.services.cloud.CloudSearch.*
import com.amap.api.services.core.LatLonPoint
import com.facebook.react.bridge.*
import com.kiki.react.amap.search.utils.AMapParse

val TAG = "kiki"

class CloudSearchModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), OnCloudSearchListener {

    internal var reactContext: ReactContext? = null
    internal var searchResultPromise: Promise? = null

    init {
        this.reactContext = reactContext
    }

    override fun getName(): String {
        return "ACloudSearch"
    }

    @ReactMethod
    fun ACloudSearch(params: ReadableMap, promise: Promise) {
        searchResultPromise = promise

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


        val tableId = if (params.hasKey("tableId")) {
            params.getString("tableId")
        } else {
            ""
        }

        val mCloudSearch = CloudSearch(reactContext)
        mCloudSearch.setOnCloudSearchListener(this)

        val latLonPoint = LatLonPoint(params.getDouble("latitude"), params.getDouble("longitude"))
        val bound = SearchBound(latLonPoint, radius)

        val mQuery = Query(tableId, keyword, bound);

        mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
    }

    override fun onCloudSearched(cloudResult: CloudResult, i: Int) {
        Log.d(TAG, "cloudSear ->> res" + cloudResult)
        searchResultPromise?.resolve(toWriteArray(cloudResult))
    }

    override fun onCloudItemDetailSearched(cloudItemDetail: CloudItemDetail, i: Int) {
        Log.d(TAG, "cloudSear ->> " + cloudItemDetail)
    }

    private fun toWriteArray(cloudResult: CloudResult): WritableMap? {
        val map = Arguments.createMap()

        map.putMap("query", AMapParse.parseQuery(cloudResult.query))

        map.putMap("bound", AMapParse.parseSearchBound(cloudResult.bound))

        map.putInt("pageCount", cloudResult.pageCount)
        map.putInt("totalCount", cloudResult.totalCount)
        map.putArray("clouds", AMapParse.parseCloudItem(cloudResult.clouds))

        return map
    }


}
