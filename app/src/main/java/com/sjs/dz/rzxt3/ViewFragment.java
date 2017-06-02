package com.sjs.dz.rzxt3;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
public class ViewFragment extends Fragment{
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
    private ImageView MessageImg;

    private int page;
    private  List<PactInfo> pactInfos;
    // The fragment argument representing
    // the section number for this fragment.
    private static final String ARG_SECTION_NUMBER = "section_number";

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

//        switch (page) {
//            case 0:
//                sectionImg.setBackgroundResource(R.drawable.ic_beenhere_black_24dp);
//                sectionLabel.setText(R.string.onboarding_section_1);
//                sectionIntro.setText(R.string.onboarding_intro_1);
//                break;
//            case 1:
//                sectionImg.setBackgroundResource(R.drawable.ic_camera_black_24dp);
//                sectionLabel.setText(R.string.onboarding_section_2);
//                sectionIntro.setText(R.string.onboarding_intro_2);
//                break;
//            case 2:
//                sectionImg.setBackgroundResource(R.drawable.ic_notifications_black_24dp);
//                sectionLabel.setText(R.string.onboarding_section_3);
//                sectionIntro.setText(R.string.onboarding_intro_3);
//                break;
//            default:
//                break;
//        }
        initData();
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

        return view;
    }

    private void initViews(View view) {
        tv_pact_no= (TextView) view.findViewById(R.id.pact_no);
        pact_start_date= (TextView) view.findViewById(R.id.pact_start_date);
        tv_pact_end_date= (TextView) view.findViewById(R.id.pact_end_date);
        tv_com_con_name= (TextView) view.findViewById(R.id.com_con_name);
        tv_com_con_ide= (TextView) view.findViewById(R.id.com_con_ide);

        tv_com_name= (TextView)view.findViewById(R.id.com_name);
        tv_tel= (TextView) view.findViewById(R.id.com_con_tel);
        tv_addr= (TextView) view.findViewById(R.id.com_addr);

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
}
