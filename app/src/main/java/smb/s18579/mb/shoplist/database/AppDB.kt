package smb.s18579.mb.shoplist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import smb.s18579.mb.shoplist.database.product.ProductDAO
import smb.s18579.mb.shoplist.database.product.ProductDTO

@Database(
    entities = [ProductDTO::class],
    version = 1
)

abstract class AppDB : RoomDatabase(){
    abstract val product: ProductDAO
    companion object {
        fun open(context: Context) = Room.databaseBuilder(
            context, AppDB::class.java, "ShopListDB"
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries().build()
    }
}