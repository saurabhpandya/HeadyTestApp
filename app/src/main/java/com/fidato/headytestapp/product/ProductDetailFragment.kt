package com.fidato.headytestapp.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fidato.headytestapp.HeadyTestApp
import com.fidato.headytestapp.base.BaseFragment
import com.fidato.headytestapp.base.ViewModelFactory
import com.fidato.headytestapp.dashboard.dataprovider.CategoryNetworkDataProvider
import com.fidato.headytestapp.databinding.FragmentProductDetailBinding
import com.fidato.headytestapp.db.model.CatProductMapping
import com.fidato.headytestapp.db.model.ProductVarientMapping
import com.fidato.headytestapp.db.model.RankingProductMapping
import com.fidato.headytestapp.networking.RetrofitClient
import com.fidato.headytestapp.product.adapter.ProductVariantAdapter
import com.fidato.headytestapp.product.viewmodel.ProductViewModel
import com.fidato.headytestapp.utils.OnItemClickListner
import com.fidato.headytestapp.utils.Status

class ProductDetailFragment : BaseFragment(), OnItemClickListner {
    private val TAG = ProductDetailFragment::class.java.canonicalName

    private lateinit var binding: FragmentProductDetailBinding
    private lateinit var viewModel: ProductViewModel

    lateinit var productVariantSizeAdapter: ProductVariantAdapter<Double>
    lateinit var productVariantColorAdapter: ProductVariantAdapter<ProductVarientMapping>
    lateinit var arylstProductVariantSize: List<Double>
    lateinit var arylstProductVariantColor: List<ProductVarientMapping>

    var productVariantMap: Map<Double, List<ProductVarientMapping>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
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
        // bind RecyclerView for Size
        arylstProductVariantSize = ArrayList<Double>()

        binding.rcyclrvwProductSize.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        productVariantSizeAdapter =
            ProductVariantAdapter(
                arylstProductVariantSize,
                true,
                this
            )
        binding.rcyclrvwProductSize.adapter = productVariantSizeAdapter

        // bind RecyclerView for Color
        arylstProductVariantColor = ArrayList<ProductVarientMapping>()
        binding.rcyclrvwProductColor.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        productVariantColorAdapter =
            ProductVariantAdapter(
                arylstProductVariantColor,
                false,
                this
            )
        binding.rcyclrvwProductColor.adapter = productVariantColorAdapter

    }

    private fun getData() {

        val catProductMapping = arguments?.get("product_details") as RankingProductMapping

        viewModel.getProductVariantsByProductId(catProductMapping.products?.id!!)
            .observe(viewLifecycleOwner, Observer {
                Log.d(TAG, "getProductVariantsByProductId Status : ${it} ")
                when (it.status) {
                    Status.LOADING -> binding.prgrs.visibility = View.VISIBLE
                    Status.ERROR -> {
                        Log.d(TAG, "Error : ${it.message}")
                        binding.prgrs.visibility = View.GONE
                        setData(catProductMapping.products, it.data)
                    }
                    Status.SUCCESS -> {
                        binding.prgrs.visibility = View.GONE
                        setData(catProductMapping.products, it.data)
                    }
                }
            })
    }

    private fun setData(
        catProductMapping: CatProductMapping?,
        productVariantMap: Map<Double, List<ProductVarientMapping>>?
    ) {
        if (catProductMapping != null) {
            binding.txtvwName.text = catProductMapping.name
        }

        this.productVariantMap = productVariantMap
        setVarientData(productVariantMap, 0)

    }

    private fun setPrice(price: Double) {
        binding.txtvwPrice.text = String.format("Price : %s", price)
    }

    private fun setVarientData(
        productVariantMap: Map<Double, List<ProductVarientMapping>>?,
        position: Int
    ) {
        if (!productVariantMap.isNullOrEmpty()) {
            arylstProductVariantSize = productVariantMap.keys.sorted()
            // it will get color variant from size
            arylstProductVariantColor =
                productVariantMap.get(arylstProductVariantSize.get(position))!!

            if (!arylstProductVariantColor.isNullOrEmpty()) {
                setPrice(arylstProductVariantColor.get(0).price)
            }

            if (!arylstProductVariantSize.isNullOrEmpty()) {
                if (arylstProductVariantSize.size == 1 && arylstProductVariantSize[0] == 0.0) {
                    binding.txtvwVariantSize.visibility = View.GONE
                    binding.rcyclrvwProductSize.visibility = View.GONE
                } else {
                    binding.txtvwVariantSize.visibility = View.VISIBLE
                    binding.rcyclrvwProductSize.visibility = View.VISIBLE
                    productVariantSizeAdapter.setProductVariants(arylstProductVariantSize)
                }
            } else {
                binding.txtvwVariantSize.visibility = View.GONE
                binding.rcyclrvwProductSize.visibility = View.GONE
            }

            if (!arylstProductVariantColor.isNullOrEmpty()) {
                binding.txtvwVariantColor.visibility = View.VISIBLE
                binding.rcyclrvwProductColor.visibility = View.VISIBLE
                productVariantColorAdapter.setProductVariants(arylstProductVariantColor)
            } else {
                binding.txtvwVariantColor.visibility = View.GONE
                binding.rcyclrvwProductColor.visibility = View.GONE
            }
        }
    }

    override fun onItemClickListner(position: Int) {

    }

    override fun onProductVariantClickListner(position: Int, isForSize: Boolean) {
        if (isForSize) {
            setVarientData(this.productVariantMap, position)
        } else {

            if (!arylstProductVariantColor.isNullOrEmpty()) {
                Toast.makeText(
                    activity,
                    "You selected ${arylstProductVariantColor.get(position).color}",
                    Toast.LENGTH_SHORT
                ).show()
                setPrice(arylstProductVariantColor.get(position).price)
            }


        }
    }
}