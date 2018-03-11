package com.mingorto.project.daway2;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mingorto.project.daway2.parsing.Comment;
import com.mingorto.project.daway2.parsing.Example;
import com.mingorto.project.daway2.parsing.Place;
import com.mingorto.project.daway2.room.AppDatabase;
import com.mingorto.project.daway2.room.DownloadedPlacesFull;
import com.mingorto.project.daway2.room.DownloadedPlacesMap;
import com.mingorto.project.daway2.room.SavedPlaces;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    AppDatabase database;

    private GoogleMap mMap;
    BottomSheetBehavior bottomSheetBehavior;
    TextView description;
    TextView greetingIdText;
    TextView greetingContentText;
    ImageView refreshButton;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private GoogleApiClient mGoogleApiClient;
    LinearLayout showAll;
    LinearLayout listen;
    LinearLayout watch;
    LinearLayout move;
    LinearLayout play;
    LinearLayout drink;
    LinearLayout eat;

    List<DownloadedPlacesFull> listOfPlaces = new ArrayList<>();
    List<String> clickedMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout llBottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "full-info").allowMainThreadQueries().build();
        listOfPlaces = database.downloadedPlaceFullInfoDao().getAll();
        if (listOfPlaces.isEmpty())
            if (isNetworkAvailable(getApplicationContext()))
                new HttpRequestTask().execute();
            else
                Toast.makeText(getApplicationContext(), "Нет интернет-соединения", Toast.LENGTH_LONG).show();

        List<DownloadedPlacesMap> downloadedPlacesMapList = database.downloadedPlacesMapDao().getAll();
        for (DownloadedPlacesMap d : downloadedPlacesMapList) {
            Log.d("downloaded", d.getName());
        }


        greetingIdText = findViewById(R.id.placeName);
        greetingContentText = findViewById(R.id.ratingNum);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        description = findViewById(R.id.placeTextInfo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(this);

        View headerView = navigationView.getHeaderView(0);
        LinearLayout header = headerView.findViewById(R.id.nav_header_click);
        header.setOnClickListener(this);

        showAll = findViewById(R.id.showAll);
        listen = findViewById(R.id.listen);
        watch = findViewById(R.id.watch);
        move = findViewById(R.id.move);
        play = findViewById(R.id.play);
        drink = findViewById(R.id.drink);
        eat = findViewById(R.id.eat);

        showAll.setOnClickListener(this);
        listen.setOnClickListener(this);
        watch.setOnClickListener(this);
        move.setOnClickListener(this);
        play.setOnClickListener(this);
        drink.setOnClickListener(this);
        eat.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.saved_places) {
            Intent intent = new Intent(this, SwipedActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng spb = new LatLng(59.9343, 30.3351);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spb, 10));
        addMarkersOnMap(mMap);

        // Add a marker in Sydney and move the camera


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                TextView placeName = findViewById(R.id.placeName);
                TextView description = findViewById(R.id.placeTextInfo);
                RatingBar ratingBar = findViewById(R.id.ratingBar);
                TextView ratingNum = findViewById(R.id.ratingNum);
                TextView ratingCount = findViewById(R.id.ratingCount);
                TextView address = findViewById(R.id.addressInfo);
                String id = null;
                for (final DownloadedPlacesFull e : listOfPlaces) {
                    if (String.valueOf(e.getPlaceId()).equals(marker.getTag().toString())) {
                        placeName.setText(String.valueOf(e.getName()));
                        ratingBar.setRating((float) e.getRating());
                        ratingNum.setText(String.valueOf(e.getRating()));
                        ratingCount.setText("(" + String.valueOf(e.getRatingCount()) + ")");
                        address.setText(e.getAddress());
                        description.setText(e.getDescription());

                        ViewPager viewPager = findViewById(R.id.viewPager);
                        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(getApplicationContext(), e.getImageLinks());
                        viewPager.setAdapter(imageSliderAdapter);

                        List<Comment> comments = e.getComments();
                        LinearLayout parentLayout = findViewById(R.id.commentView);
                        parentLayout.removeAllViews();
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view;

                        final String websiteAddress = e.getWebsite();

                        ImageView website = findViewById(R.id.website);
                        final ImageView favorite = findViewById(R.id.favorite);
                        final ImageView share = findViewById(R.id.share);

                        website.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new
                                        Intent(Intent.ACTION_VIEW, Uri.parse(websiteAddress));
                                startActivity(browserIntent);
                            }
                        });

                        share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "Я нашёл классное место через daWay2, смотри!" + websiteAddress);
                                shareIntent.setType("text/plain");
                                startActivity(Intent.createChooser(shareIntent, "Поделиться местом"));
                            }
                        });

                        class MyUndoListener implements View.OnClickListener {

                            @Override
                            public void onClick(View v) {

                            }
                        }

                        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "places").allowMainThreadQueries().build();

                        List<SavedPlaces> spList = db.savedPlacesDao().getAll();

                        boolean z = false;
                        for (SavedPlaces sp : spList)
                            if (sp.getInnerId().equals(e.getPlaceId()))
                                z = true;

                        if (z)
                            favorite.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
                        else
                            favorite.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);

                        favorite.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                List<SavedPlaces> list = db.savedPlacesDao().getAll();
                                Snackbar snackbar;
                                boolean check = true;
                                int position = 0;

                                for (SavedPlaces savedPlaces : list) {
                                    Log.d("При сохранении:", String.valueOf(savedPlaces.getPosition()));
                                    position = savedPlaces.getPosition() + 1;
                                    System.out.println(position);
                                    if (savedPlaces.getInnerId().equals(e.getPlaceId()))
                                        check = false;
                                }

                                if (check) {
                                    System.out.println("Не содержит");
                                    db.savedPlacesDao().insertPlace(new SavedPlaces(e.getPlaceId(), e.getName(), e.getType(), e.getImageLinks().get(0), e.getAddress(), e.getRating(), position));
                                    snackbar = Snackbar.make(findViewById(R.id.bottom_sheet), R.string.addedToFavorite, Snackbar.LENGTH_SHORT);
                                    favorite.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
                                } else {
                                    System.out.println("Содержит");
                                    snackbar = Snackbar.make(findViewById(R.id.bottom_sheet), R.string.deletedFromFavorite, Snackbar.LENGTH_SHORT);
                                    List<SavedPlaces> savedPlaces = db.savedPlacesDao().loadAllByInnerId(new String[]{e.getPlaceId()});
                                    db.savedPlacesDao().delete(savedPlaces.get(0));
                                    List<SavedPlaces> removedList = db.savedPlacesDao().getAll();
                                    for (int i = position; i < removedList.size(); i++)
                                        removedList.get(i).setPosition(i);
                                    db.savedPlacesDao().updateAll(removedList);
                                    favorite.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                                }
                                snackbar.show();
                            }
                        });

                        for (int i = 0; i < e.getComments().size(); i++) {
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
                        clickedMarkers.add(marker.getTag().toString());
                    }
                }

                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    id = marker.getTag().toString();
                } else if (!marker.getTag().equals(id)) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    id = marker.getTag().toString();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    id = marker.getTag().toString();
                }
                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });
        description.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if (description.getMaxLines() != Integer.MAX_VALUE)
                    description.setMaxLines(Integer.MAX_VALUE);
                else
                    description.setMaxLines(3);
            }
        });
    }

    public void addMarkersOnMap(GoogleMap googleMap) {
        for (DownloadedPlacesFull place : listOfPlaces) {
            LatLng location = new LatLng(place.getLatitude(), place.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(location)).setTag(place.getPlaceId());
        }
    }

    public void deleteMarkersOnMap(int type) {
        mMap.clear();
        for (DownloadedPlacesFull event : listOfPlaces) {
            if (event.getType() == type)
                mMap.addMarker(new MarkerOptions().position(new LatLng(event.getLatitude(), event.getLongitude()))).setTag(event.getPlaceId());
        }
    }

    public void dropMarkersOnMap(GoogleMap googleMap) {
        googleMap.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case (R.id.nav_header_click):
                Toast.makeText(this, "Click", Toast.LENGTH_LONG).show();
                break;
            case (R.id.refreshButton):
                if (isNetworkAvailable(getApplicationContext())) {
                    new HttpRequestTask().execute();
                    mMap.clear();
                    addMarkersOnMap(mMap);
                } else
                    Toast.makeText(getApplicationContext(), "Нет интернет соединения", Toast.LENGTH_LONG).show();
                break;

                /*
                Настройка нижнего бара, отвечающего за показ типов заведений
                 */

            case (R.id.showAll):
                mMap.clear();
                for (DownloadedPlacesFull event : listOfPlaces)
                    mMap.addMarker(new MarkerOptions().position(new LatLng(event.getLatitude(), event.getLongitude()))).setTag(event.getPlaceId());
                Toast.makeText(this, R.string.showAll, Toast.LENGTH_SHORT).show();
                break;
            case (R.id.listen):
                deleteMarkersOnMap(1);
                Toast.makeText(this, R.string.listen, Toast.LENGTH_SHORT).show();
                break;
            case (R.id.watch):
                deleteMarkersOnMap(2);
                Toast.makeText(this, R.string.watch, Toast.LENGTH_SHORT).show();
                break;
            case (R.id.move):
                deleteMarkersOnMap(3);
                Toast.makeText(this, R.string.move, Toast.LENGTH_SHORT).show();
                break;
            case (R.id.play):
                deleteMarkersOnMap(4);
                Toast.makeText(this, R.string.play, Toast.LENGTH_SHORT).show();
                break;
            case (R.id.drink):
                deleteMarkersOnMap(5);
                Toast.makeText(this, R.string.drink, Toast.LENGTH_SHORT).show();
                break;
            case (R.id.eat):
                deleteMarkersOnMap(6);
                Toast.makeText(this, R.string.eat, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Example> {
        RelativeLayout loadingLayout = findViewById(R.id.loadingLayout);

        @Override
        protected void onPreExecute() {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            loadingLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Example doInBackground(Void... params) {
            try {
                final String url = "http://188.166.89.117:8080/place";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                return restTemplate.getForObject(url, Example.class);
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Example example) {
            AppDatabase database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "full-info").allowMainThreadQueries().build();
            List<Place> list = example.getEmbedded().getPlace();
            List<DownloadedPlacesFull> downloadedPlacesFulls = new ArrayList<>();
            List<DownloadedPlacesFull> existingData = database.downloadedPlaceFullInfoDao().getAll();
            for (Place p : list) {
                downloadedPlacesFulls.add(new DownloadedPlacesFull(p.getPlaceId(), p.getName(), p.getLatitude(), p.getLongitude(), p.getType(), p.getDescription(), p.getAddress(), p.getRating(), p.getRatingCount(), p.getPhone(), p.getVkLink(), p.getWebsite(), p.getBuyLink(), p.getComments(), p.getImageLinks()));
            }
            database.downloadedPlaceFullInfoDao().deleteAll();
            database.downloadedPlaceFullInfoDao().insertAll(downloadedPlacesFulls);
            listOfPlaces = database.downloadedPlaceFullInfoDao().getAll();
            for (DownloadedPlacesFull downloadedPlacesFull : listOfPlaces)
                Log.d("Места c сети", downloadedPlacesFull.getName());
            loadingLayout.setVisibility(View.GONE);
        }
    }

    private class ClickedMarker {
        private String id;
        private boolean isClicked;

        public ClickedMarker(String id, boolean isClicked) {
            this.id = id;
            this.isClicked = isClicked;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isClicked() {
            return isClicked;
        }
        public void setClicked(boolean clicked) {
            isClicked = clicked;
        }
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return super.onWindowStartingActionMode(callback);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
