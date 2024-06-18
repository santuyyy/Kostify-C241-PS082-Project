package com.example.kostify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kostify.R
import com.example.kostify.data.api.response.BookmarkResponseItem
import com.example.kostify.databinding.ItemListkosBinding

class BookmarkAdapter (
    private val onItemClick: (BookmarkResponseItem) -> Unit
) : ListAdapter<BookmarkResponseItem, BookmarkAdapter.BookmarkViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = ItemListkosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val bookmark = getItem(position)
        holder.bind(bookmark)
    }

    inner class BookmarkViewHolder(val binding: ItemListkosBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookmark: BookmarkResponseItem) {
            binding.apply {
                tvNamaItem.text = bookmark.kostDetails.namaKost
                tvAlamatItem.text = bookmark.kostDetails.alamat
                tvTipeDetail.text = bookmark.kostDetails.gender
                tvHargaItem.text = root.context.getString(R.string.harga_kosItem, bookmark.kostDetails.harga)
                Glide.with(itemView.context)
                    .load(R.drawable.img_contohgambarkos)
                    .into(ivGambarKos)
                root.setOnClickListener{
                    onItemClick(bookmark)
                }
            }
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<BookmarkResponseItem>() {
        override fun areItemsTheSame(oldItem: BookmarkResponseItem, newItem: BookmarkResponseItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BookmarkResponseItem, newItem: BookmarkResponseItem): Boolean {
            return oldItem == newItem
        }
    }
}