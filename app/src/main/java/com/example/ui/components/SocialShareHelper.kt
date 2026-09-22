package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.data.model.Toy

object SocialShareHelper {
    fun shareToy(context: Context, toy: Toy) {
        val shareText = """
            🌟 WonderToy Recommendation for Kids! 🌟
            Check out "${toy.title}" by ${toy.brand}!
            Age Group: ${toy.ageGroup.label} (${toy.ageGroup.ageRange})
            Price: $${String.format("%.2f", toy.price)}
            Rating: ★ ${toy.rating}/5 (${toy.reviewCount} parent reviews)
            Safety Certified: ${toy.safetyCertifications.joinToString(", ")}
            
            Find this and more kid-safe toys on Toy Wonder!
            https://toywonder.app/toys/${toy.id}
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_TITLE, "Share ${toy.title}")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Recommend to Friends & Family")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    fun shareWishlist(context: Context, toys: List<Toy>) {
        val summary = toys.take(5).joinToString("\n") { "• ${it.title} (${it.ageGroup.label}) - $${String.format("%.2f", it.price)}" }
        val shareText = """
            🎁 My Kids Toy Wishlist on Toy Wonder! 🎁
            Here are our favorite toys:
            $summary
            ${if (toys.size > 5) "...and ${toys.size - 5} more exciting items!" else ""}
            
            View full family wishlist: https://toywonder.app/wishlist/share
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_TITLE, "Share Toy Wishlist")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Wishlist")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    fun copyToyLink(context: Context, toy: Toy) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Toy Wonder Link", "https://toywonder.app/toys/${toy.id}")
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Link copied to clipboard!", Toast.LENGTH_SHORT).show()
    }
}
