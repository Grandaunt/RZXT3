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
import org.xutils.ex.DbException;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String TAG = this.getClass().getSimpleName();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String section_number;
    private MyApplication myApplication;
    private OnFragmentInteractionListener mListener;
    private  List<Map<String, Object>>  pcatList;

    private TextView tv_pact_no,pact_start_date,tv_pact_end_date,tv_com_name,tv_com_con_name,tv_com_con_ide,tv_tel,tv_addr;
    private ImageView im_message,im_addr;
    private Button bt_upload;
    private int page;
    private  List<PactInfo> pactInfos;
    // The fragment argument representing
    // the section number for this fragment.
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_view, container, false);
        initViews(view);
        initData();
        setView();

        return view;
    }
    //初始化控件
    private void initViews(View view) {
        tv_pact_no= (TextView) view.findViewById(R.id.pact_no);
        pact_start_date= (TextView) view.findViewById(R.id.pact_start_date);
        tv_pact_end_date= (TextView) view.findViewById(R.id.pact_end_date);
        tv_com_con_name= (TextView) view.findViewById(R.id.com_con_name);
        tv_com_con_ide= (TextView) view.findViewById(R.id.com_con_ide);

        tv_com_name= (TextView)view.findViewById(R.id.com_name);
        tv_tel= (TextView) view.findViewById(R.id.com_con_tel);
        bt_upload=(Button)view.findViewById(R.id.btn_upload) ;
        tv_addr= (TextView) view.findViewById(R.id.tv_com_addr);
        im_addr=(ImageView) view.findViewById(R.id.im_com_addr);


        // 地图初始化
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        bt_upload.setOnClickListener(this);
        tv_addr.setOnClickListener(this);
        im_addr.setOnClickListener(this);
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
            return;//请先调用dbAdd()方法
        }
        else{
            Log.i(TAG,"pactInfos.size"+pactInfos.size());
//            for (int i=0;i<pactInfos.size();i++){
//                pcatList = new ArrayList<Map<String, Object>>();
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("pact_no", pactInfos.get(i).getPact_no());
//                map.put("pact_start_date", pactInfos.get(i).getPact_start_date());
//                map.put("pact_end_date", pactInfos.get(i).getPact_end_date());
//                map.put("pact_com_name",  pactInfos.get(i).getPact_com_name());
//                map.put("pact_com_addr",  pactInfos.get(i).getPact_com_addr());
//                map.put("pact_com_tel",  pactInfos.get(i).getPact_com_tel());
//                map.put("pact_com_con_name", pactInfos.get(i).getPact_com_con_name());
//                map.put("pact_com_con_ide",  pactInfos.get(i).getPact_com_con_ide());
//                pcatList.add(map);
//            }
//            Log.i(TAG,"pcatList.size"+pcatList.size());
        }
    }

    //控件注入数据
    private void setView() {
        tv_pact_no.setText(pactInfos.get(page).getPact_no());
        pact_start_date.setText(pactInfos.get(page).getPact_start_date());
        tv_pact_end_date.setText(pactInfos.get(page).getPact_end_date());
        tv_com_con_name.setText(pactInfos.get(page).getPact_com_con_name());
        tv_com_con_ide.setText(pactInfos.get(page).getPact_com_con_ide());

        tv_com_name.setText(pactInfos.get(page).getPact_com_name());
        tv_tel.setText(pactInfos.get(page).getPact_com_con_tel());
        tv_addr.setText(pactInfos.get(page).getPact_com_addr());
//        ListAdapter adapter = new SimpleAdapter(getActivity(), pcatList, R.layout.fragment_view,
//                              new String[]{"pact_no", "pact_start_date", "pact_end_date","pact_com_name", "pact_com_addr", "pact_com_tel", "pact_com_con_name", "pact_com_con_ide"},
//                              new int[]{R.id.pact_no, R.id.pact_start_date, R.id.pact_end_date,R.id.com_name,R.id.com_addr, R.id.com_con_tel, R.id.com_con_name, R.id.com_con_ide});
//        setListAdapter(adapter);


//        bt_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),GeoCoderDemo.class);
//                startActivity(intent);
//            }
//        });

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
                Log.i(TAG,"btn_upload");
                Intent intent = new Intent(getActivity(),GeoCoderDemo.class);
                startActivity(intent);
                break;
            case  R.id.tv_com_addr:
                Log.i(TAG,"tv_com_addr");
                startNavi();
                break;
            case  R.id.im_com_addr:
                Log.i(TAG,"im_com_addr");
                startNavi();
                break;
            default:
                break;
        }
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
    public String toString() {
        return super.toString();
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
