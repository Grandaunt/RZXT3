//package com.sjs.dz.rzxt3.utils;
//
//import android.content.SharedPreferences;
//import android.util.Log;
//import android.view.View;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.sjs.dz.rzxt3.DB.ItemInfo;
//import com.sjs.dz.rzxt3.DB.MtlInfo;
//import com.sjs.dz.rzxt3.DB.PactInfo;
//import com.sjs.dz.rzxt3.DB.ProInfo;
//import com.sjs.dz.rzxt3.DB.ServerBean;
//import com.sjs.dz.rzxt3.DB.UserInfo;
//import com.sjs.dz.rzxt3.MyActivity;
//import com.sjs.dz.rzxt3.R;
//
//import org.xutils.common.Callback;
//import org.xutils.ex.DbException;
//import org.xutils.http.RequestParams;
//import org.xutils.x;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
//import in.srain.cube.views.ptr.PtrDefaultHandler;
//import in.srain.cube.views.ptr.PtrFrameLayout;
//import in.srain.cube.views.ptr.PtrHandler;
//import in.srain.cube.views.ptr.util.PtrLocalDisplay;
//
//import static com.sjs.dz.rzxt3.LoginActivity.URL;
//
///**
// * Created by win on 2017/6/14.
// */
//
//public class push {
//    public void pullRefresh(){
//        mPtrFrame = (PtrFrameLayout) findViewById(R.id.ptr);
//
//        /**
//         * 经典 风格的头部实现
//         */
//        final PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(MyActivity.this);
//        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);
//
//        mPtrFrame.setHeaderView(header);
////         mPtrFrame.setPinContent(true);//刷新时，保持内容不动，仅头部下移,默认,false
//        mPtrFrame.addPtrUIHandler(header);
//        //mPtrFrame.setKeepHeaderWhenRefresh(true);//刷新时保持头部的显示，默认为true
//        mPtrFrame.disableWhenHorizontalMove(true);//如果是ViewPager，设置为true，会解决ViewPager滑动冲突问题。
//        mPtrFrame.setPtrHandler(new PtrHandler() {
//
//            //需要加载数据时触发
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                mPtrFrame.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        userAccount=sharedPrefs.getString("USER_ACCOUNT", "110");
//                        passWord=sharedPrefs.getString("PASSWORD", "110");
//                        LoginHttp(userAccount,passWord);
//                        mPtrFrame.refreshComplete();
//                        //mPtrFrame.autoRefresh();//自动刷新
//                    }
//                }, 180);
//
//            }
//            private void LoginHttp(String acc,String password) {
//                RequestParams params = new RequestParams(URL+"appLogin");
//                params.addBodyParameter("userAccount",acc);
//                params.addParameter("passWord",password);
//                Log.i(TAG,"params="+params);
//                x.http().post(params, new Callback.CommonCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.i(TAG,"result="+result);
//                        Gson gson = new Gson();
//                        java.lang.reflect.Type type = new TypeToken<ServerBean>() {}.getType();
//                        serverBean = gson.fromJson(result, type);
////                serverBean = gson.fromJson(result, ServerBean.class);
//                        String err = serverBean.getErr();
//                        String msg = serverBean.getMsg();
//                        if (err.equals("0")) {
//                            //解析成功
//                            Log.i(TAG, "解析成功：" + msg);
//                            UserInfo user = serverBean.getUser();
//                            SharedPreferences.Editor editor = sharedPrefs.edit();
//                            editor.putString("AUTH_TOKEN", user.getAUTH_TOKEN());
//                            editor.putString("USER_ACCOUNT", user.getUSER_ACCOUNT());
//                            editor.putString("USER_NAME", user.getUSER_NAME());
//                            editor.putString("USER_IDE", user.getUSER_IDE());
//                            editor.putString("USER_TEL", user.getUSER_TEL());
//                            editor.putString("USER_DEPT_NAME", user.getUSER_DEPT_NAME());
//                            editor.putString("USER_DEPT_ORG_CODE", user.getUSER_DEPT_ORG_CODE());
//                            editor.commit();
//                            List<PactInfo> pactInfos = new ArrayList<PactInfo>();
//                            List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
//                            List<ProInfo>  proInfos  = new ArrayList<ProInfo>();
//                            List<MtlInfo>  mtlInfos  = new ArrayList<MtlInfo>();
//                            pactInfos = serverBean.getHtList();
//                            itemInfos = serverBean.getXmList();
//                            proInfos  = serverBean.getCpList();
//                            mtlInfos  = serverBean.getMtList();
//                            Log.i(TAG, "pactInfos：" + pactInfos);
//                            Log.i(TAG, "mtInfos：" + mtlInfos);
//                            Log.i(TAG, "proInfos：" + proInfos);
//                            Log.i(TAG, "itemInfos：" + itemInfos);
//                            try {
//                                db.saveOrUpdate(pactInfos);
//                            } catch (DbException ex) {
//                                ex.printStackTrace();
//                            }
//                            try {
//
//                                db.saveOrUpdate(itemInfos);
//                            } catch (DbException ex) {
//                                ex.printStackTrace();
//                            }
//                            try {
//
//                                db.saveOrUpdate(proInfos);
//
//                            } catch (DbException ex) {
//                                ex.printStackTrace();
//                            }
//                            try {
//                                db.saveOrUpdate(mtlInfos);
//
//                            } catch (DbException ex) {
//                                ex.printStackTrace();
//                            }
//                        }
////                解析result
//                        initData();
//                    }
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//                    }
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//                    }
//                    @Override
//                    public void onFinished() {
//                    }
//                });
//
//            }
//            /**
//             * 检查是否可以执行下来刷新，比如列表为空或者列表第一项在最上面时。
//             */
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                System.out.println("MainActivity.checkCanDoRefresh");
//                // 默认实现，根据实际情况做改动
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//                // return true;
//            }
//        });
//
//    }
//}
