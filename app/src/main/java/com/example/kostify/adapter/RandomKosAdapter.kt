package com.example.kostify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kostify.R
import com.example.kostify.data.api.response.RandomkostitemItem
import com.example.kostify.databinding.ItemRandomlistkosBinding

// LIST ADAPTER
class RandomKosAdapter(
    private val onItemClick: (RandomkostitemItem) -> Unit
) : ListAdapter<RandomkostitemItem, RandomKosAdapter.RandomKosViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomKosViewHolder {
        val binding = ItemRandomlistkosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RandomKosViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RandomKosViewHolder, position: Int) {
        val kost = getItem(position)
        holder.bind(kost)
    }

    inner class RandomKosViewHolder(private val binding: ItemRandomlistkosBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(kost: RandomkostitemItem) {
            binding.apply {
                tvTipeDetail.text = kost.gender
                tvNamaItem.text = kost.namaKost
                tvAlamatItem.text = kost.alamat
                tvHargaItem.text = root.context.getString(R.string.harga_kosItem, kost.harga)
                Glide.with(itemView.context)
                    .load(R.drawable.img_contohgambarkos)
                    .into(ivGambarKos)
                root.setOnClickListener{
                    onItemClick(kost)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RandomkostitemItem>() {
            override fun areItemsTheSame(oldItem: RandomkostitemItem, newItem: RandomkostitemItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RandomkostitemItem, newItem: RandomkostitemItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}


