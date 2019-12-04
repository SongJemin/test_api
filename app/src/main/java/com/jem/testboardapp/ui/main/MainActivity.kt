package com.jem.testboardapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jem.testboardapp.R
import com.jem.testboardapp.ui.main.list.ListFragment
import com.jem.testboardapp.ui.main.rank.RankFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var nowFrag : Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callHomeFragment()
        addBottom()

    }

    fun addBottom(){
        btn_list_main.setOnClickListener {
            callFragment("list")
        }

        btn_rank_main.setOnClickListener {
            callFragment("rank")
        }
    }

    fun callHomeFragment(){
        callFragment("list")
    }

    fun callFragment(frag: String){
        when(frag){
            "list" -> {
                nowFrag = ListFragment()
            }
            "rank" -> {
                nowFrag = RankFragment()
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_content_main, nowFrag)
        transaction.commit()
    }
}