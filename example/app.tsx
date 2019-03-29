import React, {Component} from "react";
import {View, Text, NativeModules} from "react-native";
import {cloudSearch} from "react-native-amap-search";

export default class App extends React.Component {

    componentDidMount(): void {
        const params = {
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
