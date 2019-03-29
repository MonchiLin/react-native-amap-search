import React, {Component} from "react";
import {View, Text, NativeModules} from "react-native";
import {cloudSearch} from "react-native-amap-search";

export default class App extends React.Component {

    componentDidMount(): void {
        // const search = NativeModules.AMapCloudSearch.AMapCloudSearch;

        cloudSearch({
            radius: 1000,
            tableId: "5c67d39f2376c16734b5e7a7",
            latitude: 22.7137471660,
            longitude: 114.2523837090
        }).then((res: any) => {
            console.log(res);
        }).catch((err: any) => {
            console.log("err", err);
        });

    }

    render(): React.ReactNode {
        return (
            <View><Text>Hello World</Text></View>
        );
    }
}
