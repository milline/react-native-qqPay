package cn.test.book.qqwallet;

import com.tencent.mobileqq.openpay.api.IOpenApi;
import com.tencent.mobileqq.openpay.api.IOpenApiListener;
import com.tencent.mobileqq.openpay.api.OpenApiFactory;
import com.tencent.mobileqq.openpay.data.base.BaseResponse;
import com.tencent.mobileqq.openpay.data.pay.PayResponse;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ReadableMap;

public class CallbackActivity extends Activity implements IOpenApiListener {

  IOpenApi openApi;

  // 处理支付回调
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    openApi = OpenApiFactory.getInstance(this, QqWalletModule.APP_ID);
    openApi.handleIntent(getIntent(), this);
  }

  // 处理支付回调
  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    openApi.handleIntent(intent, this);
  }

  // 返回支付结果
  @Override
  public void onOpenResponse(BaseResponse response) {
    String message;

    if (response == null) {
      message = null;
      return;
    } else {
      if (response instanceof PayResponse) {
        PayResponse payResponse = (PayResponse) response;
        message = "{\"apiName\":\"" + payResponse.apiName + "\","
                + "\"serialnumber\":\"" + payResponse.serialNumber + "\","
                + "\"isSucess\":" + payResponse.isSuccess() + ","
                + "\"retCode\":" + payResponse.retCode + ","
                + "\"retMsg\":\"" + payResponse.retMsg + "\"";
        // if (payResponse.isSuccess()) {
        //     if (!payResponse.isPayByWeChat()) {
        //         message += ",\"transactionId:\"" + payResponse.transactionId + "\","
        //                 + "\"payTime:\"" + payResponse.payTime + "\","
        //                 + "\"callbackUrl:\"" + payResponse.callbackUrl + "\","
        //                 + "\"totalFee:\"" + payResponse.totalFee + "\"";
        //     }
        // }
        message += "}";
      } else {
        message = "notPayResponse";
      }
    }
    QqWalletModule.promise.resolve(message);
    finish();
  }

}
