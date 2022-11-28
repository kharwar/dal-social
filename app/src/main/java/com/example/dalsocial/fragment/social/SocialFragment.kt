package com.example.dalsocial.fragment.social
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dalsocial.R
import com.example.dalsocial.cards.MatchesAdapter
import com.example.dalsocial.model.social.ISocialMatches
import com.example.dalsocial.model.social.Match
import com.example.dalsocial.model.social.MatchPersistence
import com.example.dalsocial.model.social.SocialMatches
import com.example.dalsocial.model.user.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import com.tapadoo.alerter.Alerter

class SocialFragment : Fragment() {

    val userManagement: IUserManagement = UserManagement()
    val userPersistence: IUserPersistence = UserPersistence()
    val socialMatches: ISocialMatches = SocialMatches(MatchPersistence(), userManagement)

    lateinit var flingContainer: SwipeFlingAdapterView

    var data: ArrayList<User> = ArrayList()
    val filteredInterests: ArrayList<String> = ArrayList()

    private var arrayAdapter: MatchesAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_social, container, false)

        val interestChipGroup = view.findViewById<ChipGroup>(R.id.socialFilterChipGroup)
        val tvFilterLabel = view.findViewById<TextView>(R.id.tvFilterDisplayText)
        var isFilterOpen = false

        flingContainer = view.findViewById<View>(R.id.frame) as SwipeFlingAdapterView

        userManagement.getUserByID(userPersistence, userManagement.getFirebaseUserID()!!) { user ->
            if (user?.interests != null) {
                for (interest in user.interests!!) {
                    val filterChip = Chip(context)
                    filterChip.text = interest
                    filterChip.isChecked = true
                    filterChip.isCheckedIconVisible = true
                    filterChip.isCheckable = true
                    interestChipGroup.addView(filterChip)
                    filterChip.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            filteredInterests.add(interest)
                        } else {
                            filteredInterests.remove(interest)
                        }
                        load(filteredInterests)
                    }
                }
            }
        }

        load(filteredInterests)

        flingContainer.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {

            override fun removeFirstObjectInAdapter() {
                data.removeAt(0)
                arrayAdapter!!.notifyDataSetChanged()
            }

            override fun onLeftCardExit(dataObject: Any) {
                val user = dataObject as User
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

            override fun onRightCardExit(dataObject: Any) {
                val user = dataObject as User
                val includedUserIds = ArrayList<String>()
                includedUserIds.add(user.userID!!)
                includedUserIds.add(userManagement.getFirebaseUserID()!!)

                val match = Match(
                    matchInitiatorUserId = userManagement.getFirebaseUserID()!!,
                    toBeMatchedUserId = user.userID!!,
                    matchInitiatorUserIdLiked = true,
                    includedUsers = includedUserIds,
                )
                socialMatches.match(match) { matched ->
                    if (matched) {
                        Alerter.create(requireActivity())
                            .setText("You matched with ${user.firstName}!")
                            .setBackgroundColorRes(com.tapadoo.alerter.R.color.alerter_default_success_background)
                            .show()
                    }
                }
            }

            override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {}
            override fun onScroll(scrollProgressPercent: Float) {}
        })

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener { itemPosition, dataObject ->
            Toast.makeText(
                requireContext(),
                "Item Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }


        val filterButton = view.findViewById<View>(R.id.btnSocialFilter)
        filterButton.setOnClickListener {
            if (isFilterOpen) {
                interestChipGroup.visibility = View.GONE
                tvFilterLabel.visibility = View.GONE
            } else {
                interestChipGroup.visibility = View.VISIBLE
                tvFilterLabel.visibility = View.VISIBLE
            }
            isFilterOpen = !isFilterOpen
        }

        return view
    }

    fun load(filteredInterests: ArrayList<String>) {
        if (filteredInterests.isEmpty()) {
            userManagement.getAllUsers(userPersistence) { users ->
                socialMatches.filterRemoveAlreadyLikedUsers(users) { filteredUsers ->
                    data = filteredUsers
                    arrayAdapter = MatchesAdapter(requireContext(), R.layout.item_matches, data, flingContainer)
                    flingContainer.adapter = arrayAdapter
                    arrayAdapter!!.notifyDataSetChanged()
                }
            }

        } else {
            userManagement.getAllUsersByInterests(userPersistence, filteredInterests) { users ->
                socialMatches.filterRemoveAlreadyLikedUsers(users) { filteredData ->
                    data = filteredData
                    arrayAdapter = MatchesAdapter(requireContext(), R.layout.item_matches, data, flingContainer)
                    flingContainer.adapter = arrayAdapter
                    arrayAdapter!!.notifyDataSetChanged()
                }
            }
        }

    }
}
//Reference: https://github.com/LukeKotlin/tinder-clone-kotlin
