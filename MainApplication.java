package cn.test.book;

import android.app.Application;

//  引入你android项目下新建的QqWalletPackage
import cn.test.book.qqwallet.QqWalletPackage;

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
        // 注册 QqWalletPackage
        new QqWalletPackage()
      );
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Fresco.initialize(this);
    SoLoader.init(this, /* native exopackage */ false);
  }
}
