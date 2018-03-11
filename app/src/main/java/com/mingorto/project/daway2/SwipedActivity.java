package com.mingorto.project.daway2;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mingorto.project.daway2.room.AppDatabase;
import com.mingorto.project.daway2.room.SavedPlaces;

import java.util.ArrayList;
import java.util.List;

public class SwipedActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private static final String TAG = SwipedActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<SavedPlaces> savedPlaces;
    private CartListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    BottomSheetBehavior bottomSheetBehavior;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_to_delete);

        textView = findViewById(R.id.savedCondition);

        recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        savedPlaces = new ArrayList<>();

        LinearLayout llBottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        OnItemTouchListener itemTouchListener = new OnItemTouchListener() {

            @Override
            public void onPlaceClicked(View view, int position) {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "places").allowMainThreadQueries().build();
                SavedPlaces place = db.savedPlacesDao().loadAllByPosition(new int[]{position}).get(0);
                Intent intent = new Intent(getApplicationContext(), ScrollingActivity.class);
                intent.putExtra("innerPlaceId", place.getInnerId());
                startActivity(intent);
            }
        };

        mAdapter = new CartListAdapter(this, savedPlaces, itemTouchListener);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        prepareCart();
    }

    private void prepareCart() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "places").allowMainThreadQueries().build();
        savedPlaces.clear();
        savedPlaces.addAll(db.savedPlacesDao().getAll());
        showText();
        mAdapter.notifyDataSetChanged();
    }

    private void showText() {
        if (savedPlaces.isEmpty()) {
            System.out.println("EMPTY");
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
            System.out.println("NOT EMPTY");
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartListAdapter.MyViewHolder) {
            String name = savedPlaces.get(viewHolder.getAdapterPosition()).getName();
            String placeId = savedPlaces.get(viewHolder.getAdapterPosition()).getInnerId();
            final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "places").allowMainThreadQueries().build();
            final List<SavedPlaces> list = db.savedPlacesDao().loadAllByInnerId(new String[]{placeId});

            db.savedPlacesDao().delete(list.get(0));
            List<SavedPlaces> removedList = db.savedPlacesDao().getAll();
            for (int i = position; i < removedList.size(); i++)
                removedList.get(i).setPosition(i);
            db.savedPlacesDao().updateAll(removedList);

            final SavedPlaces deletedItem = savedPlaces.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            mAdapter.removeItem(viewHolder.getAdapterPosition());

            showText();

            Snackbar snackbar = Snackbar.make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                    db.savedPlacesDao().insertPlace(list.get(0));
                    showText();
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
