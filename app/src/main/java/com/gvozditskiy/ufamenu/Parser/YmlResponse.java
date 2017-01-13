package com.gvozditskiy.ufamenu.Parser;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Alexey on 13.01.2017.
 */

@Root(name = "yml_catalog")
public class YmlResponse {

    @Attribute
    private String date;

    @Element
    Shop shop;

    public String getDate() {
        return date;
    }

    public Shop getShop() {
        return shop;
    }



}
