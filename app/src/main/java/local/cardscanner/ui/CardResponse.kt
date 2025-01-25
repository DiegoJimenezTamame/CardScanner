package local.cardscanner.ui

data class CardResponse(
    val id: String,
    val name: String,
    val set: String,
    val imageUris: ImageUris,
    val manaCost: String
)

data class ImageUris(
    val small: String,
    val normal: String,
    val large: String
)