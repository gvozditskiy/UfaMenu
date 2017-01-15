package com.gvozditskiy.ufamenu.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gvozditskiy.ufamenu.Constants;
import com.gvozditskiy.ufamenu.Interfaces.OnOfferClickListener;
import com.gvozditskiy.ufamenu.Parser.Offer;
import com.gvozditskiy.ufamenu.Parser.Param;
import com.gvozditskiy.ufamenu.R;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Alexey on 15.01.2017.
 */

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferVH> {
    List<Offer> dataSet;
    Context context;
    OnOfferClickListener onOfferClickListener;

    public OfferAdapter(List<Offer> dataSet, Context context, OnOfferClickListener onOfferClickListener) {
        this.dataSet = dataSet;
        this.context = context;
        this.onOfferClickListener = onOfferClickListener;
    }

    @Override
    public OfferVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.card_offer, parent, false);
        return new OfferVH(v);
    }

    @Override
    public void onBindViewHolder(OfferVH holder, final int position) {
        Offer offer = dataSet.get(position);
        holder.titleTv.setText(offer.getName());
        holder.priceTv.setText(offer.getPrice());
        //// TODO: 15.01.2017  no_picture в .error(), сделать ProgressBar на время загрузки
        Glide.with(context).load(offer.getPictureUrl()).placeholder(R.drawable.no_picture).into(holder.imageView);
        holder.weightTv.setText(offer.getWeight());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOfferClickListener.onOfferClicked(dataSet.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataSet==null) {
            return 0;
        } else {
            return dataSet.size();
        }
    }

    public class OfferVH extends RecyclerView.ViewHolder {
        TextView priceTv;
        TextView titleTv;
        TextView weightTv;
        ImageView imageView;

        public OfferVH(View itemView) {
            super(itemView);
            priceTv = (TextView) itemView.findViewById(R.id.card_offer_price);
            titleTv = (TextView) itemView.findViewById(R.id.card_offer_title);
            weightTv = (TextView) itemView.findViewById(R.id.card_offer_weight);
            imageView = (ImageView) itemView.findViewById(R.id.card_offer_image);
        }
    }
}
