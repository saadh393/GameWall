package com.wwinc.moviewall.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.wwinc.moviewall.database.DatabaseWallpaper
import com.wwinc.moviewall.Model.EntityTopRatedWallpaper
import com.wwinc.moviewall.Model.EntityWallpaper
import com.wwinc.moviewall.Model.OtherAppListModel
import com.wwinc.moviewall.networking.WallpaperApi
import com.wwinc.moviewall.networking.Wallpaper_ModelItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryWallpaper(private val databaseRef: DatabaseWallpaper) {

    // Top Rated Wallpaper Repository
    var topRatedWallPaper: LiveData<List<Wallpaper_ModelItem>> =
        Transformations.map(databaseRef.getDaoTopRated.get_toprated_links()) {
            it.map {
                Wallpaper_ModelItem(
                    imageURL = it.imageURL,
                    favOrNot = false
                )
            }
        }


    // Popular Wallpaper Repository
    var popularWallpaperData: LiveData<List<Wallpaper_ModelItem>> = Transformations.map(databaseRef.getDaoPopular.get_all_links()){ it ->
        it.shuffled().map {
            Wallpaper_ModelItem(
                imageURL = it.imageURL,
                favOrNot = it.fav
            )

        }
    }

    // Fav Wallpapers
    val favouriteWallpapersViewModel : LiveData<List<Wallpaper_ModelItem>> = Transformations.map(databaseRef.getDaoPopular.get_all_favLinks()){
        it.map {
            Wallpaper_ModelItem(
                imageURL = it.imageURL,
                favOrNot = it.fav
            )

        }
    }

    // Fav Wallpapers
    val favouriteWallpapersTopRated : LiveData<List<Wallpaper_ModelItem>> = Transformations.map(databaseRef.getDaoTopRated.get_all_favLinks()){
        it.map {
            Wallpaper_ModelItem(
                imageURL = it.imageURL,
                favOrNot = it.fav
            )

        }
    }

    // Getting Our Other App List
    val getOurOtherApplist : LiveData<List<OtherAppListModel>> = databaseRef.getDaoOtherapps.getOtherApplist()



    // Refresh both popular and top Rated wallpapers
    suspend fun refreshWallpapers() {

        withContext(Dispatchers.IO) {
            // Popular Wallpaper
            val imageUrl = WallpaperApi.retrofitService.getLinksAsync().await()
            Log.d("123as123", "Resutl :  \t\t  :  $imageUrl");
            databaseRef.getDaoPopular.update_all_links(imageUrl.map {
                EntityWallpaper(
                    imageURL = it.imageURL!!
                )
            })


            // Top Rated Wallpaper
            val topRated_imgUrl = WallpaperApi.retrofitService.getTopRated().await()
            databaseRef.getDaoTopRated.update_toprated_links(topRated_imgUrl.map {
                EntityTopRatedWallpaper(
                    imageURL = it.imageURL!!
                )
            })

//            // Other Apps List
//            val otherApplist = WallpaperApi.retrofitService.othersApplist2().await()
//            databaseRef.getDaoOtherapps.insertData(otherApplist)

        }


    }

}





