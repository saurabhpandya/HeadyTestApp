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
import com.fidato.headytestapp.databinding.FragmentSubcategoriesBinding
import com.fidato.headytestapp.db.model.CategoryRanking
import com.fidato.headytestapp.networking.RetrofitClient
import com.fidato.headytestapp.utils.OnItemClickListner
import com.fidato.headytestapp.utils.Status

class SubCategoryFragment : BaseFragment(), OnItemClickListner {

    private val TAG = CategoryFragment::class.java.canonicalName

    private lateinit var binding: FragmentSubcategoriesBinding
    private lateinit var viewModel: CategoryViewModel

    lateinit var attendanceAdapter: CategoryRankingAdapter
    lateinit var arylstSubCategoryRanking: ArrayList<CategoryRanking>

    var nextTitle = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubcategoriesBinding.inflate(inflater, container, false)
        setupViewModel()
        binding.vm = viewModel
        setupRecyclerView()
        getData()
        return binding.root
    }

    private fun setupRecyclerView() {
        // bind RecyclerView
        arylstSubCategoryRanking = ArrayList<CategoryRanking>()
        binding.rcyclrvwSubcategories.layoutManager = LinearLayoutManager(activity)
        binding.rcyclrvwSubcategories.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rcyclrvwSubcategories.setHasFixedSize(true)
        attendanceAdapter = CategoryRankingAdapter(arylstSubCategoryRanking, this)
        binding.rcyclrvwSubcategories.adapter = attendanceAdapter
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

    private fun getSubCategories(catId: Int?) {

        viewModel.getCategoryByCatId(catId!!).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> binding.prgrs.visibility = View.VISIBLE
                Status.ERROR -> binding.prgrs.visibility = View.GONE
                Status.SUCCESS -> {
                    val arylstCategoryRanking = it.data
                    binding.prgrs.visibility = View.GONE
                    if (!arylstCategoryRanking.isNullOrEmpty()) {
                        this.arylstSubCategoryRanking = arylstCategoryRanking
                        if (this.arylstSubCategoryRanking.size > 0) {
                            binding.noData.visibility = View.GONE
                            binding.rcyclrvwSubcategories.visibility = View.VISIBLE
                            attendanceAdapter.setCategoryRanking(this.arylstSubCategoryRanking)
                        } else {
                            binding.noData.visibility = View.VISIBLE
                            binding.rcyclrvwSubcategories.visibility = View.GONE
                        }
                    } else {
                        var bundle = bundleOf("cat_id" to catId, "cat_name" to nextTitle)
                        view?.findNavController()
                            ?.navigate(R.id.action_subCategoryFragment_to_productFragment, bundle)
                    }
                }
            }
        })
    }

    private fun getData() {
        binding.prgrs.visibility = View.VISIBLE
        val parentId = arguments?.getInt("cat_id", -1)
        val title = arguments?.getString("cat_name", "")
        updateTitle(title)
        getSubCategories(parentId)
    }

    override fun onItemClickListner(position: Int) {
        Log.d(TAG, "onItemClickListner::${arylstSubCategoryRanking[position]}")

        if (arylstSubCategoryRanking.size > 0) {
            nextTitle = arylstSubCategoryRanking[position].name
            updateTitle(nextTitle)
            getSubCategories(arylstSubCategoryRanking[position].id)
        }

    }

    override fun onProductVariantClickListner(position: Int, isForSize: Boolean) {

    }

}