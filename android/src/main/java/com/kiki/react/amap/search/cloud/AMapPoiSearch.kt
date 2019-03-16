package com.rnplayground.cloud

import android.util.Log
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener
import com.amap.api.services.poisearch.SubPoiItem
import com.facebook.react.bridge.*


class AMapPoiSearch(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), OnPoiSearchListener {
    internal var reactContext: ReactContext? = null
    internal var promise: Promise? = null

    override fun onPoiItemSearched(p0: PoiItem?, code: Int) {
        Log.d("kiki", "kiki ->> PoiItem")
    }

    override fun onPoiSearched(result: PoiResult?, p1: Int) {
        Log.d("kiki", "kiki ->> res " + result)

        if (result == null) {
            this.promise?.resolve(null)
        } else {
            this.promise?.resolve(toWriteArray(result))
        }

    }

    init {
        this.reactContext = reactContext
    }

    override fun getName(): String {
        return "AMapPoiSearch"
    }

    @ReactMethod
    fun AMapPoiSearch(params: ReadableMap, promise: Promise) {
        this.promise = promise

        val keyword = try {
            params.getString("keyword")
        } catch (e: NoSuchKeyException) {
            ""
        }

        val query = PoiSearch.Query(keyword, "", "362636")
        val poiSearch = PoiSearch(reactContext, query)
        poiSearch.setOnPoiSearchListener(this)
        poiSearch.searchPOIAsyn();
    }

    private fun toWriteArray(result: PoiResult): WritableArray? {
        val pois = result.pois
        val arr = Arguments.createArray()

        pois.forEach { poi ->
            val map = Arguments.createMap()
            map.putString("adCode", poi.adCode)

            map.putString("adName", poi.adName)

            map.putString("businessArea", poi.businessArea)

            map.putString("cityCode", poi.cityCode)

            map.putString("cityName", poi.cityName)

            map.putString("direction", poi.direction)

            map.putString("email", poi.email)

            map.putString("parkingType", poi.parkingType)

            map.putString("poiId", poi.poiId)

            map.putString("postcode", poi.postcode)

            map.putString("provinceCode", poi.provinceCode)

            map.putString("provinceName", poi.provinceName)

            map.putString("shopID", poi.shopID)

            map.putString("snippet", poi.snippet)

            map.putString("tel", poi.tel)

            map.putString("title", poi.title)

            map.putString("typeCode", poi.typeCode)

            map.putString("typeDes", poi.typeDes)

            map.putString("website", poi.website)

            map.putInt("distance", poi.distance)

            val latLonPoint = Arguments.createMap()
            latLonPoint.putDouble("latitude", poi.latLonPoint.latitude)
            latLonPoint.putDouble("longitude", poi.latLonPoint.longitude)
            map.putMap("latLonPoint", latLonPoint)

            map.putString("photos", poi.photos.toString())

            map.putArray("subPois", subPoiSearchResultToWritableArray(poi.subPois))

            arr.pushMap(map)
        }

        return arr
    }

    private fun subPoiSearchResultToWritableArray(subPoiItems: MutableList<SubPoiItem>): WritableArray? {
        val arr = Arguments.createArray()

        subPoiItems.forEach { it ->
            val map = Arguments.createMap()
            map.putInt("distance", it.distance)
            map.putString("poiId", it.poiId)
            map.putString("snippet", it.snippet)
            map.putString("subName", it.subName)
            map.putString("subTypeDes", it.subTypeDes)
            map.putString("title", it.title)

            val latLonPoint = Arguments.createMap()
            latLonPoint.putDouble("latitude", it.latLonPoint.latitude)
            latLonPoint.putDouble("longitude", it.latLonPoint.longitude)
            map.putMap("latLonPoint", latLonPoint)

            arr.pushMap(map)
        }

        return arr
    }

}
