package com.mingorto.project.daway2;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mingorto.project.daway2.room.AppDatabase;
import com.mingorto.project.daway2.room.SavedPlaces;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SavedPlacesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_places_activity);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "saved-places").allowMainThreadQueries().build();

        View view;
        LinearLayout parentLayout = findViewById(R.id.savedPlacesActivity);
        parentLayout.removeAllViews();
        LayoutInflater layoutInflater = getLayoutInflater();

        if (db.savedPlacesDao().getAll() != null) {
            List<SavedPlaces> savedPlacesList = db.savedPlacesDao().getAll();
            for (int i = 0; i < savedPlacesList.size(); i++) {
                view = layoutInflater.inflate(R.layout.saved_places_row, null);

                ImageView savedPlaceImage = view.findViewById(R.id.savedPlaceImage);
                Picasso.with(getApplicationContext()).load(savedPlacesList.get(i).getMainPhotoUrl()).resize(150, 150).transform(new CircularTransformation()).into(savedPlaceImage);

                final TextView savedPlaceName = view.findViewById(R.id.savedPlaceName);
                savedPlaceName.setText(savedPlacesList.get(i).getName());

                final RatingBar savedPlaceRatingBar = view.findViewById(R.id.savedPlaceRatingBar);
                savedPlaceRatingBar.setRating((float) savedPlacesList.get(i).getRating());

                final TextView savedPlaceRatingNum = view.findViewById(R.id.savedPlaceRatingNum);
                savedPlaceRatingNum.setText(String.valueOf(savedPlacesList.get(i).getRating()));

                parentLayout.addView(view);
            }
        }
    }
}
