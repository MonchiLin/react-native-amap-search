package com.kiki.react.amap.search.cloud

import android.content.ContentValues.TAG
import android.util.Log
import com.amap.api.services.cloud.CloudItemDetail
import com.amap.api.services.cloud.CloudResult
import com.amap.api.services.cloud.CloudSearch
import com.amap.api.services.cloud.CloudSearch.*
import com.amap.api.services.core.LatLonPoint
import com.facebook.react.bridge.*
import com.kiki.react.amap.search.utils.AMapParse

class AMapCloudSearch(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), OnCloudSearchListener {

    internal var reactContext: ReactContext? = null
    internal var searchResultPromise: Promise? = null

    init {
        this.reactContext = reactContext
    }

    override fun getName(): String {
        return "AMapCloudSearch"
    }

    @ReactMethod
    fun AMapCloudSearch(params: ReadableMap, promise: Promise) {
        searchResultPromise = promise

        val radius = try {
            params.getInt("radius")
        } catch (e: NoSuchKeyException) {
            4000
        }

        val keyword = try {
            params.getString("keyword")
        } catch (e: NoSuchKeyException) {
            ""
        }

        val tableId = try {
            params.getString("tableId")
        } catch (e: NoSuchKeyException) {
            return
        }

        val mCloudSearch = CloudSearch(reactContext)
        mCloudSearch.setOnCloudSearchListener(this)

        val latLonPoint = LatLonPoint(params.getDouble("latitude"), params.getDouble("longitude"))
        val bound = SearchBound(latLonPoint, radius)

        val mQuery = Query(tableId, keyword, bound);

        mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
    }

    @ReactMethod
    override fun onCloudSearched(cloudResult: CloudResult, i: Int) {
        Log.d(TAG, "cloudSear ->> res" + cloudResult)
        val ss = toWriteArray(cloudResult)
        searchResultPromise?.resolve(ss)
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
