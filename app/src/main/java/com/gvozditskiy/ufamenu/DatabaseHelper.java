package com.gvozditskiy.ufamenu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gvozditskiy.ufamenu.Parser.Category;
import com.gvozditskiy.ufamenu.Parser.Offer;
import com.gvozditskiy.ufamenu.Parser.Param;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Alexey on 13.01.2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "ufamenu.db";
    private static final int DATABSE_VERSION = 1;

    private DaoCategory daoCategory = null;
    private DaoOffer daoOffer = null;
    private DaoParam daoParam = null;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABSE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, Offer.class);
            TableUtils.createTable(connectionSource, Param.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//// TODO: 13.01.2017 обработать Upgrade базы данных 
    }

    ////////////Синглеты для DAO
    public DaoCategory getDaoCAtegory() throws SQLException {
        if (daoCategory == null) {
            daoCategory = new DaoCategory(getConnectionSource(), Category.class);
        }
        return daoCategory;
    }

    public DaoOffer getDaoOffer() throws SQLException {
        if (daoOffer == null) {
            daoOffer = new DaoOffer(getConnectionSource(), Offer.class);
        }
        return daoOffer;
    }

    public DaoParam getDaoParam() throws SQLException {
        if (daoParam == null) {
            daoParam = new DaoParam(getConnectionSource(), Param.class);
        }
        return daoParam;
    }

    /////////////////////////////////
    public class DaoCategory extends BaseDaoImpl<Category, Integer> {

        protected DaoCategory(ConnectionSource connectionSource, Class<Category> dataClass) throws SQLException {
            super(connectionSource, dataClass);
        }

        public List<Category> getAllCategories() throws SQLException {
            return this.queryForAll();
        }
    }

    public class DaoOffer extends BaseDaoImpl<Offer, Integer> {

        protected DaoOffer(ConnectionSource connectionSource, Class<Offer> dataClass) throws SQLException {
            super(connectionSource, dataClass);
        }

        public List<Offer> getAllOffers() throws SQLException {
            return this.queryForAll();
        }

        public List<Offer> getOffersByCategoryId(String id) throws SQLException {
            QueryBuilder<Offer, Integer> queryBuilder = queryBuilder();
            queryBuilder.where().eq(Offer.CATEGORY_ID_COLUMN, id);
            PreparedQuery<Offer> preparedQuery = queryBuilder.prepare();
            return query(preparedQuery);
        }

        public List<Offer> getOfferById(String id) throws SQLException {
            QueryBuilder<Offer, Integer> queryBuilder = queryBuilder();
            queryBuilder.where().eq(Offer.ID_COLUMN, id);
            PreparedQuery<Offer> preparedQuery = queryBuilder.prepare();
            return query(preparedQuery);
        }
    }

    public class DaoParam extends BaseDaoImpl<Param, Integer> {


        protected DaoParam(ConnectionSource connectionSource, Class<Param> dataClass) throws SQLException {
            super(connectionSource, dataClass);
        }

        public List<Param> getParamForOffer(String offerId, String paramType) throws SQLException {
            QueryBuilder<Param, Integer> queryBuilder = queryBuilder();
            queryBuilder.where().eq(Param.OFFER_ID_COLUMN, offerId).and().eq(Param.TYPE_COLUMN, paramType);
            PreparedQuery<Param> preparedQuery = queryBuilder.prepare();
            return query(preparedQuery);
        }
    }


}
