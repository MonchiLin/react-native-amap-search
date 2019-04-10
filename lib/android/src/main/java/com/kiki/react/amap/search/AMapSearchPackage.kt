package com.kiki.react.amap.search

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.kiki.react.amap.search.cloud.CloudSearchModule
import com.kiki.react.amap.search.cloud.GeocodeModule
import com.kiki.react.amap.search.cloud.PoiSearchModule

class AMapSearchPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(
                CloudSearchModule(reactContext),
                PoiSearchModule(reactContext),
                GeocodeModule(reactContext)
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }

}
