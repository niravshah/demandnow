package com.demandnow.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

/**
 * Created by Nirav on 16/12/2015.
 */
public class ParentJobInfo implements ParentObject {

    private List<Object> mChildrenList;
    private int title;

    @Override
    public List<Object> getChildObjectList() {
        return mChildrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        this.mChildrenList = list;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }
}