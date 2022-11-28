package com.example.dalsocial.fragment.profile

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dalsocial.R
import com.example.dalsocial.model.user.IUserPersistence
import com.example.dalsocial.model.user.User
import com.example.dalsocial.model.user.UserManagement
import com.example.dalsocial.model.user.UserPersistence
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.tapadoo.alerter.Alerter
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*


class AccountDetailsFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account_details, container, false)

        val tiFirstName = view.findViewById<TextInputEditText>(R.id.tiAccountsFirstName)
        val tiLastName = view.findViewById<TextInputEditText>(R.id.tiAccountsLastName)
        val dobDatePicker = view.findViewById<DatePicker>(R.id.datePickerAccountDetails)

        var user = User()

        val userManagement = UserManagement()
        val userPersistence: IUserPersistence = UserPersistence()
        userManagement.getUserByID(userPersistence, userManagement.getFirebaseUserID()!!) { usr ->
            if (usr != null) {
                user = usr

                tiFirstName.setText(usr.firstName)
                tiLastName.setText(usr.lastName)

                // reference: https://stackoverflow.com/questions/16686298/string-timestamp-to-calendar-in-java
                val timestamp = usr.dob
                val date = Date(timestamp!!)
                val calendar = Calendar.getInstance()
                calendar.time = date

                dobDatePicker.updateDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            }
        }

        val btnSave = view.findViewById<FloatingActionButton>(R.id.btnAccountDetailsSave)
        btnSave.setOnClickListener {
            val firstName = tiFirstName.text.toString()
            val lastName = tiLastName.text.toString()

            val date =
                LocalDate.of(dobDatePicker.year, dobDatePicker.month, dobDatePicker.dayOfMonth)
            val timestamp = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

            user.dob = timestamp
            user.firstName = firstName
            user.lastName = lastName

            userManagement.createOrUpdateUser(userPersistence, user) { success ->
                if (success) {
                    Alerter.create(requireActivity())
                        .setText("Details update successfully")
                        .setBackgroundColorRes(R.color.md_theme_light_secondary)
                        .show()
                } else {
                    Alerter.create(requireActivity())
                        .setText("Something went wrong")
                        .setBackgroundColorRes(R.color.md_theme_light_error)
                        .show()
                }
            }
        }

        val btnResetPassword = view.findViewById<Button>(R.id.btnAccountDetailsResetPassword)
        btnResetPassword.setOnClickListener {
            userManagement.resetPassword { success ->
                if (success) {
                    Snackbar.make(view, "Reset email sent to registered email address", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(view, "Something went wrong", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        // reference: https://stackoverflow.com/questions/5448653/how-to-implement-onbackpressed-in-fragments
        val btnBack = view.findViewById<ImageView>(R.id.backAccountDetailBtn)
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

}