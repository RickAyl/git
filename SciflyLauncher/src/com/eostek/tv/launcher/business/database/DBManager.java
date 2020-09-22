
package com.eostek.tv.launcher.business.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.eostek.tv.launcher.R;
import com.eostek.tv.launcher.HomeApplication;
import com.eostek.tv.launcher.business.listener.OnAppChangedListener;
import com.eostek.tv.launcher.model.JsonHeadBean;
import com.eostek.tv.launcher.model.MetroInfo;
import com.eostek.tv.launcher.model.MetroPage;
import com.eostek.tv.launcher.util.LConstants;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.XmlResourceParser;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * projectName： TVLauncher 
 * moduleName： DBManager.java
 * 
 * @author cloud.li
 * @version 1.0.0
 * @time 2014-7-20 上午10:59:32
 * @Copyright © 2014 Eos Inc.
 */

public final class DBManager {

    private final String TAG = DBManager.class.getSimpleName();

    private static DBManager mDbManager;

    private DBHelper mDbHelper;

    private Context mContext;
    
    private SQLiteDatabase  mDatabase;
    
    private OnAppChangedListener mListener = null;

    private DBManager(Context context) {
        mContext = context;
        mDbHelper = new DBHelper(context);
    }

    /**
     * get download manager instancegetJsonHeadBean
     * 
     * @param context context
     * @return The Instance of DbManager
     */
    public static DBManager getDbManagerInstance(Context context) {
        if (mDbManager == null) {
            mDbManager = new DBManager(context);
        }
        return mDbManager;
    }
    
	public void setAppChangedListener(OnAppChangedListener appChangedListener) {
		mListener = appChangedListener;
	}

    private List<MetroPage> loadDataFromXML() {
        DataFromXML data = new DataFromXML();
        try {
            data.parse(mContext.getResources().getXml(HomeApplication.getDefaultXml()));
            insertPages(data.getPages());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.getPages();
    }

    /**
     * udpate metroinfo
     * 
     * @param info The metroinfo object to update
     * @return The udpate record id
     */
    public long updateInfo(MetroInfo info) {
        return mDbHelper.update(DBHelper.METROINFO_TABLE_NAME, info, null, null);
    }

    /**
     * inset the page to table metropages and insert the list in MetroPage to
     * table metroinfos
     * 
     * @param pages The List<MetroPage> object
     * @throws Exception 
     */
    public void insertPages(List<MetroPage> pages)  {
        mDatabase=mDbHelper.getReadableDatabase();
        mDatabase.beginTransaction();
        try{
            for (MetroPage page : pages) {
             mDbHelper.insertMetroPage(page);
                for (MetroInfo info : page.getListInPage()) {
                      mDbHelper.insertMetroInfo(info);
                }
            }
            mDatabase.setTransactionSuccessful();
        }catch(Exception e){
            mDatabase.endTransaction();
           e.printStackTrace();
        }
      mDatabase.endTransaction();
    }

    /**
     * add the JsonHeadBean to db
     * 
     * @param bean see {@link JsonHeadBean}
     */
    public void insertJsonHeadBean(JsonHeadBean bean) {
        mDbHelper.insertResponseHead(bean);
    }

    /**
     * get all MetroPage infomations
     * 
     * @return The MetroPage list from db
     */
    public List<MetroPage> getMetroPages() {
        List<MetroPage> pages = new ArrayList<MetroPage>();
        if (isDBEmpty()) {
            pages = loadDataFromXML();
        } else {
            Cursor cursor = mDbHelper.getMetroPageCursor();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndex(DBHelper.PAGE_COLUMN_TITLE));
                    int appcategory = cursor.getInt(cursor.getColumnIndex(DBHelper.PAGE_COLUMN_APPCATEGORY));
                    MetroPage page = new MetroPage(title, appcategory);
                    List<MetroInfo> infos = getPageInfos(title, appcategory);
                    Log.v("DBManager", "" + infos.size() + ";" + title + ";" + appcategory);
                    page.setList(infos);
                    pages.add(page);
                }
                cursor.close();
            }
        }

        return pages;
    }

    /**
     * get all MetroPage infomations
     * 
     * @since V1.2.1
     * @param country The country flag
     * @return The MetroPage list from db
     */
    public List<MetroPage> getMetroPages(String country) {
        List<MetroPage> pages = new ArrayList<MetroPage>();
        if (isDBEmpty(country)) {
            pages = loadDataFromXML();
        } else {
            Cursor cursor = mDbHelper.getMetroPageCursor(country);
            Log.v(TAG, "getMetroPages count  = " + cursor.getCount());
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndex(DBHelper.PAGE_COLUMN_TITLE));
                    int appcategory = cursor.getInt(cursor.getColumnIndex(DBHelper.PAGE_COLUMN_APPCATEGORY));
                    MetroPage page = new MetroPage(title, appcategory);
                    List<MetroInfo> infos = getPageInfos(country, title, appcategory);
                    Log.v("DBManager", "" + infos.size() + ";" + title + ";" + appcategory);
                    page.setList(infos);
                    pages.add(page);
                }
                cursor.close();
            }
        }

        return pages;
    }

    /**
     * get all package name in db
     * 
     * @return The package list in db
     */
    public Set<String> getMetroPackages() {
        Set<String> list = new HashSet<String>();
        Cursor cursor = mDbHelper.query(DBHelper.METROINFO_TABLE_NAME, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String pkgName = cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_PACKAGE_NAME));
                list.add(pkgName);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * . query the database to find the list in page
     * 
     * @param selections
     * @param whereArgs
     * @return
     */
    private List<MetroInfo> getPageInfos(String selections, String[] whereArgs) {
        List<MetroInfo> list = new ArrayList<MetroInfo>();
        Cursor cursor = mDbHelper.query(DBHelper.METROINFO_TABLE_NAME, null, selections, whereArgs, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MetroInfo info = new MetroInfo();
                info.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_ID)));
                info.setX(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_POSITION_X)));
                info.setY(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_POSITION_Y)));
                info.setWidthSize(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_WIDTH)));
                info.setHeightSize(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_HEIGHT)));
                info.setTypeTitle(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_TYPE_TITLE)));
                info.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_APP_TITLE)));
                info.setClsName(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_CLASS_NAME)));
                info.setPkgName(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_PACKAGE_NAME)));
                info.setItemType(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_APP_TYPE)));
                info.setAppCategory(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_APPCATEGORY)));
                info.setExtraStrInfo(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_STR_FLAG)));
                info.setExtraIntInfo(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_INT_FLAG)));
                info.setIconPathB(cursor.getString(cursor
                        .getColumnIndex(DBHelper.METROINFO_COLUMN_ICON_PATH_BACKGROUND)));
                info.setIconPathF(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_ICON_PATH_FOREGROUD)));
                info.setApkUrl(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_ICON_APP_URL)));
                list.add(info);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * get the metroinfo list from page
     * 
     * @param titile The page title
     * @param appcategory The page appcategory
     * @return The MetroInfo list with both the given titile and appcategory
     */
    public List<MetroInfo> getPageInfos(String titile, int appcategory) {
        String[] whereArgs = {
                titile, String.valueOf(appcategory)
        };
        String selections = DBHelper.METROINFO_COLUMN_TYPE_TITLE + "=? AND " + DBHelper.METROINFO_COLUMN_APPCATEGORY
                + "=?";
        return getPageInfos(selections, whereArgs);
    }

    /**
     * get the metroinfo list from page
     * 
     * @since V1.2.1
     * @param country The country flag
     * @param titile The page title
     * @param appcategory The page appcategory
     * @return The MetroInfo list with both the given titile and appcategory
     */
    public List<MetroInfo> getPageInfos(String country, String titile, int appcategory) {
        String[] whereArgs = {
                country, titile, String.valueOf(appcategory)
        };
        String selections = DBHelper.METROINFO_COLUMN_COUNTRY + "=? AND " + DBHelper.METROINFO_COLUMN_TYPE_TITLE
                + "=? AND " + DBHelper.METROINFO_COLUMN_APPCATEGORY + "=?";
        return getPageInfos(selections, whereArgs);
    }

    /**
     * get the getJsonHeadBean infomation from DB
     * 
     * @return JsonHeadBean object,see {@link JsonHeadBean},the value of object
     *         may be empty if db has no data
     */
    public JsonHeadBean getJsonHeadBean() {
        JsonHeadBean bean = new JsonHeadBean();
        if (!mDbHelper.isTableExist(DBHelper.RESPONSE_HEAD_TABLE_NAME)) {
            return bean;
        }
        Cursor cursor = mDbHelper.query(DBHelper.RESPONSE_HEAD_TABLE_NAME, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                bean.setResponse(cursor.getInt(cursor.getColumnIndex(DBHelper.RESPONCE_HEAD_COLUMN_ERROR)));
                int value = cursor.getInt(cursor.getColumnIndex(DBHelper.RESPONCE_HEAD_COLUMN_ERROR));
                bean.setHasReflection(value == 1 ? true : false);
                bean.setBackgroundUrl(cursor.getString(cursor.getColumnIndex(DBHelper.RESPONCE_HEAD_COLUMN_BACKGROUND)));
                bean.setLogoUrl(cursor.getString(cursor.getColumnIndex(DBHelper.RESPONCE_HEAD_COLUMN_LOGO)));
                bean.setLogoX(cursor.getInt(cursor.getColumnIndex(DBHelper.RESPONCE_HEAD_COLUMN_LOGO_X)));
                bean.setLogoY(cursor.getInt(cursor.getColumnIndex(DBHelper.RESPONCE_HEAD_COLUMN_LOGO_Y)));
                bean.setCounLang(cursor.getString(cursor.getColumnIndex(DBHelper.RESPONCE_HEAD_COLUMN_COUNTRY)));
            }
            cursor.close();
        }
        return bean;
    }

    /**
     * get the getJsonHeadBean infomation from DB
     * 
     * @since V1.2.1
     * @param country The country flag
     * @return JsonHeadBean object,see {@link JsonHeadBean},the value of object
     *         may be empty if db has no data
     */
    public JsonHeadBean getJsonHeadBean(String country) {
        JsonHeadBean bean = new JsonHeadBean();
        if (!mDbHelper.isTableExist(DBHelper.RESPONSE_HEAD_TABLE_NAME)) {
            return bean;
        }
        Cursor cursor = mDbHelper.query(DBHelper.RESPONSE_HEAD_TABLE_NAME, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                bean.setResponse(cursor.getInt(cursor.getColumnIndex(DBHelper.RESPONCE_HEAD_COLUMN_ERROR)));
                int value = cursor.getInt(cursor.getColumnIndex(DBHelper.RESPONCE_HEAD_COLUMN_ERROR));
                bean.setHasReflection(value == 1 ? true : false);
                bean.setBackgroundUrl(cursor.getString(cursor.getColumnIndex(DBHelper.RESPONCE_HEAD_COLUMN_BACKGROUND)));
                bean.setLogoUrl(cursor.getString(cursor.getColumnIndex(DBHelper.RESPONCE_HEAD_COLUMN_LOGO)));
                bean.setLogoX(cursor.getInt(cursor.getColumnIndex(DBHelper.RESPONCE_HEAD_COLUMN_LOGO_X)));
                bean.setLogoY(cursor.getInt(cursor.getColumnIndex(DBHelper.RESPONCE_HEAD_COLUMN_LOGO_Y)));
                bean.setCounLang(cursor.getString(cursor.getColumnIndex(DBHelper.RESPONCE_HEAD_COLUMN_COUNTRY)));
            }
            cursor.close();
        }
        return bean;
    }

    /**
     * to find out whether the DB is empty
     * 
     * @return True is DB is empty,else false
     */
    public boolean isDBEmpty() {
        boolean isCountryExsit = mDbHelper.checkColumnExist(DBHelper.METROINFO_TABLE_NAME,
                DBHelper.METROINFO_COLUMN_COUNTRY);
        // older version do not has country column
        if (!isCountryExsit) {
            mDbHelper.updateDatabase();
        }
        return mDbHelper.getCount() == 0 ? true : false;
    }

    /**
     * to find out whether the DB has data with the given column
     * 
     * @since V1.2.1
     * @param country The column name
     * @return True is DB is empty,else false
     */
    public boolean isDBEmpty(String country) {
        boolean isCountryExsit = mDbHelper.checkColumnExist(DBHelper.METROINFO_TABLE_NAME,
                DBHelper.METROINFO_COLUMN_COUNTRY);
        // for the older version,do nothing
        if (!isCountryExsit) {
            mDbHelper.updateDatabase();
        }
        return mDbHelper.getCount(country) == 0 ? true : false;
    }

    /**
     * delete data from db
     * 
     * @since V1.2.1
     * @param country The country flag
     * @return The count of delete data
     */
    public long emptyDB(String country) {
        Log.w(TAG, "emptyDB called !");
        return mDbHelper.emptyDBData(country);
    }

    /**
     * get the country etag,see more {@link DBHelper#getCountryETag}
     * 
     * @since V1.2.1
     * @param country The country flag
     * @return the country eTag,null if not exist
     */
    public String getETag(String country) {
        return mDbHelper.getCountryETag(country);
    }

    /**
     * udpate the etag with the given country ,see more
     * {@link DBHelper#udpateCountryETag}
     * 
     * @since V.1.2.1
     * @param country The country flag
     * @param eatg The etag to udpate
     */
    public void setETag(String country, String etag) {
        mDbHelper.udpateCountryETag(country, etag);
    }

    /**
     * reset the etag with the given country ,see more
     * {@link DBHelper#udpateCountryETag}
     * 
     * @since V.1.2.1
     * @param country The country flag
     */
    public void resetETag(String country) {
        mDbHelper.udpateCountryETag(country, LConstants.DEFAULT_ETAG);
    }
    
    public List<MetroInfo> getAppPageInfo(String country) {
		// TODO 读取新表应用数据
		List<MetroInfo> list = new ArrayList<MetroInfo>();
		if (isAppTableEmpty(country)) {
			loadAppDataFromXML(country);
		}

		String[] whereArgs = { country };

		String selections = DBHelper.METROINFO_COLUMN_COUNTRY + "=?";
		Cursor cursor = mDbHelper.query(DBHelper.APP_TABLE_NAME, null,
				selections, whereArgs, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				MetroInfo info = new MetroInfo();
				info.setId(cursor.getInt(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_ID)));
				info.setX(cursor.getInt(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_POSITION_X)));
				info.setY(cursor.getInt(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_POSITION_Y)));
				info.setWidthSize(cursor.getInt(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_WIDTH)));
				info.setHeightSize(cursor.getInt(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_HEIGHT)));
				info.setTypeTitle(cursor.getString(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_TYPE_TITLE)));
				info.setTitle(cursor.getString(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_APP_TITLE)));
				info.setClsName(cursor.getString(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_CLASS_NAME)));
				info.setPkgName(cursor.getString(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_PACKAGE_NAME)));
				info.setItemType(cursor.getInt(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_APP_TYPE)));
				info.setAppCategory(cursor.getInt(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_APPCATEGORY)));
				info.setExtraStrInfo(cursor.getString(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_STR_FLAG)));
				info.setExtraIntInfo(cursor.getInt(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_INT_FLAG)));
				info.setIconPathB(cursor.getString(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_ICON_PATH_BACKGROUND)));
				info.setIconPathF(cursor.getString(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_ICON_PATH_FOREGROUD)));
				info.setNet(cursor.getInt(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_NET)));
				info.setApkUrl(cursor.getString(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_ICON_APP_URL)));
				info.setCounLang(cursor.getString(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_COUNTRY)));
				info.setHide(cursor.getInt(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_HIDE)));
				info.setMoveType(cursor.getInt(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_MOVE_TYPE)));
				info.setPositionId(cursor.getInt(cursor
						.getColumnIndex(DBHelper.METROINFO_COLUMN_POSITION_ID)));
				list.add(info);
			}
			cursor.close();
		}
		return list;
	}
    
    public boolean isAppTableEmpty(String country) {
		return mDbHelper.getAppTableCount(country) == 0 ? true : false;
	}

	private List<MetroInfo> loadAppDataFromXML(String country) {
		DataFromXML data = new DataFromXML();
		int xml = R.xml.apppage_default_en;;

		try {
			data.parse(mContext.getResources().getXml(xml));
			insertAppPage(data.getPages().get(0).getListInPage());
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data.getPages().get(0).getListInPage();
	}
	
	private void insertAppPage(List<MetroInfo> list) {
		mDbHelper.beginTransaction();
		try {
			for (MetroInfo info : list) {
				mDbHelper.insertAppMetroInfo(info);
			}
			mDbHelper.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e(TAG, "insertPages Exception");
			e.printStackTrace();
		} finally {
			mDbHelper.endTransaction();
		}
	}
    
	public ArrayList<ResolveInfo> getEnAppToAdd(Context context) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		ArrayList<ResolveInfo> listApps = new ArrayList<ResolveInfo>();
		List<ResolveInfo> tempInfos = packageManager.queryIntentActivities(
				intent, 0);

		List<MetroPage> metroPages = getAppAddMetroPages("en-us");
		List<String> pkgList = new ArrayList<String>();
		List<String> blackList = getBlackListFromXml(context);
		
		for (int i = 0; i < metroPages.size(); i++) {
			pkgList.add(metroPages.get(i).getTitle());
		}
		pkgList.addAll(blackList);
		
		Cursor cursor = mDbHelper.getAppPageCursor();
		while(cursor.moveToNext()){
			pkgList.add(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_PACKAGE_NAME)));
		}
		cursor.close();
		
		for (int i = 0; i < tempInfos.size(); i++) {
			if (!pkgList.contains(tempInfos.get(i).activityInfo.packageName)) {
				listApps.add(tempInfos.get(i));
			}
		}
		
		return listApps;
	}
	
	/**
	 * the xml file is something like this <packages> <app
	 * pkg="com.google.android.voicesearch"/>
	 * 
	 * @param ctx
	 * @return
	 */
	public List<String> getBlackListFromXml(Context ctx) {
		List<String> list = new ArrayList<String>();
		XmlPullParser parser;
		parser = ctx.getResources().getXml(R.xml.blacklist_dangle);
		try {
			while (parser.getEventType() != XmlResourceParser.END_DOCUMENT) {
				if (parser.getEventType() == XmlResourceParser.START_TAG) {
					String tagName = parser.getName();
					if (tagName.equals("app")) {
						String pkgName = parser.getAttributeValue(null, "pkg");
						list.add(pkgName);
					}
				}
				parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		list.add(LConstants.PKG_DOWNLOADMANAGER);
		
		return list;
	}
	
	private List<MetroPage> getAppAddMetroPages(String country) {
		List<MetroPage> pages = new ArrayList<MetroPage>();
		Cursor cursor = mDbHelper.getAppAddMetroPageCursor(country);
		Log.v(TAG, "getMetroPages count  = " + cursor.getCount());
		if (cursor != null) {
			while (cursor.moveToNext()) {
				String title = cursor.getString(cursor
						.getColumnIndex("PkgName"));
				int appcategory = cursor.getInt(cursor
						.getColumnIndex(DBHelper.PAGE_COLUMN_APPCATEGORY));
				MetroPage page = new MetroPage(title, appcategory);
				List<MetroInfo> infos = getPageInfos(country, title,
						appcategory);
				Log.v("DBManager", "" + infos.size() + ";" + title + ";"
						+ appcategory);
				page.setList(infos);
				Log.d("ahri", "page.setList" + page.getListInPage().size());
				pages.add(page);
			}
			cursor.close();
		}

		return pages;
	}
	
	public MetroInfo getLastDBData() {
		Cursor cursor = mDbHelper.getAppPageCursor();
		cursor.moveToLast();
		
		MetroInfo info = new MetroInfo();
		info.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_ID)));
		info.setX(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_POSITION_X)));
		info.setY(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_POSITION_Y)));
		info.setWidthSize(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_WIDTH)));
		info.setHeightSize(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_HEIGHT)));
		info.setTypeTitle(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_TYPE_TITLE)));
		info.setTitle(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_APP_TITLE)));
		info.setClsName(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_CLASS_NAME)));
		info.setPkgName(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_PACKAGE_NAME)));
		info.setItemType(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_APP_TYPE)));
		info.setAppCategory(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_APPCATEGORY)));
		info.setExtraStrInfo(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_STR_FLAG)));
		info.setExtraIntInfo(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_INT_FLAG)));
		info.setIconPathB(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_ICON_PATH_BACKGROUND)));
		info.setIconPathF(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_ICON_PATH_FOREGROUD)));
		info.setApkUrl(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_ICON_APP_URL)));
		info.setCounLang(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_COUNTRY)));
		info.setNet(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_NET))));
		info.setHide(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_HIDE)));
		info.setMoveType(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_MOVE_TYPE)));
		info.setPositionId(cursor.getInt(cursor.getColumnIndex(DBHelper.METROINFO_COLUMN_POSITION_ID)));
		
		cursor.close();
		
		return info;
	}
	
	public void updateLastDBData(MetroInfo tempMetroInfo) {
		long x = mDbHelper.insertAppMetroInfo(tempMetroInfo);
		Log.d("ahri", "insert add tempMetroInfo.getPositionId() =" + tempMetroInfo.getPositionId());
		Log.d("ahri", "insertAppMetroInfo result =" + x);
	}
	
	public void updateAddAppDBData(MetroInfo tempMetroInfo, int id) {
		long x = mDbHelper.updateEnAddDBDate(tempMetroInfo,id);
		Log.d("ahri", "updateEnAddDBDate result =" + x);
	}
	
	public void removeEnAppFromPage(MetroInfo info) {
		
		if (mListener != null) {
			mListener.onEnAppRemove(info);
		}
		
		deleteAppFromDB(info);
	}
	
	private void deleteAppFromDB(MetroInfo info) {
		int x = mDbHelper.DeleteEnAppFromDB(info.getClsName());
		Log.d("ahri", "deleteAppFromDB =" + x); 
		mDbHelper.updateIdAndPosition(info.getPositionId(),info.getX(),info.getY());
	}
	
	public void uninstallEnApp(String appPkg) {
		try {
			Cursor deleteCursor = mDbHelper.query(DBHelper.APP_TABLE_NAME, new String[]{DBHelper.METROINFO_COLUMN_POSITION_ID}, 
					DBHelper.METROINFO_COLUMN_PACKAGE_NAME + "=?", new String[]{appPkg}, null);
			deleteCursor.moveToFirst();
			int deleteId = deleteCursor.getInt(deleteCursor.getColumnIndex(DBHelper.METROINFO_COLUMN_POSITION_ID));
			
			Log.d("ahri", "deleteId =" + deleteId);
			
			mDbHelper.delete(DBHelper.APP_TABLE_NAME,
					DBHelper.METROINFO_COLUMN_PACKAGE_NAME + "=?",
					new String[] { appPkg });
			
			updateAppIDAfterDel(deleteId);
		} catch (Exception e) {
			Log.d("ahri", "Exception =" + e);
		}
		
		if (mListener != null) {
			mListener.onEnAppUninstall(appPkg);
		}
	}
	
	/**
	 * update AppID After Delete data from DB.
	 * @param id 
	 * @param deleteCursor 
	 */
	private void updateAppIDAfterDel(int id) {
		Log.d("ahri","updateAppIDAfterDel");
		mDbHelper.updateEnAppID(id);
	}
	
	
}
