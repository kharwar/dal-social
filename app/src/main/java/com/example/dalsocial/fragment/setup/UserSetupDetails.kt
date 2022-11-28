package com.example.dalsocial.fragment.setup

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dalsocial.R
import com.tapadoo.alerter.Alerter
import java.time.LocalDate
import java.time.ZoneOffset

class UserSetupDetails : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_setup_details, container, false)

        val edFirstName = view.findViewById<EditText>(R.id.edUserSetupFirstName)
        val edLastName = view.findViewById<EditText>(R.id.edUserSetupLastName)
        val dpDob = view.findViewById<DatePicker>(R.id.datePickerUserSetup)
        val btnNext = view.findViewById<Button>(R.id.btnUserSetupDetails)

        btnNext.setOnClickListener {
            val firstName = edFirstName.text.toString()
            val lastName = edLastName.text.toString()

            // get timestamp in ms from date picker
            // reference: https://stackoverflow.com/questions/45813379/how-can-i-return-localdate-now-in-milliseconds
            val date = LocalDate.of(dpDob.year, dpDob.month, dpDob.dayOfMonth)
            val timestamp = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

            val userDetails: HashMap<String, Any> =
                arguments?.getSerializable("userDetails") as HashMap<String, Any>
            userDetails["firstName"] = firstName
            userDetails["lastName"] = lastName
            userDetails["dob"] = timestamp

            val bundle = Bundle()
            bundle.putSerializable("userDetails", userDetails)

            if (firstName.isEmpty()) {
                Alerter.create(requireActivity())
                    .setText("Duh, you need to enter your first name!")
                    .setBackgroundColorRes(R.color.md_theme_light_error)
                    .show()
            } else if (lastName.isEmpty()) {
                Alerter.create(requireActivity())
                    .setText("Duh, you need to enter your last name!")
                    .setBackgroundColorRes(R.color.md_theme_light_error)
                    .show()
            } else if (date.isAfter(LocalDate.now().minusYears(18))) {
                Alerter.create(requireActivity())
                    .setText("You must be 18")
                    .setBackgroundColorRes(R.color.md_theme_light_error)
                    .show()
            } else {
                findNavController().navigate(
                    R.id.action_userSetupDetails_to_userSetupInterests,
                    bundle
                )
            }
        }

        return view
    }

}