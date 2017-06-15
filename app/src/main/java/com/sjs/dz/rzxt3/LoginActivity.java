package com.sjs.dz.rzxt3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.sjs.dz.rzxt3.DB.*;
import com.sjs.dz.rzxt3.base.MyApplication;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();
    private Context context = LoginActivity.this;
    //http://172.16.10.242:8080/bcm_rz/appInterface/appLogin?userAccount=yang&passWord=1
    private String ACC,PASSWORD;
//    public static String URL="http://123.57.29.113:8080/MVNFHM/appInterface/";
    public static String URL="http://172.16.10.242:8080/bcm_rz/appInterface/";

//            "http://172.16.10.242:8080/bcm_rz/appInterface/appLogin";
    private SharedPreferences sharedPrefs;
    private Button ll_update;
    private ProgressDialog pDialog;
    private String nowVersion;
    private ProgressDialog progressDialog;
    private MyApplication myApplication;
    private  DbManager db;
    private ServerBean serverBean;
    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;
    @ViewInject(R.id.et_acc)
    EditText et_Acc;
    @ViewInject(R.id.et_password)
    EditText et_Password;
    @ViewInject(R.id.btn_login)
    EditText btn_Login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(TAG,"LoginActivity登陆");
        initWindow();
        x.view().inject(this);
        sharedPrefs = getSharedPreferences("RZ3Share", Context.MODE_PRIVATE);

//        myApplication=new MyApplication();
        XDBManager.initDb();
         db = x.getDb(XDBManager.getDaoConfig());
        String name=db.getDaoConfig().getDbName().toString();
        Log.i(TAG,"initData.db_addr"+name);

        getPersimmions();

    }
    /**
     * 单击事件
     * type默认View.OnClickListener.class，故此处可以简化不写，@Event(R.id.bt_main)
     */
    @Event(type = View.OnClickListener.class,value = R.id.btn_login)
    private void testInjectOnClick(View v){
        ACC = et_Acc.getText().toString();
        PASSWORD = et_Password.getText().toString();
        LoginHttp(ACC,PASSWORD);
//            Snackbar.make(v,"OnClickListener",Snackbar.LENGTH_SHORT).show();
    }
    /**
     * 长按事件
     @Event(type = View.OnLongClickListener.class,value = R.id.bt_main)
    private boolean testOnLongClickListener(View v){
        Snackbar.make(v,"testOnLongClickListener",Snackbar.LENGTH_SHORT).show();
        return true;
    }
     */
    private void LoginHttp(String acc,String password) {
        RequestParams params = new RequestParams(URL+"appLogin");
        params.addBodyParameter("userAccount",acc);
        params.addParameter("passWord",password);
        Log.i(TAG,"params="+params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG,"result="+result);
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<ServerBean>() {}.getType();
                serverBean = gson.fromJson(result, type);
//                serverBean = gson.fromJson(result, ServerBean.class);
                String err = serverBean.getErr();
                String msg = serverBean.getMsg();
                if (err.equals("0")) {
                    //解析成功
                    Log.i(TAG, "解析成功：" + msg);
                    UserInfo user = serverBean.getUser();
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("AUTH_TOKEN", user.getAUTH_TOKEN());
                    editor.putString("USER_ACCOUNT", user.getUSER_ACCOUNT());
                    editor.putString("USER_NAME", user.getUSER_NAME());
                    editor.putString("USER_IDE", user.getUSER_IDE());
                    editor.putString("USER_TEL", user.getUSER_TEL());
                    editor.putString("USER_DEPT_NAME", user.getUSER_DEPT_NAME());
                    editor.putString("PASSWORD", PASSWORD);
                    editor.putString("USER_DEPT_ORG_CODE", user.getUSER_DEPT_ORG_CODE());
                    editor.putString("AUTH_TOKEN","right");
                    editor.commit();
                    List<PactInfo> pactInfos = new ArrayList<PactInfo>();
                    List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
                    List<ProInfo>  proInfos  = new ArrayList<ProInfo>();
                    List<MtlInfo>  mtlInfos  = new ArrayList<MtlInfo>();
                    pactInfos = serverBean.getHtList();
                    itemInfos = serverBean.getXmList();
                    proInfos  = serverBean.getCpList();
                    mtlInfos  = serverBean.getMtList();
                    Log.i(TAG,"sharedPrefs.getString(AUTH_TOKEN, error)="+sharedPrefs.getString("AUTH_TOKEN", "error"));
                    Log.i(TAG, "pactInfos：" + pactInfos);
                    Log.i(TAG, "mtInfos：" + mtlInfos);
                    Log.i(TAG, "proInfos：" + proInfos);
                    Log.i(TAG, "itemInfos：" + itemInfos);
                    try {
                        db.saveOrUpdate(pactInfos);
                    } catch (DbException ex) {
                        ex.printStackTrace();
                    }
                    try {

                        db.saveOrUpdate(itemInfos);
                    } catch (DbException ex) {
                        ex.printStackTrace();
                    }
                    try {

                        db.saveOrUpdate(proInfos);

                    } catch (DbException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        db.saveOrUpdate(mtlInfos);

                    } catch (DbException ex) {
                        ex.printStackTrace();
                    }
                }
//                解析result
                Intent intent = new Intent(LoginActivity.this,MyActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });

    }
    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }
    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

