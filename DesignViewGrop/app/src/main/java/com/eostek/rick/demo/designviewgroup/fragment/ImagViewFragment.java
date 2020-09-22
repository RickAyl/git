package com.eostek.rick.demo.designviewgroup.fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.eostek.rick.demo.designviewgroup.DataBean.Meizi;
import com.eostek.rick.demo.designviewgroup.R;
import com.eostek.rick.demo.designviewgroup.RecylerviewActivity;
import com.eostek.rick.demo.designviewgroup.adapter.GridAdapter;
import com.eostek.rick.demo.designviewgroup.dialog.ImageViewDialog;
import com.eostek.rick.demo.designviewgroup.utils.ViewUtils;
import com.eostek.rick.demo.designviewgroup.view.DesignedToast;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by a on 17-6-29.
 */

public class ImagViewFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    public static final String FULIURL = "http://gank.io/api/data/福利/10/1";

    public static final String ANDROID = "http://gank.io/api/data/Android/10/1";

    private static boolean mAllowNextNetWorkTask = true;

    private GridLayoutManager mGridLayoutManager;

    private LinearLayoutManager mLinearLayoutManager;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    //private RelativeLayout mRelativeLayout;

    private ProgressBar mProgressBar;

    private GridAdapter mAdapter;

    private List<Meizi> mMeiziList;

    private int page = 1;

    private int lastVisibleItem = 0;

    private int startNotifyItem = 0;

    private int endNotifyItem = 0;

    private Context mActivity;

    private Set<NetWorkAsyncTask> netWorkSet = new HashSet<NetWorkAsyncTask>();

    public GridAdapter getGridAdapter() {
        return mAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("ricks", "window width dp : " + ViewUtils.px2dip(getActivity(), 1080));
        Log.v("ricks", "window height dp : " + ViewUtils.px2dip(getActivity(), 1920));
        mActivity = getActivity();
        WindowManager wm = ((Activity)mActivity).getWindowManager();
        Point p = new Point();
        wm.getDefaultDisplay().getSize(p);
        Log.d("ricks","p(x,y) : " +"("+p.x+","+p.y+")");
        View imageViewFragment = inflater.inflate(R.layout.image_fragment, container, false);
        initView(imageViewFragment);
        return imageViewFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            cancelAllTask();
            if (mMeiziList != null) {
                mMeiziList.clear();
                startNotifyItem = 0;
            }
            page = 1;
            NetWorkAsyncTask work = new NetWorkAsyncTask();
            work.execute(FULIURL);
            netWorkSet.add(work);
        } else {
            setListener();
            NetWorkAsyncTask work = new NetWorkAsyncTask();
            work.execute(FULIURL);
            netWorkSet.add(work);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.gridview_swiperefresh);
        mSwipeRefreshLayout.
                setProgressViewOffset(true, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        //mRelativeLayout = (RelativeLayout) view.findViewById(R.id.relativelayout);
        //mProgressBar = (ProgressBar) view.findViewById(R.id.loading_bar);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.grid_recycler);
        mGridLayoutManager = new GridLayoutManager(this.getActivity(), 3, GridLayoutManager.VERTICAL, false);
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);

        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(),
        //DividerItemDecoration.VERTICAL_LIST));
    }

    private void setListener() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent),getResources().getColor(R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cancelAllTask();
                if (mMeiziList != null) {
                    mMeiziList.clear();
                    startNotifyItem = 0;
                }
                page = 1;
                NetWorkAsyncTask work = new NetWorkAsyncTask();
                netWorkSet.add(work);
                work.execute(FULIURL);

            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                // 滑动状态停止并且剩余少于两个item时，自动加载下一页
/*                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem +2>=mGridLayoutManager.getItemCount()) {
                    new NetWorkAsyncTask().execute("http://gank.io/api/data/福利/10/"+(++page));
                }*/
                Log.d("error","mAllowNextNetWorkTask : " + mAllowNextNetWorkTask);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && (!mRecyclerView.canScrollVertically(1) || (lastVisibleItem + 1) % 3 != 0) && mAllowNextNetWorkTask) {
                    NetWorkAsyncTask netWork = new NetWorkAsyncTask();
                    netWork.execute("http://gank.io/api/data/福利/10/" + (++page));
                    netWorkSet.add(netWork);

                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING
                        || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    //cancelAllTask();
                    //mAllowNextNetWorkTask = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取加载的最后一个可见视图在适配器的位置。
                lastVisibleItem = mGridLayoutManager.findLastVisibleItemPosition();
                //cancelAllTask();
            }
        });
    }

    public void cancelAllTask() {

        synchronized (netWorkSet) {
            if (netWorkSet == null) {
                return;
            }
            Iterator<NetWorkAsyncTask> iterator = netWorkSet.iterator();
            while (iterator.hasNext()) {
                NetWorkAsyncTask task = iterator.next();
                task.cancel(true);
            }
        }
    }

    public class NetWorkAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            mAllowNextNetWorkTask = false;
            String stringResult = null;
            RecylerviewActivity.MyOkhttp okHttp = new RecylerviewActivity.MyOkhttp();
            stringResult = okHttp.get(params[0]);
            Log.v("tag", "okHttp.get(params[0]) : " + stringResult);
            return stringResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //mRelativeLayout.setVisibility(View.GONE);
            //mSwipeRefreshLayout.removeView(mRelativeLayout);
            mRecyclerView.setVisibility(View.VISIBLE);
            JSONObject jsonObject;
            Gson gson = new Gson();
            String jsonData = null;
            if (TextUtils.isEmpty(s)) {
                Log.v("tag", "s is empty");
                return;
            }
            try {
                jsonObject = new JSONObject(s);
                jsonData = jsonObject.getString("results");
                Log.v("tag1", "jsonData : " + jsonData.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (mMeiziList == null || mMeiziList.size() == 0) {
                List<Meizi> data = gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {
                }.getType());
                //此处有大坑
                if (mMeiziList == null) {
                    mMeiziList = data;
                } else {
                    mMeiziList.addAll(data);
                }
                for (Meizi mz : mMeiziList) {
                    Log.v("datas", "meizi  : " + "\r\n" + mz.toString() + "\r\n");
                }
                Meizi pages = new Meizi();
                pages.setPage(page);
                mMeiziList.add(pages);//在数据链表中加入一个用于显示页数的item
            } else {
                List<Meizi> more = gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {
                }.getType());

                startNotifyItem = mMeiziList.size();
                mMeiziList.addAll(more);
                Meizi pages = new Meizi();
                pages.setPage(page);
                mMeiziList.add(pages);//在数据链表中加入一个用于显示页数的item
                endNotifyItem = mMeiziList.size() - 1;
            }
            netWorkSet.remove(this);
            mSwipeRefreshLayout.setRefreshing(false);
            mAllowNextNetWorkTask = true;
            if (mAdapter == null) {
                mRecyclerView.setAdapter(mAdapter = new GridAdapter(ImagViewFragment.this.getActivity(), mMeiziList));//recyclerview设置适配器

                //实现适配器自定义的点击监听
                mAdapter.setItemListener(new GridAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view) {
                        int position = mRecyclerView.getChildAdapterPosition(view);
                        //SnackbarUtil.ShortSnackbar(coordinatorLayout,"点击第"+position+"个",SnackbarUtil.Info).show();
                        ImageViewDialog dialog =
                                new ImageViewDialog(ImagViewFragment.this.getActivity(),
                                        R.style.Dialog_Fullscreen,
                                        mMeiziList.get(position).getUrl());
                        dialog.show();
                    }

                    @Override
                    public boolean onItemLongClick(final View view) {
                        int position = mRecyclerView.getChildAdapterPosition(view);
                        String url = mMeiziList.get(position).getUrl();


                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog);
                        builder.setTitle("Notice");
                        builder.setMessage("Do you want to download this picture ?");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int position = mRecyclerView.getChildAdapterPosition(view);
                                String url = mMeiziList.get(position).getUrl();
                                DownloadManager downloadManager =
                                        (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);

                                //创建一个Request对象
                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                                //设置下载文件路径
                                String[] list = url.split("/");
                                String fileName = list[list.length - 1];
                                //request.setDestinationInExternalPublicDir("Meizi", fileName);
                                request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_PICTURES,fileName);
                                //开始下载
                                long downloadId = downloadManager.enqueue(request);

                                /*Toast.makeText(ImagViewFragment.this.getActivity(), "Begin to download ! fileName : " + fileName, Toast.LENGTH_SHORT)
                                        .show();*/
                                DesignedToast.makeText(getActivity(),"Begin to download ! fileName : " + fileName,Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                        return true;
                    }
                });
            } else {
                //刷新数据
                if (startNotifyItem == 0) {
                    mAdapter.notifyDataSetChanged();
                } else {
                    //only reflush added Item
                    mAdapter.notifyItemRangeInserted(startNotifyItem, endNotifyItem);
                }
            }
        }
    }
}
