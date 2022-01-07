package com.example.parkingapp.feature_parking.domain.use_case

data class ParkingUseCases(
    val createParkingLot: CreateParkingLot,
    val getAllotmentStatus: GetAllotmentStatus,
    val parkVehicle: ParkVehicle
)