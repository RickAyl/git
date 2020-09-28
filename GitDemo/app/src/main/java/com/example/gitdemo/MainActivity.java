package com.example.gitdemo;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.gitdemo.service.MainService;
import com.example.gitdemo.utils.ListenerInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final String TESTFILENAME = "test.txt";

    private SwipeRefreshLayout swipeRefreshLayout;

    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_view);
        setSupportActionBar(toolbar);
        checkBasePermisson();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.nav_view_bottom);

        //关联fragment, 必须关联navigation文件夹
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .build();

        NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, controller, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, controller);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindMainService();
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSwipeListenerList != null && mSwipeListenerList.size() > 0) {
                    for (int i = 0; i < mSwipeListenerList.size(); i++) {
                        mSwipeListenerList.get(i).onSwipe();
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        unBindMainService();
        removeAllSwipeListener();
    }

    private void bindMainService(){
        Intent intent = new Intent(this, MainService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private MainService.MyBinder myBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MainService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBinder = null;
        }
    };

    private void unBindMainService(){
        unbindService(connection);
    }


    private void checkBasePermisson(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        } else {
            testPermission();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    testPermission();
                }else {
                    Toast.makeText(this,"you denied this write permission !",Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    public void setSwipeEnable(boolean flag) {
        swipeRefreshLayout.setEnabled(flag);
    }

    private void testPermission(){
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(TESTFILENAME, Context.MODE_PRIVATE);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos));
            try {
                bufferedWriter.write("app test write permission!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void setMainActivitySwipeListener(@NonNull ListenerInterface.SwipeListener listener) {
        synchronized (this) {
            mSwipeListenerList.add(listener);
        }
    }

    public void removeMainActivitySwipeListener(@NonNull ListenerInterface.SwipeListener listener) {
        if (mSwipeListenerList != null && mSwipeListenerList.contains(listener)) {
            mSwipeListenerList.remove(listener);
        }
    }

    private void removeAllSwipeListener() {
        mSwipeListenerList.clear();
    }

    public void stopSwipeRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    private List<ListenerInterface.SwipeListener> mSwipeListenerList = new ArrayList<>();

}