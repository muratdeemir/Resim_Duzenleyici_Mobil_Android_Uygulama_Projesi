package com.ss.resimduzenleyici;

public class AppSettings {
    private static AppSettings _instance;

    private AppSettings() {
        SHOW_TOAST_MESSAGE = true;
    }

    public static AppSettings getInstance() {
        if (_instance == null)
            _instance = new AppSettings();

        return _instance;
    }

    public boolean SHOW_TOAST_MESSAGE;

}
