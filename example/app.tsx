import React, {Component} from "react";
import {View, Text, NativeModules} from "react-native";
import {cloudSearch, poiSearch, CloudSearch, CloudSearchParams} from "../lib/js";

export default class App extends React.Component {

    componentDidMount(): void {
        const params: CloudSearchParams = {
            searchBoundParams: [
                {
                    latitude: 22.719681,
                    longitude: 114.247137
                },
                {
                    latitude: 22.717068,
                    longitude: 114.250828
                },
            ],
            searchBoundType: "Polygon",
            radius: 1000,
            tableId: "5c67d39f2376c16734b5e7a7",
        };

        cloudSearch(params).then((res: any) => {
            console.log("cloudSearch ->", res);
        }).catch((err: any) => {
            console.log("err", err);
        });

        poiSearch({
            searchBoundParams: {
                latitude: 22.719681,
                longitude: 114.247137
            },
            searchBoundType: "Periphery",
            keyword: "龙城"
        }).then((res: any) => {
            console.log("poiSearch ->", res);
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
