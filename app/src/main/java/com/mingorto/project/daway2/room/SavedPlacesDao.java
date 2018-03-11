package com.mingorto.project.daway2.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface SavedPlacesDao {
    @Query("SELECT * FROM savedplaces")
    List<SavedPlaces> getAll();

    @Query("SELECT * FROM savedplaces WHERE id IN (:uid)")
    List<SavedPlaces> loadAllByIds(int[] uid);

    @Insert
    void insertAll(SavedPlaces... savedPlaces);

    @Insert
    void insertPlace(SavedPlaces savedPlaces);

    @Query("SELECT * FROM savedplaces WHERE innerId IN (:innId)")
    List<SavedPlaces> loadAllByInnerId(String[] innId);

    @Query("SELECT * FROM savedplaces WHERE position IN (:pos)")
    List<SavedPlaces> loadAllByPosition(int[] pos);

    @Delete
    void delete(SavedPlaces savedPlaces);

    @Update
    void updateAll(List<SavedPlaces> savedPlaces);
}
