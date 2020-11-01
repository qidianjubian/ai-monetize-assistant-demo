package cc.lkme.linkai_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import cc.lkme.common.referral.CommonError;
import cc.lkme.linkai.adapter.callback.AiBannerListener;
import cc.lkme.linkai.adapter.referral.AiAdInfo;
import cc.lkme.linkai.core.PlatformAdPositionInfo;
import cc.lkme.linkai.core.view.AiBannerView;

public class AdBannerActivity extends AppCompatActivity {

    private FrameLayout bannerContainer;
    private AiBannerView aiBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_banner);
        bannerContainer = findViewById(R.id.banner_container);
        aiBannerView = new AiBannerView(this, "100000");
        WindowManager manager = getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        // 600:150
        bannerContainer.addView(aiBannerView, new FrameLayout.LayoutParams(outMetrics.widthPixels, FrameLayout.LayoutParams.WRAP_CONTENT));
        PlatformAdPositionInfo.Builder builder = new PlatformAdPositionInfo.Builder();
        builder.appendOpenAd("945438054").appendBaiduAd("7221721").appendGdtAd("1041028674945370");
        aiBannerView.setCustomAdSort(builder.build());
        aiBannerView.setOnAiBannerListener(new AiBannerListener() {

            @Override
            public void onLoad(AiAdInfo aiAdInfo) {
                Toast.makeText(AdBannerActivity.this, "有广告可展示", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(CommonError error) {
                Toast.makeText(AdBannerActivity.this, "无广告展示", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReady() {
                Toast.makeText(AdBannerActivity.this, "渲染成功，可以展示", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onShow(AiAdInfo aiAdInfo) {
                Toast.makeText(AdBannerActivity.this, "广告展示", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onClick(AiAdInfo aiAdInfo) {
                Toast.makeText(AdBannerActivity.this, "广告点击", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClose(AiAdInfo aiAdInfo) {

            }
        });
        aiBannerView.load();
    }

    //在合适的时机，释放广告的资源
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (aiBannerView != null) {
            //调用destroy()方法释放
            aiBannerView.destroy();
        }
    }

}
