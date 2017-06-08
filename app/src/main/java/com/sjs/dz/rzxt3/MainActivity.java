package com.sjs.dz.rzxt3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    private ListView listView;   //定义ListView对象，用来获取布局文件中的ListView控件
    private String[] name = {"小明","小华","小梁","小王","小林","小赵"};  //定义一个名字数组，用来为数据源提供姓名
    private int[] images = {R.mipmap.ic_about,R.mipmap.ic_about,R.mipmap.ic_about,R.mipmap.ic_about,R.mipmap.ic_about,R.mipmap.ic_about};//定义一个 整形数组，用来为数据源中的头像
    private List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>(); //定义一个适配器对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView); //获取布局文件中的ListView对象

        //1.准备好数据源，循环为listView添加数据（数据源的准备工作，这里是模拟从SQLite中查询数据）
        for(int i=0;i<6;i++){
            Map<String,Object> items = new HashMap<String, Object>(); //创建一个键值对的Map集合，用来存放名字和头像
            items.put("pic", images[i]);  //放入头像， 根据下标获取数组
            items.put("name", name[i]);      //放入名字， 根据下标获取数组
            list_map.add(items);   //把这个存放好数据的Map集合放入到list中，这就完成类数据源的准备工作
        }

        //2、创建适配器（可以使用外部类的方式、内部类方式等均可）
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                MainActivity.this,/*传入一个上下文作为参数*/
                list_map,         /*传入相对应的数据源，这个数据源不仅仅是数据而且还是和界面相耦合的混合体。*/
                R.layout.item_item,
                new String[]{"item_no", "item_status", "pro_type","rz_scope","rz_type","check_type"},
                new int[]{R.id.tv_item_no, R.id.tv_item_status, R.id.tv_item_pro_type,R.id.tv_item_rz_scope, R.id.tv_item_rz_type,R.id.tv_item_check_type});

        //3、为listView加入适配器
        listView.setAdapter(simpleAdapter);


}

}