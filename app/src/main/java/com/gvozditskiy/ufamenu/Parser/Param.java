package com.gvozditskiy.ufamenu.Parser;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.lang.ref.SoftReference;

/**
 * Created by Alexey on 13.01.2017.
 */
@DatabaseTable(tableName = "params")
@Root
public class Param {
    @DatabaseField (id = true)
    @Text
    String value;

    @DatabaseField
    @Attribute(name = "name")
    private String type;

    @DatabaseField
    private String offerId;

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }



    public void setOfferId(String id) {
        offerId=id;
    }

    public String getOfferId() {
        return offerId;
    }
}
