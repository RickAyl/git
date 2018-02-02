package com.eostek.rick.demo.designviewgroup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.eostek.rick.demo.designviewgroup.R;

/**
 * Created by a on 17-7-20.
 */

public class NavigationDialog extends Dialog {

    private Context mContext;

    private View view;

    public NavigationDialog(Context context) {
        this(context,0);
    }

    public NavigationDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //view = LayoutInflater.from(mContext).inflate(R.layout.navigation_layout,null);
        setContentView(R.layout.navigation_layout);
        getWindow().setGravity(Gravity.LEFT);

    }
    @Override
    public void show() {
        Window dialogWindow = getWindow();
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
        super.show();
    }
}
