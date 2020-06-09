package com.fidato.headytestapp.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fidato.headytestapp.HeadyTestApp
import com.fidato.headytestapp.R
import com.fidato.headytestapp.base.BaseFragment
import com.fidato.headytestapp.base.ViewModelFactory
import com.fidato.headytestapp.dashboard.adapter.CategoryRankingAdapter
import com.fidato.headytestapp.dashboard.dataprovider.CategoryNetworkDataProvider
import com.fidato.headytestapp.dashboard.viewmodel.CategoryViewModel
import com.fidato.headytestapp.databinding.FragmentCategoryBinding
import com.fidato.headytestapp.db.model.CategoryRanking
import com.fidato.headytestapp.networking.RetrofitClient
import com.fidato.headytestapp.utils.OnItemClickListner
import com.fidato.headytestapp.utils.Status

class CategoryFragment : BaseFragment(), OnItemClickListner {
    private val TAG = CategoryFragment::class.java.canonicalName

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModel: CategoryViewModel

    lateinit var attendanceAdapter: CategoryRankingAdapter
    lateinit var arylstCategoryRanking: ArrayList<CategoryRanking>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        setupViewModel()
        binding.vm = viewModel
        setupRecyclerView()
        getData()
        return binding.root
    }

    private fun setupRecyclerView() {
        // bind RecyclerView
        arylstCategoryRanking = ArrayList<CategoryRanking>()
        binding.rcyclrvwCategories.layoutManager = LinearLayoutManager(activity)
        binding.rcyclrvwCategories.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rcyclrvwCategories.setHasFixedSize(true)
        attendanceAdapter = CategoryRankingAdapter(arylstCategoryRanking, this)
        binding.rcyclrvwCategories.adapter = attendanceAdapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this.activity!!,
            ViewModelFactory(
                CategoryNetworkDataProvider(RetrofitClient.CATEGORY_SERVICE),
                HeadyTestApp.instance
            )
        ).get(CategoryViewModel::class.java)
    }

    private fun setupObserver() {
        viewModel.getCategoryByParentId(0).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> binding.prgrs.visibility = View.VISIBLE
                Status.ERROR -> binding.prgrs.visibility = View.GONE
                Status.SUCCESS -> {
                    val arylstCategoryRanking = it.data
                    binding.prgrs.visibility = View.GONE
                    if (!arylstCategoryRanking.isNullOrEmpty()) {
                        this.arylstCategoryRanking = arylstCategoryRanking
                        if (this.arylstCategoryRanking.size > 0) {
                            binding.noData.visibility = View.GONE
                            binding.rcyclrvwCategories.visibility = View.VISIBLE
                            attendanceAdapter.setCategoryRanking(this.arylstCategoryRanking)
                        } else {
                            binding.noData.visibility = View.VISIBLE
                            binding.rcyclrvwCategories.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    private fun getData() {

        viewModel.getJson().observe(viewLifecycleOwner, Observer {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.prgrs.visibility = View.GONE
                        setupObserver()
                    }
                    Status.LOADING -> {
                        binding.prgrs.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        binding.prgrs.visibility = View.GONE
                        Log.d(TAG, "Setup Observer : ERROR : ${resource.message}")
                    }
                }
            }
        })
    }

    override fun onItemClickListner(position: Int) {
        Log.d(TAG, "onItemClickListner::${arylstCategoryRanking[position]}")
        val categoryRanking = arylstCategoryRanking[position]
        if (categoryRanking.isCategory) {
            var bundle = bundleOf(
                "cat_id" to arylstCategoryRanking[position].id,
                "cat_name" to arylstCategoryRanking[position].name
            )
            view?.findNavController()
                ?.navigate(R.id.action_categoryFragment_to_subCategoryFragment, bundle)
        } else {
            var bundle = bundleOf(
                "rank_id" to arylstCategoryRanking[position].id,
                "cat_name" to arylstCategoryRanking[position].name
            )
            view?.findNavController()
                ?.navigate(R.id.action_categoryFragment_to_productFragment, bundle)
        }

    }

    override fun onProductVariantClickListner(position: Int, isForSize: Boolean) {

    }
}