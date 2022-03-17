package com.android.interviewapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.interviewapp.R
import com.android.interviewapp.databinding.LayUseritemBinding
import com.android.interviewapp.model.UserItem
import com.bumptech.glide.Glide

class UsersAdapter(
    private val listOfUsers: MutableList<UserItem> = mutableListOf()
) : RecyclerView.Adapter<UsersAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(LayUseritemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.setData(listOfUsers[position])
    }

    override fun getItemCount(): Int = listOfUsers.size

    fun addAll(listitems: MutableList<UserItem>) {
    for(user in listitems){
            add(user)
        }
    }

    fun add(userItem: UserItem) {
        listOfUsers.add(userItem)
        notifyItemInserted(listOfUsers.size - 1)
    }

    class CardViewHolder(itemView: LayUseritemBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val imgUser: ImageView = itemView.imgUser
        private val txtUsername: TextView = itemView.txtUsername
        private val txtUseremail: TextView = itemView.txtUseremail

        fun setData(userItem: UserItem) {
            txtUsername.text = userItem.firstName+" "+ userItem.lastName
            txtUseremail.text = userItem.email

            Glide.with(itemView)
                .load(userItem.avatar)
                .placeholder(R.drawable.load_image)
                .error(R.drawable.image_error)
                .fallback(R.drawable.no_image_24)
                .into(imgUser)
        }
    }
}




