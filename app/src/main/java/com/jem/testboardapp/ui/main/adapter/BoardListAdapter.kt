package com.jem.testboardapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.jem.testboardapp.R
import com.jem.testboardapp.data.Board

class BoardListAdapter(var boardList : List<Board>, var requestManager: RequestManager) : RecyclerView.Adapter<BoardListViewHolder>() {

    private lateinit var onItemClick : View.OnClickListener

    fun setOnItemClickListener (l : View.OnClickListener){
        onItemClick = l;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardListViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_board, parent, false)
        mainView.setOnClickListener(onItemClick)
        return BoardListViewHolder(mainView)
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    override fun onBindViewHolder(holder: BoardListViewHolder, position: Int) {
        requestManager.load(boardList[position].board_img).centerCrop().into(holder.boardImage)
        holder.boardTitle.text = boardList[position].board_title
        holder.boardContent.text = boardList[position].board_content
    }

}