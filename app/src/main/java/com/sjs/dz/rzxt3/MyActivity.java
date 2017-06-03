package com.sjs.dz.rzxt3;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.sjs.dz.rzxt3.Adapter.MyFragmentAdapter;
import com.sjs.dz.rzxt3.DB.PactInfo;
import com.sjs.dz.rzxt3.base.MyApplication;
import com.sjs.dz.rzxt3.utils.DepthPageTransformer;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class MyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,ViewPager.OnPageChangeListener , ViewFragment.OnFragmentInteractionListener {
    private String TAG = this.getClass().getSimpleName();
    private DbManager db;
    private MyApplication myApplication;
    private ViewPager viewPager;
    private  CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle("合同");//设置主标题
//        toolbar.setTitleTextColor(Color.rgb(8, 8, 8));
//        toolbar.setDrawingCacheBackgroundColor(Color.rgb(8, 8, 8));
//        toolbar.setSubtitleTextColor(Color.rgb(8, 8, 8));
        //侧边栏的按钮
//        toolbar.setNavigationIcon(R.mipmap.ic_back);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //启用返回按钮
        //浸透式状态栏
        initWindow();
        //侧边栏
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager=(ViewPager) findViewById(R.id.viewpager);
        //指示器
        indicator = (CircleIndicator) findViewById(R.id.indicator);


        //初始化数据
        initData();


    }

    private void initData() {
        myApplication=new MyApplication();
        db = x.getDb(myApplication.getDaoConfig());
        List<PactInfo> pactInfos = null;
        try {
           pactInfos = db.findAll(PactInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        if(pactInfos == null || pactInfos.size() == 0){
            return;//请先调用dbAdd()方法
        }
        else{
            Log.i(TAG,"pactInfos.size"+pactInfos.size());
                    initFragment(pactInfos);
        }

    }

    private void initFragment(List<PactInfo> pactInfos) {
        List<Fragment> list = new ArrayList<Fragment>();
        for(int i=0;i<pactInfos.size();i++){
            ViewFragment fragment0= new ViewFragment();
            list.add(fragment0);
        }


//        viewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener) this);//设置页面切换时的监听器(可选，用了之后要重写它的回调方法处理页面切换时候的事务)
        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), list));
        indicator.setViewPager(viewPager);
        //页面切换动画
        viewPager.setPageTransformer(true, new DepthPageTransformer());

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        if (id == R.id.nav_clear) {
            // Handle the camera action
        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_out) {

        }
//        else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
