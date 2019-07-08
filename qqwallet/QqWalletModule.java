package cn.test.book.qqwallet;

import java.io.IOException;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import android.app.Activity;

import com.tencent.mobileqq.openpay.api.IOpenApi;
import com.tencent.mobileqq.openpay.api.OpenApiFactory;
import com.tencent.mobileqq.openpay.constants.OpenConstants;
import com.tencent.mobileqq.openpay.data.pay.PayApi;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Promise;

public class QqWalletModule extends ReactContextBaseJavaModule {

  private final static String INVOKE_FAILED = "qq API invoke returns false.";
  static Promise promise = null;

  QqWalletModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "QqWallet";
  }

  int paySerial = 1;
  String callbackScheme = "qwallet111111111";

  static String APP_ID = "111111111";
  IOpenApi openApi = OpenApiFactory.getInstance(getReactApplicationContext(), APP_ID);

  @ReactMethod
  public void onBtnIsMqqInstalled(Callback callback) {
    boolean isInstalled = openApi.isMobileQQInstalled();
    callback.invoke(null, isInstalled);
  }

  @ReactMethod
  public void onBtnIsMqqSupportPay(Callback callback) {
    boolean isSupport = openApi.isMobileQQSupportApi(OpenConstants.API_NAME_PAY);
    callback.invoke(null, isSupport);
  }

  @ReactMethod
  public void onBtnMqqPay(ReadableMap data, Promise promise) {
    if (TextUtils.isEmpty(data.getString("tokenId"))) {
      Toast.makeText(getReactApplicationContext(), "tokenId is null.", Toast.LENGTH_LONG).show();
    }
    QqWalletModule.promise = promise;
    PayApi api = new PayApi();
    if (data.hasKey("appId")) {
      api.appId = data.getString("appId");
    }
    if (data.hasKey("tokenId")) {
      api.tokenId = data.getString("tokenId");
    }
    if (data.hasKey("bargainorId")) {
      api.bargainorId = data.getString("bargainorId");
    }
    if (data.hasKey("nonce")) {
      api.nonce = data.getString("nonce");
    }
    if (data.hasKey("pubAcc")) {
      api.pubAcc = data.getString("pubAcc");
    }
    if (data.hasKey("sig")) {
      api.sig = data.getString("sig");
    }

    api.serialNumber = "" + paySerial++;
    api.callbackScheme = callbackScheme;

    api.pubAccHint = "";
    api.timeStamp = System.currentTimeMillis() / 1000;
    api.sigType = "HMAC-SHA1";

    if (api.checkParams()) {
      openApi.execApi(api);
    }
  }

}