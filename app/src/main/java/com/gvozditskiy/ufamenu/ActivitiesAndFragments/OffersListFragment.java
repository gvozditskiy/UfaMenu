package com.gvozditskiy.ufamenu.ActivitiesAndFragments;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gvozditskiy.ufamenu.Adapters.OfferAdapter;
import com.gvozditskiy.ufamenu.Constants;
import com.gvozditskiy.ufamenu.HelperFactory;
import com.gvozditskiy.ufamenu.Interfaces.DataUpdateCallback;
import com.gvozditskiy.ufamenu.Interfaces.OnOfferClickListener;
import com.gvozditskiy.ufamenu.Interfaces.RegisterUpdateCallback;
import com.gvozditskiy.ufamenu.Interfaces.UnRegisterUpdateCallback;
import com.gvozditskiy.ufamenu.Parser.Offer;
import com.gvozditskiy.ufamenu.Parser.Param;
import com.gvozditskiy.ufamenu.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OffersListFragment extends Fragment implements DataUpdateCallback {
    RecyclerView recyclerView;
    List<Offer> dataList;
    OfferAdapter offerAdapter;
    OnOfferClickListener onOfferClickListener;
    RegisterUpdateCallback registerUpdateCallback;
    UnRegisterUpdateCallback unRegisterUpdateCallback;

    public static OffersListFragment newInstance(String id) {
        OffersListFragment fragment = new OffersListFragment();
        Bundle args = new Bundle();
        args.putString(Constants.OFFERFRAG_ARGS_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOfferClickListener) {
            onOfferClickListener = (OnOfferClickListener) context;
        } else {
            throw new ClassCastException("Activity should implements OnOfferClickListener Interface!");
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

    public OffersListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        registerUpdateCallback.onRegisterInterface(this);
        return inflater.inflate(R.layout.fragment_offers_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.frag_offers_list_recycler);
        dataList = new ArrayList<>();
        offerAdapter = new OfferAdapter(dataList, getContext(), onOfferClickListener);
        recyclerView.setAdapter(offerAdapter);
        int cardQount;
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            cardQount = 2;
        } else {
            cardQount = 3;
        }
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(cardQount, StaggeredGridLayoutManager.VERTICAL));

        loadDataFromDb();


    }

    private void loadDataFromDb() {
        try {
            dataList.clear();
            List<Offer> offers = HelperFactory.getHelper().getDaoOffer()
                    .getOffersByCategoryId(getArguments().getString(Constants.OFFERFRAG_ARGS_ID));
            for (Offer offer : offers) {
                List<Param> params = new ArrayList<>();
                try {
                    params = HelperFactory.getHelper().getDaoParam().getParamForOffer(offer.getId(), "Вес");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (params.size() > 0 && params.get(0) != null) {
                    offer.setWeight(params.get(0).getValue());
                }
            }

            dataList.addAll(offers);
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateData() {
        Log.d("OffersListFrag", "updateData()");
        loadDataFromDb();
    }
}
