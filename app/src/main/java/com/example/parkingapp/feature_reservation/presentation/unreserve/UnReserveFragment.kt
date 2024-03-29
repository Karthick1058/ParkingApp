package com.example.parkingapp.feature_reservation.presentation.unreserve

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.parkingapp.R
import com.example.parkingapp.databinding.FragmentUnreserveBinding
import com.example.parkingapp.feature_fee_collection.domain.util.DialogUtil
import com.example.parkingapp.feature_parking.common.Resource
import com.example.parkingapp.feature_parking.domain.model.Vehicle
import com.example.parkingapp.feature_parking.domain.util.VehicleType
import com.example.parkingapp.feature_reservation.presentation.ReservationViewModel
import com.example.parkingapp.feature_reservation.presentation.reservation.ReservationEvent
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UnReserveFragment : Fragment() {

    private lateinit var binding: FragmentUnreserveBinding
    private val viewModel by activityViewModels<ReservationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnreserveBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    private fun setupViews() {
        binding.btnUnReserve.setOnClickListener {
            val reservationTicketNum = binding.txtInpTicketNum.editText?.text.toString()
            val vehicleNum = binding.txtVehicleNum.editText?.text.toString()
            val vehicle = Vehicle(vehicleNum, "", "", VehicleType.CAR, reservationTicketNum = reservationTicketNum)
            viewModel.onEvent(ReservationEvent.UnReserveParkingSpace(vehicle))
        }
        listenForUnReserveResult()
    }

    private fun listenForUnReserveResult() {
        lifecycleScope.launch {
            viewModel.unReserveResult.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when(it){
                        is Resource.Error -> {
                            Snackbar.make(
                                requireView(),
                                it.message.toString(), BaseTransientBottomBar.LENGTH_SHORT
                            ).show()
                        }
                        is Resource.Loading -> TODO()
                        is Resource.Success -> {
                            if(it.data == true) showDialog()
                            else{
                                Snackbar.make(
                                    requireView(),
                                    "Input not valid", BaseTransientBottomBar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
        }
    }

    private fun showDialog() {
        val alertDialog = DialogUtil.create(
            requireContext(),
            "Vehicle UnReserved",
            "Your amount will be refunded to your account. Thank you",
            "Okay"
        ) { navigateToWelcomeFragment() }
        alertDialog.show()
    }

    private fun navigateToWelcomeFragment() {
        val action =
            UnReserveFragmentDirections.actionUnReserveFragmentToReservationWelcomeFragment()
        val controller = findNavController()
        if (controller.currentDestination?.id == R.id.unReserveFragment) {
            controller.navigate(action)
        }
    }
}