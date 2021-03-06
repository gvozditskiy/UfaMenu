package com.gvozditskiy.ufamenu.ActivitiesAndFragments;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gvozditskiy.ufamenu.Adapters.CategoriesAdapter;
import com.gvozditskiy.ufamenu.HelperFactory;
import com.gvozditskiy.ufamenu.Interfaces.DataUpdateCallback;
import com.gvozditskiy.ufamenu.Interfaces.OnCategoryClickListener;
import com.gvozditskiy.ufamenu.Interfaces.RegisterUpdateCallback;
import com.gvozditskiy.ufamenu.Interfaces.UnRegisterUpdateCallback;
import com.gvozditskiy.ufamenu.Parser.Category;
import com.gvozditskiy.ufamenu.R;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment implements DataUpdateCallback {
    RecyclerView recyclerView;
    List<Category> categoryList;
    OnCategoryClickListener onCategoryClickListener;
    RegisterUpdateCallback registerUpdateCallback;
    UnRegisterUpdateCallback unRegisterUpdateCallback;


    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCategoryClickListener) {
            onCategoryClickListener = (OnCategoryClickListener) context;
        } else {
            throw new ClassCastException("Activity should implements OnCategoryClickListener Interface!");
        }
        if (context instanceof RegisterUpdateCallback) {
            registerUpdateCallback = (RegisterUpdateCallback) context;
        } else {
            throw new ClassCastException("Activity should implements RegisterUpdateCallback Interface!");
        }
        if (context instanceof UnRegisterUpdateCallback) {
            unRegisterUpdateCallback = (UnRegisterUpdateCallback) context;
        } else {
            throw new ClassCastException("Activity should implements RegisterUpdateCallback Interface!");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterUpdateCallback.unRegisterInterface(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        registerUpdateCallback.onRegisterInterface(this);
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.frag_categories_recycler);
        categoryList = new ArrayList<>();
        CategoriesAdapter catAdapter = new CategoriesAdapter(getContext(), categoryList, onCategoryClickListener);
        recyclerView.setAdapter(catAdapter);
        int cardQount;
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            cardQount = 2;
        } else {
            cardQount = 3;
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), cardQount));

        loadDataFromDb();
    }

    private void loadDataFromDb() {

        try {
            categoryList.clear();
            categoryList.addAll(HelperFactory.getHelper().getDaoCAtegory().getAllCategories());
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateData() {
        loadDataFromDb();
    }
}
