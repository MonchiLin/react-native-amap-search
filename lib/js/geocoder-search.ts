import {NativeModules} from "react-native";
import {LatLonPoint} from "./define";

const {AMapGeocode} = NativeModules;

/**
 * 地理编码
 * @ref https://lbs.amap.com/api/android-sdk/guide/map-data/geo
 * @param name - 名称
 * @param code - 查询城市，中文或者中文全拼，citycode、adcode
 */
export type GeocodeSearchParams = {
    name: string,
    code: string
}

/**
 * 逆地理编码
 * @ref https://lbs.amap.com/api/android-sdk/guide/map-data/geo
 * @param latLonPoint - 坐标
 * @param radius - 查询半径
 * @param coordinateSystem - 坐标系系统
 */
export type RegeocodeSearchParams = {
    latLonPoint: LatLonPoint,
    radius?: number,
    coordinateSystem?: "gps" | "autonavi"
}

export type GeocodeSearch = (params: GeocodeSearchParams) => Promise<any>
export type RegeocodeSearch = (params: RegeocodeSearchParams) => Promise<any>

export const geocodeSearch: GeocodeSearch = AMapGeocode.AMapGeocodeSearch;
export const regeocodeSearch: RegeocodeSearch = AMapGeocode.AMapRegeocodeSearch;
