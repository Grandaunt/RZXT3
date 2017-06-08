package com.sjs.dz.rzxt3.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.sjs.dz.rzxt3.DB.MtlInfo;
import com.sjs.dz.rzxt3.R;
import com.sjs.dz.rzxt3.base.MyApplication;
import com.sjs.dz.rzxt3.utils.FileUtils;
import com.sjs.dz.rzxt3.utils.MyUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SJS on 2017/1/4.
 */


public class TaskInfoGet1ReAdapter extends RecyclerView.Adapter<TaskInfoGet1ReAdapter.ViewHolder> {
    private String TAG = this.getClass().getSimpleName();
    private LayoutInflater mInflater;
    private String mtl_name;
    private String item_no;
    private  Context mContext;
    private  List<MtlInfo> mtlInfos;
    public TaskInfoGet1ReAdapter(Context context, String item_no) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext=context;
        this.item_no=item_no;
        MyApplication myApplication=new MyApplication();
        DbManager db = x.getDb(myApplication.getDaoConfig());
        mtlInfos = new ArrayList<MtlInfo>();
        try {
            mtlInfos = db.selector(MtlInfo.class)
                    .where("item_no","=",item_no)
                    .and("mtl_type","=","7")
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(mtlInfos == null || mtlInfos.size() == 0){
            Log.i(TAG,"TaskInfoGet1ReAdapter:mtlInfos.size"+mtlInfos.size()+"未查到数据");
        }
        else {
            Log.i(TAG, "mtlInfos.size" + mtlInfos.size());
            Log.i(TAG, "mtlInfos.size" + mtlInfos.get(0));
        }
    }

    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_info_get,parent,false);
        int screenWidth = MyUtils.getScreenMetrics(mInflater.getContext()).widthPixels;
        int space = (int) MyUtils.dp2px(mInflater.getContext(), 30f);
        int mImageWidth = (screenWidth - space) / 4;
        LinearLayout item=(LinearLayout)view.findViewById(R.id.item_get);
        item.setLayoutParams(new RecyclerView.LayoutParams(mImageWidth,250));
//        View view = mInflater.inflate(R.layout.item_info_download), parent, false);
        //view.setBackgroundColor(Color.RED);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
//        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
//                HomeActivity.this).inflate(R.layout.item_home, parent,
//                false));
//        return holder;
    }



    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


//        Log.i(TAG,"onBindViewHolder+position="+position);
        //初始化数据
        mtl_name= mtlInfos.get(position).getMtl_name().toString();
//        Log.i(TAG,"DownInfo="+GetInfo[position]);
//        Log.i(TAG,"type[0]="+type[0]);
//        Log.i(TAG,"type[1]="+type[1]);
        String path   =Environment.getExternalStorageDirectory().getAbsolutePath() +  File.separator +"RZXT/"+item_no+"/upload/"+mtl_name;;
        File f = new File(path, mtl_name);
        if (!f.exists()) {
            Log.i(TAG,mtl_name+"照片不存在");
            holder.im.setImageResource(R.mipmap.im_photo_defult);

        }
        else{
            Bitmap zoombm = FileUtils.getCompressBitmap(path+mtl_name);
            holder.im.setImageBitmap(zoombm);
        }


        // 如果设置了回调，则设置点击事件

        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mtl_name= mtlInfos.get(pos).getMtl_name().toString();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos,mtl_name);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mtl_name= mtlInfos.get(pos).getMtl_name().toString();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos,mtl_name);
                    return false;
                }
            });
        }
        else{
            Log.i(TAG,"mOnItemClickLitener = null");
        }
    }

    @Override
    public int getItemCount() {

        return mtlInfos.size()+1;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView im ;
//        public TextView tv;

        public ViewHolder(View view) {
            super(view);
            im = (ImageView)view.findViewById(R.id.item_im_get);
//            tv = (TextView) view.findViewById(R.id.item_tv_get);
        }
    }

    public void addData(int position) {
//        mDatas.add(position, "Insert One");
        notifyItemInserted(position);
    }

    public void removeData(int position) {
//        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position, String numm);
        void onItemLongClick(View view, int position, String numm);
    }

    private OnItemClickLitener mOnItemClickLitener;
    private AdapterView.OnItemLongClickListener mOnItemLongClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
        this.mOnItemLongClickLitener =mOnItemLongClickLitener;

    }



}