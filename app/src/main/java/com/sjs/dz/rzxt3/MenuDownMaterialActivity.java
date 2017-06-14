package com.sjs.dz.rzxt3;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.sjs.dz.rzxt3.DB.MtlInfo;
import com.sjs.dz.rzxt3.DB.XDBManager;
import com.sjs.dz.rzxt3.utils.FileUtils;
import com.sjs.dz.rzxt3.utils.ListHUtil;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sjs.dz.rzxt3.LoginActivity.URL;

public class MenuDownMaterialActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();
//    private String URL="http://172.16.10.242:8080";

    private ImageView im_back;
    private ListView listView1,listView2,listView3,listView4,listView5,listView6,listView7,listView8;
    private LinearLayout ly_pro_material_01,ly_pro_material_02,ly_pro_material_03,ly_pro_material_04,ly_pro_material_05,ly_pro_material_06,ly_pro_material_07,ly_pro_material_08;
    private boolean isVisible1 = true,isVisible2 = true,isVisible3 = true,isVisible4 = true,isVisible5 = true,isVisible6 = true,isVisible7 = true,isVisible8 = true;
    private List<Map<String,Object>> list_map1 = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private List<Map<String,Object>> list_map2 = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private List<Map<String,Object>> list_map3 = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private List<Map<String,Object>> list_map4 = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private List<Map<String,Object>> list_map5 = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private List<Map<String,Object>> list_map6 = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private List<Map<String,Object>> list_map7 = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private List<Map<String,Object>> list_map8 = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private List<MtlInfo> mtlInfos1 = new ArrayList<MtlInfo>();
    private List<MtlInfo> mtlInfos2 = new ArrayList<MtlInfo>();
    private List<MtlInfo> mtlInfos3 = new ArrayList<MtlInfo>();
    private List<MtlInfo> mtlInfos4 = new ArrayList<MtlInfo>();
    private List<MtlInfo> mtlInfos5 = new ArrayList<MtlInfo>();
    private List<MtlInfo> mtlInfos6 = new ArrayList<MtlInfo>();
    private List<MtlInfo> mtlInfos7 = new ArrayList<MtlInfo>();
    private List<MtlInfo> mtlInfos8 = new ArrayList<MtlInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_material);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_down);
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

        //c初始化控件
        initViews();
        //从数据库获取数据
        initDate();
        //注入数据
        SimpleList(listView1,list_map1);
        SimpleList(listView2,list_map2);
        SimpleList(listView3,list_map3);
        SimpleList(listView4,list_map4);
        SimpleList(listView5,list_map5);
        SimpleList(listView6,list_map6);
        SimpleList(listView7,list_map7);
        SimpleList(listView8,list_map8);
        //设置控件监听
        onclickView();

    }



    private void initDate() {
//        Log.i(TAG,"mtlInfos.item_no"+item_no);
//        MyApplication myApplication=new MyApplication();
        DbManager db = x.getDb(XDBManager.getDaoConfig());
        String name=db.getDaoConfig().getDbName();
        Log.i(TAG,"数据库名称"+name);
        try {
            mtlInfos1.clear();
            mtlInfos1 = db.selector(MtlInfo.class)
                    .where("mtl_type","=","1")
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            mtlInfos2.clear();
            mtlInfos2 = db.selector(MtlInfo.class)
                    .where("mtl_type","=","2")
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            mtlInfos3.clear();
            mtlInfos3 = db.selector(MtlInfo.class)
                    .where("mtl_type","=","3")
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            mtlInfos4.clear();
            mtlInfos4 = db.selector(MtlInfo.class)
                    .where("mtl_type","=","4")
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            mtlInfos5.clear();
            mtlInfos5 = db.selector(MtlInfo.class)
                    .where("mtl_type","=","5")
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            mtlInfos6.clear();
            mtlInfos6= db.selector(MtlInfo.class)
                    .where("mtl_type","=","6")
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            mtlInfos7.clear();
            mtlInfos7 = db.selector(MtlInfo.class)
                    .where("mtl_type","=","13")
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            mtlInfos8.clear();
            mtlInfos8 = db.selector(MtlInfo.class)
                    .where("mtl_type","=","8")
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        //转化数据
        RData(mtlInfos1,list_map1);
        RData(mtlInfos2,list_map2);
        RData(mtlInfos3,list_map3);
        RData(mtlInfos4,list_map4);
        RData(mtlInfos5,list_map5);
        RData(mtlInfos6,list_map6);
        RData(mtlInfos7,list_map7);
        RData(mtlInfos8,list_map8);




    }

    private void RData(List<MtlInfo> mtlInfos, List<Map<String, Object>> list_map) {
        if(mtlInfos == null || mtlInfos.size() == 0){
            Log.i(TAG,"mtlInfos.size"+mtlInfos.size()+"未查到数据");
        }
        else {
            Log.i(TAG, "mtlInfos.size" + mtlInfos.size());
            Log.i(TAG, "mtlInfos.size" + mtlInfos.get(0));
            //1.准备好数据源，循环为listView添加数据（数据源的准备工作，这里是模拟从SQLite中查询数据）
            for (int i = 0; i < mtlInfos.size(); i++) {
                Map<String, Object> items = new HashMap<String, Object>(); //创建一个键值对的Map集合，用来存放名字和头像
                if(mtlInfos.get(i).getMtl_format().equals("1")){
                    items.put("mtl_format",R.mipmap.ic_word );
                }
                else   if(mtlInfos.get(i).getMtl_format().equals("2")){
                    items.put("mtl_format",R.mipmap.ic_pdf );
                }
                else   if(mtlInfos.get(i).getMtl_format().equals("3")){
                    items.put("mtl_format",R.mipmap.ic_excle );
                }
                else   if(mtlInfos.get(i).getMtl_format().equals("4")){
                    items.put("mtl_format",R.mipmap.ic_im );
                }
                else   if(mtlInfos.get(i).getMtl_format().equals("5")){
                    items.put("mtl_format",R.mipmap.ic_zip );
                }

                items.put("mtl_name", mtlInfos.get(i).getMtl_name());
                items.put("mtl_size", mtlInfos.get(i).getMtl_size());
                items.put("mtl_down_time", mtlInfos.get(i).getMtl_time());

                list_map.add(items);   //把这个存放好数据的Map集合放入到list中，这就完成类数据源的准备工作
            }

        }

    }

    private void onclickView() {

        ly_pro_material_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible1) {
                    isVisible1 = false;
                    listView1.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    listView1.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible1 = true;
                }
            }
        });
        ly_pro_material_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible2) {
                    isVisible2 = false;
                    listView2.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    listView2.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible2 = true;
                }
            }
        });
        ly_pro_material_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible3) {
                    isVisible3 = false;
                    listView3.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    listView3.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible3 = true;
                }
            }
        });
        ly_pro_material_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible4) {
                    isVisible4 = false;
                    listView4.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    listView4.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible4 = true;
                }
            }
        });
        ly_pro_material_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible5) {
                    isVisible5 = false;
                    listView5.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    listView5.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible5 = true;
                }
            }
        });
        ly_pro_material_06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible6) {
                    isVisible6 = false;
                    listView6.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    listView6.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible6= true;
                }
            }
        });
        ly_pro_material_07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible7) {
                    isVisible7 = false;
                    listView1.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    listView7.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible7 = true;
                }
            }
        });
        ly_pro_material_08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible8) {
                    isVisible8 = false;
                    listView8.setVisibility(View.VISIBLE);//这一句显示布局LinearLayout区域
                } else {
                    listView8.setVisibility(View.GONE);//这一句即隐藏布局LinearLayout区域
                    isVisible8 = true;
                }
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lookMtl(mtlInfos1.get(position).getMtl_name(),mtlInfos1.get(position).getMtl_down_path(),mtlInfos1.get(position).getMtl_format(),mtlInfos1.get(position).getItem_no());
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lookMtl(mtlInfos2.get(position).getMtl_name(),mtlInfos2.get(position).getMtl_down_path(),mtlInfos2.get(position).getMtl_format(),mtlInfos2.get(position).getItem_no());
            }
        });
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lookMtl(mtlInfos3.get(position).getMtl_name(),mtlInfos3.get(position).getMtl_down_path(),mtlInfos3.get(position).getMtl_format(),mtlInfos3.get(position).getItem_no());
            }
        });
        listView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lookMtl(mtlInfos4.get(position).getMtl_name(),mtlInfos4.get(position).getMtl_down_path(),mtlInfos4.get(position).getMtl_format(),mtlInfos4.get(position).getItem_no());
            }
        });
        listView5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lookMtl(mtlInfos5.get(position).getMtl_name(),mtlInfos5.get(position).getMtl_down_path(),mtlInfos5.get(position).getMtl_format(),mtlInfos5.get(position).getItem_no());
            }
        });
        listView6.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lookMtl(mtlInfos6.get(position).getMtl_name(),mtlInfos6.get(position).getMtl_down_path(),mtlInfos6.get(position).getMtl_format(),mtlInfos6.get(position).getItem_no());
            }
        });
        listView7.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lookMtl(mtlInfos7.get(position).getMtl_name(),mtlInfos7.get(position).getMtl_down_path(),mtlInfos7.get(position).getMtl_format(),mtlInfos7.get(position).getItem_no());
            }
        });
        listView8.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lookMtl(mtlInfos8.get(position).getMtl_name(),mtlInfos8.get(position).getMtl_down_path(),mtlInfos8.get(position).getMtl_format(),mtlInfos8.get(position).getItem_no());
            }
        });

    }



    private void SimpleList(ListView listViewi,List<Map<String,Object>> list_map) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(
                MenuDownMaterialActivity.this,
                list_map,
                R.layout.item_material,
                new String[]{"mtl_format", "mtl_name", "mtl_size","mtl_down_time"},
                new int[]{R.id.im_item_material, R.id.tv_item_material_name, R.id.tv_item_material_size,R.id.tv_item_material_down_time});
        listViewi.setAdapter(simpleAdapter);
        ListHUtil.setListViewHeightBasedOnChildren(listViewi);
    }
    private void initViews() {
        listView1=(ListView)findViewById(R.id.list_pro_material_01);
        listView2=(ListView)findViewById(R.id.list_pro_material_02);
        listView3=(ListView)findViewById(R.id.list_pro_material_03);
        listView4=(ListView)findViewById(R.id.list_pro_material_04);
        listView5=(ListView)findViewById(R.id.list_pro_material_05);
        listView6=(ListView)findViewById(R.id.list_pro_material_06);
        listView7=(ListView)findViewById(R.id.list_pro_material_07);
        listView8=(ListView)findViewById(R.id.list_pro_material_08);

        ly_pro_material_01=(LinearLayout) findViewById(R.id.ly_pro_material_01);
        ly_pro_material_02=(LinearLayout) findViewById(R.id.ly_pro_material_02);
        ly_pro_material_03=(LinearLayout) findViewById(R.id.ly_pro_material_03);
        ly_pro_material_04=(LinearLayout) findViewById(R.id.ly_pro_material_04);
        ly_pro_material_05=(LinearLayout) findViewById(R.id.ly_pro_material_05);
        ly_pro_material_06=(LinearLayout) findViewById(R.id.ly_pro_material_06);
        ly_pro_material_07=(LinearLayout) findViewById(R.id.ly_pro_material_07);
        ly_pro_material_08=(LinearLayout) findViewById(R.id.ly_pro_material_08);

    }

    private void lookMtl(String Dname,String DdownPath,String format,String item_no) {
        String path= Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator +"RZXT/"+item_no+"/"+"/down/";
        if(FileUtils.isFileExist(path+Dname)){
           //文件存在，使用本地软件打开
//            Log.i(TAG,Dname+"文件存在");
//            Uri uri = Uri.parse(path+Dname);
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(uri, "*/*");
            File file = new File(path+Dname);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);


            if(format.equals("1")){
//                    sMimeTypeMap.loadEntry("application/msword", "doc");
//                intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                intent.setDataAndType(uri, "application/msword");
            }
            else   if(format.equals("2")){
                intent.setDataAndType(uri, "application/pdf");
            }
            else   if(format.equals("3")){
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            }
            else   if(format.equals("4")){
                intent.setDataAndType(uri, "image/*");
            }
            else   if(format.equals("5")){
                intent.setDataAndType(uri, "application/rar");
            }
            else   if(format.equals("6")){
                intent.setDataAndType(uri, "text/plain");
            }
            else {
                intent.setDataAndType(uri, "*/*");
            }
                        startActivity(intent);
        }
        else {

            //文件不存在，创建文件目录并下载
            Log.i(TAG,Dname+"文件不存在");
            try {
                File file= FileUtils.createSDDir(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            downloadFile(URL+"appDownFile",Dname,path,DdownPath,format,item_no);
        }
//

    }

    private void downloadFile(String url, final String name, String Filepath, final String downPath, final  String format, final String item_no) {

        RequestParams requestParams = new RequestParams(url);
        requestParams.addParameter("FILE_NAME", name);
        requestParams.addParameter("FILE_PATH", downPath);
        requestParams.setSaveFilePath(Filepath+name);
        Log.i(TAG,"requestParams="+requestParams);
        Log.i(TAG,"Filepath+name="+Filepath+name);
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onSuccess(File result) {
                Log.i(TAG,"onSuccess=下载成功");
                lookMtl(name,downPath,format,item_no);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                Toast.makeText(MenuDownMaterialActivity.this, "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(MenuDownMaterialActivity.this, "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {

            }
        });
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
