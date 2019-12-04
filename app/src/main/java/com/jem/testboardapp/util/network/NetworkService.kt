package com.jem.testboardapp.util.network

import com.jem.testboardapp.data.Comment
import com.jem.testboardapp.util.network.delete.DeleteResponse
import com.jem.testboardapp.util.network.get.GetAllBoardResponse
import com.jem.testboardapp.util.network.get.GetBoardResponse
import com.jem.testboardapp.util.network.get.GetCommentsResponse
import com.jem.testboardapp.util.network.post.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    // GET
    // 게시글 불러오기
    @GET("/testapi/api/rest/board/all")
    fun getBoardList(
    ) : Call<GetAllBoardResponse>

    // 특정 게시글 불러오기
    @GET("/testapi/api/rest/board/{board_id}")
    fun getOneBoard(
        @Path("board_id") board_id : Int
    ) : Call<GetBoardResponse>

    // 댓글 가져오기
    @GET("/testapi/api/rest/comment/select/{board_id}")
    fun getComment(
        @Path ("board_id") board_id : Int
    ) : Call<GetCommentsResponse>

    // POST
    @Multipart
    @POST("/testapi/api/rest/board/insert")
    fun postBoard(
        @Part ("board_title") board_title: RequestBody,
        @Part ("board_content") board_content: RequestBody,
        @Part board_img: MultipartBody.Part?
    ) : Call<PostResponse>

    @POST("/testapi/api/rest/comment/insert")
    fun postComment(
        @Body comment : Comment
    ) : Call<PostResponse>

    // PUT
    @Multipart
    @PUT("/testapi/api/rest/board/update")
    fun updateBoard(
        @Part("board_id") board_id: RequestBody,
        @Part("board_title") board_title : RequestBody,
        @Part("board_content") board_content: RequestBody,
        @Part board_img : MultipartBody.Part?
    ) : Call<PostResponse>

    @PUT("/testapi/api/rest/board/update/clicked/{board_id}")
    fun updateClicked(
        @Path("board_id") board_id : Int
    ) : Call<PostResponse>

    // DELETE
    @DELETE("/testapi/api/rest/board/delete/{board_id}")
    fun deleteBoard(
        @Path("board_id") board_id: Int
    ) : Call<DeleteResponse>

    @DELETE("/testapi/api/rest/comment/delete/{comment_id}")
    fun deleteComment(
        @Path("comment_id") comment_id : Int
    ) : Call<DeleteResponse>
}