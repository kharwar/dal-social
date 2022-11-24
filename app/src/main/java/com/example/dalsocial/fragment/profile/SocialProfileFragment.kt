package com.example.dalsocial.fragment.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.dalsocial.R
import com.example.dalsocial.model.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class SocialProfileFragment : Fragment() {

    var user: User = User()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_social_profile, container, false)

        val userManagement = UserManagement()
        val userPersistence = UserPersistence()

        val edDisplayName = view.findViewById<TextInputEditText>(R.id.tiSocialProfileDisplayName)
        val edBio = view.findViewById<TextInputEditText>(R.id.tiSocialProfileBio)

        // reference: https://medium.com/android-beginners/material-chips-material-components-for-android-dd0ef942e5ce#:~:text=Android%20Chips%20is%20one%20the,block%2C%20such%20as%20a%20contact.
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupScannedUserInterests)

        userManagement.getUserByID(userPersistence, userManagement.getFirebaseUserID()!!) { usr ->
            val imageViewProfilePic = view?.findViewById<ImageView>(R.id.ivSocialProfilePic)
            if (usr != null) {
                user = usr

                edDisplayName.setText(user.displayName)
                edBio.setText(user.bio)

                if (usr.profilePictureURL != null && usr.profilePictureURL != "") {
                    Glide.with(this).load(usr.profilePictureURL).into(imageViewProfilePic!!)
                } else {
                    Glide.with(this).load(R.drawable.ic_baseline_account_circle_24).into(imageViewProfilePic!!)
                }

                for (interest in user.interests!!) {
                    val chip = Chip(requireContext())
                    chip.text = interest
                    chip.isCloseIconVisible = true
                    chip.setOnCloseIconClickListener {
                        chipGroup.removeView(chip)
                        user.interests!!.remove(chip.text.toString())
                    }
                    chipGroup.addView(chip)
                }
            }
        }

        val imageViewProfilePic = view.findViewById<ImageView>(R.id.ivSocialProfilePic)
        imageViewProfilePic.setOnClickListener {
            val popupMenu = PopupMenu(context, view)
            popupMenu.menuInflater.inflate(R.menu.profile_pic_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
                when (item!!.itemId) {
                    R.id.pic_option_1 -> {
                        // reference: https://www.geeksforgeeks.org/android-upload-an-image-on-firebase-storage-with-kotlin/
                        //https://stackoverflow.com/questions/61610024/how-to-upload-an-image-to-firebase-storage-using-kotlin-in-android-q

                        val galleryIntent = Intent(Intent.ACTION_PICK)
                        galleryIntent.type = "image/*"
                        imagePickerActivityResult.launch(galleryIntent)
                    }
                    R.id.pic_option_2 -> {

                    }
                }
                true
            }

            popupMenu.show()
        }

        val btnSave = view.findViewById<FloatingActionButton>(R.id.btnSocialProfileSave)
        btnSave.setOnClickListener {

            user.displayName = edDisplayName.text.toString()
            user.bio = edBio.text.toString()

            userManagement.createOrUpdateUser(userPersistence, user) {
                if (it) {
                    Snackbar.make(view, "Details updated successfully!", Snackbar.LENGTH_SHORT)
                        .show()
                } else {
                    Snackbar.make(view, "Something went wrong!", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        val tiInterest = view.findViewById<TextInputEditText>(R.id.tiSocialProfileInterestsInput)
        val fabAddInterest = view.findViewById<FloatingActionButton>(R.id.fabSocialProfileAddInterest)

        fabAddInterest.setOnClickListener {
            val chip = Chip(requireContext())
            chip.isCloseIconVisible = true
            chip.text = tiInterest.text.toString()
            chip.setOnCloseIconClickListener {
                chipGroup.removeView(chip)
                user.interests!!.remove(chip.text.toString())
            }
            chipGroup.addView(chip)
            tiInterest.text?.clear()
            user.interests?.add(chip.text.toString())
        }

        return view
    }

    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =

        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val imageViewProfilePic = view?.findViewById<ImageView>(R.id.ivSocialProfilePic)
            if (result != null) {
                val userManagementFragment: IUserManagement = UserManagement()
                val userPersistence: IUserPersistence = UserPersistence()
                userManagementFragment.uploadProfileImage(
                    userPersistence,
                    result.data?.data!!,
                ) { url ->
                    user.profilePictureURL = url
                    Glide.with(this).load(url).into(imageViewProfilePic!!)
                }
            }
        }

    private fun getFileExtension(uri: Uri): String? {
        return uri.toString().substring(uri.toString().lastIndexOf("/") + 1);
    }

}