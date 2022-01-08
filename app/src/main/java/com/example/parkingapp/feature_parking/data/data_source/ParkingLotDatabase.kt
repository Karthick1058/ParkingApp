package com.example.parkingapp.feature_parking.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.parkingapp.feature_fee_collection.domain.model.ParkingTicket
import com.example.parkingapp.feature_parking.domain.model.ParkingSpace

@Database(entities = [ParkingSpace::class, ParkingTicket::class], version = 1)
abstract class ParkingLotDatabase: RoomDatabase() {

     abstract val parkingSpaceDao: ParkingSpaceDao

     companion object{
          const val DATABASE_NAME = "parking_space_db"
     }
}