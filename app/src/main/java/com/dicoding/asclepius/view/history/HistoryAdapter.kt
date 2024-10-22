package com.dicoding.asclepius.view.history

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.CancerEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding

class HistoryAdapter: ListAdapter<CancerEntity, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryViewHolder(
        private val binding: ItemHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cancer: CancerEntity) {
            binding.tvResult.text = cancer.result

            val imageUri = Uri.parse(cancer.image)
            Glide.with(binding.ivImage.context)
                .load(imageUri)
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.ivImage)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<CancerEntity> =
            object : DiffUtil.ItemCallback<CancerEntity>() {
                override fun areItemsTheSame(
                    oldItem: CancerEntity,
                    newItem: CancerEntity
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: CancerEntity,
                    newItem: CancerEntity
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }


}