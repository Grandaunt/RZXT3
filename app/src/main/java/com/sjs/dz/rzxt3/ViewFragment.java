package com.sjs.dz.rzxt3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.AppCompatTextView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.sjs.dz.rzxt3.DB.PactInfo;
import com.sjs.dz.rzxt3.base.MyApplication;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewFragment extends Fragment implements OnGetGeoCoderResultListener ,View.OnClickListener{

    private String TAG = this.getClass().getSimpleName();
    // TODO: Rename and change types of parameters
    private String URL="http://172.16.10.242:8080/bcm_rz/appInterface";
    private MyApplication myApplication;
    private OnFragmentInteractionListener mListener;
    private TextView tv_pact_no,tv_pact_start_date,tv_pact_end_date,tv_com_name,tv_com_con_name,tv_com_con_ide,tv_com_con_tel,tv_addr,onclick1,onclick2;
    private ImageView im_message,im_tel,im_addr;
    private Button bt_upload;
    private int page;
    private  List<PactInfo> pactInfos;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private BaiduMap mBaiduMap = null;
    private MapView mMapView = null;
    // 天安门坐标
    double mLat1 = 39.915291;
    double mLon1 = 116.403857;
    // 百度大厦坐标
    double mLat2 = 40.056858;
    double mLon2 = 116.308194;
    public ViewFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ViewFragment newInstance(int sectionNumber) {
        ViewFragment fragment = new ViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        else{
            Log.i(TAG,"getArguments() == null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        initViews(view);
        initData();
        setView();

        return view;
    }
    //初始化控件
    private void initViews(View view) {
        tv_pact_no= (TextView) view.findViewById(R.id.tv_pact_no);
        tv_pact_start_date= (TextView) view.findViewById(R.id.tv_pact_start_date);
        tv_pact_end_date= (TextView) view.findViewById(R.id.tv_pact_end_date);
        tv_com_con_name= (TextView) view.findViewById(R.id.tv_com_con_name);
        tv_com_con_ide= (TextView) view.findViewById(R.id.tv_com_con_ide);

        tv_com_name= (TextView)view.findViewById(R.id.tv_com_name);
        tv_com_con_tel= (TextView) view.findViewById(R.id.tv_com_con_tel);
        bt_upload=(Button)view.findViewById(R.id.btn_upload) ;
        tv_addr= (TextView) view.findViewById(R.id.tv_com_addr);
        im_addr=(ImageView) view.findViewById(R.id.im_com_addr);

        im_message=(ImageView)view.findViewById(R.id.im_con_con_tel_dx);
        im_tel=(ImageView) view.findViewById(R.id.im_con_con_tel);
        onclick1=(TextView) view.findViewById(R.id.onclick1);
        onclick2=(TextView) view.findViewById(R.id.onclick2);

        // 地图初始化
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        bt_upload.setOnClickListener(this);
        tv_addr.setOnClickListener(this);
        im_addr.setOnClickListener(this);
        tv_com_con_tel.setOnClickListener(this);
        im_tel.setOnClickListener(this);

        im_message.setOnClickListener(this);
        tv_pact_no.setOnClickListener(this);
        tv_pact_start_date.setOnClickListener(this);
        tv_pact_end_date.setOnClickListener(this);
        tv_com_con_name.setOnClickListener(this);

        tv_com_con_ide.setOnClickListener(this);
        tv_com_name.setOnClickListener(this);
        onclick1.setOnClickListener(this);
        onclick2.setOnClickListener(this);
    }

    //初始化数据
    private void initData() {
        myApplication=new MyApplication();
        DbManager db = x.getDb(myApplication.getDaoConfig());
        pactInfos = new ArrayList<PactInfo>();
        try {
            pactInfos = db.findAll(PactInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        if(pactInfos == null || pactInfos.size() == 0){
            Toast.makeText(getActivity(),"无任务信息",Toast.LENGTH_LONG).show();
            return;//请先调用dbAdd()方法
        }
        else{
            for (int i=0;i<pactInfos.size();i++)
            Log.i(TAG,pactInfos.get(i).getPact_no());
        }
    }

    //控件注入数据
    private void setView() {
        Log.i(TAG,"page="+page);
        tv_pact_no.setText(pactInfos.get(page).getPact_no());
        tv_pact_start_date.setText(pactInfos.get(page).getPact_start_date());
        tv_pact_end_date.setText(pactInfos.get(page).getPact_end_date());
        tv_com_con_name.setText(pactInfos.get(page).getPact_com_con_name());
        tv_com_con_ide.setText(pactInfos.get(page).getPact_com_con_ide());

        tv_com_name.setText(pactInfos.get(page).getPact_com_name());
        tv_com_con_tel.setText(pactInfos.get(page).getPact_com_con_tel());
        tv_addr.setText(pactInfos.get(page).getPact_com_addr());

        /**
         * 发起搜索
         *
         * @param v
         */
        mSearch.geocode(new GeoCodeOption().city(
                "北京市").address("海淀区上地十街10号"));
    }

    //动态监听
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btn_upload:
                Log.i(TAG,"onClick.btn_upload");
                upload();
                break;
            case  R.id.tv_com_addr:
                Log.i(TAG,"onClick.tv_com_addr");
                startNavi();
                break;
            case  R.id.im_com_addr:
                Log.i(TAG,"onClick.im_com_addr");
                startNavi();
                break;
            case R.id.tv_com_con_tel:
                Log.i(TAG,"onClick.tv_com_con_tel");
                cellTel();
                break;
            case R.id.im_con_con_tel:
                Log.i(TAG,"onClick.im_con_con_tel");
                cellTel();
                break;
            case R.id.im_con_con_tel_dx:
                Log.i(TAG,"onClick.im_con_con_tel");
                cellMessage();
                break;
            case R.id.tv_pact_no:
                Log.i(TAG,"onClick.tv_pact_no");
               onclickItem();
                break;
            case R.id.tv_com_con_name:
                Log.i(TAG,"onClick.tv_com_con_name");
                onclickItem();
                break;
            case R.id.tv_com_name:
                Log.i(TAG,"onClick.tv_com_name");
                onclickItem();
                break;
            case R.id.tv_com_con_ide:
                Log.i(TAG,"onClick.tv_com_con_ide");
                onclickItem();
                break;
            case R.id.tv_pact_start_date:
                Log.i(TAG,"onClick.tv_pact_start_date");
                onclickItem();
                break;
            case R.id.tv_pact_end_date:
                Log.i(TAG,"onClick.tv_pact_end_date");
                onclickItem();
                break;
            case R.id.onclick1:
                Log.i(TAG,"onClick.onclick1");
                onclickItem();
                break;
            case R.id.onclick2:
                Log.i(TAG,"onClick.onclick2");
                onclickItem();
                break;

            default:
                break;
        }
    }
//合同资料上传
    private void upload() {
        RequestParams params = new RequestParams(URL+"/makesure");

        //根据当前请求方式添加参数位置
        params.addParameter("TASK_ID",pactInfos.get(page).getTask_id() );
        params.addParameter("APPLY_ID", pactInfos.get(page).getApply_id());
        Log.i(TAG, "params："+params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "result："+result);
                //成功则改变任务状态 失败则Toast
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
    //打开合同详情
    private void onclickItem() {
        Intent intent = new Intent(getActivity(),ItemActivity.class);
        intent.putExtra("pact_no", tv_pact_no.getText().toString());
        startActivity(intent);
    }
    //发送短信
    private void cellMessage() {
        //发短息
        String phone = tv_com_con_tel.getText().toString();
        Uri smsToUri = Uri.parse("smsto:"+phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", "");
        startActivity(intent);

    }
    //拨打电话
    private void cellTel() {
        //激活可以打电话的组件
        String phone = tv_com_con_tel.getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }


    //文字信息查找地理位置
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.icon_marka)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
        Log.i(TAG,"地理位置："+strInfo);
//        Toast.makeText(getActivity(), strInfo, Toast.LENGTH_LONG).show();
    }

    //经纬度查找地理位置
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(getActivity(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
//                    .show();
//            return;
//        }
//        mBaiduMap.clear();
//        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.mipmap.icon_marka)));
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
//                .getLocation()));
//        Toast.makeText(getActivity(), result.getAddress(),
//                Toast.LENGTH_LONG).show();

    }

    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("您确认打开百度地图客户端？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LatLng pt1 = new LatLng(mLat1, mLon1);
                LatLng pt2 = new LatLng(mLat2, mLon2);

                // 构建 导航参数
                NaviParaOption para = new NaviParaOption()
                        .startPoint(pt1).endPoint(pt2)
                        .startName("天安门").endName("百度大厦");

                try {
                    BaiduMapNavigation.openBaiduMapNavi(para, getActivity());
                } catch (BaiduMapAppNotSupportNaviException e) {
                    e.printStackTrace();
                    showDialog();
                }
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(getActivity());
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();


    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(getActivity());
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mSearch.destroy();
        super.onDestroy();
    }
}
