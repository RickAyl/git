package viewgroup.demo.eostek.com.designviewgroup;

import android.app.Activity;
import android.app.DownloadManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import viewgroup.demo.eostek.com.designviewgroup.DataBean.Meizi;
import viewgroup.demo.eostek.com.designviewgroup.adapter.GridAdapter;
import viewgroup.demo.eostek.com.designviewgroup.dialog.ImageViewDialog;
import viewgroup.demo.eostek.com.designviewgroup.utils.ViewUtils;
import viewgroup.demo.eostek.com.designviewgroup.view.DividerItemDecoration;

import static android.R.attr.progressBarStyleSmall;

/**
 * Created by a on 17-6-16.
 */

public class RecylerviewActivity extends AppCompatActivity {

    private List<Meizi> mMeiziList;

    private int page = 1;

    public static final String BAIDU_URL = "http://www.baidu.com";

    public static final String FULIURL = "http://gank.io/api/data/福利/10/1";

    private NetWorkAsyncTask mNetWork;

    private int lastVisibleItem;

    private RequestQueue mNetWorkQueue;

    private RecyclerView mRecyclerView;

    private GridLayoutManager mGridLayoutManager;

    private LinearLayoutManager mLinearLayoutManager;

    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ProgressBar mProgressBar;

    private GridAdapter mAdapter;

    private String result = null;

    private Set<NetWorkAsyncTask> netWorkSet = new HashSet<NetWorkAsyncTask>();

    private RelativeLayout.LayoutParams relayoutParams;

    private RelativeLayout relativeLayout;

    private TabLayout mTabLayout;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerviewlayout);
        init();
        addTab();
        setToolTitle();
        setListener();
        NetWorkAsyncTask work = new NetWorkAsyncTask();
        work.execute(FULIURL);
        netWorkSet.add(work);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setToolTitle(){
        mToolbar.setTitle("AcFun");
        mToolbar.setSubtitle("");
        mToolbar.setTitleMarginStart(0);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        //mToolbar.setNavigationIcon(R.mipmap.ic_launcher);
        mToolbar.inflateMenu(R.menu.app_bar_menu);
        //setSupportActionBar(mToolbar);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.v("menu","===================Menuclick");
                return false;
            }
        });
    }

    private void addTab(){
        mTabLayout.addTab(mTabLayout.newTab().setText("美图"));
        mTabLayout.addTab(mTabLayout.newTab().setText("电视剧"));
        mTabLayout.addTab(mTabLayout.newTab().setText("电影"));
        mTabLayout.addTab(mTabLayout.newTab().setText("综艺"));
        mTabLayout.addTab(mTabLayout.newTab().setText("鬼畜"));
        mTabLayout.addTab(mTabLayout.newTab().setText("动漫"));
        mTabLayout.addTab(mTabLayout.newTab().setText("直播"));

    }

    private void init() {
        mNetWorkQueue = Volley.newRequestQueue(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.grid_swipe_refresh);
        mSwipeRefreshLayout.
                setProgressViewOffset(true, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mRecyclerView = (RecyclerView) findViewById(R.id.grid_recycler);
        mGridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
    }

    private void setListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cancelAllTask();
                if (mMeiziList != null){
                    mMeiziList.clear();
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && !mRecyclerView.canScrollVertically(1)) {
                    NetWorkAsyncTask netWork = new NetWorkAsyncTask();
                    netWork.execute("http://gank.io/api/data/福利/10/" + (++page));
                    netWorkSet.add(netWork);

                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING
                        || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    cancelAllTask();
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
            if (netWorkSet == null){
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
            String stringResult = null;
            MyOkhttp okHttp = new MyOkhttp();
            stringResult = okHttp.get(params[0]);
            Log.v("tag", "okHttp.get(params[0]) : " + stringResult);
            return stringResult;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObject;
            Gson gson = new Gson();
            String jsonData = null;
            if (s == null || s.equals("")) {
                Log.v("tag", "s is empty");
                return;
            }
            try {
                jsonObject = new JSONObject(s);
                jsonData = jsonObject.getString("results");
                Log.v("tag", "jsonData : " + jsonData.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (mMeiziList == null || mMeiziList.size() == 0) {
                List<Meizi> data = gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {
                }.getType());
                //此处有大坑
                if (mMeiziList == null) {
                    mMeiziList = data;
                }else{
                    mMeiziList.addAll(data);
                }
                for (Meizi mz : mMeiziList) {
                    Log.v("rick3", "meizi  : " + "\r\n" + mz.toString() + "\r\n");
                }
                Meizi pages = new Meizi();
                pages.setPage(page);
                mMeiziList.add(pages);//在数据链表中加入一个用于显示页数的item
            } else {
                List<Meizi> more = gson.fromJson(jsonData, new TypeToken<List<Meizi>>() {
                }.getType());
                mMeiziList.addAll(more);

                Meizi pages = new Meizi();
                pages.setPage(page);
                mMeiziList.add(pages);//在数据链表中加入一个用于显示页数的item
            }
            if (mAdapter == null) {
                mRecyclerView.setAdapter(mAdapter = new GridAdapter(RecylerviewActivity.this, mMeiziList));//recyclerview设置适配器

                //实现适配器自定义的点击监听
                mAdapter.setItemListener(new GridAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view) {
                        int position = mRecyclerView.getChildAdapterPosition(view);
                        //SnackbarUtil.ShortSnackbar(coordinatorLayout,"点击第"+position+"个",SnackbarUtil.Info).show();
                        ImageViewDialog dialog =
                                new ImageViewDialog(RecylerviewActivity.this,
                                        R.style.Dialog_Fullscreen,
                                        mMeiziList.get(position).getUrl());
                        dialog.show();
                    }

                    @Override
                    public boolean onItemLongClick(View view) {

                        int position = mRecyclerView.getChildAdapterPosition(view);
                        String url = mMeiziList.get(position).getUrl();
                        DownloadManager downloadManager =
                                (DownloadManager)getSystemService(DOWNLOAD_SERVICE);

                        //创建一个Request对象
                        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url));
                        //设置下载文件路径
                        String[] list = url.split("/");
                        String fileName = list[list.length - 1];
                        request.setDestinationInExternalPublicDir("Meizi",fileName);
                        //开始下载
                        long downloadId=downloadManager.enqueue(request);

                        Toast.makeText(RecylerviewActivity.this,"Begin to download ! fileName : " + fileName ,Toast.LENGTH_SHORT)
                        .show();

                        return true;
                    }
                });
            } else {
                //让适配器刷新数据
                mAdapter.notifyDataSetChanged();
            }
            netWorkSet.remove(this);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


    public static class MyOkhttp {

        public OkHttpClient client = new OkHttpClient();

        public String get(String url) {
            try {
                client.newBuilder().connectTimeout(10000, TimeUnit.MILLISECONDS);
                okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();

                okhttp3.Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String result = new String(response.body().string());
                    return result;
                } else {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }
}
