package com.example.englishtester;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;

import com.example.englishtester.common.OOMHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SlidePageInstructionActivity extends FragmentActivity {

    private static final String TAG = SlidePageInstructionActivity.class.getSimpleName();

    private RelativeLayout showGroup1;
    private RelativeLayout showGroup2;

    private ViewPager viewPager;

    //包裹點點的LinearLayout
    private ViewGroup group;
    private ImageView[] imageViews;

    private ImageView imageView1;

    private List<Integer> imageList;

    public static final String SLIDE_PAGE_INSTRUCTION_PIC_IMAGE_LIST_KEY = SlidePageInstructionActivity.class.getSimpleName() + "_ImgListKey";
    public static final String SLIDE_PAGE_INSTRUCTION_IMAGE_RESIZE = SlidePageInstructionActivity.class.getSimpleName() + "_Resize";

    private static boolean isDoResize = false;

    /**
     * 開啟技術指導Activity
     */
    public static void callInstrcutionActivity(Activity activity, ArrayList<Integer> imageList, boolean resize) {
        int requestCode = 0;
        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        intent.setClass(activity, SlidePageInstructionActivity.class);
        bundle.putIntegerArrayList(SlidePageInstructionActivity.SLIDE_PAGE_INSTRUCTION_PIC_IMAGE_LIST_KEY, imageList);
        bundle.putBoolean(SlidePageInstructionActivity.SLIDE_PAGE_INSTRUCTION_IMAGE_RESIZE, resize);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, //
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_slidepage);

        if (getIntent().getExtras().containsKey(SLIDE_PAGE_INSTRUCTION_PIC_IMAGE_LIST_KEY)) {
            imageList = getIntent().getExtras().getIntegerArrayList(SLIDE_PAGE_INSTRUCTION_PIC_IMAGE_LIST_KEY);
        } else {
            imageList = new ArrayList<Integer>();
            imageList.add(R.drawable.ic_launcher);
        }

        if (getIntent().getExtras().containsKey(SLIDE_PAGE_INSTRUCTION_IMAGE_RESIZE)) {
            isDoResize = getIntent().getExtras().getBoolean(SLIDE_PAGE_INSTRUCTION_IMAGE_RESIZE);
        }

        showGroup1 = (RelativeLayout) findViewById(R.id.showGroup1);
        showGroup2 = (RelativeLayout) findViewById(R.id.showGroup2);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        group = (ViewGroup) this.findViewById(R.id.viewGroup);

        initViewPager();

        initPointer(imageList.size());

        initZoomControls();

        initShowGroupButton();

        showGroup(1);
    }

    /**
     * 初始化群組按鈕
     */
    private void initShowGroupButton() {
        //放大圖
        imageView1.setOnTouchListener(new View.OnTouchListener() {
            private FrameLayout container = (FrameLayout) findViewById(R.id.container);
            private int currentX;
            private int currentY;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        currentX = (int) event.getRawX();
                        currentY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x2 = (int) event.getRawX();
                        int y2 = (int) event.getRawY();
                        container.scrollBy(currentX - x2, currentY - y2);
                        currentX = x2;
                        currentY = y2;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });

        //顯示群組2
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showGroup(2);

                //設定當下圖片
                int imagePic = imageList.get(viewPager.getCurrentItem());

                try {
                    Bitmap bm = OOMHandler.new_decode(SlidePageInstructionActivity.this, imagePic);

                    if(bm != null){
                        Log.v(TAG, String.format("# resize1 = w: %s, h : %s", bm.getWidth(), bm.getHeight()));
                        imageView1.setImageBitmap(bm);

                        int width = bm.getWidth();
                        int height = bm.getHeight();
                        DisplayMetrics dm = new DisplayMetrics();
                        SlidePageInstructionActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
                        int newHeight1 = dm.heightPixels;
                        float scaleHeight = ((float)newHeight1) / height;
                        int newWidth = (int) (scaleHeight * width);

                        imageView1.getLayoutParams().width = newWidth;
                        imageView1.getLayoutParams().height = newHeight1;

                        Log.v(TAG, String.format("# resize2 = w: %s, h : %s", newWidth, newHeight1));
                    }
                } catch (Throwable e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });
        //顯示群組1
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGroup(1);
            }
        });
    }

    /**
     * 顯示群組
     */
    private void showGroup(int group) {
        switch (group) {
            case 1:
                showGroup1.setVisibility(View.VISIBLE);
                showGroup2.setVisibility(View.GONE);
                break;
            case 2:
                showGroup1.setVisibility(View.GONE);
                showGroup2.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initZoomControls() {
        final ZoomControls zoomControl = (ZoomControls) findViewById(R.id.zoomControl);
        zoomControl.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomControl.setZoomSpeed(1000L);
                float scaleX = imageView1.getScaleX();
                scaleX += 0.1;
                if (scaleX > 4) {
                    scaleX = 4;
                }
                imageView1.setScaleX(scaleX);
                imageView1.setScaleY(scaleX);
            }
        });
        zoomControl.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomControl.setZoomSpeed(1000L);
                float scaleX = imageView1.getScaleX();
                scaleX -= 0.1;
                if (scaleX <= 0.3) {//原本是1
                    scaleX = 0.3f;
                }
                imageView1.setScaleX(scaleX);
                imageView1.setScaleY(scaleX);
            }
        });
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        MyPageAdapter pageAdapter = new MyPageAdapter(getSupportFragmentManager(), getFragments());
        //OldPageAdapter pageAdapter2 = new OldPageAdapter(this);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());
    }

    /**
     * Fragment
     */
    public static class MyFragment extends Fragment {

        public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

        int drawableResourceId = -1;

        public static final MyFragment newInstance(String message, int drawableResourceId) {
            MyFragment f = new MyFragment();
            Bundle bdl = new Bundle(1);
            bdl.putString(EXTRA_MESSAGE, message);
            f.setArguments(bdl);
            f.drawableResourceId = drawableResourceId;
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            String message = getArguments().getString(EXTRA_MESSAGE);
//            View v = inflater.inflate(R.layout.myfragment_layout, container, false);
//            TextView messageTextView = (TextView)v.findViewById(R.id.textView);
//            messageTextView.setText(message);
//            return v;
            ImageView v1 = new ImageView(this.getActivity());
//            v1.setImageDrawable(getResources().getDrawable(drawableResourceId));
//            v1.setImageResource(drawableResourceId);

            if (isDoResize) {
                Picasso.with(this.getActivity()).load(drawableResourceId).resize(720, 1280).noFade().into(v1);
            } else {
                Picasso.with(this.getActivity()).load(drawableResourceId).noFade().into(v1);
            }
            return v1;
        }
    }

    /**
     * 每頁對應的圖
     */
    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        List<Integer> imgList = imageList;
        for (int ii = 0; ii < imgList.size(); ii++) {
            fList.add(MyFragment.newInstance("Step " + (ii + 1), imgList.get(ii)));
        }
        return fList;
    }

    /**
     * PageAdapter
     */
    class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

    /**
     * PageAdapter
     */
    private class OldPageAdapter extends PagerAdapter {
        final List<View> viewPages = new ArrayList<View>();

        OldPageAdapter(Context context) {
            List<Integer> imgList = imageList;
            for (int ii = 0; ii < imgList.size(); ii++) {
                viewPages.add(createImage(imgList.get(ii), context));
            }
        }

        private ImageView createImage(int resId, Context context) {
            ImageView v1 = new ImageView(context);
//            v1.setImageDrawable(getResources().getDrawable());
//            v1.setImageResource(resId);

            if (isDoResize) {
                Picasso.with(context).load(resId).resize(720, 1280).into(v1);
            } else {
                Picasso.with(context).load(resId).into(v1);
            }
            return v1;
        }

        //獲取當前界面個數
        @Override
        public int getCount() {
            return viewPages.size();
        }

        //判斷是否由對象生成頁面
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewPages.get(position));
        }

        //返回一個對象，這個對象表明瞭PagerAdapter適配器選擇哪個對象放在當前的ViewPager中
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = viewPages.get(position);
            container.addView(view);
            return view;
        }
    }

    //初始化下麵的小圓點的方法
    private void initPointer(int size) {
        //有多少個界面就new多長的數組
        imageViews = new ImageView[size];
        for (int i = 0; i < imageViews.length; i++) {
            ImageView imageView = new ImageView(this);
            //設置控制項的寬高
            imageView.setLayoutParams(new ViewGroup.LayoutParams(25, 25));
            //設置控制項的padding屬性
            imageView.setPadding(20, 0, 20, 0);
            imageViews[i] = imageView;
            //初始化第一個page頁面的圖片的原點為選中狀態
            if (i == 0) {
                //表示當前圖片
                imageViews[i].setBackgroundResource(R.mipmap.page_indicator_focused);
                /**
                 * 在java代碼中動態生成ImageView的時候
                 * 要設置其BackgroundResource屬性才有效
                 * 設置ImageResource屬性無效
                 */
            } else {
                imageViews[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
            }
            group.addView(imageViews[i]);
        }
    }

    //ViewPager的onPageChangeListener監聽事件，當ViewPager的page頁發生變化的時候調用
    private class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        //頁面滑動完成後執行
        @Override
        public void onPageSelected(int position) {
            //判斷當前是在那個page，就把對應下標的ImageView原點設置為選中狀態的圖片
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[position].setBackgroundResource(R.mipmap.page_indicator_focused);
                if (position != i) {
                    imageViews[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
                }
            }
        }

        //監聽頁面的狀態，0--靜止  1--滑動   2--滑動完成
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}