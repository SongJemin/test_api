package com.jem.testboardapp.ui.main.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jem.testboardapp.R
import kotlinx.android.synthetic.main.item_board.view.*

class BoardListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var boardImage : ImageView = itemView.findViewById(R.id.board_main_img)
    var boardTitle : TextView = itemView.findViewById(R.id.board_title_tv)
    var boardContent : TextView = itemView.findViewById(R.id.board_content_tv)
}