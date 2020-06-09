package com.fidato.headytestapp.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fidato.headytestapp.HeadyTestApp
import com.fidato.headytestapp.R
import com.fidato.headytestapp.base.BaseFragment
import com.fidato.headytestapp.base.ViewModelFactory
import com.fidato.headytestapp.dashboard.dataprovider.CategoryNetworkDataProvider
import com.fidato.headytestapp.databinding.FragmentProductBinding
import com.fidato.headytestapp.db.model.RankingProductMapping
import com.fidato.headytestapp.networking.RetrofitClient
import com.fidato.headytestapp.product.adapter.ProductsAdapter
import com.fidato.headytestapp.product.viewmodel.ProductViewModel
import com.fidato.headytestapp.utils.OnItemClickListner
import com.fidato.headytestapp.utils.Status

class ProductFragment : BaseFragment(), OnItemClickListner {

    private val TAG = ProductFragment::class.java.canonicalName

    private lateinit var binding: FragmentProductBinding
    private lateinit var viewModel: ProductViewModel

    lateinit var productsAdapter: ProductsAdapter
    lateinit var arylstRankingProducts: List<RankingProductMapping>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        setupViewModel()
        binding.vm = viewModel
        setupRecyclerView()
        getData()
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.activity!!,
            ViewModelFactory(
                CategoryNetworkDataProvider(RetrofitClient.CATEGORY_SERVICE),
                HeadyTestApp.instance
            )
        ).get(ProductViewModel::class.java)
    }

    private fun setupRecyclerView() {
        // bind RecyclerView
        arylstRankingProducts = ArrayList<RankingProductMapping>()
//        binding.rcyclrvwProducts.layoutManager = LinearLayoutManager(activity)
        binding.rcyclrvwProducts.layoutManager = GridLayoutManager(activity, 2)
//        binding.rcyclrvwProducts.addItemDecoration(
//            DividerItemDecoration(
//                context,
//                LinearLayoutManager.VERTICAL
//            )
//        )
        binding.rcyclrvwProducts.setHasFixedSize(true)
        productsAdapter =
            ProductsAdapter(
                arylstRankingProducts,
                this
            )
        binding.rcyclrvwProducts.adapter = productsAdapter
    }

    private fun getData() {
        binding.prgrs.visibility = View.VISIBLE
        val catId = arguments?.getInt("cat_id", -1) ?: -1
        val rankingId = arguments?.getInt("rank_id", -1) ?: -1
        val title = arguments?.getString("cat_name", "")
        updateTitle(title)
        if (catId > 0) {
            getProductsByCatId(catId)
        } else if (rankingId > 0) {
            getProductsByRankingId(rankingId)
        }
    }

    private fun getProductsByRankingId(rankingId: Int) {

        viewModel.getProductsByRankingId(rankingId).observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "getProductsByRankingId Status : ${it} ")
            when (it.status) {
                Status.LOADING -> binding.prgrs.visibility = View.VISIBLE
                Status.ERROR -> {
                    binding.prgrs.visibility = View.GONE
                    Log.d(TAG, "Error : ${it.message}")
                }
                Status.SUCCESS -> {
                    Log.d(TAG, "Products By Ranking Id List: ${it} ")
                    val arylstProducts = it.data
                    binding.prgrs.visibility = View.GONE
                    if (!arylstProducts.isNullOrEmpty()) {
                        this.arylstRankingProducts = arylstProducts
                        if (this.arylstRankingProducts.size > 0) {
                            binding.noData.visibility = View.GONE
                            binding.rcyclrvwProducts.visibility = View.VISIBLE
                            productsAdapter.setProducts(this.arylstRankingProducts)
                        } else {
                            binding.noData.visibility = View.VISIBLE
                            binding.rcyclrvwProducts.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    private fun getProductsByCatId(catId: Int) {

        viewModel.getProductsByCatId(catId).observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "getProductsByCatId Status : ${it} ")
            when (it.status) {
                Status.LOADING -> binding.prgrs.visibility = View.VISIBLE
                Status.ERROR -> {
                    binding.prgrs.visibility = View.GONE
                    Log.d(TAG, "Error : ${it.message}")
                }
                Status.SUCCESS -> {
                    Log.d(TAG, "Products By Cat Id List: ${it} ")
                    val arylstRankingProducts = it.data
                    binding.prgrs.visibility = View.GONE
                    if (!arylstRankingProducts.isNullOrEmpty()) {
                        this.arylstRankingProducts = arylstRankingProducts
                        if (this.arylstRankingProducts.size > 0) {
                            binding.noData.visibility = View.GONE
                            binding.rcyclrvwProducts.visibility = View.VISIBLE
                            productsAdapter.setProducts(this.arylstRankingProducts)
                        } else {
                            binding.noData.visibility = View.VISIBLE
                            binding.rcyclrvwProducts.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    override fun onItemClickListner(position: Int) {
        if (!arylstRankingProducts.isNullOrEmpty()) {
            var bundle = bundleOf("product_details" to arylstRankingProducts[position])
            view?.findNavController()
                ?.navigate(R.id.action_productFragment_to_productDetailFragment, bundle)
        }
    }

    override fun onProductVariantClickListner(position: Int, isForSize: Boolean) {

    }
}