package smb.s18579.mb.shoplist.database.product

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDAO {
    @Insert
    fun insert(product: ProductDTO) : Long

    @Query("SELECT * FROM product;")
    fun selectAll(): List<ProductDTO>

    @Update
    fun update(product: ProductDTO)

    @Delete
    fun delete(product: ProductDTO)
}