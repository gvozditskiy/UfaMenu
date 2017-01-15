package com.gvozditskiy.ufamenu.Parser;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 * Created by Alexey on 13.01.2017.
 */

@DatabaseTable(tableName = "categories")
@Root
public class Category {
    public static final String ID_CATEGORY_COLUMN_NAME = "cat_Id";
    public static final String ID_COLUMN_NAME = "offer_Id";

    public Category() {
    }

    @DatabaseField(id = true, columnName = ID_CATEGORY_COLUMN_NAME)
    @Attribute
    String id;

    @DatabaseField
    @Text
    String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
