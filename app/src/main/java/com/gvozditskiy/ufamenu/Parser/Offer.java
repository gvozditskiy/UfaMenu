package com.gvozditskiy.ufamenu.Parser;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexey on 13.01.2017.
 */

@DatabaseTable(tableName = "offers")
@Root(name = "offer")
public class Offer {

    public static final String CATEGORY_ID_COLUMN = "categoryId";
    public static final String ID_COLUMN = "offer_Id";

    public Offer() {
    }

    @DatabaseField(id = true, columnName = ID_COLUMN)
    @Attribute(name = "id")
    private String id;

    @Element
    private String url;

    @DatabaseField
    @Element
    private String name;

    @DatabaseField
    @Element(required = false)
    private String price;

    @DatabaseField
    @Element(name = "picture", required = false)
    private String pictureUrl;

    @DatabaseField (columnName = CATEGORY_ID_COLUMN)
    @Element
    private String categoryId;

    @DatabaseField
    @Element(required = false)
    private String description;


    @ElementList(inline = true, required = false)
    private ArrayList<Param> params;

public List<Param> getParams() {
    return params;
}

public String getId() {
    return id;
}


//    public static String getCategoryId() {
//        return categoryId;
//    }

}
