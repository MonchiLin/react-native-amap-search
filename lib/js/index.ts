import {NativeModules} from "react-native";

const {AMapPoiSearch, AMapCloudSearch} = NativeModules;

// TODO 带补全结果类型

type CloudSearch = (params: { radius: number; tableId: string; latitude: number; longitude: number }) => Promise<any>

interface Module {
    poiSearch: any
    cloudSearch: CloudSearch
}

export default {
    poiSearch: AMapPoiSearch.AMapPoiSearch,
    cloudSearch: AMapCloudSearch.AMapCloudSearch
} as Module;
