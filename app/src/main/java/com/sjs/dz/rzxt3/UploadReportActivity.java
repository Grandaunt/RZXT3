package com.sjs.dz.rzxt3;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sjs.dz.rzxt3.Adapter.TaskInfoGet1ReAdapter;
import com.sjs.dz.rzxt3.Adapter.TaskInfoGet2ReAdapter;
import com.sjs.dz.rzxt3.Adapter.TaskInfoGet3ReAdapter;
import com.sjs.dz.rzxt3.DB.ItemInfo;
import com.sjs.dz.rzxt3.DB.MtlInfo;
import com.sjs.dz.rzxt3.DB.ResultBean;
import com.sjs.dz.rzxt3.DB.XDBManager;
import com.sjs.dz.rzxt3.base.MyApplication;
import com.sjs.dz.rzxt3.service.LocationService;
import com.sjs.dz.rzxt3.utils.FileUtils;
import com.sjs.dz.rzxt3.utils.ImageUtil;
import com.sjs.dz.rzxt3.view.ActionSheetDialog;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import static com.sjs.dz.rzxt3.LoginActivity.URL;
import static com.sjs.dz.rzxt3.base.MyApplication.mFunctionConfigBuilder;

public class UploadReportActivity extends AppCompatActivity  {
    private String TAG = this.getClass().getSimpleName();
    private LinearLayout ly_memo,ly_get1,ly_get2,ly_get3;
    private RecyclerView Get_RecyclerView1,Get_RecyclerView2,Get_RecyclerView3;
    private EditText Get_EditText;
    private ImageView im_back;
    private Button btn_upload;
    private String item_no="",mtl_type="0",mtl_name="",GPSTIMEStr = "",mtl_time="";
    private int mtl_no;
    private boolean isVisible = true;
    private LinearLayoutManager mLayoutManager;
    private TaskInfoGet1ReAdapter mGetAdapter1;
    private TaskInfoGet2ReAdapter mGetAdapter2;
    private TaskInfoGet3ReAdapter mGetAdapter3;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int REQUEST_CODE_CROP = 1002;
    private final int REQUEST_CODE_EDIT = 1003;
    private DbManager db;
    private ProgressDialog UpDialog;
    private SharedPreferences sharedPrefs;
    private LocationService locationService;

    private int Upos;
    private boolean Uflag=true;
    private   SimpleDateFormat formatter;
    private  Date curDate;
    //    private String URL="http://172.16.10.242:8080";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_upload);
        toolbar.setTitle("");//设置主标题
        setSupportActionBar(toolbar);
        im_back = (ImageView) findViewById(R.id.im_upload_back);
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //浸透式状态栏
        initWindow();
        sharedPrefs = getSharedPreferences("RZ3Share", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        item_no=intent.getStringExtra("item_no");
        db = x.getDb(XDBManager.getDaoConfig());
        initViews();
        //获取地理位置开始
         formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss");
         curDate =  new Date(System.currentTimeMillis());
        getGPSTIMEStr();

    }

    private void initViews() {
//        ly_memo=(LinearLayout) findViewById(R.id.ly_upload_memo);
        ly_get1=(LinearLayout) findViewById(R.id.ly_upload_get1);
        ly_get2=(LinearLayout) findViewById(R.id.ly_upload_get2);
        ly_get3=(LinearLayout) findViewById(R.id.ly_upload_get3);
        btn_upload=(Button) findViewById(R.id.btn_report_upload);
        Get_RecyclerView1=(RecyclerView)findViewById(R.id.recyclerview_info_get1);
        Get_RecyclerView2=(RecyclerView)findViewById(R.id.recyclerview_info_get2);
        Get_RecyclerView3=(RecyclerView)findViewById(R.id.recyclerview_info_get3);
//        Get_EditText=(EditText)findViewById(R.id.et_info_memo);
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
//        ly_memo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isVisible) {
//                    isVisible = false;
//                    Get_EditText.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
//                } else {
//                    Get_EditText.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
//                    isVisible = true;
//                }
//            }
//        });


   btn_upload.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           uploadHttp();
       }
   });
        SetGetRListAdapter();

    }

    private void SetGetRListAdapter() {
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
            public void onItemClick(View view,  int position)
            {
                mtl_type="7";
                Upos=position;
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
                                })
                        .addSheetItem("查看", ActionSheetDialog.SheetItemColor.DEFAULT,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        //带配置
                                        String str=Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "RZXT/" + item_no + "/upload/7_" + Upos+".jpg";
                                        Log.i(TAG,"查看str="+str);
                                        GalleryFinal.openEdit(REQUEST_CODE_EDIT, str,mOnHanlderResultCallback);


                                    }
                                }).show();
            }

            @Override
            public void onItemLongClick(View view, int position)
            {

            }
        });

        //设置固定大小
        Get_RecyclerView2.setHasFixedSize(true);
        Get_RecyclerView2.setLayoutManager(new GridLayoutManager(UploadReportActivity.this,4));
//        Get_RecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
//                StaggeredGridLayoutManager.HORIZONTAL));

        //创建适配器，并且设置
        mGetAdapter2 = new TaskInfoGet2ReAdapter(UploadReportActivity.this,item_no);
        Get_RecyclerView2.setAdapter(mGetAdapter2);
        mGetAdapter2.setOnItemClickLitener(new TaskInfoGet2ReAdapter.OnItemClickLitener()
        {

            @Override
            public void onItemClick(View view,  int position)
            {
                mtl_type="8";
                Upos=position;
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
                                })
                        .addSheetItem("查看", ActionSheetDialog.SheetItemColor.DEFAULT,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        //带配置
                                        String str=Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "RZXT/" + item_no + "/upload/8_" + Upos+".jpg";
                                        Log.i(TAG,"查看str="+str);
                                        GalleryFinal.openEdit(REQUEST_CODE_EDIT, str,mOnHanlderResultCallback);


                                    }
                                }).show();
            }

            @Override
            public void onItemLongClick(View view, int position)
            {

            }
        });

        //设置固定大小
        Get_RecyclerView3.setHasFixedSize(true);
        Get_RecyclerView3.setLayoutManager(new GridLayoutManager(UploadReportActivity.this,4));
//        Get_RecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
//                StaggeredGridLayoutManager.HORIZONTAL));

        //创建适配器，并且设置
        mGetAdapter3 = new TaskInfoGet3ReAdapter(UploadReportActivity.this,item_no);
        Get_RecyclerView3.setAdapter(mGetAdapter3);
        mGetAdapter3.setOnItemClickLitener(new TaskInfoGet3ReAdapter.OnItemClickLitener()
        {

            @Override
            public void onItemClick(View view,  int position)
            {
                mtl_type="9";
                Upos=position;
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
                                })
                     .addSheetItem("查看", ActionSheetDialog.SheetItemColor.DEFAULT,
                             new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            //带配置
                            String str=Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "rzxt/" + item_no + "/upload/9_" + Upos+".jpg";
                            Log.i(TAG,"查看str="+str);

                            File f = new File(str);
                            if (f.exists()){
                                GalleryFinal.openEdit(REQUEST_CODE_EDIT, str,mOnHanlderResultCallback);
                            }
                          else{
                                Toast.makeText(UploadReportActivity.this,"照片不存在",Toast.LENGTH_SHORT).show();
                            }


                        }
                    }).show();
            }

            @Override
            public void onItemLongClick(View view, int position)
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
                    .and("mtl_type", "in", new int[]{7, 8, 9})
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        //获取应用id
        List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
        try {
            itemInfos = db.selector(ItemInfo.class)
                    .where("item_no", "=", item_no)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (mtlInfos.size()>0) {
            int a = mtlInfos.size();
            Log.i(TAG, "mtlInfos.size();=" + mtlInfos.size());
            for (int imagei = 0; imagei < mtlInfos.size(); imagei++) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "RZXT/" + item_no + "/upload/" + mtlInfos.get(imagei).getMtl_name();
                Log.i(TAG, "path=" + path);
                File f = new File(path);
                if (f.exists()) {
                    RequestParams params = new RequestParams(URL + "appUploadFile");
                    //支持断点续传
                    params.setAutoResume(true);
                    params.setMultipart(true);
                params.addBodyParameter("FILE", new File(path));
                    params.addBodyParameter("ITEM_NO", item_no);
                    params.addBodyParameter("APPLY_ID", itemInfos.get(0).getApply_id());
                    params.addBodyParameter("TYPE", mtlInfos.get(imagei).getMtl_type());
                    Log.i(TAG, "376params:" + params);
                    x.http().post(params, new Callback.CommonCallback<String>() {

                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            java.lang.reflect.Type type = new TypeToken<ResultBean>() {
                            }.getType();
                            ResultBean Bean = gson.fromJson(result, type);
//                serverBean = gson.fromJson(result, ServerBean.class);
                            String err = Bean.getErr();
                            String msg = Bean.getMsg();
                            if (err.equals("0")) {

                            }
                            else {
                                Uflag = false;
                            }



                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Uflag = false;
                            if (UpDialog != null && UpDialog.isShowing()) {
                                UpDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                            Uflag = false;
                            if (UpDialog != null && UpDialog.isShowing()) {
                                UpDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFinished() {

                        }
                    });

                } else {
                    Uflag = false;
//                Toast.makeText(UploadReportActivity.this, "照片上传失败，不存在", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, path + "照片上传失败，不存在");
                }
                //for end...
            }

            if (Uflag) {
                //上传成功
                Toast.makeText(UploadReportActivity.this, "报告上传成功", Toast.LENGTH_SHORT).show();
                UDB();
                finish();
            } else {
                //上传失败
                Toast.makeText(UploadReportActivity.this, "照片上传失败，不存在", Toast.LENGTH_SHORT).show();
                Uflag = true;

            }
        }
        else {
            Toast.makeText(UploadReportActivity.this,"无内容，请及时处理",Toast.LENGTH_SHORT).show();
            Log.i(TAG,"数据库无上传图片");
        }
        if (UpDialog != null && UpDialog.isShowing()) {
            UpDialog.dismiss();
        }
    }

    private void UDB() {

        try {

            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("item_no","=",item_no);
            db.update(ItemInfo.class,whereBuilder,
                    new KeyValue("item_status",2));//对User表中复合whereBuilder所表达的条件的记录更新email和mobile
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    //拍照照片回调函数
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {

        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            //查询讯息，确定mtl_name
if(reqeustCode==REQUEST_CODE_CAMERA||reqeustCode==REQUEST_CODE_GALLERY) {
    mtl_name = mtl_type + "_" + Upos + ".jpg";
    //存储图片
    String path = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "rzxt/" + item_no + "/upload/";
    if (!FileUtils.isFileExist(path)) {
        try {
            File file = FileUtils.createSDDir(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//            GPSTIMEStr=sharedPrefs.getString("GPSTIMEStr","无信息");
    Log.i(TAG, "GPSTIMEStr=" + GPSTIMEStr);
    //获取图片
    Bitmap Bitmap = BitmapFactory.decodeFile(resultList.get(0).getPhotoPath());
    //添加水印处理
    Bitmap textBitmap = ImageUtil.drawTextToRightBottom(UploadReportActivity.this, Bitmap, GPSTIMEStr, 30, Color.WHITE, 16, 16);

    Log.i(TAG, resultList.get(0).getPhotoPath());
    //把新图片放到指定路径
    Log.i(TAG, "805 mPhotoname =" + mtl_name);
    FileUtils.saveBitmap(path, mtl_name, textBitmap);
    //添加信息到数据库
    List<MtlInfo> mtlInfos1 = new ArrayList<MtlInfo>();
    try {
        mtlInfos1 = db.selector(MtlInfo.class)
                .where("mtl_name", "=", mtl_name)
                .findAll();
    } catch (DbException e) {
        e.printStackTrace();
    }

    if (mtlInfos1.size() == 0) {
        try {
            mtlInfos1 = db.selector(MtlInfo.class)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        int mtl_no = mtlInfos1.get(mtlInfos1.size() - 1).getMtl_no() + 1;
        mtl_time = formatter.format(curDate);
        Log.i(TAG, "mtl_no=" + mtl_no + "item_no=" + item_no + "mtl_type=" + mtl_type + "mtl_name=" + mtl_name);
        MtlInfo childInfo = new MtlInfo(mtl_no, item_no, mtl_type, mtl_name, getBitmapSize(Bitmap), mtl_time, "4", "");
        try {
            db.save(childInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
            SetGetRListAdapter();
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(UploadReportActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };



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
    public void getGPSTIMEStr() {
        // -----------location config ------------
        locationService = ((MyApplication)getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        Log.i(TAG, "locationService.start()" );
        locationService.start();// 定位SDK

    }
    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub

            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
  GPSTIMEStr=location.getAddrStr()+"\n"+formatter.format(curDate);
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putString("mLat1", String.valueOf(location.getLatitude()));
//                editor.putString("mLon1", String.valueOf(location.getLongitude()));
//                editor.putString("GPSTIMEStr",location.getAddrStr()+formatter.format(curDate));
//
//                editor.commit();


            }
        }

        public void onConnectHotSpotMessage(String s, int i){
        }
    };

    @Override
    protected void onStart() {
        Log.i(TAG,"onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG,"onStop");
        locationService.unregisterListener(mListener); //注销掉监听
		locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.i(TAG,"onPause");
        super.onPause();
    }

    public UploadReportActivity() {
        super();
    }

    @Override
    protected void onResume() {
        Log.i(TAG,"onResume");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG,"onDestroy");
//        locationService.unregisterListener(mListener); //注销掉监听
//        locationService.stop(); //停止定位服务
        super.onDestroy();
    }
}
