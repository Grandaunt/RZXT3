package com.sjs.dz.rzxt3;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.sjs.dz.rzxt3.Adapter.MyFragmentAdapter;
import com.sjs.dz.rzxt3.DB.PactInfo;
import com.sjs.dz.rzxt3.DB.ServerBean;
import com.sjs.dz.rzxt3.DB.XDBManager;
import com.sjs.dz.rzxt3.base.MyApplication;
import com.sjs.dz.rzxt3.service.LocationService;
import com.sjs.dz.rzxt3.utils.DepthPageTransformer;
import com.sjs.dz.rzxt3.utils.checkUpdateUtils;
import com.sjs.dz.rzxt3.view.ActionSheetDialog;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import in.srain.cube.views.ptr.PtrFrameLayout;
import me.relex.circleindicator.CircleIndicator;

public class MyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,ViewPager.OnPageChangeListener , ViewFragment.OnFragmentInteractionListener {
    private String TAG = this.getClass().getSimpleName();
//    private String URL="http://172.16.10.242:8080";
    private DbManager db;
    private MyApplication myApplication;
    private ViewPager viewPager;
    private  CircleIndicator indicator;
    private SharedPreferences sharedPrefs;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_CROP = 1002;
    private final int REQUEST_CODE_EDIT = 1003;
    private List<PactInfo> pactInfos = null;
    private String userAccount="",nowVersion="",passWord="";
    private ImageView headicon;
    private ProgressDialog progressDialog,pDialog;
    private  Toolbar toolbar;
    private PtrFrameLayout mPtrFrame;
    private ServerBean serverBean;
    private LocationService locationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        //浸透式状态栏
        initWindow();
        initView();
        //初始化数据
        initData();
//       getGPSTIMEStr();


    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle("合同");//设置主标题
//        toolbar.setTitleTextColor(Color.rgb(8, 8, 8));
//        toolbar.setDrawingCacheBackgroundColor(Color.rgb(8, 8, 8));
//        toolbar.setSubtitleTextColor(Color.rgb(8, 8, 8));
        //侧边栏的按钮
//        toolbar.setNavigationIcon(R.mipmap.ic_back);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //启用返回按钮
        //侧边栏
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //获取侧边栏头部view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        headicon = (ImageView)headerView.findViewById(R.id.headicon);
        //给侧边栏头部username，phone赋值，头像添加点击事件


        //显示用户名
        sharedPrefs = getSharedPreferences("RZ3Share", Context.MODE_PRIVATE);
        TextView usernametv = (TextView)headerView.findViewById(R.id.username);
        usernametv.setText(sharedPrefs.getString("USER_NAME", "110"));
        //查询并显示用户头像
        String drawablepath= sharedPrefs.getString("USER_ICON","");
        ImageOptions options = new ImageOptions.Builder()
                //设置圆形
                .setCircular(true)
                //设置加载过程中的图片
                .setLoadingDrawableId(R.mipmap.ic_launcher)
//设置加载失败后的图片
                .setFailureDrawableId(R.mipmap.ic_launcher)
                //某些手机拍照时图片自动旋转，设置图片是否自动旋转为正
                .setAutoRotate(true)
                //等比例缩放居中显示
                .setImageScaleType(ImageView.ScaleType.FIT_XY)
                .build();
        x.image().bind(headicon, drawablepath, options);

        headicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"头像点击");
                showSheetDialog();
            }
        });

        viewPager=(ViewPager) findViewById(R.id.viewpager);
        //指示器
        indicator = (CircleIndicator) findViewById(R.id.indicator);
//        pullRefresh();

    }

    private void initData() {
//        myApplication=new MyApplication();
//        db = x.getDb(myApplication.getDaoConfig());
         db = x.getDb(XDBManager.getDaoConfig());
        String name=db.getDaoConfig().getDbName().toString();
        Log.i(TAG,"initData.db_addr"+name);

        try {
//           pactInfos = db.findAll(PactInfo.class);
            pactInfos = db.selector(PactInfo.class)
                    .where("pact_status","=",0)
                    .findAll();
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


    //头像选择
    private void showSheetDialog() {

        new ActionSheetDialog(this).builder()
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.DEFAULT,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
//                                Toast.makeText(MainActivity.this,"paizhao",Toast.LENGTH_LONG).show();
                                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, mOnHanlderResultCallback);


                            }
                        })
                .addSheetItem("相册", ActionSheetDialog.SheetItemColor.DEFAULT,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                            }
                        }).show();
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {

        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {


            ImageOptions options = new ImageOptions.Builder()
                    //设置圆形
                    .setCircular(true)
                    //某些手机拍照时图片自动旋转，设置图片是否自动旋转为正
                    .setAutoRotate(true)
                    //等比例缩放居中显示
                    .setImageScaleType(ImageView.ScaleType.FIT_XY)
                    .build();

            if (resultList != null) {
                //获取图片路径
                String photoPath= resultList.get(0).getPhotoPath();
                x.image().bind(headicon,photoPath, options);

                //存储头像路径到数据库
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("USER_ICON",photoPath);
                editor.commit();

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(MyActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    private void initFragment(List<PactInfo> pactInfos) {
        List<Fragment> list = new ArrayList<Fragment>();
        for(int i=0;i<pactInfos.size();i++){
//            ViewFragment fragment0= new ViewFragment();
            Fragment fragment0 = ViewFragment.newInstance(i);
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
        //toolbar搜索
        MenuItem searchViewButton = (MenuItem) menu.findItem(R.id.ab_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewButton);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG,"onQueryTextSubmit"+query);
                try {
                    pactInfos.clear();
                    pactInfos = db.selector(PactInfo.class)
                            .where("pact_no","%",query)
                            .and("pact_name","%",query)
                            .and("pact_start_date","%",query)
                            .and("pact_end_date","%",query)
                            .and("pact_com_con_tel","%",query)
                            .findAll();
                } catch (DbException e) {
                    e.printStackTrace();
                }

                if(pactInfos == null || pactInfos.size() == 0){

                }
                else{
                    Log.i(TAG,"pactInfos.size"+pactInfos.size());
                    initFragment(pactInfos);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG,"onQueryTextChange"+newText);

                try {
                    pactInfos.clear();
                    pactInfos = db.selector(PactInfo.class)
                            .where("pact_no","%",newText)
                            .and("pact_name","%",newText)
                            .and("pact_start_date","%",newText)
                            .and("pact_end_date","%",newText)
                            .and("pact_com_con_tel","%",newText)
                            .findAll();
                } catch (DbException e) {
                    e.printStackTrace();
                }

                if(pactInfos == null || pactInfos.size() == 0){

                }
                else{
                    Log.i(TAG,"pactInfos.size"+pactInfos.size());
                    initFragment(pactInfos);
                }

                return false;
            }
        });

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

        if (id == R.id.nav_upload) {
            Intent intent = new Intent(MyActivity.this,MenuUploadActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_down) {
            Intent intent = new Intent(MyActivity.this,MenuDownMaterialActivity.class);
            intent.putExtra("item_no", "*");
            startActivity(intent);
        } else if (id == R.id.nav_clear) {
            Intent intent = new Intent(MyActivity.this,ClearActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_updateVersion) {
            checkUpdateUtils.checkUpdate(MyActivity.this);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MyActivity.this,AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_out) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
            builder.setMessage("您确认退出应用？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("AUTH_TOKEN","error");
                    editor.commit();
                    System.exit(0);

                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //浸入式状态栏
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
//    public void getGPSTIMEStr() {
//        // -----------location config ------------
//        locationService = ((MyApplication)getApplication()).locationService;
//        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
//        locationService.registerListener(mListener);
//        //注册监听
//        int type = getIntent().getIntExtra("from", 0);
//        if (type == 0) {
//            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
//        } else if (type == 1) {
//            locationService.setLocationOption(locationService.getOption());
//        }
//        Log.i(TAG, "locationService.start()" );
//        locationService.start();// 定位SDK
//
//    }
//    /*****
//     *
//     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
//     *
//     */
//    private BDLocationListener mListener = new BDLocationListener() {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            // TODO Auto-generated method stub
//            SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss");
//            Date curDate =  new Date(System.currentTimeMillis());
//            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putString("mLat1", String.valueOf(location.getLatitude()));
//                editor.putString("mLon1", String.valueOf(location.getLongitude()));
//                editor.putString("GPSTIMEStr",location.getAddrStr()+formatter.format(curDate));
//
//                editor.commit();
//
//
//            }
//        }
//
//        public void onConnectHotSpotMessage(String s, int i){
//        }
//    };

}
