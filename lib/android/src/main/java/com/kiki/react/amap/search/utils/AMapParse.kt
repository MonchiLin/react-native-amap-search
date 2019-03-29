package com.kiki.react.amap.search.utils

import com.amap.api.services.cloud.CloudImage
import com.amap.api.services.cloud.CloudItem
import com.amap.api.services.cloud.CloudSearch
import com.amap.api.services.core.LatLonPoint
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import java.util.*

class AMapParse {
    companion object {
        fun parseCloudItem(clouds: ArrayList<CloudItem>): WritableArray? {
            val arr = Arguments.createArray()

            clouds.forEach { it ->
                val map = Arguments.createMap()
                map.putString("createtime", it.createtime)
                map.putString("id", it.id)
                map.putString("snippet", it.snippet)
                map.putString("title", it.title)
                map.putString("updatetime", it.updatetime)
                map.putArray("cloudImage", parseCloudImageList(it.cloudImage))

                map.putString("customfield", it.customfield.toString())

                map.putInt("distance", it.distance)
                map.putMap("latLonPoint", parseLatLonPoint(it.latLonPoint))

                arr.pushMap(map)
            }

            return arr
        }

        fun parseCloudImageList(cloudImage: MutableList<CloudImage>): WritableArray? {
            val arr = Arguments.createArray()
            cloudImage.forEach { it -> arr.pushMap(parseCloudImage(it)) }
            return arr
        }

        fun parseCloudImage(cloudImage: CloudImage): WritableMap? {
            val map = Arguments.createMap()

            map.putString("id", cloudImage.id)
            map.putString("preurl", cloudImage.preurl)
            map.putString("url", cloudImage.url)

            return map
        }

        fun parseQuery(query: CloudSearch.Query): WritableMap? {
            val map = Arguments.createMap()
            map.putString("filterNumString", query.filterNumString)

            map.putString("filterString", query.filterString)

            map.putString("queryString", query.queryString)

            map.putString("tableID", query.tableID)

            map.putMap("bound", parseSearchBound(query.bound))

            map.putInt("pageNum", query.pageNum)

            map.putInt("pageSize", query.pageSize)

            map.putString("sortingrules", parseSortingrules(query.sortingrules))

            return map
        }


        fun parseSortingrules(sortingrules: CloudSearch.Sortingrules?): String {
            return sortingrules.toString()

        }


        fun parseSearchBound(bound: CloudSearch.SearchBound): WritableMap? {
            val map = Arguments.createMap()
            map.putString("city", bound.city ?: null)

            map.putString("shape", bound.shape ?: null)

            map.putMap("center", parseLatLonPoint(bound.center))

            if (bound.lowerLeft == null) {
                map.putNull("lowerLeft")
            } else {
                map.putMap("lowerLeft", parseLatLonPoint(bound.lowerLeft))
            }

            if (bound.polyGonList == null) {
                map.putNull("polyGonList")
            } else {
                map.putArray("polyGonList", parseLatLonPointList(bound.polyGonList))
            }

            map.putInt("range", bound.range)

            if (bound.upperRight == null) {
                map.putNull("upperRight")
            } else {
                map.putMap("upperRight", parseLatLonPoint(bound.upperRight))
            }

            return map
        }


        fun parseLatLonPoint(latLonPoint: LatLonPoint?): WritableMap? {
            val map = Arguments.createMap()

            if (latLonPoint == null) {
                map.putNull("latitude")
                map.putNull("longitude")
                return map
            }

            map.putDouble("latitude", latLonPoint.latitude)
            map.putDouble("longitude", latLonPoint.longitude)

            return map
        }


        fun parseLatLonPointList(latLonPoint: MutableList<LatLonPoint>): WritableArray? {
            val arr = Arguments.createArray()

            latLonPoint.forEach { arr.pushMap(parseLatLonPoint(it)) }

            return arr
        }


    }
}