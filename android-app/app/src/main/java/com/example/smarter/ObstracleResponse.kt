package com.example.smartar

data class ImageRequest(
    val image: String
)

data class Alert(
    val `object`: String,
    val distance_cm: Double
)

data class Prediction(
    val label: String,
    val confidence: Double,
    val distance_cm: Double,
    val bbox: List<Int>
)

data class ObstacleResponse(
    val alerts: List<Alert>,
    val predictions: List<Prediction>
)
