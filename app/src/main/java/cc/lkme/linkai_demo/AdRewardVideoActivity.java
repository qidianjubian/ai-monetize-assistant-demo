package cc.lkme.linkai_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cc.lkme.common.referral.CommonError;
import cc.lkme.linkai.adapter.callback.AiRewardVideoListener;
import cc.lkme.linkai.adapter.referral.AiAdInfo;
import cc.lkme.linkai.core.PlatformAdPositionInfo;
import cc.lkme.linkai.core.view.AiRewardVideoAd;

public class AdRewardVideoActivity extends AppCompatActivity {

    private AiRewardVideoAd aiRewardVideoAd;
    private Button loadRewardVideo, showRewardVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_reward_video);
        loadRewardVideo = findViewById(R.id.load_reward_video);
        showRewardVideo = findViewById(R.id.show_reward_video);
        aiRewardVideoAd = new AiRewardVideoAd(this, "100004");
        aiRewardVideoAd.setOnAiRewardVideoListener(new AiRewardVideoListener() {

            @Override
            public void onLoad(AiAdInfo aiAdInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AdRewardVideoActivity.this.getApplicationContext(), "有广告可展示", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCached(AiAdInfo aiAdInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AdRewardVideoActivity.this.getApplicationContext(), "视频资源已缓存", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFail(CommonError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AdRewardVideoActivity.this.getApplicationContext(), "无广告展示", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onShow(AiAdInfo aiAdInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AdRewardVideoActivity.this.getApplicationContext(), "广告展示", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onClick(AiAdInfo aiAdInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AdRewardVideoActivity.this.getApplicationContext(), "广告点击", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onVideoComplete(AiAdInfo aiAdInfo) {

            }

            @Override
            public void onVideoError(AiAdInfo aiAdInfo) {

            }

            @Override
            public void onRewardVerify(AiAdInfo aiAdInfo, boolean rewardVerify) {

            }

            @Override
            public void onSkippedVideo(AiAdInfo aiAdInfo, float progress) {

            }

            @Override
            public void onClose(AiAdInfo aiAdInfo) {

            }
        });

        PlatformAdPositionInfo.Builder builder = new PlatformAdPositionInfo.Builder();
        builder.appendBaiduAd("7228298").appendGdtAd("9041425803910658").appendOpenAd("945453114");
        // 客户原序必须设置
        aiRewardVideoAd.setCustomAdSort(builder.build());
        loadRewardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiRewardVideoAd.load();
            }
        });
        showRewardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiRewardVideoAd.show();
            }
        });
    }

}
