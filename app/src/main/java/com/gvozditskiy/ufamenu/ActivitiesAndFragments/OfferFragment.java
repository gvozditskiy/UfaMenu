package com.gvozditskiy.ufamenu.ActivitiesAndFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gvozditskiy.ufamenu.Constants;
import com.gvozditskiy.ufamenu.HelperFactory;
import com.gvozditskiy.ufamenu.Parser.Offer;
import com.gvozditskiy.ufamenu.Parser.Param;
import com.gvozditskiy.ufamenu.R;

import java.sql.SQLException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferFragment extends Fragment {

    ImageView imageView;
    TextView titleTv;
    TextView priceTv;
    TextView weightTv;
    TextView descriptionTv;


    public OfferFragment() {
        // Required empty public constructor
    }

    public static OfferFragment newInstance(String id) {
        OfferFragment fragment = new OfferFragment();
        Bundle args = new Bundle();
        args.putString(Constants.OFFERFRAG_ARGS_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = (ImageView) view.findViewById(R.id.frag_offer_image);
        titleTv = (TextView) view.findViewById(R.id.frag_offer_title);
        priceTv = (TextView) view.findViewById(R.id.frag_offer_price);
        weightTv = (TextView) view.findViewById(R.id.frag_offer_weight);
        descriptionTv = (TextView) view.findViewById(R.id.frag_offer_description);
        Offer offer = getOffer(getArguments().getString(Constants.OFFERFRAG_ARGS_ID));
        if (offer!=null) {
            //// TODO: 15.01.2017  no_picture в .error(), сделать ProgressBar на время загрузки
            Glide.with(getContext()).load(offer.getPictureUrl()).placeholder(R.drawable.no_picture).into(imageView);
            titleTv.setText(offer.getName());
            priceTv.setText(offer.getPrice());
            weightTv.setText(offer.getWeight());
            descriptionTv.setText(offer.getDescription());
        }

    }

    private Offer getOffer(String id) {
        Offer offer=null;
        try {
            offer = HelperFactory.getHelper().getDaoOffer().getOfferById(id).get(0);
            List<Param> params = HelperFactory.getHelper().getDaoParam().getParamForOffer(id, "Вес");
            if (params.size()>0 && params.get(0).getValue()!=null)
            offer.setWeight(params.get(0).getValue());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offer;
    }
}
