package com.mbarrben.actionbarhelper.demo;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.mbarrben.actionbarhelper.ActionBarHelper;
import com.mbarrben.actionbarhelper.demo.R;

public class MainActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ActionBarHelper.Builder builder = new ActionBarHelper.Builder(R.layout.activity_main)
            .fade(R.drawable.ab_solid, R.id.header)
            .blur(3); // use values below 3 under your own risk of OOM errors
        
        setContentView(builder.build(this));
    }

}
