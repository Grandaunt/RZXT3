package com.sjs.dz.rzxt3;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.sjs.dz.rzxt3.Adapter.TaskInfoGet1ReAdapter;
import com.sjs.dz.rzxt3.DB.ItemInfo;
import com.sjs.dz.rzxt3.DB.MtlInfo;
import com.sjs.dz.rzxt3.base.MyApplication;
import com.sjs.dz.rzxt3.utils.FileUtils;
import com.sjs.dz.rzxt3.utils.ImageUtil;
import com.sjs.dz.rzxt3.view.ActionSheetDialog;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import static com.sjs.dz.rzxt3.LoginActivity.URL;

public class UploadReportActivity extends AppCompatActivity implements AMapLocationListener {
    private String TAG = this.getClass().getSimpleName();
    private LinearLayout ly_memo,ly_get1,ly_get2,ly_get3;
    private RecyclerView Get_RecyclerView1,Get_RecyclerView2,Get_RecyclerView3;
    private EditText Get_EditText;
    private ImageView im_back;
    private Button btn_upload;
    private String item_no="",mtl_type="0",mtl_name="",GPSTIMEStr = "",mtl_time="";
    private boolean isVisible = true;
    private LinearLayoutManager mLayoutManager;
    private TaskInfoGet1ReAdapter mGetAdapter1;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_CROP = 1002;
    private final int REQUEST_CODE_EDIT = 1003;
    private DbManager db;
    private ProgressDialog UpDialog;
    //声明mLocationOption对象
    AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mLocationClient=null;
    private static SimpleDateFormat sdf = null;
//    private String URL="http://172.16.10.242:8080";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_upload);
        toolbar.setTitle("");//设置主标题
        setSupportActionBar(toolbar);
        im_back = (ImageView) findViewById(R.id.im_down_back);
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //浸透式状态栏
        initWindow();
        Intent intent = getIntent();
        item_no=intent.getStringExtra("item_no");
        MyApplication myApplication=new MyApplication();
        db = x.getDb(myApplication.getDaoConfig());
//        List<MtlInfo> mtlInfos1 = new ArrayList<MtlInfo>();
//        try {
//            mtlInfos1 = db.selector(MtlInfo.class)
//                    .where("item_no","=",item_no)
//                    .and("mtl_type","=","1")
//                    .findAll();
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
        initViews();
        //获取地理位置开始
        getGPSTIMEStr();

    }

    private void initViews() {
        ly_memo=(LinearLayout) findViewById(R.id.ly_upload_memo);
        ly_get1=(LinearLayout) findViewById(R.id.ly_upload_get1);
        ly_get2=(LinearLayout) findViewById(R.id.ly_upload_get2);
        ly_get3=(LinearLayout) findViewById(R.id.ly_upload_get3);
        btn_upload=(Button) findViewById(R.id.btn_upload);
        Get_RecyclerView1=(RecyclerView)findViewById(R.id.recyclerview_info_get1);
        Get_RecyclerView2=(RecyclerView)findViewById(R.id.recyclerview_info_get2);
        Get_RecyclerView3=(RecyclerView)findViewById(R.id.recyclerview_info_get3);
        Get_EditText=(EditText)findViewById(R.id.et_info_memo);
        ly_get1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    isVisible = false;
                    Get_RecyclerView1.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    Get_RecyclerView1.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible = true;
                }
            }
        });
        ly_get2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    isVisible = false;
                    Get_RecyclerView2.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    Get_RecyclerView2.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible = true;
                }
            }
        });
        ly_get3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    isVisible = false;
                    Get_RecyclerView3.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    Get_RecyclerView3.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible = true;
                }
            }
        });
        ly_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    isVisible = false;
                    ly_get3.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    ly_get3.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible = true;
                }
            }
        });
        ly_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    isVisible = false;
                    Get_EditText.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    Get_EditText.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible = true;
                }
            }
        });

   btn_upload.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           uploadHttp();
       }
   });

        //创建线性布局
        mLayoutManager = new LinearLayoutManager(UploadReportActivity.this);
//        mLayoutManager = new MyCustomLayoutManager(getActivity());
//        Goos_RecyclerView.setLayoutManager(mLayoutManager);
        //垂直方向
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        //给RecyclerView设置布局管理器
//        recyclerView_one.setLayoutManager(mLayoutManager);


        //设置固定大小
        Get_RecyclerView1.setHasFixedSize(true);
        Get_RecyclerView1.setLayoutManager(new GridLayoutManager(UploadReportActivity.this,4));
//        Get_RecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
//                StaggeredGridLayoutManager.HORIZONTAL));

        //创建适配器，并且设置
        mGetAdapter1 = new TaskInfoGet1ReAdapter(UploadReportActivity.this,item_no);
        Get_RecyclerView1.setAdapter(mGetAdapter1);
        mGetAdapter1.setOnItemClickLitener(new TaskInfoGet1ReAdapter.OnItemClickLitener()
        {

            @Override
            public void onItemClick(View view, final int position, String num)
            {
                mtl_type="8";
                new ActionSheetDialog(UploadReportActivity.this).builder()
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.DEFAULT,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Log.i(TAG,"ActionSheetDialog拍照");
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

            @Override
            public void onItemLongClick(View view, int position,String num)
            {

            }
        });

        //设置固定大小
        Get_RecyclerView2.setHasFixedSize(true);
        Get_RecyclerView2.setLayoutManager(new GridLayoutManager(UploadReportActivity.this,4));
//        Get_RecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
//                StaggeredGridLayoutManager.HORIZONTAL));

        //创建适配器，并且设置
        mGetAdapter1 = new TaskInfoGet1ReAdapter(UploadReportActivity.this,item_no);
        Get_RecyclerView2.setAdapter(mGetAdapter1);
        mGetAdapter1.setOnItemClickLitener(new TaskInfoGet1ReAdapter.OnItemClickLitener()
        {

            @Override
            public void onItemClick(View view, final int position, String num)
            {
                mtl_type="8";
                new ActionSheetDialog(UploadReportActivity.this).builder()
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.DEFAULT,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Log.i(TAG,"ActionSheetDialog拍照");
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

            @Override
            public void onItemLongClick(View view, int position,String num)
            {

            }
        });
        //创建线性布局
        mLayoutManager = new LinearLayoutManager(UploadReportActivity.this);
//        mLayoutManager = new MyCustomLayoutManager(getActivity());
//        Goos_RecyclerView.setLayoutManager(mLayoutManager);
        //垂直方向
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        //给RecyclerView设置布局管理器
//        recyclerView_one.setLayoutManager(mLayoutManager);
        //设置固定大小
        Get_RecyclerView3.setHasFixedSize(true);
        Get_RecyclerView3.setLayoutManager(new GridLayoutManager(UploadReportActivity.this,4));
//        Get_RecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
//                StaggeredGridLayoutManager.HORIZONTAL));

        //创建适配器，并且设置
        mGetAdapter1 = new TaskInfoGet1ReAdapter(UploadReportActivity.this,item_no);
        Get_RecyclerView3.setAdapter(mGetAdapter1);
        mGetAdapter1.setOnItemClickLitener(new TaskInfoGet1ReAdapter.OnItemClickLitener()
        {

            @Override
            public void onItemClick(View view, final int position, String num)
            {
                mtl_type="9";
                new ActionSheetDialog(UploadReportActivity.this).builder()
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.DEFAULT,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Log.i(TAG,"ActionSheetDialog拍照");
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

            @Override
            public void onItemLongClick(View view, int position,String num)
            {

            }
        });

    }

    private void uploadHttp() {
        UpDialog = new ProgressDialog(this);
        UpDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        UpDialog.setMessage("努力上传中。。。");
        UpDialog.show();
        UpDialog.setMax((int) 100);
        UpDialog.setProgress((int) 0);
        UpDialog.setCancelable(false);//弹框弹出时页面无法点击
        List<MtlInfo> mtlInfos = new ArrayList<MtlInfo>();
        try {
            mtlInfos = db.selector(MtlInfo.class)
                    .where("item_no", "=", item_no)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
        try {
            itemInfos = db.selector(ItemInfo.class)
                    .where("item_no", "=", item_no)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        for (int imagei = 0; imagei < mtlInfos.size(); imagei++) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "RZXT/" + item_no + "/upload/" + mtlInfos.get(imagei).getMtl_name();


        Log.i(TAG, "path=" + path);
            File f = new File(path);
            if (f.exists()) {
                RequestParams params = new RequestParams(URL);
                //支持断点续传
                params.setAutoResume(true);
                params.setMultipart(true);
                /**
                 * 多媒体文件上传
                 *
                 * @param AUTH_TOKEN 手机令牌
                 * @param FILE 文件对象
                 * @param APPLY_ID 多媒体文件类型，0-照片，1-录像
                 * @param TASK_NO 信贷员ID
                 * @param TASK_ITEM_NO 任务号，客户基本信息的任务号为info
                 * @param FILE_TYPE 文件描述，标明文件的位置及说明
                 * @return
                 * setAsJsonContent（boolean is）
                 * 如果有中文，要进行URLEncoder.encode方法
                 *params.addBodyParameter("deviceIntoTime",URLEncoder.encode(deviceIntoTime, "utf-8"));
                 */
                params.addBodyParameter("FILE", new File(path));
                params.addBodyParameter("APPLY_ID", itemInfos.get(0).getApply_id());
                params.addBodyParameter("ITEM_NO", item_no);
                params.addBodyParameter("TYPE", mtlInfos.get(imagei).getMtl_type());
                Log.i(TAG, "693params:" + params);
                x.http().post(params, new Callback.CommonCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
//                        Log.i(TAG, result);
//                        Gson gson = new Gson();
//                        java.lang.reflect.Type type = new TypeToken<ServerBean>() {
//                        }.getType();
//                        ServerBean serverBean = gson.fromJson(result, type);
////                serverBean = gson.fromJson(result, ServerBean.class);
//                        int err = serverBean.getError();
//                        String msg = serverBean.getMsg();
//                        if (err == 000) {
//
//                            uploadi++;
//                        } else {
//                            Toast.makeText(TaskInfoActivity.this, msg + "请检查网络", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                        UpDialog.setProgress((int) fen * uploadi);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        if (UpDialog != null && UpDialog.isShowing()) {
                            UpDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        if (UpDialog != null && UpDialog.isShowing()) {
                            UpDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFinished() {
//                        Log.i(TAG, "uploadi=" + uploadi);
//                        if (uploadi == sumLeng) {
//                            mMedialist.mt_status = 2 + "";
//                            String okstr = mediapro.UpdateStatus(mMedialist);
//                            Log.i(TAG, "okstr=" + okstr);
//                            if (UpDialog != null && UpDialog.isShowing()) {
//                                UpDialog.dismiss();
//                            }
//                        }
//                        Intent intent = new Intent(UploadReportActivity.this, MainActivity.class);
//                        startActivity(intent);
                    }
                });

            } else {
                if (UpDialog != null && UpDialog.isShowing()) {
                    UpDialog.dismiss();
                }
                Toast.makeText(UploadReportActivity.this, "照片上传失败，不存在", Toast.LENGTH_SHORT).show();
                Log.i(TAG, path + "照片上传失败，不存在");
            }
        }
    }


    //拍照照片回调函数
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {

        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            //查询讯息，确定mtl_name
            List<MtlInfo> mtlInfos1 = new ArrayList<MtlInfo>();
            try {
                mtlInfos1 = db.selector(MtlInfo.class)
                        .where("item_no","=",item_no)
                        .findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            int names=mtlInfos1.size()+1;
            mtl_name=item_no+names;
       //存储图片
            String path= Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator +"RZXT/"+item_no+"/upload/";
            if(!FileUtils.isFileExist(path)){
                try {
                    File file= FileUtils.createSDDir(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.i(TAG,"GPSTIMEStr="+GPSTIMEStr);
            //获取图片
            Bitmap Bitmap = BitmapFactory.decodeFile(resultList.get(0).getPhotoPath());
           //添加水印处理
            Bitmap textBitmap = ImageUtil.drawTextToRightBottom(UploadReportActivity.this, Bitmap, GPSTIMEStr, 30, Color.WHITE, 16, 16);

            Log.i(TAG,resultList.get(0).getPhotoPath());
            //把新图片放到指定路径
            Log.i(TAG,"805 mPhotoname ="+mtl_name);
            FileUtils.saveBitmap(path,mtl_name+ ".JPG",textBitmap);
            //添加信息到数据库

            MtlInfo childInfo = new MtlInfo(item_no,mtl_type,mtl_name,getBitmapSize(Bitmap),mtl_time,"image","");
            try {
                db.save(childInfo);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(UploadReportActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };
    /***
     * 获取地理位置
     */
    public void getGPSTIMEStr() {


        mLocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mLocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
////设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
//设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.onDestroy();
        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }


    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        StringBuffer sb = new StringBuffer();

        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
//                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                aMapLocation.getLatitude();//获取纬度
//                aMapLocation.getLongitude();//获取经度
//                aMapLocation.getAccuracy();//获取精度信息
//
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(aMapLocation.getTime());
//                df.format(date);//定位时间
                sb.append(  aMapLocation.getAddress() + "\n\n");
//				sb.append("兴趣点    : " + location.getPoiName() + "\n");
                //定位完成的时间
                sb.append( formatUTC(aMapLocation.getTime(), "yyyy-MM-dd HH:mm:ss:sss") + "\n");
                mtl_time=formatUTC(aMapLocation.getTime(), "yyyy-MM-dd HH:mm:ss:sss");
                GPSTIMEStr = sb.toString();
                Log.i(TAG, "GPSTIMEStr=" + GPSTIMEStr);

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e(TAG, "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }
    public synchronized static String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
            }
        } else {
            sdf.applyPattern(strPattern);
        }
        if (l <= 0l) {
            l = System.currentTimeMillis();
        }
        return sdf == null ? "NULL" : sdf.format(l);
    }

    /**
     * 得到bitmap的大小
     */
    public static String getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount()+"";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount()+"";
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight()+"";                //earlier version
    }

    //浸入式状态栏
    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}
