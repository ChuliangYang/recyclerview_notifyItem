package com.example.recyclerview_notify_test

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*

/**
 * Created on 02/03/2021 Wed
 *
 * @author Chuliang
 */

data class Item(val id: String, var name: String){
    override fun toString(): String {
        return """
            $name
            
        """.trimIndent()

    }

    override fun equals(other: Any?): Boolean {
        return if (other is Item){
            id == other.id && name == other.name
        }else{
            false
        }
    }
}

class MyRecyclerviewAdapter(private var itemList: MutableList<Item>) :
    RecyclerView.Adapter<MyViewHolder>() {

    private var internalList = itemList.toMutableList().map {
        it.copy()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mItemView.textView.text = internalList[position].name
    }

    fun updateListWithAnim(newList: List<Item>) {
        val newMap = hashMapOf<String, Int>()
        val orginalMap = hashMapOf<String, Int>()
        val currentState = internalList.toMutableList().map {
            it.copy()
        }.toMutableList()
        var removedCount = 0
        Log.e("result-o",internalList.toString())

        Log.e("result-final list",newList.toString())
        for (index in newList.indices) {
            newMap.put(newList[index].id, index)
        }
        for (index in internalList.indices) {
            orginalMap.put(internalList[index].id, index)
            if (!newMap.containsKey(internalList[index].id)) {
                notifyItemRemoved(index - removedCount)
                currentState.removeAt(index - removedCount)
                removedCount++
            }
        }
        Log.e("result-currentState-removed",currentState.toString())


        for (index in newList.indices) {
            if (!orginalMap.containsKey(newList[index].id)) {
                notifyItemInserted(index)
                currentState.add(index, newList[index])
            }
        }


        var leftIndex = 0
        Log.e("result-currentStateInserted",currentState.toString())
        while (leftIndex < currentState.size) {
            val currentNode = currentState[leftIndex]
            val destinationIndex = newMap[currentNode.id]!!
            if(destinationIndex == null) {
                leftIndex++
                continue
            }
            when {
                leftIndex == destinationIndex -> {
                    leftIndex++
                }
                destinationIndex < leftIndex -> {
                    notifyItemMoved(leftIndex, destinationIndex)
                    currentState.removeAt(leftIndex)
                    currentState.add(destinationIndex,currentNode)
                }
                else -> {
                    notifyItemMoved(leftIndex, destinationIndex)
                    currentState.removeAt(leftIndex)
                    currentState.add(destinationIndex,currentNode)
                }
            }
        }

        Log.e("result1",currentState.toString())


        for (index in newList.indices) {
            if (orginalMap.containsKey(newList[index].id)) {
                orginalMap[newList[index].id]?.let {
                    if (internalList[it].name != newList[index].name) {
                        currentState.set(index, newList[index])
                        notifyItemChanged(index)
                    }
                }
            }
        }

        itemList = newList.toMutableList()
        internalList = itemList.map {
            it.copy()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}

class MyViewHolder(val mItemView: View) : RecyclerView.ViewHolder(mItemView)