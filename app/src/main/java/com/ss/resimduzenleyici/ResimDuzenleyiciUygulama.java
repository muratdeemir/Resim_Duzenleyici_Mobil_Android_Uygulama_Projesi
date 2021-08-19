package com.ss.resimduzenleyici;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.ss.resimduzenleyici.utils.GLog;
import com.ss.resimduzenleyici.widget.BaseToolObject;
import com.ss.resimduzenleyici.widget.ToolStructureBuilder;

import java.util.List;

public class ResimDuzenleyiciUygulama extends Application {

    public ResimDuzenleyiciUygulama() {
        super();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppConfigs.getInstance().editorFeatures = buildEditorTools();
        GLog.setLogMode(GLog.LogMode.VERBOSE);
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        assert windowManager != null;
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        AppConfigs.getInstance().deviceWidth = metrics.widthPixels;
        AppConfigs.getInstance().deviceHeight = metrics.heightPixels;
    }


    private List<BaseToolObject> buildEditorTools() {
        ToolStructureBuilder tb = new ToolStructureBuilder(this);
        return tb.build();
    }
}
