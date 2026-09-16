package kh.edu.rupp.taskmanagement.location

import android.content.Context
import android.location.Geocoder
import android.location.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

// a readable line for a fix: no map, no key, and allowed to answer nothing
object PlaceNamer {
    suspend fun label(context: Context, location: Location): String? =
        withContext(Dispatchers.IO) {
            if (!Geocoder.isPresent()) return@withContext null
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                addresses?.firstOrNull()?.getAddressLine(0)
            } catch (e: Exception) {
                null
            }
        }
}
