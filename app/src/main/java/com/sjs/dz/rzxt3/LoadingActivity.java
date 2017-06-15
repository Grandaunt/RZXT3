package com.sjs.dz.rzxt3;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;

public class LoadingActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();
    private static final int TIME = 3000;
    private static final int GO_HOME = 15000;
    private static final int GO_MAIN = 15001;
//    private boolean isFirstIn = false;
    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GO_HOME:
                    goHome();
                    finish();
                    break;
                case GO_MAIN:
                    goMain();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        initWindow();
        getPersimmions();
        SharedPreferences sharedPrefs = getSharedPreferences("RZ3Share", Context.MODE_PRIVATE);
        String error=sharedPrefs.getString("AUTH_TOKEN", "error");
        Log.i(TAG,"sharedPrefs.getString(AUTH_TOKEN, error)="+sharedPrefs.getString("AUTH_TOKEN", "error"));
       if( sharedPrefs.getString("AUTH_TOKEN", "error").equals("error")||sharedPrefs.getString("AUTH_TOKEN", "error").equals(""))
       {
           handler.sendEmptyMessageDelayed(GO_HOME,TIME);
       }
       else {
           handler.sendEmptyMessageDelayed(GO_MAIN, TIME);
       }
        Log.i("LoadingActivity","LoadingActivity欢迎。。。");

    }

    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void goHome(){
        Log.i("LoadingActivity","LoadingActivity欢迎。。。Intent");
        Intent intent = new Intent(LoadingActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void goMain(){
        Log.i("LoadingActivity","LoadingActivity欢迎。。。Intent");
        Intent intent = new Intent(LoadingActivity.this,MyActivity.class);
        startActivity(intent);
        finish();
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.CAMERA);
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
