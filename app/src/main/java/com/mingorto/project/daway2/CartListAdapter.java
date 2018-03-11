package com.mingorto.project.daway2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingorto.project.daway2.room.SavedPlaces;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> {
    private Context context;
    private List<SavedPlaces> savedPlacesList;
    private OnItemTouchListener onItemTouchListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView savedPlaceName, savedPlaceRatingNum, savedPlaceAddressInfo;
        public ImageView savedPlaceImage;
        public RatingBar savedPlaceRatingBar;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            savedPlaceName = view.findViewById(R.id.savedPlaceName);
            savedPlaceRatingNum = view.findViewById(R.id.savedPlaceRatingNum);
            savedPlaceAddressInfo = view.findViewById(R.id.savedPlaceAddressInfo);
            savedPlaceImage = view.findViewById(R.id.savedPlaceImage);
            savedPlaceRatingBar = view.findViewById(R.id.savedPlaceRatingBar);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            viewForeground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(getAdapterPosition());
                    onItemTouchListener.onPlaceClicked(v, getAdapterPosition());
                }
            });
        }
    }

    public CartListAdapter(Context context, List<SavedPlaces> savedPlacesList, OnItemTouchListener onItemTouchListener) {
        this.context = context;
        this.savedPlacesList = savedPlacesList;
        this.onItemTouchListener = onItemTouchListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SavedPlaces item = savedPlacesList.get(position);
        holder.savedPlaceName.setText(item.getName());
        holder.savedPlaceRatingNum.setText(String.valueOf(item.getRating()));
        holder.savedPlaceAddressInfo.setText(item.getAddress());
        holder.savedPlaceRatingBar.setRating((float) item.getRating());
        Picasso.with(context).load(item.getMainPhotoUrl()).into(holder.savedPlaceImage);
    }

    @Override
    public int getItemCount() {
        return savedPlacesList.size();
    }

    public void removeItem(int position) {
        savedPlacesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(SavedPlaces item, int position) {
        savedPlacesList.add(position, item);
        notifyItemInserted(position);
    }
}