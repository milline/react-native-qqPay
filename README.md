# react-native 接QQ钱包支付的SDK #

>#### 目前没有npm安装，需要自行引入第三方sdk####

>#### 因业务需求需要接QQ钱包支付，网上也没有相应的教程、步骤。只好自己摸索，在这里写下我的解决方案给需要用到的人，更快的接好QQ钱包支付，也给自己记下笔记（目前接触react-native才1个月多不是很深入了解其原理，只停留在实现业务功能的层面，解决方案不是很到位） ####

## 配置步骤 ##

## android(ios 要走内购流程IAP)

1.去[QQ钱包商户平台](https://qpay.qq.com/buss/wiki/38/1195)下载需要的[SDK](https://i.gtimg.cn/channel/imglib/201806/upload_e17215817f1aef539c6f0185f14e62b2.zip)把下载好的 mqqopenpay.jar 放在 android>app>libs 目录下

2.在android>app>src>main>java>cn>你的包名>book下创建qqwallet文件夹（名字可以自己取）里面分别放三个文件（拷贝即可）

>#### CallbackActivity.java （处理支付回调）
>#### QqWalletModule.java （编写react-native可以使用的方法）
>#### QqWalletPackage.java （连接android跟react-native）

3.需要修改的内容

> CallbackAcvity.java

```java
package cn.test.book.qqwallet; (test改成你的包名)
```

> QqWalletPackage.java

```java
package cn.test.book.qqwallet; (test改成你的包名)
```

> QqWalletModule.java

```java
package cn.test.book.qqwallet; (test改成你的包名)
```

```java
String callbackScheme = "qwallet111111111"; （111111111改成你在qq商户平台申请的appid）
```

```java
static String APP_ID = "111111111";（同上）
```

4.修改配置文件

> app > build.gradle

```js
android {
  // 添加这条
  sourceSets {
    main {
        jniLibs.srcDirs = ['libs']
    }
  }
}
```

> AndroidManifest.xml

```xml
<!-- 添加qq支付 回调监听 -->
  <activity
    android:name=".qqwallet.CallbackActivity"
    android:launchMode="singleTop"
    android:exported="true" >
    <intent-filter>
      <action android:name="android.intent.action.VIEW" />
      <category android:name="android.intent.category.BROWSABLE"/>
      <category android:name="android.intent.category.DEFAULT"/>
      <data android:scheme="qwallet111111111"/>
    </intent-filter>
  </activity>
<!-- end -->
```

> MainApplication.java

```java
package cn.test.book;(test改成你的包名)

import cn.test.book.qqwallet.QqWalletPackage;(test改成你的包名)
```

```java
protected List<ReactPackage> getPackages() {
  return Arrays.<ReactPackage>asList(
    // + QqWalletPackage
    new QqWalletPackage()
  );
}
```

5.创建 qqwallet.js (放在src目录下 自己找位置放,复制即可)

```js
// 判断用户手机是否安装了QQ
onBtnIsMqqInstalled().then((installed) => {
  if (installed) {
    // 安装了
  }
})

// 判断用户安装的QQ是否支持支付功能
onBtnIsMqqSupportPay().then((isSupportPay) => {
  if (isSupportPay) {
    // 支持
  }
})

/**
 * qqPay
 * @param {Object} data
 * @param {String} data.tokenId
 * @param {String} data.bargainorId
 * @param {String} data.sig
 * @returns {Promise}
 */

qqPay(data) // 调起第三方支付方法 参数详情见QQ商户开发文档.

// 返回值是个json
/** 包含如下字段
 * apiName
 * serialnumber
 * isSucess
 * retCode
 * retMsg
 */

// 使用方法
qqPay({
  appId: '',
  tokenId: '',
  bargainorId: '',
  nonce: '',
  pubAcc: '',
  sig: '',
}).then(data => {
  if (data !== null && data !== 'notPayResponse') {
    let res = JSON.parse(data)
    if (res.isSucess) {
      // 充值成功
    } else {
      // 充值失败
      console.log(res.retMsg)
    }
  } else {
    console.log('返回值错误或没有')
  }
})
```