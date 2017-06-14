package com.sjs.dz.rzxt3.base;

import android.app.Application;
import android.app.Service;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.sjs.dz.rzxt3.loader.XUtilsImageLoader;
import com.sjs.dz.rzxt3.service.LocationService;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.io.File;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;


/**
 * 拍照MyApplication
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    private static FunctionConfig mFunctionConfig;
    public static FunctionConfig.Builder mFunctionConfigBuilder;
    public LocationService locationService;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        instance = this;
//      initImageLoader(getApplicationContext());
        initGalleryFinal();
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());


        // 全局默认信任所有https域名 或 仅添加信任的https域名
        // 使用RequestParams#setHostnameVerifier(...)方法可设置单次请求的域名校验
//        x.Ext.setDefaultHostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        });
    }

    /***
     * 初始化GalleryFinal
     */
    private void initGalleryFinal() {
        mFunctionConfigBuilder = new FunctionConfig.Builder();
        //设置主题
        //ThemeConfig.CYAN
        ThemeConfig theme = new ThemeConfig.Builder()
                //标题栏背景颜色
                .setTitleBarBgColor(Color.rgb(0x35, 0x98, 0xdb))
                //标题栏文本字体颜色
                .setTitleBarTextColor(Color.rgb(0xff,0xff,0xff))
                //设置Floating按钮Nornal状态颜色
                .setFabNornalColor(Color.rgb(0xDD, 0xDD, 0xDD))
                //设置Floating按钮Pressed状态颜色
                .setFabPressedColor(Color.rgb(0xBB, 0xBB, 0xBB))
                //选择框未选颜色
                .setCheckSelectedColor(Color.rgb(0xEE, 0xEE, 0xEE))
                //设置裁剪控制点和裁剪框颜色
                .setCropControlColor(Color.rgb(0xEE, 0xEE, 0xEE))
                .build();
        // mFunctionConfigBuilder.setSelected(mPhotoList);//添加过滤集合
        //配置功能
        mFunctionConfig = mFunctionConfigBuilder
                .setEnableEdit(true)//开启编辑功能
                .setEnableCamera(true)//开启相机功能
                .setEnableCrop(true)//开启裁剪功能
                .setEnableRotate(true)//开启旋转功能
                .setCropSquare(true)//裁剪正方形
                .setEnablePreview(true)//是否开启预览功能
                .setRotateReplaceSource(true)//配置选择图片时是否替换原始图片，默认不替换
                .build();

        //配置imageloader
        ImageLoader imageloader = new XUtilsImageLoader();

        CoreConfig coreConfig = new CoreConfig.Builder(getInstance(), imageloader, theme)
               // .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(mFunctionConfig)
               .build();
        GalleryFinal.init(coreConfig);
    }


    public static MyApplication getInstance(){
        return instance;
    }
    public static FunctionConfig getFunctionConfig() {
        return mFunctionConfig;
    }
    public static FunctionConfig.Builder getFunctionConfigBuilder() {
        return mFunctionConfigBuilder;
    }


}
