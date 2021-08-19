package com.ss.resimduzenleyici.widget;

import com.ss.resimduzenleyici.imageprocessing.ITransformation;

import java.util.ArrayList;
import java.util.List;

public class BaseToolObject {
    public String name;
    public int iconResourceId;
    public ITransformation transform;
    public List<BaseToolObject> childrenTools;

    public BaseToolObject() {
        childrenTools = new ArrayList<BaseToolObject>();

    }

    public void addChild(BaseToolObject child) {
        this.childrenTools.add(child);
    }

    public List<BaseToolObject> getChildrenTools() {
        return this.childrenTools;
    }

}
