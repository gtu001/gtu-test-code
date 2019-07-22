package com.example.mcdonald;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utils.RandomUtil;
import com.example.utils.StringUtilsEx;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends ActionBarActivity { //

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final SimpleDateFormat SEC_FORMAT = new SimpleDateFormat(" mm  :  ss ");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy 年 MM 月 dd 日  ");

    ImageView mainImageView;
    TextView textViewPic1;
    TextView textViewPic1_dayHours;
    TextView textViewPic5;
    TextView textViewSec;
    TextView textViewPic5Sec;
    Timer timer;

    FoodMenu tempFoodMenu = FoodMenu.burgers10Pork;


    private enum FoodMenu {
        burgers(R.id.item_burger, new int[]{R.drawable.burger01, R.drawable.burger02, R.drawable.burger03, R.drawable.burger04, R.drawable.burger05}), //
        burgers10Pork(R.id.item_burgers10Pork, new int[]{R.drawable.burger_port_10_01, R.drawable.burger_port_10_02, R.drawable.burger_port_10_03, R.drawable.burger_port_10_04, R.drawable.burger_port_10_05}), //
        burgersPorkTwo(R.id.item_burgersPorkTwo, new int[]{R.drawable.burger_pork_oneone_01, R.drawable.burger_pork_oneone_02, R.drawable.burger_pork_oneone_03, R.drawable.burger_pork_oneone_04, R.drawable.burger_pork_oneone_05}), //
        burgers10Egg(R.id.item_burgers10Egg, new int[]{R.drawable.burger_egg10_01, R.drawable.burger_egg10_02, R.drawable.burger_egg10_03, R.drawable.burger_egg10_04, R.drawable.burger_egg10_05}), //
        burgers10Fish(R.id.item_burgerFish, new int[]{R.drawable.burger_fish_01, R.drawable.burger_fish_02, R.drawable.burger_fish_03, R.drawable.burger_fish_04, R.drawable.burger_fish_05}), //
        burgers10Chicken(R.id.item_burger10Chicken, new int[]{R.drawable.burger_chicken10_01, R.drawable.burger_chicken10_02, R.drawable.burger_chicken10_03, R.drawable.burger_chicken10_04, R.drawable.burger_chicken10_05}), //
        burgers33(R.id.item_burgers33, new int[]{R.drawable.burger_33_01, R.drawable.burger_33_02, R.drawable.burger_33_03, R.drawable.burger_33_04, R.drawable.burger_33_05}), //
        burgerPorkEgg(R.id.item_burgerPorkEgg, new int[]{R.drawable.burger_pork_egg_01, R.drawable.burger_pork_egg_02, R.drawable.burger_pork_egg_03, R.drawable.burger_pork_egg_04, R.drawable.burger_pork_egg_05}), //
        burgersEgg(R.id.item_burgersEgg, new int[]{R.drawable.burger_egg01, R.drawable.burger_egg02, R.drawable.burger_egg03, R.drawable.burger_egg04, R.drawable.burger_egg05}), //
        burgerChicken33(R.id.item_burgerChicken33, new int[]{R.drawable.burger_chicken_33_01, R.drawable.burger_chicken_33_02, R.drawable.burger_chicken_33_03, R.drawable.burger_chicken_33_04, R.drawable.burger_chicken_33_05}), //
        burgers40(R.id.item_burgers40, new int[]{R.drawable.burger40_01, R.drawable.burger40_02, R.drawable.burger40_03, R.drawable.burger40_04, R.drawable.burger40_05}), //
        bigmacOneone(R.id.item_bigmac_oneone, new int[]{R.drawable.bigmac_oneone_01, R.drawable.bigmac_oneone_02, R.drawable.bigmac_oneone_03, R.drawable.bigmac_oneone_04, R.drawable.bigmac_oneone_05}), //
        burgersTwo(R.id.item_burgersTwo, new int[]{R.drawable.burger_two_01, R.drawable.burger_two_02, R.drawable.burger_two_03, R.drawable.burger_two_04, R.drawable.burger_two_05}), //
        burgersChickenTwo(R.id.item_burgersChickenTwo, new int[]{R.drawable.burger_roast_chicken_01, R.drawable.burger_roast_chicken_02, R.drawable.burger_roast_chicken_03, R.drawable.burger_roast_chicken_04, R.drawable.burger_roast_chicken_05}), //
        chickens(R.id.item_chicken, new int[]{R.drawable.chicken01, R.drawable.chicken02, R.drawable.chicken03, R.drawable.chicken04, R.drawable.chicken05}), //
        bbq(R.id.item_bbq, new int[]{R.drawable.bbq01, R.drawable.bbq02, R.drawable.bbq03, R.drawable.bbq04, R.drawable.bbq05}), //
        nugget(R.id.item_nugget, new int[]{R.drawable.nugget01, R.drawable.nugget02, R.drawable.nugget03, R.drawable.nugget04, R.drawable.nugget05}), //
        nugget2(R.id.item_nugget2, new int[]{R.drawable.nugget_two_01, R.drawable.nugget_two_02, R.drawable.nugget_two_03, R.drawable.nugget_two_04, R.drawable.nugget_two_05}), //
        gpei(R.id.item_gpei, new int[]{R.drawable.gpei_01, R.drawable.gpei_02, R.drawable.gpei_03, R.drawable.gpei_04, R.drawable.gpei_05}), //
        frenchfries(R.id.item_frenchfries, new int[]{R.drawable.frenchfries01, R.drawable.frenchfries02, R.drawable.frenchfries03, R.drawable.frenchfries04, R.drawable.frenchfries05}), //
        frenchfryBig(R.id.item_frenchfryBig, new int[]{R.drawable.frenchfry_big01, R.drawable.frenchfry_big02, R.drawable.frenchfry_big03, R.drawable.frenchfry_big04, R.drawable.frenchfry_big05}), //
        icecream(R.id.item_icecream, new int[]{R.drawable.icecream01, R.drawable.icecream02, R.drawable.icecream03, R.drawable.icecream04, R.drawable.icecream05}), //
        icecream2(R.id.item_icecream2, new int[]{R.drawable.icecream20_01, R.drawable.icecream20_02, R.drawable.icecream20_03, R.drawable.icecream20_04, R.drawable.icecream20_05}), //
        icecreamTea(R.id.item_icecreamTea, new int[]{R.drawable.icecream_tea_01, R.drawable.icecream_tea_02, R.drawable.icecream_tea_03, R.drawable.icecream_tea_04, R.drawable.icecream_tea_05}), //
        coffee(R.id.item_coffee, new int[]{R.drawable.coffee_01, R.drawable.coffee_02, R.drawable.coffee_03, R.drawable.coffee_04, R.drawable.coffee_05}), //
        potato(R.id.item_potato, new int[]{R.drawable.potato01, R.drawable.potato02, R.drawable.potato03, R.drawable.potato04, R.drawable.potato05}), //
        potatoOne(R.id.item_potatoOne, new int[]{R.drawable.potato_one_01, R.drawable.potato_one_02, R.drawable.potato_one_03, R.drawable.potato_one_04, R.drawable.potato_one_05}), //
        bigDrink(R.id.item_bigDrink, new int[]{R.drawable.big_drink01, R.drawable.big_drink02, R.drawable.big_drink03, R.drawable.big_drink04, R.drawable.big_drink05}), //
        f_potatoOne(R.id.item_f_potatoOne, new int[]{R.drawable.f_potato_one_01, R.drawable.f_potato_one_02, R.drawable.f_potato_one_03, R.drawable.f_potato_one_04, R.drawable.f_potato_one_05}), //
        f_icecream(R.id.item_f_icecream, new int[]{R.drawable.f_icecream01, R.drawable.f_icecream02, R.drawable.f_icecream03, R.drawable.f_icecream04, R.drawable.f_icecream05}), //
        f_burgersChickenTwo(R.id.item_f_burgersChickenTwo, new int[]{R.drawable.f_burger_roast_chicken_01, R.drawable.f_burger_roast_chicken_02, R.drawable.f_burger_roast_chicken_03, R.drawable.f_burger_roast_chicken_04, R.drawable.f_burger_roast_chicken_05}), //
        f_nugget2(R.id.item_f_nugget2, new int[]{R.drawable.f_nugget01, R.drawable.f_nugget02, R.drawable.f_nugget03, R.drawable.f_nugget04, R.drawable.f_nugget05}), //
        f_frenchfryBig(R.id.item_f_frenchfryBig, new int[]{R.drawable.f_frenchfry_big01, R.drawable.f_frenchfry_big02, R.drawable.f_frenchfry_big03, R.drawable.f_frenchfry_big04, R.drawable.f_frenchfry_big05}), //
        f_bigBreakFast(R.id.item_f_bigBreakfast, new int[]{R.drawable.f_big_breakfast_01, R.drawable.f_big_breakfast_02, R.drawable.f_big_breakfast_03, R.drawable.f_big_breakfast_04, R.drawable.f_big_breakfast_05}), //
        f_applePie(R.id.item_f_applePie, new int[]{R.drawable.f_apple_pie_01, R.drawable.f_apple_pie_02, R.drawable.f_apple_pie_03, R.drawable.f_apple_pie_04, R.drawable.f_apple_pie_05}), //
        f_frenchfries(R.id.item_f_frenchfries, new int[]{R.drawable.f_frenchfries01, R.drawable.f_frenchfries02, R.drawable.f_frenchfries03, R.drawable.f_frenchfries04, R.drawable.f_frenchfries05}), //
        f_chicken(R.id.item_f_chicken, new int[]{R.drawable.f_chicken01, R.drawable.f_chicken02, R.drawable.f_chicken03, R.drawable.f_chicken04, R.drawable.f_chicken05}), //
        f_bigmacOneone(R.id.item_f_bigmacOneone, new int[]{R.drawable.f_bigmac_oneone_01, R.drawable.f_bigmac_oneone_02, R.drawable.f_bigmac_oneone_03, R.drawable.f_bigmac_oneone_04, R.drawable.f_bigmac_oneone_05}), //
        f_burgerFish40(R.id.item_f_burgerFish40, new int[]{R.drawable.f_burger_fish_40_01, R.drawable.f_burger_fish_40_02, R.drawable.f_burger_fish_40_03, R.drawable.f_burger_fish_40_04, R.drawable.f_burger_fish_40_05}), //
        f_bbqChickenWing(R.id.item_f_bbqChickenWing, new int[]{R.drawable.f_bbq_chicken_wing_01, R.drawable.f_bbq_chicken_wing_02, R.drawable.f_bbq_chicken_wing_03, R.drawable.f_bbq_chicken_wing_04, R.drawable.f_bbq_chicken_wing_05}), //
        f_bigMicTwo(R.id.item_f_bigMicTwo, new int[]{R.drawable.f_big_mic_two_01, R.drawable.f_big_mic_two_02, R.drawable.f_big_mic_two_03, R.drawable.f_big_mic_two_04, R.drawable.f_big_mic_two_05}), //
        f_chickenFiredBurgerTwo(R.id.item_f_chickenFiredBurgerTwo, new int[]{R.drawable.f_chicken_fired_burger_two_01, R.drawable.f_chicken_fired_burger_two_02, R.drawable.f_chicken_fired_burger_two_03, R.drawable.f_chicken_fired_burger_two_04, R.drawable.f_chicken_fired_burger_two_05}), //
        f_redteaTwo(R.id.item_f_redteaTwo, new int[]{R.drawable.f_redtea_01, R.drawable.f_redtea_02, R.drawable.f_redtea_03, R.drawable.f_redtea_04, R.drawable.f_redtea_05}), //
        f_milkteaTen(R.id.item_f_milkteaTen, new int[]{R.drawable.f_milktea_ten_01, R.drawable.f_milktea_ten_02, R.drawable.f_milktea_ten_03, R.drawable.f_milktea_ten_04, R.drawable.f_milktea_ten_05}), //
        f_chickenWingTwo(R.id.item_f_chickenWingTwo, new int[]{R.drawable.f_chicken_wing_oneone_01, R.drawable.f_chicken_wing_oneone_02, R.drawable.f_chicken_wing_oneone_03, R.drawable.f_chicken_wing_oneone_04, R.drawable.f_chicken_wing_oneone_05}), //
        f_gpei(R.id.item_f_gpei, new int[]{R.drawable.f_gpie_001, R.drawable.f_gpie_002, R.drawable.f_gpie_003, R.drawable.f_gpie_004, R.drawable.f_gpie_005}), //
        f_potato(R.id.item_f_potato, new int[]{R.drawable.f_potato_001, R.drawable.f_potato_002, R.drawable.f_potato_003, R.drawable.f_potato_004, R.drawable.f_potato_005}), //
        f_blackTea(R.id.item_f_blackTea, new int[]{R.drawable.f_blacktea_001, R.drawable.f_blacktea_002, R.drawable.f_blacktea_003, R.drawable.f_blacktea_004, R.drawable.f_blacktea_005}), //
        f_fiber_coke(R.id.item_f_fiberCoke, new int[]{R.drawable.f_fiber_coke_01, R.drawable.f_fiber_coke_02, R.drawable.f_fiber_coke_03, R.drawable.f_fiber_coke_04, R.drawable.f_fiber_coke_05}), //
        f_latte(R.id.item_f_latte, new int[]{R.drawable.f_latte_01, R.drawable.f_latte_02, R.drawable.f_latte_03, R.drawable.f_latte_04, R.drawable.f_latte_05}), //
        f_nugget_new(R.id.item_f_nugget_new, new int[]{R.drawable.f_nugget_new_01, R.drawable.f_nugget_new_02, R.drawable.f_nugget_new_03, R.drawable.f_nugget_new_04, R.drawable.f_nugget_new_05}), //
        f_shrimp_burger(R.id.item_f_shrimpBurger, new int[]{R.drawable.f_shrimp_burger_01, R.drawable.f_shrimp_burger_02, R.drawable.f_shrimp_burger_03, R.drawable.f_shrimp_burger_04, R.drawable.f_shrimp_burger_05}), //
        f_spicywing(R.id.item_f_spicyWing,  new int[]{R.drawable.f_spicywing_01, R.drawable.f_spicywing_02, R.drawable.f_spicywing_03, R.drawable.f_spicywing_04, R.drawable.f_spicywing_05}), //
        ;

        final int menuId;
        final int[] pics;

        FoodMenu(int menuId, int[] pics) {
            this.pics = pics;
            this.menuId = menuId;
        }
    }

    int picIndex = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // requestWindowFeature(Window.FEATURE_NO_TITLE);

        removeStatusBarAndNavigation();

        setStatusBarAndNavigationBarColor();

        FrameLayout layout = createContentView();

        mainImageView = new ImageView(this);
        textViewPic1 = new TextView(this);
        textViewPic1_dayHours = new TextView(this);
        textViewSec = new TextView(this);
        textViewPic5 = new TextView(this);
        textViewPic5Sec = new TextView(this);

        initMainImageView(mainImageView, layout);

        initSystemDateForPic1(textViewPic1, textViewPic1_dayHours, layout);
        initSystemDateForPicSec(textViewSec, layout);

        initSystemDateForPic5(textViewPic5, textViewPic5Sec, layout);

        initActionBar();

        overlayRedImageStatueBar(layout);
    }

    /**
     * 初始化位置與字形
     */
    private void applyPositionAndSize() {
        //↓↓↓↓↓↓↓ 第一頁
        if (!tempFoodMenu.name().startsWith("f_")) {
            //系統日
            textViewPic1.setBackgroundColor(Color.parseColor("#00519d"));
            textViewPic1.setTextColor(Color.parseColor("#FFFFFF"));
            textViewPic1.setTextSize(16.3f);//大台手機
            FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        param.setMargins(180, 1060, 0, 0);//SonyZ1大台手機
//        param.setMargins(120, 707, 0, 0);//SonyZ1小台手機
            param.setMargins(180, 1060, 0, 0);//紅米note4x手機
            textViewPic1.setLayoutParams(param);

            //倒數日和時
            textViewPic1_dayHours.setBackgroundColor(Color.parseColor("#00519d"));
            textViewPic1_dayHours.setTextColor(Color.parseColor("#FFFFFF"));
            textViewPic1_dayHours.setTextSize(16.0f);//大台手機
            FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        param2.setMargins(795, 1060, 0, 0);//SonyZ1大台手機
//        param2.setMargins(530, 707, 0, 0);//SonyZ1小台手機
            param2.setMargins(795, 1060, 0, 0);//紅米note4x手機
            textViewPic1_dayHours.setLayoutParams(param2);
        } else {
            //系統日
            textViewPic1.setBackgroundColor(Color.parseColor("#00519d"));
            textViewPic1.setTextColor(Color.parseColor("#FFFFFF"));
            textViewPic1.setTextSize(15.5f);//大台手機
            FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            param.setMargins(210, 1045, 0, 0);//紅米note4x手機
            textViewPic1.setLayoutParams(param);

            //倒數日和時
            textViewPic1_dayHours.setBackgroundColor(Color.parseColor("#00519d"));
            textViewPic1_dayHours.setTextColor(Color.parseColor("#FFFFFF"));
            textViewPic1_dayHours.setTextSize(15.5f);//大台手機
            FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            param2.setMargins(745, 1045, 0, 0);//紅米note4x手機
            textViewPic1_dayHours.setLayoutParams(param2);
        }
        //↑↑↑↑↑↑↑　第一頁


        //↓↓↓↓↓↓↓ 第3頁
        if (!tempFoodMenu.name().startsWith("f_")) {
            textViewSec.setText(getDefaultSecStr());
            textViewSec.setBackgroundColor(Color.parseColor("#ffa500"));
            textViewSec.setTextColor(Color.parseColor("#FFFFFF"));
            textViewSec.setTextSize(24.3f);//大台手機
            FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        param.setMargins(638, 1585, 0, 0);//SonyZ1大台手機
//        param.setMargins(410, 1055, 0, 0);//SonyZ1小台手機
            param.setMargins(638, 1585, 0, 0);//紅米note4x手機
            textViewSec.setLayoutParams(param);
        } else {
            textViewSec.setText(getDefaultSecStr());
            textViewSec.setBackgroundColor(Color.parseColor("#ffa500"));
            textViewSec.setTextColor(Color.parseColor("#FFFFFF"));
            textViewSec.setTextSize(24.3f);//大台手機
            FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            param.setMargins(610, 1725, 0, 0);//紅米note4x手機
            textViewSec.setLayoutParams(param);
        }
        //↑↑↑↑↑↑↑　第3頁

        //↓↓↓↓↓↓↓ 第5頁
        if (!tempFoodMenu.name().startsWith("f_")) {
            textViewPic5.setBackgroundColor(Color.parseColor("#6f92c2"));
            textViewPic5.setTextColor(Color.parseColor("#FFFFFF"));
            textViewPic5.setTextSize(16.3f);//大台手機
            FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        param.setMargins(180, 1060, 0, 0);//SonyZ1大台手機
//        param.setMargins(120, 707, 0, 0);//SonyZ1小台手機
            param.setMargins(180, 1060, 0, 0);//紅米note4x手機
            textViewPic5.setLayoutParams(param);

            textViewPic5Sec.setText(getDefaultSecStr());
            textViewPic5Sec.setBackgroundColor(Color.parseColor("#FFFFFF"));
            textViewPic5Sec.setTextColor(Color.parseColor("#000000"));
            textViewPic5Sec.setTextSize(23.5f);//大台手機
            FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        param2.setMargins(450, 1185, 0, 0);//大台手機
//        param2.setMargins(300, 787, 0, 0);//小台手機
            param2.setMargins(450, 1185, 0, 0);//紅米note4x手機
            textViewPic5Sec.setLayoutParams(param2);
        } else {
            textViewPic5.setBackgroundColor(Color.parseColor("#6f92c2"));
            textViewPic5.setTextColor(Color.parseColor("#FFFFFF"));
            textViewPic5.setTextSize(15.5f);//大台手機
            FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            param.setMargins(220, 1045, 0, 0);//紅米note4x手機
            textViewPic5.setLayoutParams(param);

            textViewPic5Sec.setText(getDefaultSecStr());
            textViewPic5Sec.setBackgroundColor(Color.parseColor("#FFFFFF"));
            textViewPic5Sec.setTextColor(Color.parseColor("#000000"));
            textViewPic5Sec.setTextSize(23.5f);//大台手機
            FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            param2.setMargins(450, 1160, 0, 0);//紅米note4x手機
            textViewPic5Sec.setLayoutParams(param2);
        }
        //↑↑↑↑↑↑↑　第5頁
    }


    /**
     * 覆蓋一塊紅色狀態欄在上面
     */
    private void overlayRedImageStatueBar(FrameLayout layout) {
        ImageView redImageView = new ImageView(this);
        layout.addView(redImageView, //
                new LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT));
        redImageView.setBackgroundColor(Color.rgb(221, 20, 2));

        redImageView.setTop(0);
        redImageView.setLeft(0);

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        redImageView.getLayoutParams().height = 70;
        redImageView.getLayoutParams().width = width;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getDrawable(R.drawable.transparent));
        actionBar.setTitle("");
        actionBar.hide();
    }

    /**
     * 設定倒數時間
     */
    private void resetPic1Data() {
        String rearfix = "";
        if (!tempFoodMenu.name().startsWith("f_")) {
            rearfix = "";
        } else {
            rearfix = "    ";
        }
        int totalHours = RandomUtil.rangeInteger(6, 47);
        int days = totalHours / 24;
        int hours = totalHours % 24;
        String dateStr = getSystemDateStr(days, hours);
        textViewPic1.setText(dateStr);
        textViewPic1_dayHours.setText(days + " 天 " + StringUtilsEx.leftPad(String.valueOf(hours), 2, ' ') + " 小時" + rearfix);
        textViewPic5.setText(dateStr);
    }

    /**
     * 第一張圖片系統日 TODO
     */
    private void initSystemDateForPic1(final TextView textview, final TextView textviewDayHours, FrameLayout layout) {
        //系統日
        textview.setBackgroundColor(Color.parseColor("#00519d"));
        textview.setTextColor(Color.parseColor("#FFFFFF"));
        textview.setTextSize(16.3f);//大台手機
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        param.setMargins(180, 1060, 0, 0);//SonyZ1大台手機
//        param.setMargins(120, 707, 0, 0);//SonyZ1小台手機
        param.setMargins(180, 1060, 0, 0);//紅米note4x手機
        textview.setLayoutParams(param);
        layout.addView(textview);

        //倒數日和時
        textviewDayHours.setBackgroundColor(Color.parseColor("#00519d"));
        textviewDayHours.setTextColor(Color.parseColor("#FFFFFF"));
        textviewDayHours.setTextSize(16.0f);//大台手機
        FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        param2.setMargins(795, 1060, 0, 0);//SonyZ1大台手機
//        param2.setMargins(530, 707, 0, 0);//SonyZ1小台手機
        param2.setMargins(795, 1060, 0, 0);//紅米note4x手機
        textviewDayHours.setLayoutParams(param2);
        layout.addView(textviewDayHours);
        //初始化預設值
        resetPic1Data();
    }

    /**
     * 第3張圖片讀秒文字方塊 TODO
     */
    private void initSystemDateForPicSec(final TextView textview, FrameLayout layout) {
        textview.setText(getDefaultSecStr());
        textview.setBackgroundColor(Color.parseColor("#ffa500"));
        textview.setTextColor(Color.parseColor("#FFFFFF"));
        textview.setTextSize(24.3f);//大台手機
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        param.setMargins(638, 1585, 0, 0);//SonyZ1大台手機
//        param.setMargins(410, 1055, 0, 0);//SonyZ1小台手機
        param.setMargins(638, 1585, 0, 0);//紅米note4x手機
        textview.setLayoutParams(param);
        layout.addView(textview);
    }

    /**
     * 第5張圖片系統日 TODO
     */
    private void initSystemDateForPic5(final TextView textview, final TextView textviewSec, FrameLayout layout) {
        textview.setBackgroundColor(Color.parseColor("#6f92c2"));
        textview.setTextColor(Color.parseColor("#FFFFFF"));
        textview.setTextSize(16.3f);//大台手機
        FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        param.setMargins(180, 1060, 0, 0);//SonyZ1大台手機
//        param.setMargins(120, 707, 0, 0);//SonyZ1小台手機
        param.setMargins(180, 1060, 0, 0);//紅米note4x手機
        textview.setLayoutParams(param);
        layout.addView(textview);

        textviewSec.setText(getDefaultSecStr());
        textviewSec.setBackgroundColor(Color.parseColor("#FFFFFF"));
        textviewSec.setTextColor(Color.parseColor("#000000"));
        textviewSec.setTextSize(23.5f);//大台手機
        FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        param2.setMargins(450, 1185, 0, 0);//大台手機
//        param2.setMargins(300, 787, 0, 0);//小台手機
        param2.setMargins(450, 1185, 0, 0);//紅米note4x手機
        textviewSec.setLayoutParams(param2);
        layout.addView(textviewSec);
    }

    /**
     * 取得系統日 , 此處還剩一天11小時所以要加上
     */
    private String getSystemDateStr(int day, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);//tempFoodMenu.day
        cal.add(Calendar.HOUR, hours);//tempFoodMenu.hours

        if (!tempFoodMenu.name().startsWith("f_")) {
            return DATE_FORMAT.format(cal.getTime());
        } else {
            String rtnVal = cal.get(Calendar.YEAR) + " 年 " + (cal.get(Calendar.MONTH) + 1) + " 月 " + cal.get(Calendar.DATE) + " 日";
            for (int ii = 0, fill = 23 - rtnVal.getBytes().length; ii < fill; ii++) { //25
                rtnVal += " ";
            }
            return rtnVal;
        }
    }

    /**
     * 初始化圖片
     */
    private void initMainImageView(final ImageView v1, FrameLayout layout) {
        layout.addView(v1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        v1.setScaleType(ScaleType.FIT_XY);
        v1.setAdjustViewBounds(true);
        // v1.setImageDrawable(this.getResources().getDrawable(getNextBurgerRes()));
        v1.setImageResource(getNextPicturesGroup(0));
        v1.setClickable(true);
        v1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // recycleImageView(v1);
                // v1.setImageDrawable(MainActivity.this.getResources().getDrawable(getNextBurgerRes()));
                v1.setImageResource(getNextPicturesGroup(null));
            }
        });

        v1.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar.isShowing()) {
                    actionBar.hide();
                } else {
                    actionBar.show();
                }
            }
        });
    }

    /**
     * 釋放記憶體
     */
    private void recycleImageView(ImageView imageView) {
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        } catch (Throwable ex) {
            Log.e(TAG, "recycleImageView : " + ex.getMessage());
        }
    }

    /**
     * 設定顯示或隱藏文字方塊
     */
    private void setTextviewVisiable() {
        View[] hiddenGroup = new View[]{textViewPic1, textViewPic1_dayHours, textViewSec, textViewPic5, textViewPic5Sec};
        for (View v : hiddenGroup) {
            v.setVisibility(View.GONE);
        }
        switch (picIndex) {
            case 0:
                textViewPic1.setVisibility(View.VISIBLE);
                textViewPic1_dayHours.setVisibility(View.VISIBLE);
                break;
            case 3:
                textViewSec.setVisibility(View.VISIBLE);
                setTextviewSecTimerGo(new TextView[]{textViewSec, textViewPic5Sec});
                break;
            case 4:
                textViewPic5.setVisibility(View.VISIBLE);
                textViewPic5Sec.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 設定計時器文字方塊倒數
     */
    private void setTextviewSecTimerGo(final TextView[] textviews) {
        final SimpleDateFormat sdf2 = new SimpleDateFormat("mmss");
        final Handler hander = new Handler();
        if (timer != null) {
            timer.cancel();
        }
        for (TextView v : textviews) {
            v.setText(getDefaultSecStr());
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                hander.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Date d = SEC_FORMAT.parse(textviews[0].getText().toString());
                            d = new Date(d.getTime() - 1000);
                            String ver = SEC_FORMAT.format(d);
                            for (TextView v : textviews) {
                                v.setText(ver);
                            }

                            // 時間到
                            if ("0000".equals(sdf2.format(d))) {
                                Log.v(TAG, "##STOP##");
                                timer.cancel();
                            }
                        } catch (ParseException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                });
            }
        }, 0L, 1000L);
    }

    /**
     * 取得預設倒數分秒
     */
    private String getDefaultSecStr() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 2);
        cal.set(Calendar.SECOND, 0);
        return SEC_FORMAT.format(cal.getTime());
    }

    /**
     * 取得下一個漢堡圖
     */
    private int getNextPicturesGroup(Integer indicateIndex) {
        if (indicateIndex != null) {
            picIndex = indicateIndex;
        }
        if (picIndex < 0 || picIndex > tempFoodMenu.pics.length - 1) {
            picIndex = 0;
        }
        int rtn = tempFoodMenu.pics[picIndex];

        // 此時決定是否要隱藏顯示textview
        setTextviewVisiable();

        picIndex++;

        Log.v(TAG, "pic : " + rtn + ", pciIndex - " + picIndex);
        return rtn;
    }

    /**
     * 移除狀態列
     */
    private void removeStatusBarAndNavigation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 設定系統列和底下home鑑列 顏色
     */
    private void setStatusBarAndNavigationBarColor() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

        // 设置一个颜色给系统栏
        tintManager.setTintColor(Color.parseColor("#F60000"));
        // 设置一个样式背景给导航栏
        tintManager.setNavigationBarTintResource(R.drawable.black);
        // 设置一个状态栏资源
        tintManager.setStatusBarTintDrawable(getResources().getDrawable(R.drawable.red));
    }

    private FrameLayout createContentView() {
        FrameLayout layout = new FrameLayout(this);
        ScrollView scroll = new ScrollView(this);
        setContentView(scroll);
        // layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout, //
                new LayoutParams(LayoutParams.FILL_PARENT, // 設定與螢幕同寬
                        LayoutParams.WRAP_CONTENT));// 高度隨內容而定
        return layout;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        for (FoodMenu e : FoodMenu.values()) {
            if (e.menuId == id) {
                tempFoodMenu = e;
                break;
            }
        }

        // 初始化第一張圖狀態
        mainImageView.setImageResource(getNextPicturesGroup(0));
        // 初始化日期方塊狀態
        resetPic1Data();

        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();

        //初始化位置與字形
        applyPositionAndSize();
        return super.onOptionsItemSelected(item);
    }
}
