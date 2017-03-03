
package com.eostek.tv.launcher.model;

import java.io.Serializable;


/*
 * projectName： TVLauncher
 * moduleName： Position.java
 *
 * @author chadm.xiang
 * @version 1.0.0
 * @time  2014-7-8 下午8:44:30
 * @Copyright © 2014 Eos Inc.
 */
/**
 * Launcher interface for each Metro block information, including the size,
 * position, used to draw the Metro block position; the package name, class name
 * and download address, used to start the application, if the application is
 * not installed, then download from the given address; a int parameters and a
 * string parameter, when starting some time passed as parameter
 **/
public class MetroInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** the x coordinate of item **/
    private int x;

    /** the y coordinate of item **/
    private int y;

    /** the width of item **/
    private int widthSize;

    /** the height of item **/
    private int heightSize;

    /** the identity **/
    private int id;

    /** the item in which kind,like TV,Videos,apks,or setting **/
    private String typeTitle;

    /** the item show title **/
    private String title;

    /** the item associate apk classname **/
    private String clsName;

    /** the item associate apk pakage name **/
    private String pkgName;

    /** the item type,should be apk,web url,widget or others **/
    private int itemType;

    /** the url to download backgroud icon **/
    private String iconPathB;

    /** the url to download apk **/
    private String apkUrl;

    /** the extra int param added in intent to start an activity **/
    private int extraIntInfo;

    /** the typeTitle index **/
    private int appCategory;

    /** the extra string param added in intent to start an activity **/
    private String extraStrInfo;

    /** the url to download foreground icon **/
    private String iconPathF;
    
    /** 是否需要网络 **/
    private int mNet = 0;

    /** 是否隐藏,1隐藏 **/
    private int mHide = 0;

    /** 0无限制,1不可移除,2不弹出菜单 **/
    private int mMoveType = 0;

    /** the country flag,contains anguage and country,like zh-cn **/
    private String counLang;
    
    /** 位置id **/
    private int mPositionId = -1;

    public MetroInfo(int index, int positionX, int positionY, int width, int height) {
        this.id = index;
        this.x = positionX;
        this.y = positionY;
        this.widthSize = width;
        this.heightSize = height;
    }

    public MetroInfo() {

    }

    public int getX() {
        return x;
    }

    public void setX(int xVal) {
        this.x = xVal;
    }

    public int getY() {
        return y;
    }

    public void setY(int yVal) {
        this.y = yVal;
    }

    public int getWidthSize() {
        return widthSize;
    }

    public void setWidthSize(int wSize) {
        this.widthSize = wSize;
    }

    public int getHeightSize() {
        return heightSize;
    }

    public void setHeightSize(int hSize) {
        this.heightSize = hSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int mid) {
        this.id = mid;
    }

    public String getTypeTitle() {
        return typeTitle;
    }

    public void setTypeTitle(String typTitle) {
        this.typeTitle = typTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String tit) {
        this.title = tit;
    }

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String className) {
        this.clsName = className;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkg) {
        this.pkgName = pkg;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemTyp) {
        this.itemType = itemTyp;
    }

    public String getIconPathB() {
        return iconPathB;
    }

    public void setIconPathB(String iconB) {
        this.iconPathB = iconB;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String url) {
        this.apkUrl = url;
    }

    public int getExtraIntInfo() {
        return extraIntInfo;
    }

    public void setExtraIntInfo(int intInfo) {
        this.extraIntInfo = intInfo;
    }

    public int getAppCategory() {
        return appCategory;
    }

    public String getCounLang() {
        return counLang;
    }

    public void setCounLang(String counLang) {
        this.counLang = counLang;
    }

    public void setAppCategory(int appCat) {
        this.appCategory = appCat;
    }

    public String getExtraStrInfo() {
        return extraStrInfo;
    }

    public void setExtraStrInfo(String extraStr) {
        this.extraStrInfo = extraStr;
    }

    public String getIconPathF() {
        return iconPathF;
    }

    public void setIconPathF(String iconF) {
        this.iconPathF = iconF;
    }

    /**
     * get flag whether need network, 1 is need
     * 
     * @return flag value
     */
    public int getNet() {
        return mNet;
    }

    /**
     * set flag whether need network
     * 
     * @param net
     */
    public void setNet(int net) {
        this.mNet = net;
    }
    
    public void setHide(int value) {
        mHide = value;
    }

    public int getHide() {
        return mHide;
    }

    public void setMoveType(int value) {
        mMoveType = value;
    }

    public int getMoveType() {
        return mMoveType;
    }

    public int getPositionId() {
        return mPositionId;
    }
    
    public void setPositionId(int id) {
        mPositionId = id;
    }
    
    @Override
    public String toString() {
        return "MetroInfo[x=" + x + "];[y=" + y + "];[widthSize=" + widthSize + "];[heightSize=" + heightSize
                + "];[id=" + id + "];[title=" + title + "];[typeTitle=" + typeTitle + "];[clsName=" + clsName
                + "];[pkgName=" + pkgName + "];[iconPathB=" + iconPathB + "];[iconPathF=" + iconPathF + "];[y="
                + apkUrl + "];[appCategory=" + appCategory + "];[extraIntInfo=" + extraIntInfo + "];[extraStrInfo="
                + extraStrInfo + "];[net=" + mNet + "]";
    }

}
