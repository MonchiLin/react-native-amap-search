
package com.kiki.react.amap.search;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class AMapSearchModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public AMapSearchModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "AMapSearch";
  }
}