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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import smb.s18579.mb.shoplist.adapters.ProductAdapter
import smb.s18579.mb.shoplist.callbacks.DeleteCallback
import smb.s18579.mb.shoplist.database.Product
import smb.s18579.mb.shoplist.databinding.ActivityProductListBinding
import java.lang.Thread.sleep

class ProductListActivity : AppCompatActivity() {
    private val binding by lazy { ActivityProductListBinding.inflate(layoutInflater) }
    var listOfProducts : MutableList<Product> = ArrayList()
    lateinit var db : FirebaseFirestore
    private lateinit var uid : String
    private var showAll : Boolean = false
    override fun onStart() {
        super.onStart()
        setUpAdapter()

        binding.addproduct.setOnClickListener{
            onClickProductDialog(title = "New Product")
        }
        binding.arrowview.setOnClickListener {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        db = Firebase.firestore
        uid = intent.extras!!.getString("uid").toString()
        showAll = intent.extras!!.getBoolean("showall")

    }

     fun setUpAdapterNew(){
        listOfProducts  = ArrayList()
        when (showAll) {
            true -> {
                db.collection("products")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val product = document.toObject<Product>()
                            product.id = document.id
                            listOfProducts.add(product)
                        }
                    }.addOnCompleteListener { Log.d("true",listOfProducts.toString())
                        val adapterVH = ProductAdapter(this@ProductListActivity)

                        binding.productview.apply {
                            adapter = adapterVH
                            layoutManager = LinearLayoutManager(context)
                        }
                        val swipeHandler = object : DeleteCallback(context = this) {
                            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                                adapterVH.removeAt(viewHolder.adapterPosition)
                            }
                        }
                        val itemTouchHelper = ItemTouchHelper(swipeHandler)
                        itemTouchHelper.attachToRecyclerView(binding.productview) }
            }
            false -> {
                db.collection("products")
                    .whereEqualTo("uid", uid)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val product = document.toObject<Product>()
                            product.id = document.id
                            listOfProducts.add(product)
                        }
                    }.addOnCompleteListener {
                        Log.d("false",listOfProducts.toString())
                        val adapterVH = ProductAdapter(this@ProductListActivity)

                        binding.productview.apply {
                            adapter = adapterVH
                            layoutManager = LinearLayoutManager(context)
                        }
                        val swipeHandler = object : DeleteCallback(context = this) {
                            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                                adapterVH.removeAt(viewHolder.adapterPosition)
                            }
                        }
                        val itemTouchHelper = ItemTouchHelper(swipeHandler)
                        itemTouchHelper.attachToRecyclerView(binding.productview)
                    }
            }
        }
    }

    fun setUpAdapter(){
        setUpAdapterNew()
        Log.d("outside",listOfProducts.toString())
        val adapterVH = ProductAdapter(this@ProductListActivity)

        binding.productview.apply {
            adapter = adapterVH
            layoutManager = LinearLayoutManager(context)
        }
        val swipeHandler = object : DeleteCallback(context = this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapterVH.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.productview)
    }




    fun onClickProductDialog(product: Product? = null, title: String? = null, description: String? = null){
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
            when(product){
                null -> {
                    setPositiveButton(android.R.string.ok) { _, _ ->
                        val pro = hashMapOf(
                            "uid" to uid,
                            "name" to name.text.toString(),
                            "quantity" to if(quantity.text.toString()
                                    .isNotEmpty() && quantity.text.toString().isDigitsOnly()) quantity.text.toString().toInt() else 0,
                            "price" to if(price.text.toString()
                                    .isNotEmpty())  price.text.toString().toDouble() else 0.0,
                            "bought" to false
                        )

                        db.collection("products")
                            .add(pro)
                            .addOnSuccessListener { documentReference ->
                                Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
                                setUpAdapterNew()
                            }


                    }

                }
                else -> {
                    name.setText(product.name)
                    quantity.setText(product.quantity.toString())
                    price.setText(product.price.toString())
                    setPositiveButton(android.R.string.ok) { _, _ ->
                        val pro = hashMapOf<String, Any>(
                            "uid" to uid,
                            "name" to name.text.toString(),
                            "quantity" to if(quantity.text.toString()
                                    .isNotEmpty() && quantity.text.toString().isDigitsOnly()) quantity.text.toString().toInt() else 0,
                            "price" to if(price.text.toString()
                                    .isNotEmpty())  price.text.toString().toDouble() else 0.0,
                            "bought" to product.bought
                        )

                        db.collection("products").document(product.id).update(pro).addOnSuccessListener {  setUpAdapterNew() }
                    }
                }
            }
            setView(dialogView)
            setNegativeButton(android.R.string.cancel) { _, _ ->  }
            create().show()
        }

    }


}