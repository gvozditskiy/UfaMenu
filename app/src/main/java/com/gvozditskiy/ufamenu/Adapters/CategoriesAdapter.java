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
import com.gvozditskiy.ufamenu.Interfaces.OnCategoryClickListener;
import com.gvozditskiy.ufamenu.Parser.Category;
import com.gvozditskiy.ufamenu.R;

import java.util.List;

/**
 * Created by Alexey on 14.01.2017.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CatVH> {
    List<Category> dataSet;
    Context context;
    OnCategoryClickListener onCategoryClickListener;

    public CategoriesAdapter(Context context, List<Category> dataSet, OnCategoryClickListener onCategoryClickListener) {
        this.context = context;
        this.dataSet = dataSet;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @Override
    public CatVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_category, parent, false);
        return new CatVH(view);
    }

    @Override
    public void onBindViewHolder(CatVH holder, final int position) {
        holder.caption.setText(dataSet.get(position).getValue());
//                <category id = "25" > Патимейкер </category >
//                <category id = "1" > Пицца </category >
//                <category id = "2" > Сеты </category >
//                <category id = "18" > Роллы </category >
//                <category id = "5" > Суши </category >
//                <category id = "23" > Добавки </category >
//                <category id = "10" > Десерты </category >
//                <category id = "9" > Напитки </category >
//                <category id = "20" > Закуски </category >
//                <category id = "3" > Лапша </category >
//                <category id = "6" > Супы </category >
//                <category id = "7" > Салаты </category >
//                <category id = "8" > Теплое </category >
        int drawableId=0;
        switch (dataSet.get(position).getId()) {
            case "25":
                drawableId = R.drawable.patymaker;
                break;
            case "1":
                drawableId = R.drawable.pizza;
                break;
            case "2":
                drawableId = R.drawable.set;
                break;
            case "18":
                drawableId = R.drawable.roll;
                break;
            case "5":
                drawableId = R.drawable.sushi;
                break;
            case "23":
                drawableId = R.drawable.addition;
                break;
            case "10":
                drawableId = R.drawable.desert;
                break;
            case "9":
                drawableId = R.drawable.drinks;
                break;
            case "20":
                drawableId = R.drawable.snacks;
                break;
            case "3":
                drawableId = R.drawable.noodles;
                break;
            case "6":
                drawableId = R.drawable.soup;
                break;
            case "7":
                drawableId = R.drawable.salad;
                break;
            case "8":
                drawableId = R.drawable.warm;
                break;
        }
        Glide.with(context).load("").placeholder(drawableId).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCategoryClickListener.onCategoryClicked(dataSet.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dataSet == null) {
            return 0;
        } else {
            return dataSet.size();
        }
    }

    public class CatVH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView caption;

        public CatVH(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.card_category_image);
            caption = (TextView) itemView.findViewById(R.id.card_category_caption);

        }
    }
}
