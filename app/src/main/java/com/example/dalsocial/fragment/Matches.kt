package com.example.dalsocial.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dalsocial.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class Matches : Fragment() {
    private var mRecyclerView: RecyclerView? = null
    private var mMatchesAdapter: RecyclerView.Adapter<*>? = null
    private var mMatchesLayoutManager: RecyclerView.LayoutManager? = null
    private var cusrrentUserID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_matches, container, false)
        cusrrentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        mRecyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView
        mRecyclerView!!.isNestedScrollingEnabled = false
        mRecyclerView!!.setHasFixedSize(true)
        mMatchesLayoutManager = LinearLayoutManager(requireContext())
        mRecyclerView!!.layoutManager = mMatchesLayoutManager
        mMatchesAdapter = MatchesAdapter(dataSetMatches, requireContext())
        mRecyclerView!!.adapter = mMatchesAdapter
        userMatchId
        return inflater.inflate(R.layout.fragment_matches, container, false)

    }
    private val userMatchId: Unit
        private get() {
            val matchDb = FirebaseDatabase.getInstance().reference.child("Users").child(
                cusrrentUserID.toString()
            ).child("connections").child("matches")
            matchDb.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (match in dataSnapshot.children) {
                            FetchMatchInformation(match.key.toString())
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    private fun FetchMatchInformation(key: String) {
        val userDb = FirebaseDatabase.getInstance().reference.child("Users").child(key)
        userDb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userId = dataSnapshot.key
                    var name = ""
                    var profileImageUrl = ""
                    if (dataSnapshot.child("name").value != null) {
                        name = dataSnapshot.child("name").value.toString()
                    }
                    if (dataSnapshot.child("profileImageUrl").value != null) {
                        profileImageUrl = dataSnapshot.child("profileImageUrl").value.toString()
                    }
                    val obj = MatchesObject(userId!!, name, profileImageUrl)
                    resultsMatches.add(obj)
                    mMatchesAdapter!!.notifyDataSetChanged()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private val resultsMatches = ArrayList<MatchesObject>()
    private val dataSetMatches: List<MatchesObject>
        private get() = resultsMatches

}

//Reference: https://github.com/LukeKotlin/tinder-clone-kotlin