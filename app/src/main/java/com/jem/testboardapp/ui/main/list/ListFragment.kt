package com.jem.testboardapp.ui.main.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import kotlinx.android.synthetic.main.fragment_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListFragment : Fragment(), View.OnClickListener {

    lateinit var boardData : ArrayList<Board>
    lateinit var boardListAdapter: BoardListAdapter
    lateinit var requestManager: RequestManager
    var networkService: NetworkService = ApiClient.getRetrofit().create(NetworkService::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v : View = inflater.inflate(R.layout.fragment_list, container, false)

        requestManager = Glide.with(this)
        //insertData()
        insertDataFromServer()

        v.board_upload_btn.setOnClickListener {
            var intent = Intent(context, UploadActivity::class.java)
            intent.putExtra("insertFlag", 1)
            startActivity(intent)
        }

        v.btn_refresh_main.setOnClickListener {
            insertDataFromServer()
        }

        return v
    }

    fun updateClicked(idx : Int){
        var updateClickedResponse = networkService.updateClicked(boardData[idx].board_id)

        updateClickedResponse.enqueue(object : Callback<PostResponse> {
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
        getBoardListRespone.enqueue(object : Callback<GetAllBoardResponse> {
            override fun onFailure(call: Call<GetAllBoardResponse>, t: Throwable) {
                Log.v("MainActivity", "received server error = " + t.toString())
            }

            override fun onResponse(call: Call<GetAllBoardResponse>, response: Response<GetAllBoardResponse>) {
                Log.v("MainActivity", "received data = " + response.body().toString())
                if(response.isSuccessful){
                    if(response.body()!!.data != null){
                        boardData = response.body()!!.data
                        boardListAdapter =  BoardListAdapter(boardData, requestManager)
                        boardListAdapter.setOnItemClickListener(this@ListFragment)
                        board_main_rv.adapter = boardListAdapter
                        board_main_rv.layoutManager = LinearLayoutManager(context)
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {
        val idx : Int = board_main_rv.getChildAdapterPosition(v!!)
        updateClicked(idx)
        var intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("board_id", boardData[idx].board_id)
        startActivity(intent)
    }
}