package cc.lkme.linkai_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import cc.lkme.common.referral.CommonError;
import cc.lkme.linkai.adapter.callback.AiSplashListener;
import cc.lkme.linkai.adapter.referral.AiAdInfo;
import cc.lkme.linkai.core.view.AiSplashView;

public class AdSplashActivity extends AppCompatActivity {

    private FrameLayout splashContainer;
    private AiSplashView aiSplashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_splash);
        splashContainer = findViewById(R.id.splash_container);
        aiSplashView = new AiSplashView(this, "100007");
        splashContainer.addView(aiSplashView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        aiSplashView.setOnAiSplashListener(new AiSplashListener() {

            @Override
            public void onLoad(AiAdInfo aiAdInfo) {
                Toast.makeText(AdSplashActivity.this, "有广告可展示", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(CommonError error) {
                Toast.makeText(AdSplashActivity.this, "无广告展示", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReady() {
                Toast.makeText(AdSplashActivity.this, "渲染成功，可以展示", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onShow(AiAdInfo aiAdInfo) {
                Toast.makeText(AdSplashActivity.this, "广告展示", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onClick(AiAdInfo aiAdInfo) {
                Toast.makeText(AdSplashActivity.this, "广告点击", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSkip(AiAdInfo aiAdInfo) {
                Toast.makeText(AdSplashActivity.this, "广告跳过", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTimeOver(AiAdInfo aiAdInfo) {
                Toast.makeText(AdSplashActivity.this, "广告倒计时结束", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClose(AiAdInfo aiAdInfo) {
                Toast.makeText(AdSplashActivity.this, "广告关闭", Toast.LENGTH_SHORT).show();
            }
        });
        aiSplashView.load();
    }

    //在合适的时机，释放广告的资源
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (aiSplashView != null) {
            //调用destroy()方法释放
            aiSplashView.destroy();
        }
    }

}
