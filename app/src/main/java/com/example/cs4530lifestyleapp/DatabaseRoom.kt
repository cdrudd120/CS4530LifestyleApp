package com.example.cs4530lifestyleapp

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import kotlin.jvm.Volatile
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [DetailsTable::class], version = 2, exportSchema = false)
abstract class DatabaseRoom : RoomDatabase() {
    abstract fun detailsDao(): DetailsDao

    // Make the db singleton. Could in theory
    // make this an object class, but the companion object approach
    // is nicer (imo)
    companion object {
        @Volatile
        private var mInstance: DatabaseRoom? = null
        fun getDatabase(
            context: Context,
            scope : CoroutineScope
        ): DatabaseRoom {
            return mInstance?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseRoom::class.java, "details.db"
                )
                    .addCallback(RoomDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                mInstance = instance
                instance
            }
        }

        private class RoomDatabaseCallback(
            private val scope: CoroutineScope
        ): RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                mInstance?.let { database ->
                    scope.launch(Dispatchers.IO){
                        populateDbTask(database.detailsDao())
                    }
                }
            }
        }

        suspend fun populateDbTask (detailsDao: DetailsDao) {
            detailsDao.insert(DetailsTable("Test", "User", 3, 5, "Male", 28,
                200, "New York", "sedentary", "27", "100"))
        }
    }
}