package com.example.appstartmanager;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class AppInfo {

    private String name;
    
    private String pkgname;
    
    private Drawable drawable;
    
    private Intent launchIntent;
    
    private String AppName;
    
    public AppInfo() {
        // TODO Auto-generated constructor stub
    }
    
    public String getName(){
        return this.name;
    }
    
    public Intent getLaunchIntent(){
        return this.launchIntent;
    }
    public String getPkgName(){
        return this.pkgname;
    }
    public Drawable getDrawable(){
        return this.drawable;
    }
    
    public String getAppName(){
        return AppName;
    }
    
    public void setPkgName(String name){
        this.pkgname = name;
    }
    
    public void setAppIcon(Drawable icon){
        this.drawable = icon;
    }
    
    public void setAppName(String name){
        this.AppName = name;
    }
    
    public void setIntent(Intent intent){
        this.launchIntent = intent;
    }
}
