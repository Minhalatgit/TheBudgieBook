package com.koders.budgie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koders.budgie.R;
import com.koders.budgie.model.Navigation;

import java.util.List;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.Holder> {

    private List<Navigation> navigationList;
    private Context context;
    private onNavigationClick onNavigationClick;

    public NavigationAdapter(List<Navigation> navigationList, Context context, onNavigationClick onNavigationClick) {
        this.navigationList = navigationList;
        this.context = context;
        this.onNavigationClick = onNavigationClick;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_item,parent,false);
        return new Holder(v, onNavigationClick);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Navigation navigation=navigationList.get(position);

        holder.navigationTitle.setText(navigation.getName());
        holder.navigationImage.setImageResource(navigation.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return navigationList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView navigationImage;
        public TextView navigationTitle;
        public onNavigationClick onNavigationClick;

        public Holder(@NonNull View itemView, onNavigationClick onNavigationClick) {
            super(itemView);

            navigationImage=itemView.findViewById(R.id.navigation_image);
            navigationTitle=itemView.findViewById(R.id.navigation_text);
            this.onNavigationClick = onNavigationClick;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNavigationClick.onItemClick(getAdapterPosition());
        }
    }

    public interface onNavigationClick {
        void onItemClick(int position);
    }
}
