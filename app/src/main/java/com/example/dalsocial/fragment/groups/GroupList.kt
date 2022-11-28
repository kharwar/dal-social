package com.example.dalsocial.fragment.groups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dalsocial.databinding.FragmentGroupListBinding
import com.example.dalsocial.model.groups.Group
import com.example.dalsocial.model.groups.GroupAdapter
import com.example.dalsocial.model.groups.Groups

class GroupList : Fragment() {
    var binding: FragmentGroupListBinding? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_group_list, container, false)
        binding = FragmentGroupListBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = GroupAdapter(Groups.groupList,groupListItem)
        binding?.groupList?.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        binding?.groupList?.adapter = adapter
        binding?.floatingActionButton?.setOnClickListener{
            val action = GroupListDirections.actionGroup2ToCreateGroup2()
            Navigation.findNavController(it).navigate(action)
        }
    }
    private val groupListItem = fun(group: Group){
        val action = GroupListDirections.actionGroup2ToGroupDetailsJoin()
        findNavController().navigate(action)
    }
}