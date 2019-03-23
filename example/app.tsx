import React, {Component} from "react";
import {View, Text, NativeModules} from "react-native";
// import {AMapPoiSearch, AMapCloudSearch} from "react-native-amap-search";

export default class App extends React.Component {

    componentDidMount(): void {
        // console.log(AMapCloudSearch);
        // console.log(AMapPoiSearch);

        console.log("NativeModules", NativeModules);
        const search = NativeModules.ACloudSearch.ACloudSearch;
        search({
            radius: 1000,
            tableId: "5c67d39f2376c16734b5e7a7",
            latitude: 114.2523900000,
            longitude: 22.7112300000
        }).then(res => {
            console.log(res);
        });

    }

    render(): React.ReactNode {
        return (
            <View><Text>Hello World</Text></View>
        );
    }
}
