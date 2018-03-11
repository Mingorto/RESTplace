package com.mingorto.project.daway2.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DownloadedPlacesFullDao {
    @Query("SELECT * FROM downloadedplacesfull")
    List<DownloadedPlacesFull> getAll();

    @Query("SELECT * FROM downloadedplacesfull WHERE id IN (:uid)")
    List<DownloadedPlacesFull> loadAllByIds(int[] uid);

    @Insert
    void insertAll(List<DownloadedPlacesFull> downloadedPlacesFulls);

    @Insert
    void insertPlace(DownloadedPlacesFull downloadedPlacesFull);

    @Delete
    void delete(DownloadedPlacesFull downloadedPlacesFull);

    @Query("DELETE FROM downloadedplacesfull")
    void deleteAll();

    @Query("SELECT * FROM downloadedplacesfull WHERE placeId IN (:place)")
    List<DownloadedPlacesFull> loadAllByInnerId (String[] place);

    @Update
    void updateAll(List<DownloadedPlacesFull> downloadedPlacesFullList);

}
