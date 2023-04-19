package com.example.cs4530lifestyleapp

import androidx.room.*

@Dao
interface DetailsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(detailsTable: DetailsTable)

    @Query("DELETE FROM details_table")
    suspend fun deleteAll()
}