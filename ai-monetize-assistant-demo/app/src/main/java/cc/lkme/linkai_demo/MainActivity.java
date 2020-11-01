package cc.lkme.linkai_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private Button adBanner, rewardVideo, adSplash, adNative, adInteraction, adNativeUnified;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adBanner = findViewById(R.id.ad_banner);
        adBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdBannerActivity.class);
                startActivity(intent);
            }
        });
        rewardVideo = findViewById(R.id.ad_reward_video);
        rewardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdRewardVideoActivity.class);
                startActivity(intent);
            }
        });
        adSplash = findViewById(R.id.ad_splash);
        adSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdSplashActivity.class);
                startActivity(intent);
            }
        });
        adInteraction = findViewById(R.id.ad_interaction);
        adInteraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InterstitialActivity.class);
                startActivity(intent);
            }
        });
        adNative = findViewById(R.id.ad_native);
        adNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NativeExpressActivity.class);
                startActivity(intent);
            }
        });
        adNativeUnified = findViewById(R.id.ad_native_unified);
        adNativeUnified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NativeUnifiedListActivity.class);
                startActivity(intent);
            }
        });

    }

}
