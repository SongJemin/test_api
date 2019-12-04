package com.jem.testboardapp.ui.upload

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import com.jem.testboardapp.R
import com.jem.testboardapp.ui.main.list.ListActivity
import com.jem.testboardapp.util.network.ApiClient
import com.jem.testboardapp.util.network.NetworkService
import com.jem.testboardapp.util.network.get.GetBoardResponse
import com.jem.testboardapp.util.network.post.PostResponse
import kotlinx.android.synthetic.main.activity_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.lang.Exception
import retrofit2.*;

class UploadActivity : AppCompatActivity() {

    private val CODE_SELECT_IMAGE =100
    lateinit var data : Uri
    private var board_img : MultipartBody.Part? = null
    var networkService : NetworkService = ApiClient.getRetrofit().create(NetworkService::class.java)
    var boardId : Int = 0
    var insertFlag : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        insertFlag = intent.getIntExtra("insertFlag", 0)

        // 수정 시
        if(insertFlag == 0){
            boardId = intent.getIntExtra("board_id", 0)
            btn_confirm_upload.text = "수정"
            getData()
        }

        // 등록 시
        else{
            btn_confirm_upload.text = "등록"
        }
        img_picture_upload.setOnClickListener {
            uploadImage()
        }

        // close activity
        btn_confirm_upload.setOnClickListener {
            postBoard()
        }

        btn_back_upload.setOnClickListener {
            var intent = Intent(applicationContext, ListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    fun getData(){
        var getBoardResponse = networkService.getOneBoard(boardId)
        getBoardResponse.enqueue(object : Callback<GetBoardResponse>{
            override fun onFailure(call: Call<GetBoardResponse>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<GetBoardResponse>, response: Response<GetBoardResponse>) {
                if(response.isSuccessful){
                    if(response.body()!!.data != null){
                        et_title_upload.setText(response.body()!!.data.board_title)
                        et_content_upload.setText(response.body()!!.data.board_content)
                        Glide.with(applicationContext).load(response.body()!!.data.board_img).into(img_picture_upload)
                    }
                }
            }
        })
    }

    fun postBoard(){

        var board_id : RequestBody = RequestBody.create(MediaType.parse("text.plain"), boardId.toString())
        var board_title : RequestBody = RequestBody.create(MediaType.parse("text.plain"), et_title_upload.text.toString())
        var board_content : RequestBody = RequestBody.create(MediaType.parse("text.plain"), et_content_upload.text.toString())

        // 삽입
        if(insertFlag == 1){
            var postResponse = networkService.postBoard(board_title, board_content, board_img)
            postResponse.enqueue(object : Callback<PostResponse> {
                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Log.v("UploadActivity", "실패 = " + t.toString())
                }

                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                    if(response.isSuccessful){
                        Log.v("UploadActivity", "성공")
                        var intent = Intent(applicationContext, ListActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                }
            })
        }
        // 수정
        else{
            var postResponse = networkService.updateBoard(board_id, board_title, board_content, board_img)
            postResponse.enqueue(object : Callback<PostResponse> {
                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Log.v("UploadActivity", "실패 = " + t.toString())
                }

                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                    if(response.isSuccessful){
                        Log.v("UploadActivity", "성공")
                        var intent = Intent(applicationContext, ListActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                }
            })
        }
    }

    fun uploadImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, CODE_SELECT_IMAGE)
    }

    // 갤러리로부터 이미지 갖고올 때 사용하는 오버라이딩 메소드
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    this.data = data!!.data
                    val options = BitmapFactory.Options()

                    var input: InputStream? = null // here, you need to get your context.
                    try {
                        input = contentResolver.openInputStream(this.data)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                    val bitmap = BitmapFactory.decodeStream(input, null, options) // InputStream 으로부터 Bitmap 을 만들어 준다.
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
                    val photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray())
                    val img = File(getRealPathFromURI(applicationContext,this.data).toString()) // 가져온 파일의 이름을 알아내려고 사용합니다

                    board_img = MultipartBody.Part.createFormData("board_img", img.name, photoBody)

                    // 선택한 이미지를 해당 이미지뷰에 적용
                    Glide.with(this)
                        .load(data.data)
                        .centerCrop()
                        .into(img_picture_upload)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    fun getRealPathFromURI(context : Context, contentUri : Uri) : String{
        var cursor : Cursor? = null
        try{
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        }  finally {
            cursor?.close()
        }
    }
}
