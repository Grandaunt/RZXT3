package com.sjs.dz.rzxt3;

import android.app.Activity;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sjs.dz.rzxt3.Adapter.ContactAdapter;
import com.sjs.dz.rzxt3.DB.ItemInfo;
import com.sjs.dz.rzxt3.DB.MtlInfo;
import com.sjs.dz.rzxt3.DB.PactInfo;
import com.sjs.dz.rzxt3.DB.XDBManager;
import com.sjs.dz.rzxt3.utils.FileUtils;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClearActivity extends Activity implements View.OnClickListener  {
    private String TAG = this.getClass().getSimpleName();
    private TextView tvMultiChoose;			//打开多选
    private TextView tvMultiChooseCancel;	//关闭多选
    private ListView lv;
    private ContactAdapter adapter;



    private List<PactInfo> contacts = new ArrayList<PactInfo>();
    private List<PactInfo> contactSelectedList = new ArrayList<PactInfo>(); 	//记录被选中过的item

    private LinearLayout llDeleteContainer;
    private TextView tvChooseAll;			//全选
    private TextView tvChooseDeletel;		//删除所选项
    private ImageButton backBtn;
    private  ArrayList<HashMap<String, String>> taskList;
    private String userAcc = "";//信贷员
    private int task_status=3;//任务状态
    private   DbManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear);
        findView();
        initView();
         db = x.getDb(XDBManager.getDaoConfig());
        String name=db.getDaoConfig().getDbName();
        Log.i(TAG,"数据库名称"+name);
    }
    private void findView(){
        tvMultiChoose = (TextView)findViewById(R.id.tv_batchdelete);
        tvMultiChooseCancel = (TextView)findViewById(R.id.tv_batchdelete_cancel);
        tvMultiChoose.setOnClickListener(this);
        tvMultiChooseCancel.setOnClickListener(this);
        backBtn = (ImageButton)findViewById(R.id.im_back);
        backBtn.setOnClickListener(this);
        llDeleteContainer = (LinearLayout)findViewById(R.id.ll_delete_container);
        tvChooseAll = (TextView)findViewById(R.id.tv_choose_all);
        tvChooseDeletel = (TextView)findViewById(R.id.tv_choose_delete);


        tvChooseAll.setOnClickListener( this);
        tvChooseDeletel.setOnClickListener(this);
        lv = (ListView)findViewById(R.id.lv_recent_contact);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                tvMultiChoose.setVisibility(View.GONE);
                tvMultiChooseCancel.setVisibility(View.VISIBLE);
                llDeleteContainer.setVisibility(View.VISIBLE);

                contactSelectedList.clear();	//清空被选中的item项
                adapter = new ContactAdapter(ClearActivity.this, contacts, true);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int position, long arg3) {
                        boolean isSelect = adapter.getisSelectedAt(position);

                        if(!isSelect){
                            //当前为被选中，记录下来，用于删除
                            contactSelectedList.add(contacts.get(position));
                        }else{
                            contactSelectedList.remove(contacts.get(position));
                        }

                        //选中状态的切换
                        adapter.setItemisSelectedMap(position, !isSelect);
                    }
                });
                return false;
            }
        });
    }

    private void initView(){
        //获取信贷员id
        db = x.getDb(XDBManager.getDaoConfig());
        List<PactInfo> pactInfos = new ArrayList<PactInfo>();
        try {
            pactInfos = db.selector(PactInfo.class)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }


        contacts =pactInfos;
        adapter = new ContactAdapter(this, contacts, false);
        lv.setAdapter(adapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_back:
                finish();
                break;

            case R.id.tv_batchdelete:
                //打开多选
                tvMultiChoose.setVisibility(View.GONE);
                tvMultiChooseCancel.setVisibility(View.VISIBLE);
                llDeleteContainer.setVisibility(View.VISIBLE);

                contactSelectedList.clear();	//清空被选中的item项
                adapter = new ContactAdapter(this, contacts, true);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int position, long arg3) {
                        boolean isSelect = adapter.getisSelectedAt(position);

                        if(!isSelect){
                            //当前为被选中，记录下来，用于删除
                            contactSelectedList.add(contacts.get(position));
                        }else{
                            contactSelectedList.remove(contacts.get(position));
                        }

                        //选中状态的切换
                        adapter.setItemisSelectedMap(position, !isSelect);
                    }
                });
                break;

            case R.id.tv_batchdelete_cancel: //关闭多选
                tvMultiChoose.setVisibility(View.VISIBLE);
                tvMultiChooseCancel.setVisibility(View.GONE);
                llDeleteContainer.setVisibility(View.GONE);

                contactSelectedList.clear();
                adapter = new ContactAdapter(this, contacts, false);
                lv.setAdapter(adapter);

                break;
            case R.id.tv_choose_delete: //删除所选项
                tvMultiChoose.setVisibility(View.VISIBLE);
                tvMultiChooseCancel.setVisibility(View.GONE);
                llDeleteContainer.setVisibility(View.GONE);

                for(PactInfo c : contactSelectedList){


//                    PactInfo repo1 = new TaskPro(ClearActivity.this);
//                    repo1.delete(c.task_no);
//                    //删除文件夹
//                    Log.i(TAG,"/RZXT/" + userAcc + "/" + c.task_no);


                    List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
                    try {
                        itemInfos.clear();
                        itemInfos = db.selector(ItemInfo.class)
                                .where("pact_no","=",c.getPact_no())
                                .findAll();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    for(int i=0;i<itemInfos.size();i++) {
                        String fileName = Environment.getExternalStorageDirectory() + "/rzxt/" + itemInfos.get(i).getItem_no();
                        File file = new File(fileName);
                        FileUtils.delFile(file);

                        //删除数据库
                        WhereBuilder b = WhereBuilder.b();
                        b.and("item_no","=",itemInfos.get(i).getItem_no());
                        b.and("mtl_type", "in", new int[]{7, 8, 9});//构造修改的条件
                        try {
                            db.delete(MtlInfo.class, b);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                    contacts.remove(c);
                }

                contactSelectedList.clear();
                adapter = new ContactAdapter(this, contacts, false);
                lv.setAdapter(adapter);

                break;
            case R.id.tv_choose_all: //全选

                for(int i=0; i<contacts.size(); i++){
                    adapter.setItemisSelectedMap(i, true);
                    contactSelectedList.add(contacts.get(i));
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {


        List<PactInfo> pactInfos = new ArrayList<PactInfo>();
        try {
            pactInfos.clear();
            pactInfos = db.selector(PactInfo.class)
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        contacts =pactInfos;
        /**
         * 第三个参数表示是否显示checkbox
         */
        adapter = new ContactAdapter(this, contacts, false);
        lv.setAdapter(adapter);
        super.onResume();

    }


}
