package com.health.entity;

import com.health.pojo.TbItemCat;
import com.health.pojo.TbTypeTemplate;

import java.io.Serializable;

public class ItemCat implements Serializable {
    private TbItemCat itemCat;
    private TbTypeTemplate typeTemplate;

    public ItemCat() {
    }

    public ItemCat(TbItemCat itemCat, TbTypeTemplate typeTemplate) {
        this.itemCat = itemCat;
        this.typeTemplate = typeTemplate;
    }

    public TbItemCat getItemCat() {
        return itemCat;
    }

    public void setItemCat(TbItemCat itemCat) {
        this.itemCat = itemCat;
    }

    public TbTypeTemplate getTypeTemplate() {
        return typeTemplate;
    }

    public void setTypeTemplate(TbTypeTemplate typeTemplate) {
        this.typeTemplate = typeTemplate;
    }
}
