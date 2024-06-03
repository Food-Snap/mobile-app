package com.foodsnap.app.utils

import com.foodsnap.app.data.model.Food


object FakeData {
    fun generateFood() = foodList
    fun searchFood(query: String) = foodList.filter { ingredient ->
        ingredient.name.contains(query, ignoreCase = true)
    }

    private val foodList = listOf(
        Food(
            1,
            "Bawang Merah",
            "https://img.antaranews.com/cache/1200x800/2019/06/08/1BA5654B-31C4-442A-A8EF-C49BB42E9599.jpeg.webp",
            "August 3rd, 2024",
            300,
            200,
            100,
            90,
            150
        ),
        Food(
            2,
            "Bawang Putih",
            "https://res.cloudinary.com/dk0z4ums3/image/upload/v1681161939/attached_image/5-manfaat-bawang-putih-untuk-wanita-yang-jarang-diketahui.jpg",
            "August 3rd, 2024",
            300,
            200,
            100,
            90,
            150
        ),
        Food(
            3,
            "Cabai Merah",
            "https://lh5.googleusercontent.com/proxy/rdU3azQjnUJEhNsAw5pvkYY9p-LVgpIiGELCm6_L8rlS_maBTYgCTwxQJYys_-MrjXc03oXcFNP6nlWJR0lfnfMkn343awZHuao",
            "August 3rd, 2024",
            300,
            200,
            100,
            90,
            150
        ),
        Food(
            4,
            "Cabai Rawit",
            "https://awsimages.detik.net.id/community/media/visual/2022/06/08/ilustrasi-cabai-rawit_169.jpeg?w=1200",
            "August 3rd, 2024",
            300,
            200,
            100,
            90,
            150
        ),
        Food(
            5,
            "Tomat",
            "https://umsu.ac.id/artikel/wp-content/uploads/2023/09/manfaat-tomat-buah-segar-yang-penuh-nutrisi.jpg",
            "August 3rd, 2024",
            300,
            200,
            100,
            90,
            150
        ),
        Food(
            6,
            "Kentang",
            "https://i0.wp.com/umsu.ac.id/berita/wp-content/uploads/2023/08/kentang-manfaat-dan-kandungan-gizi-untuk-kesehatan.jpg?fit=960%2C640&ssl=1",
            "August 3rd, 2024",
            300,
            200,
            100,
            90,
            150
        ),
        Food(
            7,
            "Wortel",
            "https://asset-a.grid.id/crop/0x0:0x0/945x630/photo/2021/01/01/1662466486.jpg",
            "August 3rd, 2024",
            300,
            200,
            100,
            90,
            150
        ),
        Food(
            8,
            "Buncis",
            "https://dkpp.bulelengkab.go.id/public/uploads/konten/buncis-sayuran-hijau-untuk-menurunkan-kolesterol-35.jpg",
            "August 3rd, 2024",
            300,
            200,
            100,
            90,
            150
        ),
        Food(
            9,
            "Jagung",
            "https://cdn1-production-images-kly.akamaized.net/LDzUtv4OxjFe-dUGxmQsnE5ls5o=/800x450/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/939191/original/097414500_1438079406-20150728-Jagung.jpg",
            "August 3rd, 2024",
            300,
            200,
            100,
            90,
            150
        ),
        Food(
            10,
            "Tempe",
            "https://d1vbn70lmn1nqe.cloudfront.net/prod/wp-content/uploads/2023/05/26053515/Murah-dan-Bergizi-Ini-7-Manfaat-Tempe-yang-Masih-Jarang-Diketahui-.jpg.webp",
            "August 3rd, 2024",
            300,
            200,
            100,
            90,
            150
        )
    )
}