package com.example.dalsocial.model.social

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dalsocial.R
import com.example.dalsocial.fragment.chat.ChatFragment


class MatchesViewHolders(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var mMatchId: TextView
    var mMatchName: TextView
    var mMatchImage: ImageView
    override fun onClick(view: View) {
        val intent = Intent(view.context, ChatFragment::class.java)
        val b = Bundle()
        b.putString("matchId", mMatchId.text.toString())
        intent.putExtras(b)
        view.context.startActivity(intent)
    }

    init {
        itemView.setOnClickListener(this)
        mMatchId = itemView.findViewById<View>(R.id.Matchid) as TextView
        mMatchName = itemView.findViewById<View>(R.id.MatchName) as TextView
        mMatchImage = itemView.findViewById<View>(R.id.MatchImage) as ImageView
    }
}
//Reference: https://github.com/LukeKotlin/tinder-clone-kotlin