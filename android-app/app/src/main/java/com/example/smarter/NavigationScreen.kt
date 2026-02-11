
package com.example.smartar

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@Composable
fun NavigationScreen() {

    val context = LocalContext.current
    val voiceHelper = remember { VoiceHelper(context) }

    var statusText by remember { mutableStateOf("Press button to scan") }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                statusText = "Processing..."
                sendImageToServer(it, voiceHelper) {
                    statusText = it
                }
            }
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = statusText)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                cameraLauncher.launch(null)
            }
        }) {
            Text("Scan Environment")
        }
    }
}

fun sendImageToServer(
    bitmap: Bitmap,
    voiceHelper: VoiceHelper,
    updateStatus: (String) -> Unit
) {
    val api = ApiService.create()
    val base64Image = bitmapToBase64(bitmap)

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = api.detectObstacle(ImageRequest(base64Image))

            if (response.alerts.isNotEmpty()) {
                val alert = response.alerts[0]
                val message =
                    "Obstacle ${alert.`object`} detected at ${alert.distance_cm} centimeters"

                voiceHelper.speak(message)
                updateStatus(message)
            } else {
                updateStatus("Path is clear")
                voiceHelper.speak("Path is clear")
            }

        } catch (e: Exception) {
            updateStatus("Error: ${e.message}")
        }
    }
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
    val byteArray = stream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}
