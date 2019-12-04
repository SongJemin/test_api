package com.jem.testboardapp.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jem.testboardapp.R
import com.jem.testboardapp.data.Board
import com.jem.testboardapp.data.Comment
import com.jem.testboardapp.ui.detail.adapter.CommentAdapter
import com.jem.testboardapp.ui.main.list.ListActivity
import com.jem.testboardapp.ui.upload.UploadActivity
import com.jem.testboardapp.util.network.ApiClient
import com.jem.testboardapp.util.network.NetworkService
import com.jem.testboardapp.util.network.delete.DeleteResponse
import com.jem.testboardapp.util.network.get.GetBoardResponse
import com.jem.testboardapp.util.network.get.GetCommentsResponse
import com.jem.testboardapp.util.network.post.PostResponse
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    var board_id : Int = 0
    var networkService : NetworkService = ApiClient.getRetrofit().create(NetworkService::class.java)
    lateinit var comment : Comment
    lateinit var comments : ArrayList<Comment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        board_id = intent.getIntExtra("board_id", 0)
        getComment()
        btn_back_detail.setOnClickListener {
            var intent = Intent(applicationContext, ListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        btn_modify_detail.setOnClickListener {
            updateBoard();
        }

        btn_delete_detail.setOnClickListener {
            deleteBoard();
        }

        btn_comment_confirm_detail.setOnClickListener {
            postComment()
        }

        getData()
    }

    fun getComment(){
        var getCommentsResponse = networkService.getComment(board_id)
        getCommentsResponse.enqueue(object : Callback<GetCommentsResponse>{
            override fun onFailure(call: Call<GetCommentsResponse>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<GetCommentsResponse>, response: Response<GetCommentsResponse>) {
                if(response.isSuccessful){
                    if(response.body()!!.data != null){
                        comments = response.body()!!.data
                        var commentAdapter = CommentAdapter(comments)
                        rv_comment_detail.adapter = commentAdapter
                        rv_comment_detail.layoutManager = LinearLayoutManager(applicationContext)
                    }
                }
            }
        })
    }

    fun postComment(){
        comment = Comment(0, board_id, et_comment_detail.text.toString(), null)
        var postCommentResponse = networkService.postComment(comment)

        postCommentResponse.enqueue(object : Callback<PostResponse>{
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if(response.isSuccessful){
                    et_comment_detail.setText("")
                    getComment();
                }
            }
        })
    }

    fun deleteBoard(){
        var getDeleteResponse = networkService.deleteBoard(board_id)
        getDeleteResponse.enqueue(object : Callback<DeleteResponse>{
            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                Log.v("DetailActivity", "삭제 성공")
                var intent = Intent(applicationContext, ListActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        })
    }

    fun updateBoard(){
        var intent = Intent(applicationContext,UploadActivity::class.java)
        intent.putExtra("insertFlag", 0)
        intent.putExtra("board_id", board_id)
        startActivity(intent)
    }

    fun getData(){

        var getBoardResponse = networkService.getOneBoard(board_id)
        getBoardResponse.enqueue(object : Callback<GetBoardResponse>{
            override fun onFailure(call: Call<GetBoardResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<GetBoardResponse>, response: Response<GetBoardResponse>) {
                var boardData : Board
                if(response.isSuccessful){
                    if(response.body()!!.data != null){
                        boardData = response.body()!!.data

                        tv_title_content_detail.text = boardData.board_title
                        tv_content_content_detail.text = boardData.board_content
                        Glide.with(applicationContext).load(boardData.board_img).centerCrop().into(img_picture_detail)
                    }
                }
            }
        })
    }
}
