package cc.lkme.linkai_demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import cc.lkme.common.referral.CommonError;
import cc.lkme.linkai.adapter.AiExpressAdConfig;
import cc.lkme.linkai.adapter.callback.AiNativeUnifiedListener;
import cc.lkme.linkai.adapter.referral.AiAdError;
import cc.lkme.linkai.adapter.referral.AiAdInfo;
import cc.lkme.linkai.adapter.referral.NativeUnifiedAdInfo;
import cc.lkme.linkai.adapter.view.AiNativeAdRenderAdapter;
import cc.lkme.linkai.adapter.view.AiNativeExpress;
import cc.lkme.linkai.core.view.AiNativeUnifiedAd;

public class NativeUnifiedListActivity extends AppCompatActivity {

    private static final String TAG = "FeedListActivity";

    private static final int AD_POSITION = 3;
    private static final int LIST_ITEM_COUNT = 30;
    private LoadMoreListView mListView;
    private MyAdapter myAdapter;
    private List<AiNativeExpress> mData;
    private Button adLoad;
    private AiNativeUnifiedAd aiNativeUnifiedAd;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native);
        AiExpressAdConfig.Builder aiExpressAdConfigBuilder = new AiExpressAdConfig.Builder();
        aiExpressAdConfigBuilder.setAdCount(2).setAdWidth(1080).setAdHeight(0);
        aiNativeUnifiedAd = new AiNativeUnifiedAd(this, "100009");
        aiNativeUnifiedAd.setOnAiNativeUnifiedListener(aiNativeUnifiedListener);
        initListView();
    }

    @SuppressWarnings("RedundantCast")
    private void initListView() {
        adLoad = (Button) findViewById(R.id.ad_load);
        adLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData != null) {
                    mData.clear();
                    if (myAdapter != null) {
                        myAdapter.notifyDataSetChanged();
                    }
                }
                loadListAd();
            }
        });

        mListView = (LoadMoreListView) findViewById(R.id.my_list);
        mData = new ArrayList<>();
        myAdapter = new MyAdapter(this, mData);
        mListView.setAdapter(myAdapter);
        mListView.setLoadMoreListener(new ILoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadListAd();
            }
        });

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadListAd();
            }
        }, 500);
    }

    /**
     * 加载feed广告
     */
    private void loadListAd() {
        aiNativeUnifiedAd.load();
    }

    private AiNativeUnifiedListener aiNativeUnifiedListener = new AiNativeUnifiedListener() {
        @Override
        public void onLoad(AiAdInfo aiAdInfo, List<Object> ads) {

            if (mListView != null) {
                mListView.setLoadingFinish();
            }

            if (ads == null || ads.isEmpty()) {
                Toast.makeText(NativeUnifiedListActivity.this, "on FeedAdLoaded: ad is null!", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < LIST_ITEM_COUNT; i++) {
                mData.add(null);
            }
            // TODO: 2020/10/14 lipeng 待优化
            List<AiNativeExpress> aiNativeExpressList = new LinkedList<>();
            for (Object ad : ads) {
                aiNativeExpressList.add((AiNativeExpress) ad);
            }
            bindAdListener(aiNativeExpressList);
        }

        @Override
        public void onFail(CommonError aiAdError) {
            if (mListView != null) {
                mListView.setLoadingFinish();
            }
            Toast.makeText(NativeUnifiedListActivity.this, "无广告", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClose(AiAdInfo aiAdInfo) {

        }
    };

    /**
     * 自渲染视图
     */
    public static class AiNativeAdRenderView implements AiNativeAdRenderAdapter {

        Context mContext;
        List<View> clickableView = new ArrayList<>();

        public AiNativeAdRenderView(Context context) {
            mContext = context;
        }

        View nativeUnifiedView;

        @Override
        public View renderAdView(NativeUnifiedAdInfo nativeUnifiedAdInfo) {
            if (nativeUnifiedView == null) {
                nativeUnifiedView = LayoutInflater.from(mContext).inflate(R.layout.native_unified_custom, null);
            }
            if (nativeUnifiedView.getParent() != null) {
                ((ViewGroup) nativeUnifiedView.getParent()).removeView(nativeUnifiedView);
            }
            TextView title = nativeUnifiedView.findViewById(R.id.title);
            SimpleDraweeView icon = nativeUnifiedView.findViewById(R.id.icon);
            TextView desc = nativeUnifiedView.findViewById(R.id.desc);
            SimpleDraweeView bigImg = nativeUnifiedView.findViewById(R.id.big_img);
            title.setText(nativeUnifiedAdInfo.getTitle());
            desc.setText(nativeUnifiedAdInfo.getDescription());
            icon.setImageURI(nativeUnifiedAdInfo.getIconUrl());
            bigImg.setImageURI(nativeUnifiedAdInfo.getImgUrl());
            clickableView.add(title);
            clickableView.add(icon);
            clickableView.add(desc);
            clickableView.add(bigImg);
            return nativeUnifiedView;
        }

        @Override
        public List<View> getClickableView() {
            return clickableView;
        }
    }

    private void bindAdListener(List<AiNativeExpress> ads) {
        int count = mData.size();
        for (AiNativeExpress ad : ads) {
            int random = (int) (Math.random() * LIST_ITEM_COUNT) + count - LIST_ITEM_COUNT;
            mData.set(random, ad);
            myAdapter.notifyDataSetChanged();
            if (!ad.isExpress()) {
                // 自渲染
                ad.renderAdView(new AiNativeAdRenderView(NativeUnifiedListActivity.this));
            }
            ad.setExpressAdInteractionListener(new AiNativeExpress.ExpressAdInteractionListener() {

                @Override
                public void onAdClicked(AiAdInfo aiAdInfo) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NativeUnifiedListActivity.this, "广告被点击", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

                @Override
                public void onAdShow(AiAdInfo aiAdInfo) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NativeUnifiedListActivity.this, "广告展示", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

                @Override
                public void onRenderFail(final AiAdError aiAdError) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NativeUnifiedListActivity.this, aiAdError.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }

                @Override
                public void onRenderSuccess(AiAdInfo aiAdInfo) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //返回view的宽高 单位 dp
                            Toast.makeText(NativeUnifiedListActivity.this, "渲染成功", Toast.LENGTH_SHORT).show();
                            myAdapter.notifyDataSetChanged();
                        }
                    });

                }

            });
            // 渲染广告
            ad.render();
        }

    }

    @SuppressWarnings("CanBeFinal")
    private static class MyAdapter extends BaseAdapter {

        private static final int ITEM_VIEW_TYPE_NORMAL = 0;
        private static final int ITEM_VIEW_TYPE_GROUP_PIC_AD = 1;
        private static final int ITEM_VIEW_TYPE_SMALL_PIC_AD = 2;
        private static final int ITEM_VIEW_TYPE_LARGE_PIC_AD = 3;
        private static final int ITEM_VIEW_TYPE_VIDEO = 4;
        private static final int ITEM_VIEW_TYPE_VERTICAL_IMG = 5;//竖版图片
        private static final int ITEM_VIEW_TYPE_VIDEO_VERTICAL = 6;//竖版视频

        private int mVideoCount = 0;


        private List<AiNativeExpress> mData;
        private Context mContext;

        private Map<AdViewHolder, TTAppDownloadListener> mTTAppDownloadListenerMap = new WeakHashMap<>();

        public MyAdapter(Context context, List<AiNativeExpress> data) {
            this.mContext = context;
            this.mData = data;
        }

        @Override
        public int getCount() {
            return mData.size(); // for test
        }

        @Override
        public AiNativeExpress getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //信息流广告的样式，有大图、小图、组图和视频，通过ad.getImageMode()来判断
        @Override
        public int getItemViewType(int position) {
            AiNativeExpress ad = getItem(position);
            if (ad == null) {
                return ITEM_VIEW_TYPE_NORMAL;
            } else if (ad.isExpress()) {
                return ITEM_VIEW_TYPE_SMALL_PIC_AD;
            } else {
//                Toast.makeText(mContext, "图片展示样式错误", Toast.LENGTH_SHORT).show();
                return ITEM_VIEW_TYPE_SMALL_PIC_AD;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 7;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AiNativeExpress ad = getItem(position);
            switch (getItemViewType(position)) {
                case ITEM_VIEW_TYPE_SMALL_PIC_AD:
                case ITEM_VIEW_TYPE_LARGE_PIC_AD:
                case ITEM_VIEW_TYPE_GROUP_PIC_AD:
                case ITEM_VIEW_TYPE_VERTICAL_IMG:
                case ITEM_VIEW_TYPE_VIDEO:
                case ITEM_VIEW_TYPE_VIDEO_VERTICAL:
                    return getAdView(convertView, parent, ad);
                default:
                    return getNormalView(convertView, parent, position);
            }
        }

        //渲染视频广告，以视频广告为例，以下说明
        @SuppressWarnings("RedundantCast")
        private View getAdView(View convertView, ViewGroup parent, @NonNull final AiNativeExpress ad) {
            final AdViewHolder adViewHolder;
            try {
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_ad_native_express, parent, false);
                    adViewHolder = new AdViewHolder();
                    adViewHolder.videoView = (FrameLayout) convertView.findViewById(R.id.iv_listitem_express);
                    convertView.setTag(adViewHolder);
                } else {
                    adViewHolder = (AdViewHolder) convertView.getTag();
                }

                //绑定广告数据、设置交互回调
//                bindData(convertView, adViewHolder, ad);
                if (adViewHolder.videoView != null) {
                    //获取视频播放view,该view SDK内部渲染，在媒体平台可配置视频是否自动播放等设置。
                    View adView = ad.getAdView();
                    if (adView != null) {
                        if (adView.getParent() == null) {
                            adViewHolder.videoView.removeAllViews();
                            adViewHolder.videoView.addView(adView);
                            ad.render();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }

        /**
         * 非广告list
         *
         * @param convertView
         * @param parent
         * @param position
         * @return
         */
        @SuppressWarnings("RedundantCast")
        @SuppressLint("SetTextI18n")
        private View getNormalView(View convertView, ViewGroup parent, int position) {
            NormalViewHolder normalViewHolder;
            if (convertView == null) {
                normalViewHolder = new NormalViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_normal, parent, false);
                normalViewHolder.idle = (TextView) convertView.findViewById(R.id.text_idle);
                convertView.setTag(normalViewHolder);
            } else {
                normalViewHolder = (NormalViewHolder) convertView.getTag();
            }
            normalViewHolder.idle.setText("ListView item " + position);
            return convertView;
        }

        private static class AdViewHolder {
            FrameLayout videoView;
        }

        private static class NormalViewHolder {
            TextView idle;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mData != null) {
            for (AiNativeExpress ad : mData) {
                if (ad != null) {
//                    ad.destroy();
                }
            }
        }
        mHandler.removeCallbacksAndMessages(null);
    }
}
