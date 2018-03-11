package com.mingorto.project.daway2;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mingorto.project.daway2.parsing.Comment;
import com.mingorto.project.daway2.room.AppDatabase;
import com.mingorto.project.daway2.room.DownloadedPlacesFull;
import com.mingorto.project.daway2.room.SavedPlaces;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout parentLayout;
    DownloadedPlacesFull place;
    ImageView favorite;
    String innerPlaceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_info_in_list);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        innerPlaceId = getIntent().getStringExtra("innerPlaceId");
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "full-info").allowMainThreadQueries().build();
        List<DownloadedPlacesFull> list = db.downloadedPlaceFullInfoDao().loadAllByInnerId(new String[]{innerPlaceId});
        place = list.get(0);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(place.getName());

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ViewPager viewPager = findViewById(R.id.expandedImage);
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(getApplicationContext(), place.getImageLinks());
        viewPager.setAdapter(imageSliderAdapter);

        final TextView placeInfoSaved = findViewById(R.id.placeInfoSaved);
        placeInfoSaved.setText(place.getDescription());

        placeInfoSaved.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if (placeInfoSaved.getMaxLines() != Integer.MAX_VALUE)
                    placeInfoSaved.setMaxLines(Integer.MAX_VALUE);
                else
                    placeInfoSaved.setMaxLines(3);
            }
        });

        List<Comment> comments = place.getComments();

        parentLayout = findViewById(R.id.commentViewSaved);
        parentLayout.removeAllViews();

        LayoutInflater layoutInflater = getLayoutInflater();
        View view;

        ImageView website = findViewById(R.id.websiteSaved);
        website.setOnClickListener(this);

        favorite = findViewById(R.id.favoriteSaved);
        favorite.setOnClickListener(this);

        AppDatabase savedPlaces = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "places").allowMainThreadQueries().build();
        List<SavedPlaces> spList = savedPlaces.savedPlacesDao().getAll();

        for (SavedPlaces sp : spList)
            if (sp.getInnerId().equals(place.getPlaceId()))
                favorite.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);

        ImageView share = findViewById(R.id.shareSaved);
        share.setOnClickListener(this);

        for (int i = 0; i < place.getComments().size(); i++) {
            view = layoutInflater.inflate(R.layout.row, null);
            ImageView commenterAvatar = view.findViewById(R.id.commenterAvatar);
            Picasso.with(getApplicationContext()).load(comments.get(i).getCommentatorImage()).into(commenterAvatar);
            final TextView commentatorName = view.findViewById(R.id.comentatorName);
            commentatorName.setText(comments.get(i).getCommenterName());
            final TextView commentText = view.findViewById(R.id.commentText);
            commentText.setText(comments.get(i).getDescription());
            commentText.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View view) {
                    if (commentText.getMaxLines() != Integer.MAX_VALUE)

                        commentText.setMaxLines(Integer.MAX_VALUE);
                    else
                        commentText.setMaxLines(3);
                }
            });
            parentLayout.addView(view);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.websiteSaved):
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(place.getWebsite()));
                startActivity(browserIntent);
                break;
            case (R.id.shareSaved):
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Я нашёл классное место через daWay2, смотри! \n" + place.getWebsite());
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Поделиться местом"));
                break;
            case (R.id.favoriteSaved):
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "places").allowMainThreadQueries().build();
                List<SavedPlaces> list = db.savedPlacesDao().getAll();
                Snackbar snackbar;
                boolean check = true;
                int position = 0;

                for (SavedPlaces savedPlaces : list) {
                    Log.d("При сохранении:", String.valueOf(savedPlaces.getPosition()));
                    position = savedPlaces.getPosition() + 1;
                    System.out.println(position);
                    if (savedPlaces.getInnerId().equals(place.getPlaceId()))
                        check = false;
                }

                if (check) {
                    System.out.println("Не содержит");
                    favorite.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    db.savedPlacesDao().insertPlace(new SavedPlaces(place.getPlaceId(), place.getName(), place.getType(), place.getImageLinks().get(0), place.getAddress(), place.getRating(), position));
                    snackbar = Snackbar.make(findViewById(R.id.place_info_collapsing), R.string.addedToFavorite, Snackbar.LENGTH_SHORT);
                } else {
                    System.out.println("Содержит");
                    favorite.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                    List<SavedPlaces> savedPlaces = db.savedPlacesDao().loadAllByInnerId(new String[]{innerPlaceId});
                    db.savedPlacesDao().delete(savedPlaces.get(0));
                    List<SavedPlaces> removedList = db.savedPlacesDao().getAll();
                    for (int i = position; i < removedList.size(); i++)
                        removedList.get(i).setPosition(i);
                    db.savedPlacesDao().updateAll(removedList);
                    snackbar = Snackbar.make(findViewById(R.id.place_info_collapsing), R.string.deletedFromFavorite, Snackbar.LENGTH_SHORT);
                }
                snackbar.show();
                break;
        }
    }
}
