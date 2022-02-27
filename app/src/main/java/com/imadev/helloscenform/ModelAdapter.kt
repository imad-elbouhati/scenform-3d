package com.imadev.helloscenform

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imadev.helloscenform.databinding.NodeRowItemBinding

class ModelAdapter : RecyclerView.Adapter<ModelAdapter.ViewHolder>() {

    var modelList: List<Model> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NodeRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = modelList[position]
        with(holder.binding) {

        }
    }

    override fun getItemCount(): Int = modelList.size

    inner class ViewHolder(val binding: NodeRowItemBinding) : RecyclerView.ViewHolder(binding.root)


}