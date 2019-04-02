import {NativeModules} from "react-native";
import {LatLonPoint} from './define';

const {AMapPoiSearch} = NativeModules;

export type PoiSearchParams = {
    searchBoundType: "Periphery" | "Polygon",
    searchBoundParams: LatLonPoint | LatLonPoint[],
    cityCode?: number,
    ctgr?: string,
    keyword?: string,
    radius?: number,
    pageSize?: number,
    pageNum?: number,
}

export type PoiSearch = (params: PoiSearchParams) => Promise<any>

export const poiSearch: PoiSearch = AMapPoiSearch.AMapPoiSearch;

