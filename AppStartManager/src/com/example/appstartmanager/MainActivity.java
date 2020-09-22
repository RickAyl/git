
package com.example.appstartmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

public class MainActivity extends Activity {

    private GridView mAllAppGridView;

    private AllAppAdapter mAdapter;

    private List mlistAppInfo = new ArrayList<AppInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mAllAppGridView = (GridView) findViewById(R.id.all_app_managergridview);
        queryAppInfo();
        mAdapter = new AllAppAdapter(this, mlistAppInfo);
        mAllAppGridView.setAdapter(mAdapter);
        setListener();
        int id = getIdByName(this, "layout", "activity_main");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        queryAppInfo();
        if (mAdapter == null) {
            mAdapter = new AllAppAdapter(this, mlistAppInfo);
            mAllAppGridView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setListener() {
        // 短按启动应用
        mAllAppGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Log.v("rick", "======================> position " + position);
                AppInfo info = mAdapter.getItem(position);
                startActivity(info.getLaunchIntent());
            }
        });
        // 长按卸载
        mAllAppGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Uri packageURI = Uri.parse("package:" + mAdapter.getItem(position).getPkgName());
                Log.v("rick", "======>pkguri : " + packageURI.toString());
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                startActivity(uninstallIntent);
                return true;
            }
        });
    }

    public void queryAppInfo() {
        PackageManager pm = this.getPackageManager(); // 获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);
        // 调用系统排序，根据name排序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
        if (mlistAppInfo != null) {
            mlistAppInfo.clear();
            for (ResolveInfo reInfo : resolveInfos) {
                String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
                String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名pkgname
                String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
                Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
                // 为应用程序的启动Activity 准备Intent
                Intent launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName(pkgName, activityName));
                // 创建一个AppInfo对象，并赋值
                AppInfo appInfo = new AppInfo();
                appInfo.setAppName(appLabel);
                appInfo.setPkgName(pkgName);
                appInfo.setAppIcon(icon);
                appInfo.setIntent(launchIntent);
                mlistAppInfo.add(appInfo); // 添加至列表中

                Log.v("rick", appLabel + " activityName---" + activityName + " pkgName---" + pkgName);
            }
        }
    }

    public static int getIdByName(Context context, String className, String name) {
        String packageName = context.getPackageName();
        Class r = null;
        int id = 0;
        try {
            r = Class.forName(packageName + ".R");
            //找出r类下的所有内部类
            Class[] classes = r.getClasses();
            Class desireClass = null;

            for (int i = 0; i < classes.length; ++i) {
                Log.v("tag1","classes["+i+"]"+".getName() : " + classes[i].getName());
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    Log.v("tag","classes[i].getName() : " + classes[i].getName());
                    /*String[] s = classes[i].getName().split("\\$");
                    List<String> ls = Arrays.asList(s);
                    for(i = 0;i < s.length;i++){
                        Log.v("tag","s"+"["+i+"] : "+s[i]);
                    }
                    for(String c : ls){
                        Log.v("tag","c : "+c);
                    }*/
                    desireClass = classes[i];
                    break;
                }
            }

            if (desireClass != null)
                id = desireClass.getField(name).getInt(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return id;
    }
}
