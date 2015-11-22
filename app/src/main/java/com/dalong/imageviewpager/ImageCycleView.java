package com.dalong.imageviewpager;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zhouweilong on 15/11/22.
 */
public class ImageCycleView extends LinearLayout{

    private ViewPager mViewpager;

    private LinearLayout viewGroup;

    private Context context;

    private View view;
    /**
     * 滚动图片指示视图列表
     */
    private ImageView[] mImageViews = null;

    private boolean isStop;

    /**
     * 游标是圆形还是长条，要是设置为0是长条，要是1就是圆形 默认是圆形
     */
    public int stype = 1;


    public boolean isAutoCycle = true;

    private int selectPosition = 0;

    /**
     * 切换时间
     */
    private int time = 3000;

    /**
     * 图片轮播指示个图
     */
    private ImageView mImageView = null;

    private ImageCycleAdapter mAdvAdapter;

    public ImageCycleView(Context context) {
        this(context,null);
    }

    public ImageCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        this.context=context;
        view= LayoutInflater.from(context).inflate(R.layout.image_cycle_view,null);
        mViewpager=(ViewPager)view.findViewById(R.id.adv_pager);
        mViewpager.setOnPageChangeListener(new GuidePageChangeListener());
        viewGroup=(LinearLayout)view.findViewById(R.id.viewGroup);
    }
    /**
     * 触摸停止计时器，抬起启动计时器
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        stopImageTimerTask();// 停止图片滚动
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startImageTimerTask();
                    }
                }, time);
                break;
        }

        return super.dispatchTouchEvent(event);
    }
    /**
     * 装填图片数据
     *
     * @param imageUrlList
     * @param imageCycleViewListener
     */
    public void setImageResources(ArrayList<String> imageUrlList, ImageCycleViewListener imageCycleViewListener, int stype) {

        if (imageUrlList == null || imageUrlList.size() == 0) {
            return;
        }
        this.stype = stype;
        // 清除
        viewGroup.removeAllViews();
        // 图片广告数量
        final int imageCount = imageUrlList.size();
        mImageViews = new ImageView[imageCount];
        for (int i = 0; i < imageCount; i++) {
            mImageView = new ImageView(context);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 30;
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setLayoutParams(params);

            mImageViews[i] = mImageView;
            if (i == 0) {
                if (this.stype == 1)
                    mImageViews[i].setBackgroundResource(R.drawable.autoscrollview_dots_pressed);
                else
                    mImageViews[i].setBackgroundResource(R.mipmap.cicle_banner_dian_focus);
            } else {
                if (this.stype == 1)
                    mImageViews[i].setBackgroundResource(R.drawable.autoscrollview_dots_unpressed);
                else
                    mImageViews[i].setBackgroundResource(R.mipmap.cicle_banner_dian_blur);
            }
            viewGroup.addView(mImageViews[i]);
        }
        mAdvAdapter = new ImageCycleAdapter(context, imageUrlList, imageCycleViewListener);
        mViewpager.setAdapter(mAdvAdapter);
        int num = Integer.MAX_VALUE / 2 % imageUrlList.size();
        int itemPostion = Integer.MAX_VALUE / 2 - num;
        mViewpager.setCurrentItem(itemPostion);
        this.removeAllViews();
        addView(view);
    }

    /**
     * 图片轮播(手动控制自动轮播与否，便于资源控件）
     */
    public void startImageCycle() {
        startImageTimerTask();
    }

    /**
     * 暂停轮播—用于节省资源
     */
    public void stopImageCycle() {
        stopImageTimerTask();
    }

    /**
     * 图片滚动任务
     */
    public void startImageTimerTask() {
        stopImageTimerTask();
        // 图片滚动
        if (isAutoCycle)
            mHandler.postDelayed(mImageTimerTask, time);
    }
    /**
     * 停止图片滚动任务
     */
    public void stopImageTimerTask() {
        isStop = true;
        mHandler.removeCallbacks(mImageTimerTask);
    }

    private Handler mHandler = new Handler();

    /**
     * 图片自动轮播Task
     */
    private Runnable mImageTimerTask = new Runnable() {
        @Override
        public void run() {
            if (mImageViews != null) {
                mViewpager.setCurrentItem(mViewpager.getCurrentItem() + 1);
                if (!isStop) {  //if  isStop=true   //当你退出后 要把这个给停下来 不然 这个一直存在 就一直在后台循环
                    if (isAutoCycle)
                        mHandler.postDelayed(mImageTimerTask, time);
                }

            }
        }
    };

    /**
     * 轮播图片监听
     *
     */
    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE)
                startImageTimerTask();
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
            selectPosition = index;
            index = index % mImageViews.length;
            // 设置当前显示的图片
            // 设置图片滚动指示器背
            if (stype == 1)
                mImageViews[index].setBackgroundResource(R.drawable.autoscrollview_dots_pressed);
            else
                mImageViews[index].setBackgroundResource(R.mipmap.cicle_banner_dian_focus);
            for (int i = 0; i < mImageViews.length; i++) {
                if (index != i) {
                    if (stype == 1)
                        mImageViews[i].setBackgroundResource(R.drawable.autoscrollview_dots_unpressed);
                    else
                        mImageViews[i].setBackgroundResource(R.mipmap.cicle_banner_dian_blur);
                }
            }
        }
    }


    /**
     * 适配器
     */
    private class ImageCycleAdapter extends PagerAdapter {

        /**
         * 图片视图缓存列表
         */
        private ArrayList<ImageView> mImageViewCacheList;

        /**
         * 图片资源列表
         */
        private ArrayList<String> mAdList = new ArrayList<String>();

        /**
         * 广告图片点击监听
         */
        private ImageCycleViewListener mImageCycleViewListener;

        private Context mContext;

        public ImageCycleAdapter(Context context, ArrayList<String> adList, ImageCycleViewListener imageCycleViewListener) {
            this.mContext = context;
            this.mAdList = adList;
            mImageCycleViewListener = imageCycleViewListener;
            mImageViewCacheList = new ArrayList<ImageView>();
        }

        public void changeData(ArrayList<String> adList) {
            this.mAdList = adList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            String imageUrl = mAdList.get(position % mAdList.size());
            ImageView imageView = null;
            if (mImageViewCacheList.isEmpty()) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                imageView.setAdjustViewBounds(true);
                //test
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            } else {
                imageView = mImageViewCacheList.remove(0);
            }
            // 设置图片点击监听
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImageCycleViewListener.onImageClick(position % mAdList.size(), v);
                }
            });
            imageView.setTag(imageUrl);
            container.addView(imageView);
            ImageLoader.getInstance().displayImage(imageUrl, imageView, ImageLoadOptions.getBannerImageOptions());
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = (ImageView) object;
            mViewpager.removeView(view);
            mImageViewCacheList.add(view);

        }

    }

    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public static interface ImageCycleViewListener {

        /**
         * 单击图片事件
         *
         * @param position
         * @param imageView
         */
        public void onImageClick(int position, View imageView);
    }

}
