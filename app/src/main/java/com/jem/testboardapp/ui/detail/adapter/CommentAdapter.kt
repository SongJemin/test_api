package com.jem.testboardapp.ui.detail.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jem.testboardapp.R
import com.jem.testboardapp.data.Comment
import com.jem.testboardapp.util.network.ApiClient
import com.jem.testboardapp.util.network.NetworkService
import com.jem.testboardapp.util.network.delete.DeleteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentAdapter(var commentData : ArrayList<Comment>) : RecyclerView.Adapter<CommentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)

        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentData.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.commentContent.text = commentData[position].comment_content
        holder.commentDatetime.text = commentData[position].comment_date_time

        holder.commentDeleteBtn.setOnClickListener {
            Log.v("CommentAdapter", "선택 = " + position)
            deleteComment(position)
        }
    }

    fun deleteComment(position: Int){
        var networkService : NetworkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var deleteCommentResponse = networkService.deleteComment(commentData[position].comment_id)
        deleteCommentResponse.enqueue(object : Callback<DeleteResponse>{
            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                Log.v("CommentAdapter", "error = " + t.toString())
            }

            override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                Log.v("CommentAdapter", "데이터 = " + response.body().toString())
                if(response.isSuccessful){
                    Log.v("CommentAdapter", "성공 = " + response.body().toString())
                    commentData.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        })
    }
}