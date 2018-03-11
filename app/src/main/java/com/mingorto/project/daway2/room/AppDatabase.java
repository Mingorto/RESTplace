package com.mingorto.project.daway2.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {SavedPlaces.class, DownloadedPlacesMap.class, DownloadedPlacesFull.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract SavedPlacesDao savedPlacesDao();
    public abstract DownloadedPlacesMapDao downloadedPlacesMapDao();
    public abstract DownloadedPlacesFullDao downloadedPlaceFullInfoDao();
}
