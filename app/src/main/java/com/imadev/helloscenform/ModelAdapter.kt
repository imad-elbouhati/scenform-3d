package com.imadev.helloscenform

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.imadev.helloscenform.databinding.NodeRowItemBinding

class ModelAdapter : RecyclerView.Adapter<ModelAdapter.ViewHolder>() {

    var modelList: List<Model> = listOf()

    private var onModelClick: ((model: Model, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NodeRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = modelList[position]
        val context = holder.binding.root.context

        with(holder.binding) {

            nodeImg.setImageResource(model.image)

            nodeImg.setOnClickListener {
                onModelClick?.invoke(model, position)
            }

            if (model.selected) {
                nodeImg.background = AppCompatResources.getDrawable(context, R.drawable.border_drawble)
            }else {
                nodeImg.background = null
            }
        }


    }



    fun setOnClickListener(onClick: ((model: Model, position: Int) -> Unit)? = null) {
        this.onModelClick = onClick
    }

    override fun getItemCount(): Int = modelList.size

    open inner class ViewHolder(val binding: NodeRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)


}


