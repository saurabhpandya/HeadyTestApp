package com.fidato.headytestapp.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fidato.headytestapp.R
import com.fidato.headytestapp.databinding.RawProductsBinding
import com.fidato.headytestapp.db.model.CatProductMapping
import com.fidato.headytestapp.db.model.RankingProductMapping
import com.fidato.headytestapp.utils.OnItemClickListner

class ProductsAdapter(
    var arylstRankingProducts: List<RankingProductMapping>,
    var onItemClickListner: OnItemClickListner
) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawProductsBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.raw_products,
                parent,
                false
            )
        return ViewHolder(
            binding
        )

    }

    override fun getItemCount(): Int {
        return arylstRankingProducts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val catProductMapping = arylstRankingProducts.get(position).products
        if (catProductMapping != null)
            holder.bindItems(catProductMapping)
        else {
            holder.bindItems(CatProductMapping())
        }
        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClickListner(position)
        }
    }

    fun setProducts(arylstRankingProducts: List<RankingProductMapping>) {
        this.arylstRankingProducts = arylstRankingProducts
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RawProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItems(catProductMapping: CatProductMapping) {
            when (catProductMapping.cat_id) {
                2, 4, 8 -> binding.imgvwIcon.setImageResource(R.drawable.ic_bottom_wear)
                1, 5, 9 -> binding.imgvwIcon.setImageResource(R.drawable.ic_foot_wear)
                6, 7, 10 -> binding.imgvwIcon.setImageResource(R.drawable.ic_upper_wear)
                3 -> binding.imgvwIcon.setImageResource(R.drawable.ic_men_wear)
                11 -> binding.imgvwIcon.setImageResource(R.drawable.ic_electornics)
                12, 14, 15 -> binding.imgvwIcon.setImageResource(R.drawable.ic_mobile)
                13, 16, 17 -> binding.imgvwIcon.setImageResource(R.drawable.ic_laptop)
            }
            binding.catProductMapping = catProductMapping
            binding.executePendingBindings()
        }

    }
}