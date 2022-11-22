package com.example.dalsocial.cards
import android.content.Context
import android.text.method.TextKeyListener.clear
import com.example.dalsocial.R

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class ArrayAdapter(context: Context?, resourceId: Int, items: List<Cards?>?) : ArrayAdapter<Cards?>(context!!, resourceId, items!!) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val cardItem = getItem(position)
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.swipe_item, parent, false)
        }
        val name = convertView!!.findViewById<View>(R.id.name) as TextView
        val image = convertView.findViewById<View>(R.id.image) as ImageView
        name.text = cardItem!!.name
        when (cardItem.profileImageUrl) {
            "default" -> Glide.with(convertView.context).load(R.mipmap.ic_launcher).into(image)
            else -> {
                //Glide.clear(image)
                Glide.with(convertView.context).load(cardItem.profileImageUrl).into(image)
            }
        }
        return convertView
    }
}
