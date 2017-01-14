package com.gvozditskiy.ufamenu.ActivitiesAndFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gvozditskiy.ufamenu.Adapters.CategoriesAdapter;
import com.gvozditskiy.ufamenu.HelperFactory;
import com.gvozditskiy.ufamenu.Parser.Category;
import com.gvozditskiy.ufamenu.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment {
    RecyclerView recyclerView;
    List<Category> categoryList;


    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.frag_categories_recycler);
        categoryList = new ArrayList<>();
        CategoriesAdapter catAdapter = new CategoriesAdapter(getContext(),  categoryList);
        recyclerView.setAdapter(catAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        try {
            categoryList.addAll(HelperFactory.getHelper().getDaoCAtegory().getAllCategories());
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
