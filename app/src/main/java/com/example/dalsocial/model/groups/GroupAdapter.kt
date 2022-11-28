package com.example.dalsocial.model.groups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dalsocial.databinding.FragmentGroupCardBinding

class GroupAdapter(private var groupList:List<Group>,
                   private val onItemClicked:(Group)->Unit):
        RecyclerView.Adapter<GroupAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = FragmentGroupCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groupList[position]
        holder.bindItem(group,onItemClicked)
    }

    override fun getItemCount(): Int {
        return groupList.size
    }
    class ViewHolder(private val itemBinding: FragmentGroupCardBinding):
            RecyclerView.ViewHolder(itemBinding.root){
                fun bindItem(group: Group, itemOnClickListener: (Group)->Unit){
                    itemBinding.groupname.text =  group.name
                    itemBinding.registered.text = group.status

                    itemBinding.grouplistItem.setOnClickListener {
                        itemOnClickListener(group)
                    }
                }
            }
}