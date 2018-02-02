package com.eostek.rick.demo.designviewgroup.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import com.eostek.rick.demo.designviewgroup.DataBean.Meizi;
import com.eostek.rick.demo.designviewgroup.R;
import com.eostek.rick.demo.designviewgroup.utils.ViewUtils;

/**
 * Created by a on 17-6-20.
 */

public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private Context mContext;

    private List<Meizi> datas;//数据

    //150dp
    private int mSizeHeight;

    //50dp
    private int mSizeWidth;

    //自定义监听事件
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);

        boolean onItemLongClick(View view);
    }

    private OnRecyclerViewItemClickListener listener;

    public void setItemListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v);
        }
    }


    @Override
    public boolean onLongClick(View v) {
        if (listener != null) {
            return listener.onItemLongClick(v);
        }
        return false;
    }

    public GridAdapter(Context mContext, List<Meizi> mlist) {
        this.mContext = mContext;
        this.datas = mlist;
        mSizeHeight = ViewUtils.dip2px(mContext, 150);
        mSizeWidth = ViewUtils.dip2px(mContext, 50);
    }

    @Override
    public int getItemViewType(int position) {
        //判断item类别，是图还是显示页数（图片有URL）
        if (!TextUtils.isEmpty(datas.get(position).getUrl())) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.grid_meizi_item, parent,
                    false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            return viewHolder;
        } else {
            View view = LayoutInflater.from(
                    mContext).inflate(R.layout.page_item, parent, false);
            MyViewHolder2 holder2 = new MyViewHolder2(view);//textview用来显示页数
            return holder2;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {
            Glide.with(mContext).
                    load(datas.get(position).getUrl()).
                    asBitmap().
                    error(R.mipmap.loading2).
                    diskCacheStrategy(DiskCacheStrategy.ALL).
                    into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {

                            ((MyViewHolder) holder).iv.setImageBitmap(createScaledBitmap(bitmap));


                            /*ObjectAnimator animator = ObjectAnimator.ofFloat(((MyViewHolder) holder).iv, "alpha", 0.5f, 1f);
                            //ObjectAnimator animator1 = ObjectAnimator.ofFloat(((MyViewHolder) holder).iv, "rotation", 0f, 360f);

                            //ObjectAnimator animatorY = ObjectAnimator.ofFloat(((MyViewHolder) holder).iv, "scaleY", 1f, 1.1f, 1f);
                            //ObjectAnimator animatorX = ObjectAnimator.ofFloat(((MyViewHolder) holder).iv, "scaleX", 1f, 1.1f, 1f);
                            AnimatorSet set = new AnimatorSet();
                            //set.play(animator).with(animatorX).with(animatorY);
                            set.play(animator);
                            set.setInterpolator(new BounceInterpolator());
                            set.setDuration(500);
                            set.start();*/
                            //((MyViewHolder) holder).iv.animate().scaleX(1.1f).scaleY(1.1f).alpha(0.5f).setDuration(1000).start();
                            //((MyViewHolder) holder).iv.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(1000).setInterpolator(new BounceInterpolator()).start();
                        }
                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                        }

                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                        }
                    });

        } else if (holder instanceof MyViewHolder2) {
            ((MyViewHolder2) holder).tv.setText(datas.get(position).getPage() + "页");
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //添加一个item
    public void addItem(Meizi meizi, int position) {
        datas.add(position, meizi);
        notifyItemInserted(position);
        //recyclerview.scrollToPosition(position);//recyclerview滚动到新加item处
    }

    //删除一个item
    public void removeItem(final int position) {
        if(position >= getItemCount()){
            return;
        }
        datas.remove(position);
        notifyItemRemoved(position);
    }

    //自定义ViewHolder，用于加载图片
    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageButton iv;

        public MyViewHolder(View view) {
            super(view);
            iv = (ImageButton) view.findViewById(R.id.iv);
            iv.animate();
        }
    }

    //自定义ViewHolder，用于显示页数
    class MyViewHolder2 extends RecyclerView.ViewHolder {
        private TextView tv;

        public MyViewHolder2(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);
        }
    }


    public Bitmap createScaledBitmap(Bitmap bitmap) {

        int winWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        int winHeight = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int reqW = winWidth / 4;
        int reqH = winHeight / 4;
        float widthMultiple = w / reqW * 1.0f;
        float heithMultiple = h / reqH * 1.0f;
        float multiple = Math.max(widthMultiple, heithMultiple);
        w = (int) (w / multiple);
        h = (int) (h / multiple);
        return Bitmap.createScaledBitmap(bitmap, w, h, true);
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int winWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        int winHeight = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        int reqW = winWidth / 4;
        int reqH = winHeight / 4;
        float widthMultiple = w / reqW * 1.0f;
        float heithMultiple = h / reqH * 1.0f;
        float multiple = Math.max(widthMultiple, heithMultiple);
        w = (int) (w / multiple);
        h = (int) (h / multiple);

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        /*// 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);*/
        return bitmap;
    }
}
