package com.example.data.model

enum class AgeGroup(val label: String, val ageRange: String, val description: String, val iconEmoji: String) {
    ALL("All Ages", "0-14+ yrs", "Explore all toys", "🧸"),
    TODDLER("0-2 Years", "0-2 yrs", "Sensory, Teething & Soft Play", "👶"),
    PRESCHOOL("3-5 Years", "3-5 yrs", "Building, Pretend Play & Crafts", "🎨"),
    EXPLORER("6-8 Years", "6-8 yrs", "Puzzles, STEM & Active Games", "🚀"),
    INNOVATOR("9-12 Years", "9-12 yrs", "Robotics, Coding & Strategy", "🔬"),
    TEEN("12+ Years", "12+ yrs", "Advanced Mechanics & RC Tech", "🎮");

    companion object {
        fun fromAge(age: Int): AgeGroup = when {
            age <= 2 -> TODDLER
            age <= 5 -> PRESCHOOL
            age <= 8 -> EXPLORER
            age <= 12 -> INNOVATOR
            else -> TEEN
        }
    }
}

enum class ToyCategory(val displayName: String, val displayNameAr: String, val iconEmoji: String) {
    ALL("All Toys", "جميع الألعاب", "✨"),
    BUILDING("LEGO & Building", "ليجو وبناء", "🧱"),
    ACTION_FIGURES("Action Figures & Heroes", "شخصيات الأكشن", "🦸"),
    DOLLS("Dolls & Playsets", "دمى ومجموعات لعب", "🎀"),
    VEHICLES("Cars & Hot Wheels", "سيارات وسرعة", "🏎️"),
    STEM("STEM & Robotics", "علوم وبرمجة", "🔬"),
    OUTDOOR("Ride-Ons & Outdoor", "ألعاب خارجية وركوب", "🚲"),
    BABY_PRESCHOOL("Baby & Toddler", "أطفال ومرحلة مبكرة", "👶"),
    CREATIVE("Arts, Crafts & Slime", "فنون وأشغال وسلايم", "🎨"),
    BOARD_GAMES("Board Games & Family", "ألعاب لوحية وعائلية", "🎲"),
    PLUSH("Plush & Cuddly", "دمى محشوة ناعمة", "🧸"),
    PUZZLES("Puzzles & Brain", "ألغاز وذكاء", "🧩");

    constructor(displayName: String, iconEmoji: String) : this(displayName, displayName, iconEmoji)
}

data class Toy(
    val id: String,
    val title: String,
    val titleAr: String = "",
    val brand: String,
    val ageGroup: AgeGroup,
    val category: ToyCategory,
    val price: Double,
    val originalPrice: Double? = null,
    val rating: Float,
    val reviewCount: Int,
    val description: String,
    val descriptionAr: String = "",
    val keyFeatures: List<String> = emptyList(),
    val safetyCertifications: List<String> = listOf("GSO Gulf Toy Standard", "CE Certified", "BPA Free"),
    val batteriesRequired: String = "No Batteries Required",
    val awards: List<String> = emptyList(),
    val inStock: Boolean = true,
    val colorAccentHex: Long = 0xFFE52534,
    val drawableResName: String = "img_hero_toys",
    val isBestSellerInQatar: Boolean = false,
    val isSameDayDeliveryQatar: Boolean = true,
    val isExclusiveKiddyZone: Boolean = false
) {
    val formattedPrice: String
        get() = "QAR ${price.toInt()}"

    val formattedOriginalPrice: String?
        get() = originalPrice?.let { "QAR ${it.toInt()}" }

    fun localizedPrice(isArabic: Boolean = false): String {
        return if (isArabic) {
            "${price.toInt()} ر.ق"
        } else {
            "QAR ${price.toInt()}"
        }
    }

    fun localizedOriginalPrice(isArabic: Boolean = false): String? {
        val orig = originalPrice ?: return null
        return if (isArabic) {
            "${orig.toInt()} ر.ق"
        } else {
            "QAR ${orig.toInt()}"
        }
    }
}

data class QatarStore(
    val id: String,
    val name: String,
    val nameAr: String,
    val mall: String,
    val city: String,
    val locationDetails: String,
    val openingHours: String,
    val phone: String,
    val clickAndCollectAvailable: Boolean = true
) {
    val nameEn: String get() = name
    val mallEn: String get() = mall
    val mallAr: String get() = nameAr
}

object QatarStoreDirectory {
    val stores: List<QatarStore> = listOf(
        QatarStore(
            id = "store-moq",
            name = "Kiddy Zone - Mall of Qatar",
            nameAr = "كيدي زون - قطر مول",
            mall = "Mall of Qatar",
            city = "Al Rayyan, Doha",
            locationDetails = "Ground Floor, Near Oasis / Gate 2",
            openingHours = "10:00 AM - 10:00 PM (Thu-Fri until 11:00 PM)",
            phone = "+974 4038 3888"
        ),
        QatarStore(
            id = "store-dfc",
            name = "Kiddy Zone - Doha Festival City",
            nameAr = "كيدي زون - دوحة فستيفال سيتي",
            mall = "Doha Festival City",
            city = "Umm Salal Mohammed",
            locationDetails = "First Floor, Kids Zone & Entertainment",
            openingHours = "10:00 AM - 10:00 PM (Thu-Fri until Midnight)",
            phone = "+974 4458 5500"
        ),
        QatarStore(
            id = "store-vendome",
            name = "Kiddy Zone - Place Vendôme",
            nameAr = "كيدي زون - بلاس فاندوم",
            mall = "Place Vendôme Mall",
            city = "Lusail City",
            locationDetails = "Level 1, Canal Walk Promenade",
            openingHours = "10:00 AM - 11:00 PM Daily",
            phone = "+974 4429 8800"
        ),
        QatarStore(
            id = "store-hyatt",
            name = "Kiddy Zone - Hyatt Plaza",
            nameAr = "كيدي زون - حياة بلازا",
            mall = "Hyatt Plaza",
            city = "Al Waab, Doha",
            locationDetails = "Gate 3, Near Jungle Zone",
            openingHours = "9:00 AM - 10:00 PM",
            phone = "+974 4469 7700"
        ),
        QatarStore(
            id = "store-landmark",
            name = "Kiddy Zone - Landmark Mall",
            nameAr = "كيدي زون - لاندمارك مول",
            mall = "Landmark Mall",
            city = "Al Gharrafa, Doha",
            locationDetails = "Ground Floor, Circus Land Avenue",
            openingHours = "10:00 AM - 10:00 PM",
            phone = "+974 4487 5500"
        )
    )
}

data class Review(
    val id: String,
    val author: String,
    val rating: Int,
    val date: String,
    val comment: String,
    val verifiedBuyer: Boolean = true,
    val childAge: String = "4 yrs",
    val location: String = "Doha, Qatar"
)

