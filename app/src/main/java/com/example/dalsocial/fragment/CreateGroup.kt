
package com.example.dalsocial.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dalsocial.R
import com.example.dalsocial.databinding.FragmentCreateEventBinding
import com.example.dalsocial.databinding.FragmentCreateGroupBinding
import com.example.dalsocial.model.Group
import com.google.firebase.firestore.FirebaseFirestore

class CreateGroup : Fragment() {
    private var binding: FragmentCreateGroupBinding? = null
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_create_group, container, false)
        binding = FragmentCreateGroupBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.createGroup?.setOnClickListener{
            addToFirebase()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
    private fun addToFirebase()
    {
        val name = binding?.groupNameInput?.text.toString()
        val description = binding?.groupDescriptionInput?.text.toString()
        val meetUpdate = binding?.nextMeetupInput?.text.toString()
        val status = "registered"

        val group = Group(name,meetUpdate,description,status)

        db.collection("group").add(group)
            .addOnSuccessListener {
                document -> Log.e("Firestore Success","${document.id} added")
            }
            .addOnFailureListener{
                Log.e("Firestore error","Error creating Group")
            }
    }
}