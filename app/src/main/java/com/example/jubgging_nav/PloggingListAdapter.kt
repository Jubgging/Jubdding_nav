package com.example.jubgging_nav

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.jubgging_nav.databinding.ItemPloggingBinding

class PloggingListAdapter(private val context: LifecycleOwner) : RecyclerView.Adapter<PloggingListAdapter.ViewHolder>() {
    private var userList = mutableListOf<User>()

    fun setListData(data: MutableList<User>) {
        userList = data
    }

    class ViewHolder(val binding: ItemPloggingBinding) : RecyclerView.ViewHolder(binding.root) {
        val time = binding.txtRecTime
        val score = binding.txtRecScore
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPloggingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 실제 출력할 데이터
        val user: User = userList[position]
        holder.score.text = "${user.score}개나 플로깅하셨네요!"
        holder.time.text = user.time
    }

    override fun getItemCount(): Int = userList.size // 목록 개수 리턴
}