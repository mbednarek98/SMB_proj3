package smb.s18579.mb.shoplist.database.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductDTO (
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    @ColumnInfo(defaultValue = "")
    var name : String,
    @ColumnInfo
    var quantity : Int = 0,
    @ColumnInfo
    var price : Double = 0.0,
    @ColumnInfo
    var bought: Boolean = false,
)