package com.sjs.dz.rzxt3.base;

import android.app.Application;
import android.graphics.Color;
import android.util.Log;

import com.sjs.dz.rzxt3.loader.XUtilsImageLoader;

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
    private static FunctionConfig.Builder mFunctionConfigBuilder;
    private DbManager.DaoConfig daoConfig;
    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        instance = this;
//      initImageLoader(getApplicationContext());
        initGalleryFinal();
/**
 * 初始化DaoConfig配置
 */
        daoConfig = new DbManager.DaoConfig()
//设置数据库名，默认xutils.db
            .setDbName("rzxt3.db")
            //设置数据库路径，默认存储在app的私有目录
            .setDbDir(new File("/mnt/sdcard/"))
            //设置数据库的版本号
            .setDbVersion(3)
            //设置数据库打开的监听
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    //开启数据库支持多线程操作，提升性能，对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            //设置数据库更新的监听
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                }
            })
            //设置表创建的监听
            .setTableCreateListener(new DbManager.TableCreateListener() {
                @Override
                public void onTableCreated(DbManager db, TableEntity<?> table){
                    Log.i("JAVA", "onTableCreated：" + table.getName());
                }
            });
    //设置是否允许事务，默认true
//    .setAllowTransaction(true)

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
                .setEnableEdit(false)//开启编辑功能
                .setEnableCamera(false)//开启相机功能
                .setEnableCrop(false)//开启裁剪功能
                .setEnableRotate(false)//开启旋转功能
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
