package com.example.data.repository

import android.util.Log
import com.example.data.model.AgeGroup
import com.example.data.model.Toy
import com.example.data.model.ToyCategory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

/**
 * Service to manage products in Google Cloud Firestore for Kiddy Zone / Wonder Toy.
 * Allows syncing catalog to/from the cloud, adding new products remotely, and real-time listening.
 */
class FirestoreProductService(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val productsCollection = firestore.collection("products")

    /**
     * Upload / save a toy product to Firestore collection 'products'.
     */
    suspend fun saveProduct(toy: Toy): Boolean {
        return try {
            val productMap = hashMapOf(
                "id" to toy.id,
                "title" to toy.title,
                "titleAr" to toy.titleAr,
                "brand" to toy.brand,
                "ageGroup" to toy.ageGroup.name,
                "category" to toy.category.name,
                "price" to toy.price,
                "originalPrice" to (toy.originalPrice ?: toy.price),
                "rating" to toy.rating.toDouble(),
                "reviewCount" to toy.reviewCount,
                "description" to toy.description,
                "descriptionAr" to toy.descriptionAr,
                "keyFeatures" to toy.keyFeatures,
                "safetyCertifications" to toy.safetyCertifications,
                "batteriesRequired" to toy.batteriesRequired,
                "awards" to toy.awards,
                "isBestSellerInQatar" to toy.isBestSellerInQatar,
                "isSameDayDeliveryQatar" to toy.isSameDayDeliveryQatar,
                "isExclusiveKiddyZone" to toy.isExclusiveKiddyZone,
                "updatedAt" to System.currentTimeMillis()
            )
            productsCollection.document(toy.id).set(productMap, SetOptions.merge()).await()
            Log.d("FirestoreProductService", "Successfully saved product ${toy.id} to Firestore")
            true
        } catch (e: Exception) {
            Log.e("FirestoreProductService", "Error saving product to Firestore", e)
            false
        }
    }

    /**
     * Fetch all toy products from Firestore collection.
     */
    suspend fun fetchProducts(): List<Toy> {
        return try {
            val snapshot = productsCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                val id = doc.getString("id") ?: doc.id
                val title = doc.getString("title") ?: return@mapNotNull null
                val titleAr = doc.getString("titleAr") ?: ""
                val brand = doc.getString("brand") ?: "Kiddy Zone"
                val ageGroupName = doc.getString("ageGroup") ?: AgeGroup.ALL.name
                val categoryName = doc.getString("category") ?: ToyCategory.ALL.name
                val price = doc.getDouble("price") ?: 0.0
                val originalPrice = doc.getDouble("originalPrice")
                val rating = (doc.getDouble("rating") ?: 5.0).toFloat()
                val reviewCount = doc.getLong("reviewCount")?.toInt() ?: 0
                val description = doc.getString("description") ?: ""
                val descriptionAr = doc.getString("descriptionAr") ?: ""
                val batteriesRequired = doc.getString("batteriesRequired") ?: "No Batteries Required"
                val keyFeatures = (doc.get("keyFeatures") as? List<*>)?.mapNotNull { it?.toString() } ?: emptyList()
                val safetyCerts = (doc.get("safetyCertifications") as? List<*>)?.mapNotNull { it?.toString() } ?: listOf("GSO Gulf Toy Standard", "CE Certified")
                val isBestSeller = doc.getBoolean("isBestSellerInQatar") ?: false
                val isSameDay = doc.getBoolean("isSameDayDeliveryQatar") ?: true
                val isExclusive = doc.getBoolean("isExclusiveKiddyZone") ?: false

                Toy(
                    id = id,
                    title = title,
                    titleAr = titleAr,
                    brand = brand,
                    ageGroup = runCatching { AgeGroup.valueOf(ageGroupName) }.getOrDefault(AgeGroup.ALL),
                    category = runCatching { ToyCategory.valueOf(categoryName) }.getOrDefault(ToyCategory.ALL),
                    price = price,
                    originalPrice = originalPrice,
                    rating = rating,
                    reviewCount = reviewCount,
                    description = description,
                    descriptionAr = descriptionAr,
                    keyFeatures = keyFeatures,
                    safetyCertifications = safetyCerts,
                    batteriesRequired = batteriesRequired,
                    isBestSellerInQatar = isBestSeller,
                    isSameDayDeliveryQatar = isSameDay,
                    isExclusiveKiddyZone = isExclusive
                )
            }
        } catch (e: Exception) {
            Log.e("FirestoreProductService", "Error fetching products from Firestore", e)
            emptyList()
        }
    }

    /**
     * Seed initial catalog to Firestore if empty or on admin sync.
     */
    suspend fun seedCatalog(toys: List<Toy>): Int {
        var count = 0
        toys.forEach { toy ->
            if (saveProduct(toy)) count++
        }
        return count
    }
}
