package com.eostek.rick.demo.designviewgroup;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.eostek.rick.demo.designviewgroup.view.IconDesignView;

/**
 * Created by a on 17-6-21.
 */


public class IconDemoActivity extends Activity {

    private IconDesignView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.design_activity);
        view = (IconDesignView) findViewById(R.id.icon_drawable);
    }
}
