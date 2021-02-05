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
        var randomNumber = 20
        for(i in 0 ..  Random.nextInt(randomNumber)){
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
            val id = Random.nextInt(randomNumber+1,randomNumber+101)
            for(i in 0 .. Random.nextInt(randomNumber)){
                val insertIndex = Random.nextInt(list.size-1)
                list.add(insertIndex,Item((id+i).toString(),"This is item ${id+i}"))
            }

            for(i in 0 .. Random.nextInt(randomNumber/2)){
                val removed = Random.nextInt(list.size-1)
                list.removeAt(removed)
            }

            for(i in 0 .. Random.nextInt(randomNumber/2)){
                val changed = Random.nextInt(list.size-1)
                list[changed].name = "name change ${list[changed].id}"
            }

            list.shuffle()
            mAdapter.updateListWithAnim(list)
        }

    }
}