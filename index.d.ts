type AMapCloudSearchParams = {
    latitude: number,
    longitude: number,
    radius?: number;
    keyword?: string;
    tableId?: string;
}

type AMapPoiSearchParams = {
    keyword: string;
    cityCode?: string;
}

declare module "react-native-amap-search" {
    export function AMapCloudSearch(params: AMapCloudSearchParams);

    export function AMapPoiSearch(params: AMapPoiSearchParams);

}
