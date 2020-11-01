package cc.lkme.linkai_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cc.lkme.common.referral.CommonError;
import cc.lkme.linkai.adapter.callback.AiInteractionListener;
import cc.lkme.linkai.adapter.referral.AiAdInfo;
import cc.lkme.linkai.core.PlatformAdPositionInfo;
import cc.lkme.linkai.core.view.AiInterstitial;

/**
 * 插屏广告
 */
public class InterstitialActivity extends AppCompatActivity {

    private Button load, show;
    private AiInterstitial aiInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);
        load = findViewById(R.id.load);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiInterstitial.load();
            }
        });
        show = findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiInterstitial.show();
            }
        });
        aiInterstitial = new AiInterstitial(this, "100001");
        PlatformAdPositionInfo.Builder builder = new PlatformAdPositionInfo.Builder();
        builder.appendOpenAd("945460603");
        aiInterstitial.setCustomAdSort(builder.build());
        aiInterstitial.setOnAiInteractionListener(new AiInteractionListener() {

            @Override
            public void onLoad(AiAdInfo aiAdInfo) {
                Toast.makeText(InterstitialActivity.this, "有广告可展示", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(CommonError error) {
                Toast.makeText(InterstitialActivity.this, "无广告展示", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReady() {
                Toast.makeText(InterstitialActivity.this, "渲染成功，可以展示", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onShow(AiAdInfo aiAdInfo) {
                Toast.makeText(InterstitialActivity.this, "广告展示", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onClick(AiAdInfo aiAdInfo) {
                Toast.makeText(InterstitialActivity.this, "广告点击", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVideoComplete(AiAdInfo aiAdInfo) {
                Toast.makeText(InterstitialActivity.this, "视频播放完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSkippedVideo(AiAdInfo aiAdInfo, float progress) {
                Toast.makeText(InterstitialActivity.this, "视频被跳过", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClose(AiAdInfo aiAdInfo) {
                Toast.makeText(InterstitialActivity.this, "广告关闭", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //在合适的时机，释放广告的资源
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (aiInterstitial != null) {
            //调用destroy()方法释放
            aiInterstitial.destroy();
        }
    }
}
