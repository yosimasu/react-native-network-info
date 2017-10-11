package com.pusherman.networkinfo;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.SupplicantState;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.net.InetAddress;
import java.net.Inet4Address;

import java.net.NetworkInterface;
import java.util.Enumeration;

public class RNNetworkInfo extends ReactContextBaseJavaModule {
  WifiManager wifi;

  public static final String TAG = "RNNetworkInfo";

  public RNNetworkInfo(ReactApplicationContext reactContext) {
    super(reactContext);

    wifi = (WifiManager)reactContext.getApplicationContext()
            .getSystemService(Context.WIFI_SERVICE);
  }

  @Override
  public String getName() {
    return TAG;
  }

  @ReactMethod
  public void getSSID(final Callback callback) {
    WifiInfo info = wifi.getConnectionInfo();
    
      // This value should be wrapped in double quotes, so we need to unwrap it.
      String ssid = "error";
      if (info.getSupplicantState() == SupplicantState.COMPLETED) {
        ssid = info.getSSID();
        if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
          ssid = ssid.substring(1, ssid.length() - 1);
        }
      }
  
      callback.invoke(ssid);
  }

  @ReactMethod
  public void getBSSID(final Callback callback) {
    WifiInfo info = wifi.getConnectionInfo();

    String bssid = "error";
    if (info.getSupplicantState() == SupplicantState.COMPLETED) {
      bssid = wifi.getConnectionInfo().getBSSID();
    }

    callback.invoke(bssid);
  }

  @ReactMethod
  public void getIPAddress(final Callback callback) {
    String ipAddress = "error";

    try {
      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
        NetworkInterface intf = en.nextElement();
        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
          InetAddress inetAddress = enumIpAddr.nextElement();
          if (!inetAddress.isLoopbackAddress()) {
            ipAddress = inetAddress.getHostAddress();
          }
        }
      }
    } catch (Exception ex) {
      Log.e(TAG, ex.toString());
    }

    callback.invoke(ipAddress);
  }

  @ReactMethod
  public void getIPV4Address(final Callback callback) {
    String ipAddress = "error";

    try {
      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
        NetworkInterface intf = en.nextElement();
        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
          InetAddress inetAddress = enumIpAddr.nextElement();
          if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
            ipAddress = inetAddress.getHostAddress().toString();
          }
        }
      }
    } catch (Exception ex) {
      Log.e(TAG, ex.toString());
    }

    callback.invoke(ipAddress);
  }
}
