package com.sjs.dz.rzxt3.utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.sjs.dz.rzxt3.DB.PactInfo;
import com.sjs.dz.rzxt3.DB.ServerBean;
import com.sjs.dz.rzxt3.MyActivity;
import com.sjs.dz.rzxt3.base.MyApplication;
import com.sjs.dz.rzxt3.service.LocationService;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import me.relex.circleindicator.CircleIndicator;

import static com.sjs.dz.rzxt3.LoginActivity.URL;

/**
 * Created by win on 2017/6/14.
 */

public class checkUpdateUtils {
    //    private String URL="http://172.16.10.242:8080";
    private static SharedPreferences sharedPrefs;
    private static String userAccount="",nowVersion="",passWord="";
    private static ProgressDialog progressDialog,pDialog;
    private static Context contexts;

    /**
     * 下载更新,
     */
    //http://172.16.10.242:8080/MVNFHM/appInterface/isUpdate?userAccount=11000&appVersion=0
    public static void checkUpdate(Context context) {
        // TODO Auto-generated method stub
        contexts=context;
        sharedPrefs = context.getSharedPreferences("RZ3Share", Context.MODE_PRIVATE);
        userAccount=sharedPrefs.getString("USER_ACCOUNT", "110");
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            nowVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        proDialogShow(context, "正在查询...");
        RequestParams params = new RequestParams(URL+"appCheckUpdate");
        params.addBodyParameter("userAccount", userAccount);
        params.addBodyParameter("appVersion", nowVersion);
        System.out.println("Checkparams="+params);
        x.http().get(params, new Callback.CommonCallback<String>() {


            @Override
            public void onCancelled(CancelledException arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                PDialogHide();
//                Log.i(TAG,"版本更新err"+"提示网络错误");
            }

            @Override
            public void onFinished() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(String arg0) {
                // TODO Auto-generated method stub
                PDialogHide();

                if (arg0.equals("002")) {
                    Toast.makeText(contexts,"当前版本为最新版本",Toast.LENGTH_SHORT).show();
//                    Log.i(TAG,"当前版本为最新，不用更新");
                } else {
                    // 不同，弹出更新提示对话框
                    setUpDialog(nowVersion, arg0, "最新版");
                }
            }
        });
    }

    /**
     *
     * @param versionname
     *            地址中版本的名字
     * @param downloadurl
     *            下载包的地址
     * @param desc
     *            版本的描述
     */
    protected static void setUpDialog(String versionname, final String downloadurl,
                                      String desc) {
        // TODO Auto-generated method stub
        AlertDialog dialog = new AlertDialog.Builder(contexts).setCancelable(false)
                .setTitle("下载" + versionname + "版本").setMessage(desc)
                .setNegativeButton("取消", null)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        setDownLoad(downloadurl);
                    }
                }).create();
        dialog.show();
    }

    /**
     * 下载包
     *
     * @param downloadurl
     *            下载的url
     *
     */
    @SuppressLint("SdCardPath")
    protected static void setDownLoad(String downloadurl) {
        // TODO Auto-generated method stub
        RequestParams params = new RequestParams(downloadurl);
        params.setAutoRename(true);//断点下载

//        params.setSaveFilePath("/mnt/sdcard/rzxt.apk");
        params.setSaveFilePath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"rzxt/rzxt.apk");
        x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onCancelled(CancelledException arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                System.out.println("提示更新失败");
            }

            @Override
            public void onFinished() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(File arg0) {
                // TODO Auto-generated method stub
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"RZXT/", "rzxt.apk")),
                        "application/vnd.android.package-archive");
                contexts.startActivity(intent);
            }

            @Override
            public void onLoading(long arg0, long arg1, boolean arg2) {
                // TODO Auto-generated method stub
                progressDialog.setMax((int)arg0);
                progressDialog.setProgress((int)arg1);
            }

            @Override
            public void onStarted() {
                // TODO Auto-generated method stub
                System.out.println("开始下载");
                progressDialog = new ProgressDialog(contexts);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置为水平进行条
                progressDialog.setMessage("正在下载中...");
                progressDialog.setProgress(0);
                progressDialog.show();
            }

            @Override
            public void onWaiting() {
                // TODO Auto-generated method stub

            }
        });
    }

    private static void proDialogShow(Context context, String msg) {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(msg);
        // pDialog.setCancelable(false);
        pDialog.show();
    }

    private static void PDialogHide() {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
