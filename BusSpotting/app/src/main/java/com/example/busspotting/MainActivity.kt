package com.example.busspotting

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.busspotting.ui.theme.BusSpottingTheme
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BusSpottingTheme {

                var selectedCompany by remember { mutableStateOf<String?>(null) }
                var selectedModel by remember { mutableStateOf<String?>(null) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFF8DB624)
                ) { innerPadding ->

                    when {
                        selectedCompany == null -> {
                            CompanySelectScreen(
                                onCompanySelected = { selectedCompany = it },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        selectedModel == null -> {
                            BusModelScreen(
                                company = selectedCompany!!,
                                onBack = { selectedCompany = null },
                                onModelSelected = { selectedModel = it }
                            )
                        }

                        else -> {
                            PhotoScreen(
                                onBack = { selectedModel = null },
                                company = selectedCompany!!,
                                model = selectedModel!!
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CompanySelectScreen(
    onCompanySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("BusPhotos", Context.MODE_PRIVATE) }

    fun isCompanyCompleted(company: String): Boolean {
        val models = getModelsForCompany(company)
        return models.isNotEmpty() && models.all { model ->
            prefs.getString("photo_${company}_${model}", null) != null
        }
    }

    val companies = listOf("Stagecoach", "First Bus", "Diamond Bus", "Go-Ahead", "Morebus")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Select Your Bus Company",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(companies) { company ->
                CompanyButton(company, isCompanyCompleted(company), onCompanySelected)
            }
        }
    }
}

@Composable
fun CompanyButton(name: String, isCompleted: Boolean, onCompanySelected: (String) -> Unit) {
    Card(
        onClick = { onCompanySelected(name) },
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

fun getModelsForCompany(company: String): List<String> {
    return when (company) {
        "Stagecoach" -> listOf(
            "10001", "10008", "10009", "10431", "10432", "10433", "10434", "10435", "10436", "10437", 
            "10438", "10473", "10474", "10475", "10476", "10477", "10478", "10549", "10550", "10564", 
            "10568", "10569", "10570", "10571", "10572", "10617", "10626", "10627", "10628", "10629", 
            "10630", "10685", "10698", "10699", "10700", "10761", "10762", "10763", "10764", "10765", 
            "10766", "10767", "10768", "10769", "10770", "10771", "10772", "10773", "10774", "10775", 
            "10776", "10777", "10778", "10889", "10890", "10891", "10892", "10893", "10894", "26041", 
            "26042", "26043", "26044", "26045", "26046", "26047", "26048", "27621", "48203", "47865"
        )
        "First Bus" -> listOf(
            "30012", "30013", "30014", "30015", "30016", "30017", "30018", "30019", "30020", "30021", 
            "30022", "30023", "30024", "30025", "30026", "30103", "30104", "30105", "30106", "30107", 
            "30108", "30109", "30110", "30111", "30112", "30113", "30114", "30115", "30116", "30117", 
            "30118", "30119", "30120", "33312", "33314", "33315", "33316", "33317", "33318", "33319", 
            "33320", "33321", "65140", "65141", "65142", "65143", "65144", "65145", "65146", "65147", 
            "65148", "65149", "65150", "71060", "71061", "71062", "71063", "71064", "71065", "71066"
        )
        "Diamond Bus" -> listOf(
            "15004", "15005", "15006", "15007", "15008", "15009", "15010", "15011", "15012", "15013", 
            "15014", "15016", "15017", "15018", "15019", "15020", "15021", "15022", "15023", "15027", 
            "15028", "15030", "15031", "15032", "15033", "20014", "20016", "20050", "20051", "20053", 
            "20057", "20159", "30001", "30120", "30165", "30167", "30168", "30169", "30170", "30410"
        )
        "Go-Ahead" -> listOf(
            "46360", "46361", "46362", "46363", "46364", "46365" // Examples using new national system
        )
        "Morebus" -> listOf(
            "61101", "61686", "63828" // Examples using new national system
        )
        else -> emptyList()
    }
}

@Composable
fun BusModelScreen(
    company: String,
    onBack: () -> Unit,
    onModelSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("BusPhotos", Context.MODE_PRIVATE) }

    val models = getModelsForCompany(company)
    var searchQuery by remember { mutableStateOf("") }

    val filteredModels = models.filter { it.contains(searchQuery, ignoreCase = true) }

    val completedCount = models.count { model ->
        prefs.getString("photo_${company}_${model}", null) != null
    }
    val progress = if (models.isNotEmpty()) completedCount.toFloat() / models.size else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 16.dp)
        ) {
            Text("Back")
        }

        Text(
            text = "$company Models",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("Search models...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true
        )

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 8.dp)
        )

        Text(
            text = "Progress: $completedCount / ${models.size}",
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredModels) { model ->
                val isCompleted = prefs.getString("photo_${company}_${model}", null) != null
                ModelButton(model, isCompleted, onModelSelected)
            }
        }
    }
}

@Composable
fun ModelButton(name: String, isCompleted: Boolean, onModelSelected: (String) -> Unit) {
    Card(
        onClick = { onModelSelected(name) },
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PhotoScreen(
    onBack: () -> Unit,
    company: String,
    model: String
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("BusPhotos", Context.MODE_PRIVATE) }
    val prefKey = "photo_${company}_${model}"

    var imageUri by remember {
        mutableStateOf<Uri?>(prefs.getString(prefKey, null)?.let { Uri.parse(it) })
    }

    var tempUri by remember { mutableStateOf<Uri?>(null) }

    // CAMERA LAUNCHER
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = tempUri
            prefs.edit().putString(prefKey, imageUri.toString()).apply()
        }
    }

    // PERMISSION LAUNCHER
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            tempUri = uri
            cameraLauncher.launch(uri)
        }
    }

    // GALLERY PICKER
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val localUri = saveImageToInternalStorage(context, uri)
            if (localUri != null) {
                imageUri = localUri
                prefs.edit().putString(prefKey, localUri.toString()).apply()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 16.dp)
        ) {
            Text("Back")
        }

        Text(
            text = "Add a Photo for $company $model",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            },
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(60.dp)
        ) {
            Text("Take Photo")
        }

        Button(
            onClick = {
                galleryLauncher.launch("image/*")
            },
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(60.dp)
        ) {
            Text("Upload Photo")
        }

        if (imageUri != null) {
            Button(
                onClick = {
                    imageUri = null
                    prefs.edit().remove(prefKey).apply()
                },
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Remove Photo")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        imageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected photo",
                modifier = Modifier
                    .size(250.dp)
                    .padding(8.dp)
            )
        }
    }
}

fun createImageUri(context: Context): Uri {
    val dir = File(context.filesDir, "photos")
    if (!dir.exists()) dir.mkdirs()
    val file = File(dir, "photo_${System.currentTimeMillis()}.jpg")
    file.createNewFile()

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}

fun saveImageToInternalStorage(context: Context, uri: Uri): Uri? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val dir = File(context.filesDir, "photos")
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "photo_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
