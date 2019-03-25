import {NativeModules} from "react-native";

const {AMapPoiSearch, AMapCloudSearch} = NativeModules;

// TODO 带补全结果类型

interface IAMapCloudSearch {
    CloudSearch: (params: {
        radius: number
        tableId: string
        latitude: number
        longitude: number
    }) => Promise<any>
}

export default {
    AMapPoiSearch: AMapPoiSearch,
    AMapCloudSearch: AMapCloudSearch as IAMapCloudSearch
};
