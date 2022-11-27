package com.example.dalsocial.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.findFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.Navigation
import com.example.dalsocial.databinding.FragmentCreateEventBinding
import com.example.dalsocial.model.Event
import com.example.dalsocial.model.EventPersistence
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    private var imageUri: Uri? = null
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

        binding?.newEventBg?.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            imageLauncher.launch(gallery)
        }


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

        var dateLong: Long? = null;
        datePicker.addOnPositiveButtonClickListener { it ->
            dateLong = it
            timePicker.show(activity?.supportFragmentManager!!, "time")
        }

        var scheduledDate: String? = null;
        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour.hours
            val minute = timePicker.minute.minutes
            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
            val date = dateFormat.format(dateLong)
            scheduledDate = "$date $hour:$minute"
            binding?.etNewEventDate?.setText(scheduledDate)
        }

        binding?.newEventBtn?.setOnClickListener {
            val title: String = binding?.newEventTitle?.text.toString()
            val description: String = binding?.newEventDesc?.text.toString()
            val date: String? = scheduledDate
            var event = Event(
                "",
                title,
                description,
                date,
                "",
                ""
            )
            val eventPersistence = EventPersistence()


            eventPersistence.createEvent(event, imageUri!!) { event ->
                var message = "Test"
                if (event != null) {
                    message = "Event creation failed"
                    val bundle: Bundle = bundleOf();
                    bundle.putBoolean("isAdded", true)
                    setFragmentResult("requestKey", bundle)
                    val action =
                        CreateEventFragmentDirections.actionCreateEventFragmentToEventFragment(
                            eventId =  event.eventId!!,
                            eventTitle = event.title!!,
                            eventDescription = event.description!!,
                            eventBg = event.imageUrl!!,
                            eventDate = event.scheduledDate!!
                        )
                    Navigation.findNavController(view).navigate(action)
                }
            }
        }


    }

    var imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data
                binding?.newEventBg?.setImageURI(imageUri)
            }
        }

//    override fun onFocusChange(p0: View?, p1: Boolean) {
//
//    }

}