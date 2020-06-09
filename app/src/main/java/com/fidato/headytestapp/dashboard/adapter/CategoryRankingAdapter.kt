package com.fidato.headytestapp.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fidato.headytestapp.R
import com.fidato.headytestapp.databinding.RawCategoriesBinding
import com.fidato.headytestapp.db.model.CategoryRanking
import com.fidato.headytestapp.utils.OnItemClickListner
import com.fidato.headytestapp.utils.Utility

class CategoryRankingAdapter(
    var arylstCatRanking: ArrayList<CategoryRanking>,
    var onItemClickListner: OnItemClickListner
) :
    RecyclerView.Adapter<CategoryRankingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawCategoriesBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.raw_categories,
                parent,
                false
            )
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return arylstCatRanking.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arylstCatRanking.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClickListner(position)
        }
    }

    fun setCategoryRanking(arylstCatRanking: ArrayList<CategoryRanking>) {
        this.arylstCatRanking = arylstCatRanking
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RawCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItems(categoryRanking: CategoryRanking) {

            binding.imgvwIcon.setImageResource(
                Utility.getProductIcon(
                    categoryRanking.id,
                    categoryRanking.name
                )
            )

            binding.catRankingModel = categoryRanking
            binding.executePendingBindings()


        }

    }
}