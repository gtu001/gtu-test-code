package com.example.gtu001.qrcodemaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.gtu001.qrcodemaker.common.Log;
import com.example.gtu001.qrcodemaker.common.Mp3BroadcastReceiver;
import com.example.gtu001.qrcodemaker.common.ServiceUtil;
import com.example.gtu001.qrcodemaker.common.WindowChangeDetectingService;
import com.example.gtu001.qrcodemaker.services.UrlPlayerService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //重新導向至youtube
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(YoutubePlayerActivity.YOUTUBE_KEY)) {
            gotoActivity(YoutubePlayerActivity.class, getIntent());
        }

        //啟動service
        ServiceUtil.startStopService(true, this, WindowChangeDetectingService.class);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_qrcode_maker) {
            gotoActivity(QRCodeMackerActivity.class, null);

        } else if (id == R.id.nav_processes_loader) {
            gotoActivity(ProcessesActivity.class, null);

        } else if (id == R.id.nav_mp3_player) {
            gotoActivity(Mp3PlayerActivity.class, null);

        } else if (id == R.id.youtube_player) {
            gotoActivity(YoutubePlayerActivity.class, null);

        } else if (id == R.id.app_list_filter) {
            gotoActivity(AppListFilterActivity.class, null);

        } else if (id == R.id.big_file_list) {
            gotoActivity(BigFileActivity.class, null);

        } else if (id == R.id.html_loader) {
            gotoActivity(HtmlLoaderActivity.class, null);

        } else if (id == R.id.nintendo_switch_checker) {
            gotoActivity(NintendoSwitchCheckerActivity.class, null);

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void gotoActivity(Class clz, Intent intent2) {
        Intent intent = new Intent();
        if (intent2 != null) {
            intent.putExtras(intent2);
        }
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        intent.setClass(this, clz);
        startActivity(intent);
    }
}
