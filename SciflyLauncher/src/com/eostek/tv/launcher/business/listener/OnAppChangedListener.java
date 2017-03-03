
package com.eostek.tv.launcher.business.listener;

import com.eostek.tv.launcher.model.MetroInfo;


public interface OnAppChangedListener {
	
    public void onEnAppAdd(MetroInfo appInfo);
    public void onEnAppRemove(MetroInfo appInfo);
    public void onEnAppUninstall(String pkg);
    public void onEnAppMoveCancel();
}