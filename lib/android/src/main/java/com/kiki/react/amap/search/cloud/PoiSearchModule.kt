package com.kiki.react.amap.search.cloud

import android.util.Log
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener
import com.amap.api.services.poisearch.SubPoiItem
import com.facebook.react.bridge.*

class PoiSearchModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    internal var reactContext: ReactContext? = null

    init {
        this.reactContext = reactContext
    }

    override fun getName(): String {
        return "AMapPoiSearch"
    }

    @ReactMethod
    fun AMapPoiSearchId(id: String, promise: Promise) {
        val poiSearch = PoiSearch(this.reactContext, null)

        poiSearch.setOnPoiSearchListener(object : OnPoiSearchListener {
            override fun onPoiItemSearched(res: PoiItem?, p1: Int) {
                Log.d(TAG, "AMapPoiSearchId onPoiItemSearched => $res")
            }

            override fun onPoiSearched(res: PoiResult?, p1: Int) {
                Log.d(TAG, "AMapPoiSearchId onPoiSearched => $res")
            }

        })

        poiSearch.searchPOIId(id)
    }

    @ReactMethod
    fun AMapPoiSearch(params: ReadableMap, promise: Promise) {

        val keyword = if (params.hasKey("keyword")) {
            params.getString("keyword")
        } else {
            ""
        }

        val ctgr = if (params.hasKey("ctgr")) {
            params.getString("ctgr")
        } else {
            ""
        }

        val cityCode = if (params.hasKey("cityCode")) {
            params.getString("cityCode")
        } else {
            ""
        }

        val radius = if (params.hasKey("radius")) {
            params.getInt("radius")
        } else {
            4000
        }

        val pageSize = if (params.hasKey("pageSize")) {
            params.getInt("pageSize")
        } else {
            20
        }

        val pageNum = if (params.hasKey("pageNum")) {
            params.getInt("pageNum")
        } else {
            1
        }

        val searchBoundType = if (params.hasKey("searchBoundType")) {
            params.getString("searchBoundType")
        } else {
            promise.reject("参数错误", "请传入 searchBoundType")
            return
        }

        val searchBound: PoiSearch.SearchBound = when (searchBoundType) {
            "Periphery" -> {
                val bound = params.getMap("searchBoundParams")
                val latitude = bound.getDouble("latitude")
                val longitude = bound.getDouble("longitude")
                val latLonPoint = LatLonPoint(latitude, longitude)
                PoiSearch.SearchBound(latLonPoint, radius)
            }
            "Polygon" -> {
                val bound = params.getArray("searchBoundParams")


                val latlons = (0..bound.size() - 1).map { it ->
                    val point = bound.getMap(it)
                    val latitude = point.getDouble("latitude")
                    val longitude = point.getDouble("longitude")

                    LatLonPoint(latitude, longitude)
                }

                PoiSearch.SearchBound(latlons)
            }
            else -> {
                promise.reject("参数错误", "请传入 searchBoundType")
                return
            }
        }

        val query = PoiSearch.Query(keyword, ctgr, cityCode)
        query.pageNum = pageNum
        query.pageSize = pageSize

        val poiSearch = PoiSearch(reactContext, query)
        poiSearch.bound = searchBound

        poiSearch.setOnPoiSearchListener(object : OnPoiSearchListener {
            override fun onPoiItemSearched(p0: PoiItem?, code: Int) {
                Log.d("kiki", "kiki ->> PoiItem")
            }

            override fun onPoiSearched(result: PoiResult?, p1: Int) {
                Log.d("kiki", "kiki ->> res " + result)

                if (result == null) {
                    promise.resolve(null)
                } else {
                    promise.resolve(toWriteArray(result))
                }

            }

        })

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
