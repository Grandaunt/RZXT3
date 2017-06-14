package com.sjs.dz.rzxt3;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sjs.dz.rzxt3.DB.ItemInfo;
import com.sjs.dz.rzxt3.DB.MtlInfo;
import com.sjs.dz.rzxt3.DB.ResultBean;
import com.sjs.dz.rzxt3.DB.XDBManager;
import com.sjs.dz.rzxt3.base.MyApplication;
import com.sjs.dz.rzxt3.service.LocationService;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sjs.dz.rzxt3.LoginActivity.URL;

public class MenuUploadActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();
    private ImageView im_back;
    private ListView listView;
    private int pflag;
    private List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private DbManager db;
    private ProgressDialog UpDialog;
    private int Upos;
    private boolean Uflag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu_item);
        toolbar.setTitle("");//设置主标题
        setSupportActionBar(toolbar);
        im_back = (ImageView) findViewById(R.id.im_menu_upload_back);
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //浸透式状态栏
        initWindow();
        listView=(ListView)findViewById(R.id.menu_upload_listview);
         db = x.getDb(XDBManager.getDaoConfig());
        List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
        try {
            itemInfos = db.selector(ItemInfo.class)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(itemInfos == null || itemInfos.size() == 0){
            Log.i(TAG,"itemInfos.size"+itemInfos.size()+"未查到数据");
        }
        else{
            Log.i(TAG,"itemInfos.size"+itemInfos.size());
            Log.i(TAG,"itemInfos.size"+itemInfos.get(0));
            //1.准备好数据源，循环为listView添加数据（数据源的准备工作，这里是模拟从SQLite中查询数据）
            for(int i=0;i<itemInfos.size();i++){
                Map<String,Object> items = new HashMap<String, Object>(); //创建一个键值对的Map集合，用来存放名字和头像
                //项目编号
                items.put("item_no", itemInfos.get(i).getItem_no());
                //项目状态（0：待处理，1:待上传，2：已上传）
                if(itemInfos.get(i).getItem_status().equals("2")) {
                    items.put("item_status","已上传");
                }
                else {
                    items.put("item_status","待上传");
                }
                //产品类型（）
                items.put("pro_type", itemInfos.get(i).getPro_type());
                //认证范围（013：种植，014：养殖，015：加工）
                if(itemInfos.get(i).getRz_scope().equals("013")) {
                    items.put("rz_scope","种植" );
                }
                else  if(itemInfos.get(i).getRz_scope().equals("014")) {
                    items.put("rz_scope","养殖" );
                }
                else {
                    items.put("rz_scope","加工" );
                }
                //认证类型
                items.put("rz_type", itemInfos.get(i).getRz_type());
                //检查类型
                items.put("check_type", itemInfos.get(i).getCheck_type());
                list_map.add(items);   //把这个存放好数据的Map集合放入到list中，这就完成类数据源的准备工作
            }

            Log.i(TAG,"list_map.size"+list_map.size()+":"+list_map.get(0).get("pro_type"));
            SimpleAdapter simpleAdapter = new SimpleAdapter(
                    MenuUploadActivity.this,
                    list_map,
                    R.layout.item_menu_upload,
                    new String[]{"item_no", "item_status", "pro_type","rz_scope","rz_type","check_type"},
                    new int[]{R.id.tv_menu_item_no, R.id.tv_menu_item_status, R.id.tv_menu_item_pro_type,R.id.tv_menu_item_rz_scope, R.id.tv_menu_item_rz_type,R.id.tv_menu_item_check_type});
            listView.setAdapter(simpleAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i(TAG,"listViewonItemClick已响应");
                    Toast.makeText(MenuUploadActivity.this,"listViewonItemClick已响应",Toast.LENGTH_LONG).show();
                    pflag=position;
                    //我们需要的内容，跳转页面或显示详细信息
                    LinearLayout ly_item_pro_report=(LinearLayout)view.findViewById(R.id.ly_menu_item);
                    Button btn_upload=(Button)findViewById(R.id.btn_menu_upload);
                    ly_item_pro_report.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Toast.makeText(MenuUploadActivity.this,"ly_item_pro_reportClick已响应",Toast.LENGTH_LONG).show();
                            Log.i(TAG,"ly_item_pro_reportClick已响应");
                            Log.i(TAG,"item_no"+list_map.get(pflag).get("item_no").toString()+"跳转");
                            Intent intents = new Intent(MenuUploadActivity.this,UploadReportActivity.class);
                            intents.putExtra("item_no", list_map.get(pflag).get("item_no").toString());
                            startActivity(intents);
                        }
                    });
                    btn_upload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Toast.makeText(MenuUploadActivity.this,"btn_uploadClick已响应",Toast.LENGTH_LONG).show();
                            Log.i(TAG,"btn_uploadClick已响应");
                            uploadHttp( list_map.get(pflag).get("item_no").toString());
                        }
                    });
//                    Toast.makeText(MenuUploadActivity.this,"ly_item_pro_reportClick已响应",Toast.LENGTH_LONG).show();
//                    Log.i(TAG,"ly_item_pro_reportClick已响应");
//                    Log.i(TAG,"item_no"+list_map.get(pflag).get("item_no").toString()+"跳转");
//                    Intent intents = new Intent(MenuUploadActivity.this,UploadReportActivity.class);
//                    intents.putExtra("item_no", list_map.get(pflag).get("item_no").toString());
//                    startActivity(intents);
                }
            });
        }
    }


    private void uploadHttp(String item_no) {
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
                            Log.i(TAG, "result=" + result);
                            if(result.equals("")||result.equals(null)){
                                Uflag=false;
                            }
                            else {
                                Gson gson = new Gson();
                                java.lang.reflect.Type type = new TypeToken<ResultBean>() {
                                }.getType();
                                ResultBean Bean = gson.fromJson(result, type);
//                serverBean = gson.fromJson(result, ServerBean.class);
                                String err = Bean.getErr();
                                String msg = Bean.getMsg();
                                if (err.equals("1") || err.equals("")) {
                                    Uflag=false;
                                }

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
                Toast.makeText(MenuUploadActivity.this, "报告上传成功", Toast.LENGTH_SHORT).show();
                UDB();
                finish();
            } else {
                //上传失败
                Toast.makeText(MenuUploadActivity.this, "照片上传失败，不存在", Toast.LENGTH_SHORT).show();
                Uflag = true;

            }
        }
        else {
            Toast.makeText(MenuUploadActivity.this,"无内容，请及时处理",Toast.LENGTH_SHORT).show();
            Log.i(TAG,"数据库无上传图片");
        }
        if (UpDialog != null && UpDialog.isShowing()) {
            UpDialog.dismiss();
        }
    }

    private void UDB() {

        try {

            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("item_no","=",list_map.get(pflag).get("item_no").toString());
            db.update(ItemInfo.class,whereBuilder,
                    new KeyValue("item_status",2));//对User表中复合whereBuilder所表达的条件的记录更新email和mobile
        } catch (DbException e) {
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
    }

