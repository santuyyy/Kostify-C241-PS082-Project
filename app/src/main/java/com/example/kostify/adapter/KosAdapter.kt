package com.example.kostify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kostify.R
import com.example.kostify.data.api.response.ItemsItem
import com.example.kostify.databinding.ItemListkosBinding

class KosAdapter(
    private val onItemClick: (ItemsItem) -> Unit
) : ListAdapter<ItemsItem, KosAdapter.ListViewHolder>(DIFF_CALLBACK) {

    /*class KosAdapter : ListAdapter<ItemsItem, KosAdapter.ListViewHolder>(DIFF_CALLBACK) {*/

    /* private lateinit var onItemClickCallback: OnItemClickCallback

     fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
         this.onItemClickCallback = onItemClickCallback
     }*/

    /*   private lateinit var userItemClickListener: ListViewHolder.UserItemClickListener

       fun setUserItemClickListener(listener: ListViewHolder.UserItemClickListener) {
           userItemClickListener = listener
       }
   */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemListkosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val kost = getItem(position)
        holder.bind(kost)
    }

    inner class ListViewHolder(private val binding: ItemListkosBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(kost: ItemsItem) {
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

    /*     interface UserItemClickListener {
             fun onItemClicked(kost: ItemsItem)
         }
 */

    /*    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }*/

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
