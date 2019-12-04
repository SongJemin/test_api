package com.jem.testboardapp.ui.detail.adapter

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jem.testboardapp.R

class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val commentContent : TextView = itemView!!.findViewById(R.id.tv_content_item_comment)
    val commentDatetime : TextView = itemView!!.findViewById(R.id.tv_date_time_item_comment)
    val commentDeleteBtn : Button = itemView!!.findViewById(R.id.btn_close_item_comment)
}