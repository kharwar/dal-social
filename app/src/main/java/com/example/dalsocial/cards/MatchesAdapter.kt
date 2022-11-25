package com.example.dalsocial.cards

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.dalsocial.R
import com.example.dalsocial.model.User
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class MatchesAdapter(context: Context?, resourceId: Int, items: List<User?>?) :
    ArrayAdapter<User?>(context!!, resourceId, items!!) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val cardItem = getItem(position)
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.swipe_item, parent, false)
        }
        val profileImage = convertView!!.findViewById<ImageView>(R.id.matchCardImage)
        val firstName = convertView.findViewById<TextView>(R.id.matchCardName)
        val displayName = convertView.findViewById<TextView>(R.id.matchCardDisplayName)
        val bio = convertView.findViewById<TextView>(R.id.matchCardBio)
        val interests = convertView.findViewById<ChipGroup>(R.id.matchCardUserInterests)

        firstName.text = cardItem!!.firstName
        displayName.text = cardItem.displayName
        bio.text = cardItem.bio
        Glide.with(context).load(cardItem.profilePictureURL).into(profileImage)

        for (interest in cardItem.interests!!) {
            val chip = Chip(context)
            chip.text = interest
            interests.addView(chip)
        }


        return convertView
    }
}
//Reference: https://github.com/LukeKotlin/tinder-clone-kotlin
