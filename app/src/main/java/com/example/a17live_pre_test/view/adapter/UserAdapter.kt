package com.example.a17live_pre_test.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a17live_pre_test.R
import com.example.a17live_pre_test.model.data.User




class UserAdapter(var context: Context) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    lateinit var dataSet: MutableList<User>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false))
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setDataset(data: MutableList<User>) {
        this.dataSet = data
        notifyDataSetChanged()
    }

    fun addDataset(data: MutableList<User>) {
        val lastIndex: Int = dataSet.size
        dataSet.addAll(data)
        notifyItemRangeInserted(lastIndex, data.size)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.resizeAvatar()
        holder.bind(this.dataSet.get(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var name = itemView?.findViewById<TextView>(R.id.repoItemTitle)
        private var avatar = itemView?.findViewById<ImageView>(R.id.repoItemAvatar)

        fun resizeAvatar() {
            var avatarLayoutParams = avatar.layoutParams
            avatarLayoutParams.width = context.resources.displayMetrics.widthPixels / 6
            avatarLayoutParams.height = context.resources.displayMetrics.widthPixels / 6

        }

        fun bind(data: User) {
            name?.text = data.getLogin()
            Glide.with(context).load(data.getAvatarUrl()).into(avatar)
        }

    }
}