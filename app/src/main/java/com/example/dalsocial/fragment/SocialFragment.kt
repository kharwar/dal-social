package com.example.dalsocial.fragment
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dalsocial.R
import com.example.dalsocial.cards.ArrayAdapter
import com.example.dalsocial.cards.Cards
import com.example.dalsocial.model.*
import com.google.firebase.database.*
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import kotlin.collections.ArrayList

class SocialFragment : Fragment() {

    private var usersDb: DatabaseReference? = null
    private var arrayAdapter: ArrayAdapter? = null
    private var currentUId: String? = null
    var rowItems: ArrayList<User>? = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_social, container, false)

        val userManagement: IUserManagement = UserManagement()
        val userPersistence: IUserPersistence = UserPersistence()

        userManagement.getAllUsers(userPersistence) { cards ->
            rowItems = cards

            arrayAdapter = ArrayAdapter(requireContext(), R.layout.fragment_social, rowItems)
            val flingContainer = view.findViewById<View>(R.id.frame) as SwipeFlingAdapterView
            flingContainer.adapter = arrayAdapter
            flingContainer.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {

                override fun removeFirstObjectInAdapter() {
                    Log.d("LIST", "removed object!")
                    rowItems?.removeAt(0)
                    arrayAdapter!!.notifyDataSetChanged()
                }

                override fun onLeftCardExit(dataObject: Any) {
                    Toast.makeText(requireContext(), "Left", Toast.LENGTH_SHORT).show()
                }

                override fun onRightCardExit(dataObject: Any) {
                    Toast.makeText(requireContext(), "Right", Toast.LENGTH_SHORT).show()

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
        }
        return view
    }
}
//Reference: https://github.com/LukeKotlin/tinder-clone-kotlin
