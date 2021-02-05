package com.example.recyclerview_notify_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var mAdapter: MyRecyclerviewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = mutableListOf<Item>()
        for(i in 0 ..  9){
            list.add(Item(i.toString(),"This is item $i"))
        }
        rv_test.apply {
            adapter = MyRecyclerviewAdapter(list).apply {
                mAdapter = this
            }
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
            itemAnimator?.apply {
//                addDuration = 1000
//                removeDuration = 1000
//                moveDuration = 100
//                changeDuration = 1000
            }
        }



        lifecycleScope.launchWhenResumed {
            delay(3000)
            list.add(Item("13","This is item 13"))
            list.add(2,Item("56","This is item id56"))
            list.add(9,Item("32","This is item id32"))
            list.removeAt(0)
            list.removeAt(6)
            list.removeAt(4)
            list[0].name = "name change ${list[0].id}"
            list[1].name = "name change ${list[1].id}"
            list[8].name = "name change ${list[8].id}"
            list.shuffle()
            mAdapter.updateListWithAnim(list)
        }

    }
}