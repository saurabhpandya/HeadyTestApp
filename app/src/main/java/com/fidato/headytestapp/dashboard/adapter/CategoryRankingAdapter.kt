package com.fidato.headytestapp.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fidato.headytestapp.R
import com.fidato.headytestapp.databinding.RawCategoriesBinding
import com.fidato.headytestapp.db.model.CategoryRanking
import com.fidato.headytestapp.utils.OnItemClickListner

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

            when (categoryRanking.id) {
                2, 4, 8 -> binding.imgvwIcon.setImageResource(R.drawable.ic_bottom_wear)
                1, 5, 9 -> binding.imgvwIcon.setImageResource(R.drawable.ic_foot_wear)
                6, 7, 10 -> binding.imgvwIcon.setImageResource(R.drawable.ic_upper_wear)
                3 -> binding.imgvwIcon.setImageResource(R.drawable.ic_men_wear)
                11 -> binding.imgvwIcon.setImageResource(R.drawable.ic_electornics)
                12, 14, 15 -> binding.imgvwIcon.setImageResource(R.drawable.ic_mobile)
                13, 16, 17 -> binding.imgvwIcon.setImageResource(R.drawable.ic_laptop)
            }
            when (categoryRanking.name) {
                "Most Viewed Products" -> binding.imgvwIcon.setImageResource(R.drawable.ic_most_viewed)
                "Most OrdeRed Products" -> binding.imgvwIcon.setImageResource(R.drawable.ic_most_ordered)
                "Most ShaRed Products" -> binding.imgvwIcon.setImageResource(R.drawable.ic_most_shared)
            }

            binding.catRankingModel = categoryRanking
            binding.executePendingBindings()


        }

    }
}