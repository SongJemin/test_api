package com.jem.testboardapp.ui.main.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.jem.testboardapp.R
import com.jem.testboardapp.data.Board
import com.jem.testboardapp.ui.detail.DetailActivity
import com.jem.testboardapp.ui.main.adapter.BoardListAdapter
import com.jem.testboardapp.ui.upload.UploadActivity
import com.jem.testboardapp.util.network.ApiClient
import com.jem.testboardapp.util.network.NetworkService
import com.jem.testboardapp.util.network.get.GetAllBoardResponse
import com.jem.testboardapp.util.network.post.PostResponse
import kotlinx.android.synthetic.main.activity_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var boardData : ArrayList<Board>
    lateinit var boardListAdapter: BoardListAdapter
    lateinit var requestManager: RequestManager
    var networkService: NetworkService = ApiClient.getRetrofit().create(NetworkService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        requestManager = Glide.with(this)
        //insertData()
        insertDataFromServer()

        board_upload_btn.setOnClickListener {
            var intent = Intent(applicationContext, UploadActivity::class.java)
            intent.putExtra("insertFlag", 1)
            startActivity(intent)
        }

        btn_refresh_main.setOnClickListener {
            insertDataFromServer()
        }
    }

//    fun initToolbar(){
//        tb_top_main.setTitle("게시판 연습")
//        tb_top_main.setTitleTextColor(Color.WHITE)
//        setSupportActionBar(tb_top_main)
//    }

    fun updateClicked(idx : Int){
        var updateClickedResponse = networkService.updateClicked(boardData[idx].board_id)

        updateClickedResponse.enqueue(object : Callback<PostResponse>{
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if(response.isSuccessful){

                }
            }
        })
    }

    fun insertDataFromServer(){

        val getBoardListRespone = networkService.getBoardList()

        getBoardListRespone.enqueue(object : Callback<GetAllBoardResponse>{
            override fun onFailure(call: Call<GetAllBoardResponse>, t: Throwable) {
                Log.v("MainActivity", "received server error = " + t.toString())
            }

            override fun onResponse(call: Call<GetAllBoardResponse>, response: Response<GetAllBoardResponse>) {
                Log.v("MainActivity", "received data = " + response.body().toString())
                if(response.isSuccessful){
                    if(response.body()!!.data != null){
                        boardData = response.body()!!.data
                        boardListAdapter =  BoardListAdapter(boardData, requestManager)
                        boardListAdapter.setOnItemClickListener(this@ListActivity)
                        board_main_rv.adapter = boardListAdapter
                        board_main_rv.layoutManager = LinearLayoutManager(applicationContext)
                    }
                }
            }
        })
    }

    fun insertData(){
        boardData = ArrayList<Board>();


        boardData.add(Board(0, "제목1", "내용1" , "null"))
        boardData.add(Board(0, "제목2", "내용2" , "null"))
        boardData.add(Board(0, "제목3", "내용3" , "null"))
        boardData.add(Board(0, "제목4", "내용4" , "null"))
        boardData.add(Board(0, "제목5", "내용5" , "null"))

        boardListAdapter = BoardListAdapter(boardData, requestManager)

        board_main_rv.layoutManager = LinearLayoutManager(applicationContext)
        board_main_rv.adapter = boardListAdapter
    }

    override fun onClick(v: View?) {
        val idx : Int = board_main_rv.getChildAdapterPosition(v!!)
        updateClicked(idx)
        var intent = Intent(applicationContext, DetailActivity::class.java)
        intent.putExtra("board_id", boardData[idx].board_id)
        startActivity(intent)
    }
}
