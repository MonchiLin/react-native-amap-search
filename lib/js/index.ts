import {NativeModules} from "react-native";

const {AMapPoiSearch, AMapCloudSearch} = NativeModules;

// TODO 带补全结果类型

type CloudSearch = (params: { radius: number; tableId: string; latitude: number; longitude: number }) => Promise<any>

export const poiSearch: CloudSearch = AMapPoiSearch.AMapPoiSearch;
export const cloudSearch: any = AMapCloudSearch.AMapCloudSearch;
