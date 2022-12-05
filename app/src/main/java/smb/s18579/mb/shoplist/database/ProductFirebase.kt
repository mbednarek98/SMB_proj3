package smb.s18579.mb.shoplist.database


data class Product (
    var id : String = "",
    var uid : String = "",
    var name : String = "",
    var quantity : Int = 0,
    var price : Double = 0.0,
    var bought: Boolean = false,
)