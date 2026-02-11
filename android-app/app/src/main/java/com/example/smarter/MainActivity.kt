package com.example.smartar

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            // TODO: Show permission denied message or handle gracefully
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartARNavigationScreen(
                onScannerClick = {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        openCamera()
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            )
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            startActivity(cameraIntent)
        }
    }
}

@Composable
fun SmartARNavigationScreen(
    onScannerClick: () -> Unit
) {
    val navBarColor = Color(0xFF22396C)
    val cardBlue = Color(0xFF2243B6)
    val selectedItemColor = Color(0xFF2243B6)
    val unselectedItemColor = Color(0xFFBDBDBD)
    var selectedIndex by remember { mutableStateOf(0) }
    var glowingIndex by remember { mutableStateOf(-1) }
    var isCardPressed by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(painterResource(R.drawable.ic_home), contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedIndex == 0,
                    onClick = { selectedIndex = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = selectedItemColor,
                        selectedTextColor = selectedItemColor,
                        unselectedIconColor = unselectedItemColor,
                        unselectedTextColor = unselectedItemColor
                    )
                )
                NavigationBarItem(
                    icon = { Icon(painterResource(R.drawable.ic_scanner), contentDescription = "Scanner") },
                    label = { Text("Scanner") },
                    selected = selectedIndex == 1,
                    onClick = {
                        selectedIndex = 1
                        onScannerClick()
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = selectedItemColor,
                        selectedTextColor = selectedItemColor,
                        unselectedIconColor = unselectedItemColor,
                        unselectedTextColor = unselectedItemColor
                    )
                )
                NavigationBarItem(
                    icon = { Icon(painterResource(R.drawable.ic_settings), contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedIndex == 2,
                    onClick = { selectedIndex = 2 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = selectedItemColor,
                        selectedTextColor = selectedItemColor,
                        unselectedIconColor = unselectedItemColor,
                        unselectedTextColor = unselectedItemColor
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Smart AR Navigation",
                color = navBarColor,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            Text(
                text = "Your intelligent guide for safe travel",
                color = Color(0xFF758296),
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Centered & Clickable Start Navigation Card - Full Width
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth() // Full available width inside box
                        .height(160.dp)
                        .clickable { isCardPressed = !isCardPressed },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCardPressed) Color.Red else cardBlue
                    ),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_navigation),
                            contentDescription = "Navigate",
                            tint = Color.White,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Start Navigation",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Quick Actions",
                modifier = Modifier.padding(start = 18.dp, bottom = 10.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = navBarColor
            )
            Column {
                QuickActionCard(
                    iconRes = R.drawable.ic_location,
                    title = "Find Location",
                    subtitle = "Search for a destination",
                    isGlowing = glowingIndex == 0,
                    onClickIcon = { glowingIndex = 0 }
                )
                Spacer(modifier = Modifier.height(16.dp))
                QuickActionCard(
                    iconRes = R.drawable.ic_voice,
                    title = "Voice Commands",
                    subtitle = "Control with your voice",
                    isGlowing = glowingIndex == 1,
                    onClickIcon = { glowingIndex = 1 }
                )
                Spacer(modifier = Modifier.height(16.dp))
                QuickActionCard(
                    iconRes = R.drawable.ic_recent,
                    title = "Recent Places",
                    subtitle = "View your navigation history",
                    isGlowing = glowingIndex == 2,
                    onClickIcon = { glowingIndex = 2 }
                )
                Spacer(modifier = Modifier.height(16.dp))
                QuickActionCard(
                    iconRes = R.drawable.ic_emergency,
                    title = "Emergency Contacts",
                    subtitle = "Quick access to help",
                    isGlowing = glowingIndex == 3,
                    onClickIcon = { glowingIndex = 3 }
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE9F2FF)),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 22.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        painterResource(R.drawable.ic_emergency),
                        contentDescription = "Warning",
                        tint = Color(0xFF22396C),
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(Modifier.width(14.dp))
                    Text(
                        text = "Voice guidance is enabled. The app will announce directions and obstacles in your path.",
                        color = Color(0xFF22396C),
                        fontSize = 15.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun QuickActionCard(
    iconRes: Int,
    title: String,
    subtitle: String,
    isGlowing: Boolean,
    onClickIcon: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FAFF)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (isGlowing) Color(0xFFCDDBFF) else Color(0xFFE6EEFA),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onClickIcon() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(iconRes),
                    contentDescription = title,
                    tint = if (isGlowing) Color(0xFF2243B6) else Color(0xFF22396C),
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF22396C), fontSize = 16.sp)
                Text(subtitle, color = Color(0xFF758296), fontSize = 13.sp)
            }
        }
    }
}
