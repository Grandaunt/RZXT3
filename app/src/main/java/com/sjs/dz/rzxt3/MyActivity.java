package com.sjs.dz.rzxt3;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sjs.dz.rzxt3.Adapter.MyFragmentAdapter;
import com.sjs.dz.rzxt3.DB.PactInfo;
import com.sjs.dz.rzxt3.base.MyApplication;
import com.sjs.dz.rzxt3.utils.DepthPageTransformer;
import com.sjs.dz.rzxt3.utils.FileUtils;
import com.sjs.dz.rzxt3.view.ActionSheetDialog;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import me.relex.circleindicator.CircleIndicator;

import static com.sjs.dz.rzxt3.LoginActivity.URL;

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
    private String userAccount="",nowVersion="";
    private ProgressDialog progressDialog,pDialog;
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
        //获取侧边栏头部view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        ImageView headicon = (ImageView)headerView.findViewById(R.id.headicon);
        //给侧边栏头部username，phone赋值，头像添加点击事件


        //显示用户名
        sharedPrefs = getSharedPreferences("RZ3Share", Context.MODE_PRIVATE);
        TextView usernametv = (TextView)headerView.findViewById(R.id.username);
        usernametv.setText(sharedPrefs.getString("USER_NAME", "110"));
        //查询并显示用户头像
        String drawablepath= Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator +"RZXT/user.png";
//        if(drawablepath==null||drawablepath.equals("")) {
//            drawablepath="R.drawable.sym_def_headicon";
//            ImageOptions options = new ImageOptions.Builder()
//                    //设置加载过程中的图片
//                    .setLoadingDrawableId(R.mipmap.ic_launcher)
////设置加载失败后的图片
//                    .setFailureDrawableId(R.mipmap.ic_launcher)
//                    //设置圆形
//                    .setCircular(true)
//                    //某些手机拍照时图片自动旋转，设置图片是否自动旋转为正
//                    .setAutoRotate(true)
//                    //等比例缩放居中显示
//                    .setImageScaleType(ImageView.ScaleType.FIT_XY)
//                    .build();
//            x.image().bind(headicon, drawablepath, options);
//
//        }else{
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
//        }
//



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


        //初始化数据
        initData();


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
                String photoPath= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"RZXT/";
                Bitmap Bitmap = BitmapFactory.decodeFile(resultList.get(0).getPhotoPath());
                //将投向另存为 Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"RZXT/user.png";

                FileUtils.saveBitmap(photoPath,"user.PNG",Bitmap);
                //获取NavigationView父布局
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                ImageView headicon = (ImageView)headerView.findViewById(R.id.headicon);
                x.image().bind(headicon,photoPath, options);

                //存储头像路径到数据库
//                Intent i = getIntent();
//                UserRepo repo = new UserRepo(MainActivity.this);
//                repo.addusericon(i.getStringExtra("usernameinfo"), resultList.get(0).getPhotoPath());


            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(MyActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    private void initData() {
        myApplication=new MyApplication();
        db = x.getDb(myApplication.getDaoConfig());

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

        if (id == R.id.nav_upload) {
            Intent intent = new Intent(MyActivity.this,MenuUploadActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_down) {
            Intent intent = new Intent(MyActivity.this,DownMaterialActivity.class);
            intent.putExtra("item_no", "*");
            startActivity(intent);
        } else if (id == R.id.nav_clear) {
            Intent intent = new Intent(MyActivity.this,ClearActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_updateVersion) {
            checkUpdate();

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MyActivity.this,AboutActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_out) {
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("AUTH_TOKEN","");
            editor.commit();
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * 下载更新,
     */
    //http://172.16.10.242:8080/MVNFHM/appInterface/isUpdate?userAccount=11000&appVersion=0
    protected void checkUpdate() {
        // TODO Auto-generated method stub
        userAccount=sharedPrefs.getString("USER_ACCOUNT", "110");
        try {
            PackageInfo packageInfo = MyActivity.this.getPackageManager().getPackageInfo(
                    MyActivity.this.getPackageName(), 0);
            nowVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        proDialogShow(MyActivity.this, "正在查询...");
        RequestParams params = new RequestParams(URL+"/appCheckUpdate");
        params.addBodyParameter("userAccount", userAccount);
        params.addBodyParameter("appVersion", nowVersion);
        Log.i(TAG,params+"");
        x.http().get(params, new Callback.CommonCallback<String>() {


            @Override
            public void onCancelled(CancelledException arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                PDialogHide();
                System.out.println("提示网络错误");
            }

            @Override
            public void onFinished() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(String arg0) {
                // TODO Auto-generated method stub
                PDialogHide();

                if (arg0.equals("002")) {
                    Toast.makeText(MyActivity.this,"当前版本为最新版本",Toast.LENGTH_SHORT).show();
                    Log.i(TAG,"当前版本为最新，不用更新");
                } else {
                    // 不同，弹出更新提示对话框
                    setUpDialog(nowVersion, arg0, "最新版");
                }
            }
        });
    }

    /**
     *
     * @param versionname
     *            地址中版本的名字
     * @param downloadurl
     *            下载包的地址
     * @param desc
     *            版本的描述
     */
    protected void setUpDialog(String versionname, final String downloadurl,
                               String desc) {
        // TODO Auto-generated method stub
        AlertDialog dialog = new AlertDialog.Builder(MyActivity.this).setCancelable(false)
                .setTitle("下载" + versionname + "版本").setMessage(desc)
                .setNegativeButton("取消", null)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        setDownLoad(downloadurl);
                    }
                }).create();
        dialog.show();
    }

    /**
     * 下载包
     *
     * @param downloadurl
     *            下载的url
     *
     */
    @SuppressLint("SdCardPath")
    protected void setDownLoad(String downloadurl) {
        // TODO Auto-generated method stub
        RequestParams params = new RequestParams(downloadurl);
        params.setAutoRename(true);//断点下载

//        params.setSaveFilePath("/mnt/sdcard/rzxt.apk");
        params.setSaveFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"RZXT/rzxt.apk");
        x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onCancelled(CancelledException arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                System.out.println("提示更新失败");
            }

            @Override
            public void onFinished() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(File arg0) {
                // TODO Auto-generated method stub
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"RZXT/", "rzxt.apk")),
                        "application/vnd.android.package-archive");
                startActivity(intent);
            }

            @Override
            public void onLoading(long arg0, long arg1, boolean arg2) {
                // TODO Auto-generated method stub
                progressDialog.setMax((int)arg0);
                progressDialog.setProgress((int)arg1);
            }

            @Override
            public void onStarted() {
                // TODO Auto-generated method stub
                System.out.println("开始下载");
                progressDialog = new ProgressDialog(MyActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置为水平进行条
                progressDialog.setMessage("正在下载中...");
                progressDialog.setProgress(0);
                progressDialog.show();
            }

            @Override
            public void onWaiting() {
                // TODO Auto-generated method stub

            }
        });
    }

    private void proDialogShow(Context context, String msg) {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(msg);
        // pDialog.setCancelable(false);
        pDialog.show();
    }

    private void PDialogHide() {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
