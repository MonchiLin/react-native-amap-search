import React, {Component} from "react";
import {View, Text, NativeModules} from "react-native";
import {cloudSearch, poiSearch, CloudSearch, CloudSearchParams, poiSearchId} from "../lib/js";
import {geocodeSearch, regeocodeSearch} from "../lib/js/geocoder-search";

export default class App extends React.Component {

    componentDidMount(): void {
        // const params: CloudSearchParams = {
        //     searchBoundParams: [
        //         {
        //             latitude: 24.777343,
        //             longitude: 103.147589
        //         },
        //         {
        //             latitude: 24.377343,
        //             longitude: 103.247589
        //         },
        //     ],
        //     searchBoundType: "Polygon",
        //     radius: 1000,
        //     tableId: "please input your are key",
        // };

        // cloudSearch(params).then((res: any) => {
        //     console.log("cloudSearch ->", res);
        // }).catch((err: any) => {
        //     console.log("err", err);
        // });
        //
        // poiSearch({
        //     searchBoundParams: {
        //         latitude: 24.777343,
        //         longitude: 103.047589
        //     },
        //     searchBoundType: "Periphery",
        //     keyword: "广场"
        // }).then((res: any) => {
        //     console.log("poiSearch ->", res);
        // }).catch((err: any) => {
        //     console.log("err", err);
        // });
        //
        // poiSearchId("B0FFKG8RAM")
        //     .then((res) => {
        //         console.log("poiSearchId ->", res);
        //     }).catch(console.log);

		// geocodeSearch({name: "吐鲁番", code: "新疆"})
		//     .then(console.log);

		// regeocodeSearch({
		//     latLonPoint: {
		//         latitude: 24.777343,
		//         longitude: 103.047589
		//     },
		//     radius: 40000,
		//     coordinateSystem: "autonavi"
		// }).then(console.log);

	}

	render(): React.ReactNode {
		return (
			<View><Text>Hello World</Text></View>
		);
	}
}
