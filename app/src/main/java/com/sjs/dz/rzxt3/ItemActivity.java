package com.sjs.dz.rzxt3;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sjs.dz.rzxt3.DB.ItemInfo;
import com.sjs.dz.rzxt3.DB.XDBManager;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();
    private ImageView im_back;
    private ListView listView;
    private int pflag;
    private List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_item);
        toolbar.setTitle("");//设置主标题
        setSupportActionBar(toolbar);
        im_back = (ImageView) findViewById(R.id.im_item_back);
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //浸透式状态栏
        initWindow();
        listView=(ListView)findViewById(R.id.item_listview);
        Intent intent = getIntent();
//        MyApplication myApplication=new MyApplication();
//        DbManager db = x.getDb(myApplication.getDaoConfig());
        DbManager db = x.getDb(XDBManager.getDaoConfig());
        List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
        try {
            itemInfos = db.selector(ItemInfo.class)
                    .where("pact_no","=",intent.getStringExtra("pact_no"))
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
                    ItemActivity.this,
                    list_map,
                    R.layout.item_item,
                    new String[]{"item_no", "item_status", "pro_type","rz_scope","rz_type","check_type"},
                    new int[]{R.id.tv_item_no, R.id.tv_item_status, R.id.tv_item_pro_type,R.id.tv_item_rz_scope, R.id.tv_item_rz_type,R.id.tv_item_check_type});
            listView.setAdapter(simpleAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    pflag=position;
                    //我们需要的内容，跳转页面或显示详细信息
                    LinearLayout ly_pro_info=(LinearLayout)view.findViewById(R.id.ly_item_pro_info);
                    LinearLayout ly_material=(LinearLayout)view.findViewById(R.id.ly_item_pro_material);
                    LinearLayout ly_item_pro_report=(LinearLayout)view.findViewById(R.id.ly_item_pro_report);
                    ly_pro_info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(TAG,"ly_pro_info"+list_map.get(pflag).get("item_no").toString()+"跳转");
                            Intent intents = new Intent(ItemActivity.this,ProInfoActivity.class);
                            intents.putExtra("item_no", list_map.get(pflag).get("item_no").toString());
                            intents.putExtra("rz_scope", list_map.get(pflag).get("rz_scope").toString());
                            startActivity(intents);
                        }
                    });
                    ly_material.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(TAG,"ly_material"+list_map.get(pflag).get("item_no").toString()+"跳转");
                            Intent intents = new Intent(ItemActivity.this,DownMaterialActivity.class);
                            intents.putExtra("item_no", list_map.get(pflag).get("item_no").toString());
                            startActivity(intents);
                        }
                    });
                    ly_item_pro_report.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(TAG,"item_no"+list_map.get(pflag).get("item_no").toString()+"跳转");
                            Intent intents = new Intent(ItemActivity.this,UploadReportActivity.class);
                            intents.putExtra("item_no", list_map.get(pflag).get("item_no").toString());
                            startActivity(intents);
                        }
                    });
                }
            });
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
