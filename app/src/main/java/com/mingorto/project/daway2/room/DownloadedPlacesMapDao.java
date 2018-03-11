package com.mingorto.project.daway2.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DownloadedPlacesMapDao {
    @Query("SELECT * FROM downloadedplacesmap")
    List<DownloadedPlacesMap> getAll();

    @Query("SELECT * FROM downloadedplacesmap WHERE id IN (:uid)")
    List<DownloadedPlacesMap> loadAllByIds(int[] uid);

    @Insert
    void insertAll(List<DownloadedPlacesMap> downloadedPlacesMaps);

    @Insert
    void insertPlace(DownloadedPlacesMap downloadedPlacesMap);

    @Delete
    void delete(DownloadedPlacesMap downloadedPlacesMap);

    @Update
    void updateAll(List<DownloadedPlacesMap> downloadedPlacesMaps);
}
