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
import com.example.dalsocial.model.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MatchesAdapter(context: Context?, resourceId: Int, items: List<User?>?) :
    ArrayAdapter<User?>(context!!, resourceId, items!!) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val cardItem = getItem(position)
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.swipe_item, parent, false)
        }

        val profileImage = view!!.findViewById<ImageView>(R.id.matchCardImage)
        val firstName = view.findViewById<TextView>(R.id.matchCardName)
        val displayName = view.findViewById<TextView>(R.id.matchCardDisplayName)
        val bio = view.findViewById<TextView>(R.id.matchCardBio)
        val interests = view.findViewById<ChipGroup>(R.id.matchCardUserInterests)

        val btnLike = view.findViewById<FloatingActionButton>(R.id.matchCardLike)

        val userManagement = UserManagement()
        val socialMatches = SocialMatches(matchesPersistence = MatchPersistence(), userManagement)
        btnLike.setOnClickListener {
            cardItem?.let { it1 -> actionSwipe(it1, userManagement, socialMatches) }
        }

        val btnDislike = view.findViewById<FloatingActionButton>(R.id.matchCardDislike)
        btnDislike.setOnClickListener {
            cardItem?.let { it1 -> actionSwipe(it1, userManagement, socialMatches) }
        }

        firstName.text = cardItem!!.firstName
        displayName.text = cardItem.displayName
        bio.text = cardItem.bio
        Glide.with(context).load(cardItem.profilePictureURL).into(profileImage)

        for (interest in cardItem.interests!!) {
            val chip = Chip(context)
            chip.text = interest
            interests.addView(chip)
        }

        return view
    }

    fun actionSwipe(user: User, userManagement: UserManagement, socialMatches: SocialMatches) {
        val includedUserIds = ArrayList<String>()
        includedUserIds.add(user.userID!!)
        includedUserIds.add(userManagement.getFirebaseUserID()!!)

        val match = Match(
            matchInitiatorUserId = userManagement.getFirebaseUserID()!!,
            toBeMatchedUserId = user.userID!!,
            matchInitiatorUserIdLiked = true,
            includedUsers = includedUserIds,
        )
        socialMatches.match(match) {

        }
    }
}
//Reference: https://github.com/LukeKotlin/tinder-clone-kotlin
