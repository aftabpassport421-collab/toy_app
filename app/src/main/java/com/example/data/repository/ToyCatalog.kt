package com.example.data.repository

import com.example.data.model.AgeGroup
import com.example.data.model.Review
import com.example.data.model.Toy
import com.example.data.model.ToyCategory

object ToyCatalog {
    val toys: List<Toy> = listOf(
        Toy(
            id = "kz-lego-1",
            title = "LEGO City Space Explorer Rover & Research Station",
            titleAr = "ليجو سيتي مركبة استكشاف الفضاء ومحطة الأبحاث",
            brand = "LEGO",
            ageGroup = AgeGroup.EXPLORER,
            category = ToyCategory.BUILDING,
            price = 199.0,
            originalPrice = 249.0,
            rating = 4.9f,
            reviewCount = 428,
            description = "Embark on planetary exploration missions with this deluxe LEGO City modular space rover, laboratory station, and mini-figures. Certified European & Gulf GSO child-safe standards.",
            descriptionAr = "انطلق في مغامرات الفضاء المثيرة مع مركبة ليجو سيتي المجهزة بمختبر علمي ورواد فضاء.",
            keyFeatures = listOf(
                "Authentic licensed LEGO 425-piece building system",
                "Working all-terrain heavy suspension & opening airlock",
                "Includes 3 astronaut mini-figures & alien crystal terrain",
                "Step-by-step interactive 3D digital guide on LEGO Builder App"
            ),
            safetyCertifications = listOf("GSO Gulf Toy Standard", "CE Certified", "ASTM F963", "BPA Free"),
            batteriesRequired = "No Batteries Required",
            awards = listOf("Toy of the Year Qatar 2025", "LEGO Certified Master Builder Pick"),
            colorAccentHex = 0xFFD32F2F,
            isBestSellerInQatar = true,
            isSameDayDeliveryQatar = true,
            isExclusiveKiddyZone = true
        ),
        Toy(
            id = "kz-barbie-2",
            title = "Barbie Color Reveal Pastel Pet & Fashion Dreamhouse Playset",
            titleAr = "مجموعة باربي بيت الأحلام وأزياء الباستيل السحرية",
            brand = "Barbie",
            ageGroup = AgeGroup.PRESCHOOL,
            category = ToyCategory.DOLLS,
            price = 159.0,
            originalPrice = 189.0,
            rating = 4.8f,
            reviewCount = 512,
            description = "Unbox magical color transformation surprises with Barbie! Dip in warm water to reveal vibrant outfits, sparkling accessories, and cute pet companion in a 2-story foldable villa.",
            descriptionAr = "اكتشفي المفاجآت السحرية مع دمية باربي بتأثير تغيير الألوان بالماء الدافئ مع إكسسوارات أنيقة.",
            keyFeatures = listOf(
                "25 surprise unboxing accessories & matching puppy",
                "Color-change makeup and hair activated with ice-cold water",
                "Fold-and-go dollhouse design with carrying handle",
                "Non-toxic child-safe premium soft fabrics"
            ),
            safetyCertifications = listOf("GSO Gulf Toy Standard", "Mattel Safety Tested", "ASTM F963", "EN71"),
            batteriesRequired = "No Batteries Required",
            awards = listOf("Top Girls Toy in Doha Festival City", "Parent Choice Qatar"),
            colorAccentHex = 0xFFE91E63,
            isBestSellerInQatar = true,
            isSameDayDeliveryQatar = true
        ),
        Toy(
            id = "kz-hotwheels-3",
            title = "Hot Wheels City Ultimate T-Rex Mega Transporter & Track",
            titleAr = "هوت ويلز ناقلة الديناصور الضخمة مع مسار السرعة",
            brand = "Hot Wheels",
            ageGroup = AgeGroup.EXPLORER,
            category = ToyCategory.VEHICLES,
            price = 175.0,
            originalPrice = 210.0,
            rating = 4.9f,
            reviewCount = 630,
            description = "Massive 2-in-1 transporter that transforms into a roaring dual-lane drag race track with an interactive motorized T-Rex obstacle chomper.",
            descriptionAr = "شاحنة هوت ويلز العملاقة التي تتحول إلى حلبة سباق مزدوجة مع ديناصور تي ريكس التفاعلي.",
            keyFeatures = listOf(
                "Stores over 20 Hot Wheels die-cast cars (2 cars included)",
                "Transforms into a 3-foot dual-lane high-speed race strip",
                "Roaring dinosaur sound effects and car launcher trigger",
                "Durable crash-resistant high grade ABS plastic"
            ),
            safetyCertifications = listOf("GSO Gulf Standard", "Mattel Quality Seal", "CE Certified"),
            batteriesRequired = "3x AAA batteries (Demo batteries included)",
            awards = listOf("Best Action Toy Mall of Qatar", "Hot Wheels Gold Award"),
            colorAccentHex = 0xFFFF6F00,
            isBestSellerInQatar = true,
            isSameDayDeliveryQatar = true
        ),
        Toy(
            id = "kz-disney-4",
            title = "Disney Frozen Elsa Magical Ice Palace with Lights & Music",
            titleAr = "قصر إلسا الجليدي السحري من ديزني فروزن مع أضواء وموسيقى",
            brand = "Disney",
            ageGroup = AgeGroup.PRESCHOOL,
            category = ToyCategory.DOLLS,
            price = 229.0,
            originalPrice = 279.0,
            rating = 4.9f,
            reviewCount = 740,
            description = "Iconic Arendelle ice castle featuring a rotating light show, shimmering sound effects, sparkling snow staircase, and Olaf & Elsa mini-figures.",
            descriptionAr = "قصر ديزني فروزن السحري مع أضواء جليدية متلألئة وموسيقى فيلم فروزن الشهيرة.",
            keyFeatures = listOf(
                "Plays the iconic melody with twinkling icy LED projection",
                "Includes Elsa doll, Olaf figurine, snowflake chandelier & throne",
                "Doors swing open with realistic crystal sound chime",
                "Rounded smooth edges designed for safe small hands"
            ),
            safetyCertifications = listOf("Disney Official Licensed", "GSO Gulf Standards", "ASTM F963"),
            batteriesRequired = "3x AA batteries (Included)",
            awards = listOf("Most Loved Gift in Qatar 2024"),
            colorAccentHex = 0xFF00B0FF,
            isBestSellerInQatar = true,
            isSameDayDeliveryQatar = true
        ),
        Toy(
            id = "kz-nerf-5",
            title = "Nerf Elite 2.0 Commander Motorized Tactical Blaster",
            titleAr = "ناسف نيرف إيليت 2.0 التكتيكي الآلي",
            brand = "Nerf",
            ageGroup = AgeGroup.INNOVATOR,
            category = ToyCategory.OUTDOOR,
            price = 129.0,
            originalPrice = 149.0,
            rating = 4.7f,
            reviewCount = 380,
            description = "Customizable tactical blaster with 3 tactical rails, barrel attachment point, and a 6-dart rotating cylinder. Shoots soft foam darts up to 90 feet (27 meters).",
            descriptionAr = "ناسف نيرف الاحترافي مع سهام فوم آمنة للأطفال ومدى إطلاق يصل إلى 27 متراً.",
            keyFeatures = listOf(
                "Includes 24 official soft foam Nerf Elite darts",
                "Fast slam-fire action handle for rapid backyard play",
                "Customizable attachments for tactical gaming",
                "Flexible foam tips tested and approved for safety"
            ),
            safetyCertifications = listOf("Hasbro Safe Play", "GSO Certified", "CE Mark", "ASTM F963"),
            batteriesRequired = "Manual Air Powered (No Batteries Required)",
            awards = listOf("Best Outdoor Sports Toy"),
            colorAccentHex = 0xFF0091EA,
            isSameDayDeliveryQatar = true
        ),
        Toy(
            id = "kz-playdoh-6",
            title = "Play-Doh Kitchen Creations Deluxe Chef Burger & Pizza Cafe",
            titleAr = "مجموعة بلاي دو مطبخ البرجر والبيتزا الفاخرة للأطفال",
            brand = "Play-Doh",
            ageGroup = AgeGroup.PRESCHOOL,
            category = ToyCategory.CREATIVE,
            price = 89.0,
            originalPrice = 109.0,
            rating = 4.8f,
            reviewCount = 590,
            description = "Non-toxic modeling clay culinary workstation with grill press, pizza oven roller, French fry extruder, and 10 vibrant colorful compound cans.",
            descriptionAr = "محطة طبخ الصلصال التفاعلية مع شواية برجر وفرن بيتزا و10 علب صلصال ملونة غير سامة.",
            keyFeatures = listOf(
                "10 cans of safe, non-toxic wheat-based Play-Doh modeling compound",
                "Over 15 fun culinary kitchen tools & cutting molds",
                "Squeeze curly pretend french fries & twirling pasta",
                "Wipes down easily; promotes tactile sensory creativity"
            ),
            safetyCertifications = listOf("Non-Toxic AP Certified", "ASTM D4236", "GSO Compliant"),
            batteriesRequired = "No Batteries Required",
            awards = listOf("Creativity Toy Gold Award Qatar"),
            colorAccentHex = 0xFFFFD600,
            isSameDayDeliveryQatar = true
        ),
        Toy(
            id = "kz-marvel-7",
            title = "Marvel Avengers Titan Hero Spider-Man Web Blast Action Figure",
            titleAr = "مجسم سبايدرمان تيتان هيرو مارفل مع قاذف الشباك",
            brand = "Marvel",
            ageGroup = AgeGroup.EXPLORER,
            category = ToyCategory.ACTION_FIGURES,
            price = 99.0,
            rating = 4.8f,
            reviewCount = 475,
            description = "Large 12-inch poseable Spider-Man action figure featuring arm-mounted launcher that blasts soft webbing projectiles into high-flying superhero missions.",
            descriptionAr = "مجسم سبايدرمان بحجم 12 بوصة مع قاذف خيوط شبكية قابل للتوجيه وحركات مرنة.",
            keyFeatures = listOf(
                "12-inch detailed Marvel Comics classic suit sculpting",
                "Arm-mounted web missile launcher with 2 soft projectiles",
                "Articulated joints for dynamic web-slinging hero poses",
                "Durable drop-tested composite polymers"
            ),
            safetyCertifications = listOf("Marvel Licensed", "GSO Gulf Toy Standard", "ASTM F963"),
            batteriesRequired = "No Batteries Required",
            awards = listOf("Best Superhero Toy Qatar"),
            colorAccentHex = 0xFFD50000,
            isBestSellerInQatar = true,
            isSameDayDeliveryQatar = true
        ),
        Toy(
            id = "kz-pawpatrol-8",
            title = "Paw Patrol Movie 2 Chase Mighty Transforming Rescue Cruiser",
            titleAr = "مركبة تشيس باو باترول المتحولة للإنقاذ السريع مع أضواء",
            brand = "Paw Patrol",
            ageGroup = AgeGroup.PRESCHOOL,
            category = ToyCategory.VEHICLES,
            price = 139.0,
            originalPrice = 165.0,
            rating = 4.9f,
            reviewCount = 612,
            description = "Gear up with Chase! Push the spoiler to activate transformation into lightning rescue mode with synchronized flashing emergency lights and siren sounds.",
            descriptionAr = "سيارة شرطة تشيس من باو باترول تتحول إلى وضع السرعة الفائقة مع أصواء وأصوات إنقاذ.",
            keyFeatures = listOf(
                "One-touch spring transformation into supersonic cruiser",
                "Flashing LED emergency lights and movie sound effects",
                "Includes removable molded Chase hero police dog pup",
                "Tough ABS body designed for enthusiastic toddler play"
            ),
            safetyCertifications = listOf("Spin Master Approved", "GSO Compliant", "CE Certified"),
            batteriesRequired = "3x LR44 Button Batteries (Pre-installed)",
            awards = listOf("Top Preschool Vehicle in Qatar Malls"),
            colorAccentHex = 0xFF1565C0,
            isBestSellerInQatar = true,
            isSameDayDeliveryQatar = true
        ),
        Toy(
            id = "kz-stem-9",
            title = "Kiddy Tech Programmable Smart AI Coding Robot Bot",
            titleAr = "روبوت البرمجة الذكي كيدي تيك للأطفال مع تطبيق تعليمي",
            brand = "Kiddy Tech",
            ageGroup = AgeGroup.INNOVATOR,
            category = ToyCategory.STEM,
            price = 249.0,
            originalPrice = 299.0,
            rating = 4.9f,
            reviewCount = 310,
            description = "Interactive smart robot with ultrasonic obstacle avoidance, Bluetooth mobile app for Scratch-based block programming, and voice greeting in English & Arabic.",
            descriptionAr = "روبوت تعليمي تفاعلي يدعم تعلم لغات البرمجة والتحكم بالهاتف مع مستشعرات تجنب العوائق.",
            keyFeatures = listOf(
                "Bilingual voice responses (English & Arabic)",
                "Visual drag-and-drop block coding on iOS and Android",
                "Rechargeable USB-C fast charging battery pack",
                "Infrared path tracking and emotion matrix LED eyes"
            ),
            safetyCertifications = listOf("GSO Certified", "FCC ID", "CE Certified", "Non-Toxic Plastic"),
            batteriesRequired = "Rechargeable Lithium Battery (Type-C Cable Included)",
            awards = listOf("Qatar STEM Innovation Award 2025", "Kiddy Zone Tech Pick"),
            colorAccentHex = 0xFF6200EA,
            isExclusiveKiddyZone = true,
            isSameDayDeliveryQatar = true
        ),
        Toy(
            id = "kz-fisherprice-10",
            title = "Fisher-Price Linkimals Smooth Moves Sloth Sensory Toy",
            titleAr = "كسلان فيشر برايس الموسيقي التفاعلي لتعليم الرضع",
            brand = "Fisher-Price",
            ageGroup = AgeGroup.TODDLER,
            category = ToyCategory.BABY_PRESCHOOL,
            price = 119.0,
            originalPrice = 139.0,
            rating = 4.9f,
            reviewCount = 820,
            description = "Claps hands and bobs head to cheerful music, nursery rhymes, and colorful lights. Teaches the alphabet, numbers, opposites, and colors.",
            descriptionAr = "لعبة الكسلان التفاعلية تصفق وتغني بالأضواء المبهجة لتعليم الحروف والأرقام والألوان.",
            keyFeatures = listOf(
                "Smart Stages technology with 85+ songs, sounds, tunes & phrases",
                "Syncs and sings in harmony with other Linkimals friends",
                "Soft touch plush arms and sensory ribbed belly",
                "Helps develop baby's gross motor and listening skills"
            ),
            safetyCertifications = listOf("Mattel Infant Safety Protocol", "BPA Free", "GSO Approved"),
            batteriesRequired = "4x AA batteries (Included)",
            awards = listOf("Best Baby Developmental Toy Qatar"),
            colorAccentHex = 0xFF00BFA5,
            isSameDayDeliveryQatar = true
        ),
        Toy(
            id = "kz-monopoly-11",
            title = "Monopoly Qatar Landmark Edition Family Board Game",
            titleAr = "لعبة مونوبولي نسخة معالم قطر العائلية الشهيرة",
            brand = "Hasbro Gaming",
            ageGroup = AgeGroup.EXPLORER,
            category = ToyCategory.BOARD_GAMES,
            price = 149.0,
            rating = 4.8f,
            reviewCount = 490,
            description = "Trade famous Qatar landmarks including Souq Waqif, Katara Cultural Village, The Pearl, Lusail Marina, and National Museum of Qatar in this special collector edition.",
            descriptionAr = "امتلك وتداول أشهر معالم قطر مثل سوق واقف، واللؤلؤة، وكتارا، ولوسيل في هذه النسخة الحصرية.",
            keyFeatures = listOf(
                "Exclusive Qatar map artwork with custom gold tokens (Falcon, Dhow, Oryx)",
                "Bilingual cards and property deeds in Arabic and English",
                "Fast-dealing property game suitable for 2-6 players",
                "High quality linen finish game board & velvet money tray"
            ),
            safetyCertifications = listOf("GSO Gulf Toy Standard", "Hasbro Official Guarantee", "CE Certified"),
            batteriesRequired = "No Batteries Required",
            awards = listOf("Top Family Game in Qatar", "Heritage Edition Gold"),
            colorAccentHex = 0xFF795548,
            isBestSellerInQatar = true,
            isExclusiveKiddyZone = true,
            isSameDayDeliveryQatar = true
        ),
        Toy(
            id = "kz-crayola-12",
            title = "Crayola Inspiration Art Case Deluxe 140-Piece Coloring Studio",
            titleAr = "حقيبة كريولا الفاخرة للرسم والتلوين 140 قطعة للأطفال",
            brand = "Crayola",
            ageGroup = AgeGroup.PRESCHOOL,
            category = ToyCategory.CREATIVE,
            price = 109.0,
            originalPrice = 129.0,
            rating = 4.9f,
            reviewCount = 940,
            description = "Portable locking art studio case containing 64 crayons, 20 short colored pencils, 40 washable markers, and 15 sheets of drawing paper.",
            descriptionAr = "حقيبة أدوات الرسم المتكاملة مع ألوان خشبية وشمعية وماركرز قابلة للغسل 140 قطعة.",
            keyFeatures = listOf(
                "Ultra-washable formula: washes easily from skin and clothing",
                "140 vibrant coloring tools organized in a durable handle case",
                "Safe, non-toxic certified for young budding artists",
                "Perfect companion for travel, school, and holiday gifts"
            ),
            safetyCertifications = listOf("AP Certified Non-Toxic", "ASTM D4236", "GSO Approved"),
            batteriesRequired = "No Batteries Required",
            awards = listOf("Mom's Favorite Creative Gift"),
            colorAccentHex = 0xFFFFAB00,
            isSameDayDeliveryQatar = true
        )
    )

    val brands: List<String> = listOf(
        "All Brands",
        "LEGO",
        "Barbie",
        "Hot Wheels",
        "Disney",
        "Nerf",
        "Play-Doh",
        "Marvel",
        "Paw Patrol",
        "Fisher-Price",
        "Kiddy Tech",
        "Hasbro Gaming",
        "Crayola"
    )

    val topBrands: List<String> = brands.filter { it != "All Brands" }

    fun getSampleReviews(toyId: String): List<Review> = listOf(
        Review(
            id = "rev-1",
            author = "Fatima Al-Kuwari (Doha, Qatar)",
            rating = 5,
            date = "Yesterday",
            comment = "Delivered to West Bay in less than 3 hours with Kiddy Zone same-day delivery! Free gift wrapping was beautifully done with a red bow. My son loves it!",
            childAge = "6 yrs",
            location = "West Bay, Doha"
        ),
        Review(
            id = "rev-2",
            author = "Mohammed Al-Thani (Al Rayyan)",
            rating = 5,
            date = "3 days ago",
            comment = "Original and licensed toy, exactly as shown in Kiddy Zone Mall of Qatar branch. Very pleased with the quick service and quality.",
            childAge = "8 yrs",
            location = "Al Rayyan, Qatar"
        ),
        Review(
            id = "rev-3",
            author = "Reem S. (Lusail)",
            rating = 5,
            date = "1 week ago",
            comment = "Click & collect at Place Vendôme was super seamless. Handed directly at the counter. Earned Kiddy Club points as well!",
            childAge = "4 yrs",
            location = "Lusail, Qatar"
        )
    )
}

