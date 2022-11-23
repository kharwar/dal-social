package com.example.dalsocial.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.findFragment
import com.example.dalsocial.databinding.FragmentCreateEventBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog
import com.google.android.material.timepicker.MaterialTimePicker
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class CreateEventFragment : Fragment() {

    val TAG = "CreateEventFragment"

    var binding: FragmentCreateEventBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCreateEventBinding.inflate(layoutInflater)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        val timePicker = MaterialTimePicker.Builder()
            .setTitleText("Select Time")
            .build()

        binding?.ibNewEventDate?.setOnClickListener { it ->
            it.isEnabled = false
            Log.d("CreateEventFragment", "clicked: ib")
            datePicker.show(activity?.supportFragmentManager!!, "date")
            it.isEnabled = true
        }



        var date: String? = null
        datePicker.addOnPositiveButtonClickListener {
            val dateFormat = SimpleDateFormat("dd-MMMM-yyyy")
            date = dateFormat.format(Date(it))
            timePicker.show(activity?.supportFragmentManager!!, "time")
        }

        timePicker.addOnPositiveButtonClickListener { it ->
            val hour = timePicker.hour.hours
            val minute = timePicker.minute.minutes
            val scheduledDate = "$date $hour:$minute"
            binding?.etNewEventDate?.setText(scheduledDate)
        }


    }

}