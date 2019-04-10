package com.kiki.react.amap.search.utils

import com.amap.api.services.cloud.CloudImage
import com.amap.api.services.cloud.CloudItem
import com.amap.api.services.cloud.CloudSearch
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.*
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.SubPoiItem
import com.amap.api.services.road.Crossroad
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import java.util.*

class AMapParse {
    companion object {
        fun parserCloudItem(clouds: ArrayList<CloudItem>): WritableArray? {
            val arr = Arguments.createArray()

            clouds.forEach { it ->
                val map = Arguments.createMap()
                map.putString("createtime", it.createtime)
                map.putString("id", it.id)
                map.putString("snippet", it.snippet)
                map.putString("title", it.title)
                map.putString("updatetime", it.updatetime)
                map.putArray("cloudImage", parserCloudImageList(it.cloudImage))

                map.putString("customfield", it.customfield.toString())

                map.putInt("distance", it.distance)
                map.putMap("latLonPoint", parserLatLonPoint(it.latLonPoint))

                arr.pushMap(map)
            }

            return arr
        }

        fun parserCloudImageList(cloudImage: MutableList<CloudImage>): WritableArray? {
            val arr = Arguments.createArray()
            cloudImage.forEach { it -> arr.pushMap(parserCloudImage(it)) }
            return arr
        }

        fun parserCloudImage(cloudImage: CloudImage): WritableMap? {
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

            map.putMap("bound", parserSearchBound(query.bound))

            map.putInt("pageNum", query.pageNum)

            map.putInt("pageSize", query.pageSize)

            map.putString("sortingrules", parseSortingrules(query.sortingrules))

            return map
        }


        fun parseSortingrules(sortingrules: CloudSearch.Sortingrules?): String {
            return sortingrules.toString()

        }


        fun parserSearchBound(bound: CloudSearch.SearchBound): WritableMap? {
            val map = Arguments.createMap()
            map.putString("city", bound.city ?: null)

            map.putString("shape", bound.shape ?: null)

            map.putMap("center", parserLatLonPoint(bound.center))

            if (bound.lowerLeft == null) {
                map.putNull("lowerLeft")
            } else {
                map.putMap("lowerLeft", parserLatLonPoint(bound.lowerLeft))
            }

            if (bound.polyGonList == null) {
                map.putNull("polyGonList")
            } else {
                map.putArray("polyGonList", parserLatLonPointList(bound.polyGonList))
            }

            map.putInt("range", bound.range)

            if (bound.upperRight == null) {
                map.putNull("upperRight")
            } else {
                map.putMap("upperRight", parserLatLonPoint(bound.upperRight))
            }

            return map
        }


        fun parserLatLonPoint(latLonPoint: LatLonPoint?): WritableMap? {
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


        fun parserLatLonPointList(latLonPoint: MutableList<LatLonPoint>): WritableArray {
            val arr = Arguments.createArray()

            latLonPoint.forEach { arr.pushMap(parserLatLonPoint(it)) }

            return arr
        }

        fun parserAoi(aoi: AoiItem): WritableMap? {
            val map = Arguments.createMap()

            map.putString("adCode", aoi.adCode)
            map.putString("aoiId", aoi.aoiId)
            map.putString("aoiName", aoi.aoiName)
            map.putDouble("aoiArea", aoi.aoiArea.toDouble())
            map.putMap("aoiCenterPoint", parserLatLonPoint(aoi.aoiCenterPoint))
            return map
        }

        fun parserAoiItem(aois: MutableList<AoiItem>): WritableArray? {
            val arr = Arguments.createArray()

            aois.forEach { arr.pushMap(parserAoi(it)) }

            return arr
        }

        fun parserBusinessArea(businessArea: BusinessArea): WritableMap? {
            val map = Arguments.createMap()
            map.putString("name", businessArea.name)
            map.putMap("name", parserLatLonPoint(businessArea.centerPoint))
            return map
        }

        fun parserBusinessAreas(businessAreas: MutableList<BusinessArea>): WritableArray? {
            val arr = Arguments.createArray()

            businessAreas.forEach { arr.pushMap(parserBusinessArea(it)) }

            return arr
        }

        fun parserCrossroad(crossroad: Crossroad): WritableMap? {
            val map = Arguments.createMap()
            map.putString("direction", crossroad.direction)
            map.putString("firstRoadId", crossroad.firstRoadId)
            map.putString("firstRoadName", crossroad.firstRoadName)
            map.putString("secondRoadId", crossroad.secondRoadId)
            map.putString("secondRoadName", crossroad.secondRoadName)
            return map
        }

        fun parserCrossroads(businessAreas: MutableList<Crossroad>): WritableArray? {
            val arr = Arguments.createArray()

            businessAreas.forEach { arr.pushMap(parserCrossroad(it)) }

            return arr
        }

        fun parserRegeocodeAddress(regeocodeAddress: RegeocodeAddress): WritableMap {
            val map = Arguments.createMap()

            map.putString("adCode", regeocodeAddress.adCode)
            map.putString("building", regeocodeAddress.building)
            map.putString("city", regeocodeAddress.city)
            map.putString("cityCode", regeocodeAddress.cityCode)
            map.putString("country", regeocodeAddress.country)
            map.putString("district", regeocodeAddress.district)
            map.putString("formatAddress", regeocodeAddress.formatAddress)
            map.putString("neighborhood", regeocodeAddress.neighborhood)
            map.putString("province", regeocodeAddress.province)
            map.putString("towncode", regeocodeAddress.towncode)
            map.putString("township", regeocodeAddress.township)

            map.putArray("aois", parserAoiItem(regeocodeAddress.aois))

            map.putArray("businessAreas", parserBusinessAreas(regeocodeAddress.businessAreas))

            map.putArray("crossroads", parserCrossroads(regeocodeAddress.crossroads))

            map.putArray("pois", parserPois(regeocodeAddress.pois))
            map.putArray("roads", parserRegeocodeRoads(regeocodeAddress.roads))
            map.putMap("streetNumber", parserStreetNumber(regeocodeAddress.streetNumber))

            return map
        }

        fun parserStreetNumber(streetNumber: StreetNumber): WritableMap? {
            val map = Arguments.createMap()

            map.putString("direction", streetNumber.direction)
            map.putString("number", streetNumber.number)
            map.putString("street", streetNumber.street)
            map.putDouble("distance", streetNumber.distance.toDouble())
            map.putMap("latLonPoint", parserLatLonPoint(streetNumber.latLonPoint))

            return map
        }

        fun parserRegeocodeRoad(regeocodeRoad: RegeocodeRoad): WritableMap {
            val map = Arguments.createMap()

            map.putString("direction", regeocodeRoad.direction)
            map.putString("id", regeocodeRoad.id)
            map.putString("name", regeocodeRoad.name)
            map.putDouble("distance", regeocodeRoad.distance.toDouble())
            map.putMap("latLngPoint", parserLatLonPoint(regeocodeRoad.latLngPoint))

            return map
        }

        fun parserRegeocodeRoads(regeocodeRoad: MutableList<RegeocodeRoad>): WritableArray {
            val arr = Arguments.createArray()

            regeocodeRoad.forEach {
                arr.pushMap(parserRegeocodeRoad(it))
            }

            return arr
        }


        fun parserGeocodeAddress(geocodeAddress: GeocodeAddress): WritableMap {
            val map = Arguments.createMap()

            map.putString("adcode", geocodeAddress.adcode)
            map.putString("building", geocodeAddress.building)
            map.putString("city", geocodeAddress.city)
            map.putString("district", geocodeAddress.district)
            map.putString("formatAddress", geocodeAddress.formatAddress)
            map.putString("level", geocodeAddress.level)
            map.putString("neighborhood", geocodeAddress.neighborhood)
            map.putString("province", geocodeAddress.province)
            map.putString("township", geocodeAddress.township)
            map.putMap("latLonPoint", parserLatLonPoint(geocodeAddress.latLonPoint))

            return map
        }

        fun parserGeocodeResult(geocodeAddressList: MutableList<GeocodeAddress>): WritableArray {
            val arr = Arguments.createArray()

            geocodeAddressList.forEach {
                arr.pushMap(parserGeocodeAddress(it))
            }

            return arr
        }

        fun parserPois(pois: MutableList<PoiItem>): WritableArray {
            val arr = Arguments.createArray()

            pois.forEach { poi ->
                arr.pushMap(parserPoiItem(poi))
            }

            return arr
        }

        fun parserPoiSearch(result: PoiResult): WritableArray {
            val pois = result.pois
            val arr = Arguments.createArray()

            pois.forEach { poi ->
                arr.pushMap(parserPoiItem(poi))
            }

            return arr
        }

        fun parserSubPoiItems(subPoiItems: MutableList<SubPoiItem>): WritableArray {
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

        fun parserPoiItem(poi: PoiItem): WritableMap {
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

            map.putArray("subPois", parserSubPoiItems(poi.subPois))

            return map
        }
    }

}