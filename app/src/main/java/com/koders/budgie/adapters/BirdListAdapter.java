package com.koders.budgie.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.koders.budgie.R;
import com.koders.budgie.config.Constants;
import com.koders.budgie.model.BirdInfo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BirdListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<BirdInfo> birdInfoList;
    private Context context;
    private OnBirdClickListener onBirdClickListener;
    private boolean isLoadingAdded = false;

    public BirdListAdapter(Context context, List<BirdInfo> birdInfoList, OnBirdClickListener onBirdClickListener) {
        this.context = context;
        this.birdInfoList = birdInfoList;
        this.onBirdClickListener = onBirdClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                view = inflater.inflate(R.layout.bird_list_item, parent, false);
                viewHolder = new Holder(view, onBirdClickListener);
                break;
            case LOADING:
                view = inflater.inflate(R.layout.loading_item, parent, false);
                viewHolder = new LoadingHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BirdInfo birdInfo = birdInfoList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                Holder contentHolder = (Holder) holder;

                Log.d("ImagePath", Constants.BASE_URL + birdInfo.getImage());
                Glide.with(context).load(Constants.BASE_URL + birdInfo.getImage()).placeholder(R.drawable.bird).into(contentHolder.birdImage);
                contentHolder.ringNumber.setText(birdInfo.getRingNumber());
                contentHolder.ringOwnerName.setText(birdInfo.getRingOwnerName());
                break;
            case LOADING:
//                Do nothing
                break;
        }
    }

    @Override
    public int getItemCount() {
        return birdInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == birdInfoList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView birdImage;
        TextView ringNumber, ringOwnerName;
        OnBirdClickListener onBirdClickListener;

        public Holder(@NonNull View itemView, OnBirdClickListener onBirdClickListener) {
            super(itemView);

            birdImage = itemView.findViewById(R.id.birdImage);
            ringNumber = itemView.findViewById(R.id.ringNumber);
            ringOwnerName = itemView.findViewById(R.id.ringOwnerName);
            this.onBirdClickListener = onBirdClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onBirdClickListener.onBirdClick(ringNumber.getText().toString());
        }
    }

    public static class LoadingHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.footerProgress);
        }
    }

    public interface OnBirdClickListener {
        void onBirdClick(String ringNum);
    }

//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//                String charString = charSequence.toString();
//                if (charString.isEmpty()) {
//                    birdInfoListFiltered = birdInfoList;
//                } else {
//                    List<BirdInfo> filteredList = new ArrayList<>();
//                    for (BirdInfo birdInfo : birdInfoList) {
//
//                        if (birdInfo.getRingNumber().toLowerCase().contains(charString.toLowerCase())) {
//                            filteredList.add(birdInfo);
//                        }
//                    }
//
//                    birdInfoListFiltered = filteredList;
//                }
//
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = birdInfoListFiltered;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                birdInfoListFiltered = (ArrayList<BirdInfo>) filterResults.values;
//                notifyDataSetChanged();
//            }
//        };
//    }

    public void add(BirdInfo birdInfo) {
        birdInfoList.add(birdInfo);
        notifyItemInserted(birdInfoList.size() - 1);
    }

    public void addAll(List<BirdInfo> birdList) {
        for (BirdInfo birdInfo : birdList) {
            add(birdInfo);
        }
    }

    public void remove(BirdInfo birdInfo) {
        int position = birdInfoList.indexOf(birdInfo);
        if (position > -1) {
            birdInfoList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new BirdInfo());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = birdInfoList.size() - 1;
        BirdInfo item = getItem(position);
        if (item != null) {
            birdInfoList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public BirdInfo getItem(int position) {
        return birdInfoList.get(position);
    }
}
