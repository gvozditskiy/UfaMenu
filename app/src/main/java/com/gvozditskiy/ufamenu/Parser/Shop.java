package com.gvozditskiy.ufamenu.Parser;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Alexey on 13.01.2017.
 */

public class Shop {
    @ElementList
    private List<Category> categories;


    public List<Category> getCategories() {
        return categories;
    }

    @ElementList
    private List<Offer> offers;

    public List<Offer> getOffers() {
        return offers;
    }
}
