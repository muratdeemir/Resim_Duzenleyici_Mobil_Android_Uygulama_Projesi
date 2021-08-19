package com.ss.resimduzenleyici;


import com.ss.resimduzenleyici.widget.BaseToolObject;

import java.util.List;

public class AppConfigs {

    public List<BaseToolObject> editorFeatures;
    public int deviceWidth;
    public int deviceHeight;
    public int thumbnailSize = 100;
    public String TYPE_FACE = "fonts/Existence-Light.otf";

    private static AppConfigs _instance;

    private AppConfigs() {

    }

    public static AppConfigs getInstance() {
        if (_instance == null)
            _instance = new AppConfigs();

        return _instance;
    }
}
