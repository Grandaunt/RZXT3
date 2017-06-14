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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sjs.dz.rzxt3.DB.ItemInfo;
import com.sjs.dz.rzxt3.DB.ProInfo;
import com.sjs.dz.rzxt3.DB.XDBManager;
import com.sjs.dz.rzxt3.base.MyApplication;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProInfoActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();
   private String rz_scope,item_no;
    private ImageView im_back;
    private ListView listView;
    List<ProInfo> proInfos = new ArrayList<ProInfo>();
    private List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_info);
        Intent intent = getIntent();
        rz_scope=intent.getStringExtra("rz_scope");
        item_no=intent.getStringExtra("item_no");
        Log.i(TAG,"rz_scope="+rz_scope);
        Log.i(TAG,"item_no="+item_no);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pro);
        toolbar.setTitle("");//设置主标题
        setSupportActionBar(toolbar);
        im_back = (ImageView) findViewById(R.id.im_pro_back);
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //浸透式状态栏
        initWindow();
        listView=(ListView)findViewById(R.id.pro_info_listview);
//        MyApplication myApplication=new MyApplication();
//        DbManager db = x.getDb(myApplication.getDaoConfig());
        DbManager db = x.getDb(XDBManager.getDaoConfig());
        proInfos=null;
        try {
            proInfos = db.selector(ProInfo.class)
                    .where("item_no","=",item_no)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(proInfos == null || proInfos.size() == 0){
            Log.i(TAG,"proInfos.size"+proInfos.size()+"未查到数据");
        }
        else {
            Log.i(TAG, "proInfos.size" + proInfos.size());
            Log.i(TAG, "proInfos.size" + proInfos.get(0));

            if(rz_scope.equals("015")){
            //加工
                processing();
            }else if(rz_scope.equals("016")){
            //养殖
                farming();
            }else {
            //种植
                planting();
            }

        }

    }

    //加工
    private void processing() {
        list_map.clear();
        //1.准备好数据源，循环为listView添加数据（数据源的准备工作，这里是模拟从SQLite中查询数据）
        for (int i = 0; i < proInfos.size(); i++) {
            Map<String, Object> items = new HashMap<String, Object>(); //创建一个键值对的Map集合，用来存放名字和头像
            items.put("pro_no", proInfos.get(i).getPro_no());
            items.put("base_name", proInfos.get(i).getBase_name());
            items.put("base_addr", proInfos.get(i).getBase_address());
            items.put("base_area", proInfos.get(i).getBase_area());
            items.put("Organic_Toppings", proInfos.get(i).getOrganic_Toppings());
            items.put("Total_Supply", proInfos.get(i).getTotal_Supply());
            items.put("pro_name", proInfos.get(i).getPro_name());
            items.put("pro_num", proInfos.get(i).getPro_num());
            items.put("pro_ralue", proInfos.get(i).getPro_ralue());
            list_map.add(items);   //把这个存放好数据的Map集合放入到list中，这就完成类数据源的准备工作
        }
        Log.i(TAG, "list_map.size" + list_map.size() + ":" + list_map.get(0).get("pro_name"));
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                ProInfoActivity.this,
                list_map,
                R.layout.item_pro_info_01,
                new String[]{"pro_no", "base_name", "base_addr", "base_area", "Organic_Toppings", "Total_Supply", "pro_name", "pro_num", "pro_ralue"},
                new int[]{R.id.tv_pro_no, R.id.tv_base_name, R.id.tv_base_addr, R.id.tv_base_area, R.id.tv_Organic_toppings, R.id.tv_Total_supply, R.id.tv_pro_name, R.id.tv_pro_num, R.id.tv_pro_ralue});
        listView.setAdapter(simpleAdapter);

    }

    //养殖
    private void farming() {
        list_map.clear();
        //1.准备好数据源，循环为listView添加数据（数据源的准备工作，这里是模拟从SQLite中查询数据）
        for (int i = 0; i < proInfos.size(); i++) {
            Map<String, Object> items = new HashMap<String, Object>(); //创建一个键值对的Map集合，用来存放名字和头像
            items.put("pro_no", proInfos.get(i).getPro_no());
            items.put("base_name", proInfos.get(i).getBase_name());
            items.put("base_addr", proInfos.get(i).getBase_address());
            items.put("base_area", proInfos.get(i).getBase_area());
            items.put("pro_num", proInfos.get(i).getPro_num());
            items.put("pro_area", proInfos.get(i).getPro_area());
            items.put("pro_name", proInfos.get(i).getPro_name());
            items.put("pro_output", proInfos.get(i).getPro_output());
            items.put("pro_ralue", proInfos.get(i).getPro_ralue());
            list_map.add(items);   //把这个存放好数据的Map集合放入到list中，这就完成类数据源的准备工作
        }
        Log.i(TAG, "list_map.size" + list_map.size() + ":" + list_map.get(0).get("pro_name"));
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                ProInfoActivity.this,
                list_map,
                R.layout.item_pro_info_02,
                new String[]{"pro_no", "base_name", "base_addr", "base_area", "pro_num", "pro_area", "pro_name", "pro_out[ut", "pro_ralue"},
                new int[]{R.id.tv_pro_no_02,  R.id.tv_base_name_02, R.id.tv_base_addr_02, R.id.tv_base_area_02, R.id.tv_pro_num_02, R.id.tv_pro_area_02,R.id.tv_pro_name_02,R.id.tv_pro_output_02, R.id.tv_pro_ralue_02});
        listView.setAdapter(simpleAdapter);

    }
    //种植
    private void planting() {
        list_map.clear();
        //1.准备好数据源，循环为listView添加数据（数据源的准备工作，这里是模拟从SQLite中查询数据）
        for (int i = 0; i < proInfos.size(); i++) {
            Map<String, Object> items = new HashMap<String, Object>(); //创建一个键值对的Map集合，用来存放名字和头像
            items.put("pro_no", proInfos.get(i).getPro_no());
            items.put("base_name", proInfos.get(i).getBase_name());
            items.put("base_addr", proInfos.get(i).getBase_address());
            items.put("base_area", proInfos.get(i).getBase_area());
            items.put("pro_desc", proInfos.get(i).getPro_desc());
            items.put("pro_area", proInfos.get(i).getPro_area());
            items.put("pro_name", proInfos.get(i).getPro_name());
            items.put("pro_output", proInfos.get(i).getPro_output());
            items.put("pro_ralue", proInfos.get(i).getPro_ralue());
            list_map.add(items);   //把这个存放好数据的Map集合放入到list中，这就完成类数据源的准备工作
        }
        Log.i(TAG, "list_map.size" + list_map.size() + ":" + list_map.get(0).get("pro_name"));
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                ProInfoActivity.this,
                list_map,
                R.layout.item_pro_info_03,
                new String[]{"pro_no", "base_name", "base_addr", "base_area", "pro_desc", "pro_area", "pro_name", "pro_out[ut", "pro_ralue"},
                new int[]{R.id.tv_pro_no_03,  R.id.tv_base_name_03, R.id.tv_base_addr_03, R.id.tv_base_area_03, R.id.tv_pro_describe_03, R.id.tv_pro_area_03,R.id.tv_pro_name_03,R.id.tv_pro_output_03, R.id.tv_pro_ralue_03});
        listView.setAdapter(simpleAdapter);
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
