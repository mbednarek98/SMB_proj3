package smb.s18579.mb.shoplist

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import smb.s18579.mb.shoplist.adapters.ProductAdapter
import smb.s18579.mb.shoplist.callbacks.DeleteCallback
import smb.s18579.mb.shoplist.database.Helper
import smb.s18579.mb.shoplist.database.product.ProductDTO
import smb.s18579.mb.shoplist.databinding.ActivityProductListBinding

class ProductListActivity : AppCompatActivity() {
    private val binding by lazy { ActivityProductListBinding.inflate(layoutInflater) }
    var listOfProducts : List<ProductDTO> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpAdapter()

        binding.addproduct.setOnClickListener{
            onClickProductDialog(title = "New Product")
        }
        binding.arrowview.setOnClickListener {
            finish()
        }

    }

    fun setUpAdapter(){
        listOfProducts = Helper.db?.product?.selectAll() ?: ArrayList()
        val adapterVH = ProductAdapter(this@ProductListActivity)

        binding.productview.apply {
            adapter = adapterVH
            layoutManager = LinearLayoutManager(context)
        }
        Log.d("xD",listOfProducts.toString())
        val swipeHandler = object : DeleteCallback(context = this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapterVH.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.productview)

    }




    fun onClickProductDialog(productDTO: ProductDTO? = null, title: String? = null, description: String? = null){
        val dialog = AlertDialog.Builder(this)

        val dialogView = View.inflate(this, R.layout.product_dialog, null)
        var name : EditText; var quantity : EditText; var price : EditText
        with(dialogView){
            name = findViewById(R.id.product_name_value_dialog)
            quantity = findViewById(R.id.product_quantity_value_dialog)
            price = findViewById(R.id.product_price_value_dialog)
        }

        with(dialog){
            setTitle(title)
            setMessage(description)
            when(productDTO){
                null -> {
                    setPositiveButton(android.R.string.ok) { _, _ ->
                        Helper.db?.product?.insert(ProductDTO(name = name.text.toString(), quantity = if(quantity.text.toString()
                                .isNotEmpty() && quantity.text.toString().isDigitsOnly()) quantity.text.toString().toInt() else 0 , price = if(price.text.toString()
                                .isNotEmpty())  price.text.toString().toDouble() else 0.0))
                        setUpAdapter()
                    }

                }
                else -> {
                    name.setText(productDTO.name)
                    quantity.setText(productDTO.quantity.toString())
                    price.setText(productDTO.price.toString())
                    setPositiveButton(android.R.string.ok) { _, _ ->
                        productDTO.name = name.text.toString()
                        productDTO.quantity = quantity.text.toString().toInt()
                        productDTO.price = price.text.toString().toDouble()
                        Helper.db?.product?.update(productDTO)
                        setUpAdapter()
                    }
                }
            }
            setView(dialogView)
            setNegativeButton(android.R.string.cancel) { _, _ ->  }
            create().show()
        }

    }


}