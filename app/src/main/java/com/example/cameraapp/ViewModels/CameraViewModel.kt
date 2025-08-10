package com.example.cameraapp.ViewModels

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.cameraapp.Models.ImageAnalysisOption
import com.example.cameraapp.R
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import androidx.core.net.toUri
import com.example.cameraapp.ImageHelper
import com.example.cameraapp.Models.EfficientDetHelper
import org.tensorflow.lite.task.vision.detector.Detection

@HiltViewModel
class CameraViewModel @Inject constructor():ViewModel(){
    var shouldCleanUp = mutableStateOf(false)
    var selectedMode by mutableStateOf(ImageAnalysisOption.NORMAL)
    private var imageCapture:ImageCapture? = null
    private val _capturedImageUri = mutableStateOf<Uri?>(null)
    val capturedImageUri: State<Uri?> = _capturedImageUri
    var capturedImageBitmap:Bitmap? = null
    private var shutterAnimating by mutableStateOf(false)
    var currentFrame by mutableStateOf(0)
    val frames = listOf(
        R.drawable.cam_open,
        R.drawable.camera_mid,
        R.drawable.camera_close,
        R.drawable.camera_mid,
        R.drawable.cam_open
    )

    fun pickSelectedMode(model: ImageAnalysisOption){
        this.selectedMode = model
    }
    fun resetSelectedMode(){
        this.selectedMode = ImageAnalysisOption.NORMAL
    }

    fun setImageCapture(context: Context,imageCapture: ImageCapture){
        this.imageCapture = imageCapture
    }

    fun setUploadedImageUri(context: Context,uri: Uri){
        val realFile = if (uri.scheme == "content") {
            // Copy content:// to temp file
            val tempFile = File.createTempFile("uploaded_", ".jpg", context.cacheDir)
            context.contentResolver.openInputStream(uri)?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } else {
            File(uri.path!!) // Already a file:// from camera
        }
        _capturedImageUri.value = Uri.fromFile(realFile)
        val bitmap = BitmapFactory.decodeFile(realFile.path)
        Toast.makeText(context,"bitmap : $bitmap",Toast.LENGTH_LONG).show()
        capturedImageBitmap = bitmap
    }

    fun uploadedUriToBitmap(context: Context,uri: Uri):Bitmap?{
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    fun captureImage(context:Context,onImageCaptured:(Uri)->Unit){
        val photoFile = File(context.cacheDir,"${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object :ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    _capturedImageUri.value = savedUri
                    val bitmap = uriToBitmap(context,savedUri)
                    capturedImageBitmap = bitmap
                    onImageCaptured(savedUri)
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(context, "Capture failed: ${exc.message}", Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    fun saveToGallery(context: Context,uri:Uri){
//        val resolver = context.contentResolver
//        val inputStream = resolver.openInputStream(uri)?:return
//        val filename = "neoscope_${System.currentTimeMillis()}.jpg"
//
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME,filename)
//            put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")
//            put(MediaStore.Images.Media.RELATIVE_PATH,Environment.DIRECTORY_PICTURES)
//        }
//
//        val outputUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)
//        outputUri?.let {
//            resolver.openOutputStream(it)?.use { outputStream->
//                inputStream.copyTo(outputStream)
//            }
//        }
//
//        inputStream.close()
//        Toast.makeText(context,"Image saved to device",Toast.LENGTH_SHORT).show()
        ImageHelper.saveToGallery(context,uri)
    }

    fun discardImage(uri:Uri){
//        File(uri.path!!).delete()
//        _capturedImageUri.value = null
//        capturedImageBitmap = null
        _capturedImageUri.value = null
        capturedImageBitmap = null
        File(uri.path!!).delete()
    }

    fun animateShutter(scope:CoroutineScope){
        if(!shutterAnimating){
            shutterAnimating = true
            scope.launch {
                for (i in frames.indices){
                    currentFrame = i
                    delay(20L)
                }
            }
            shutterAnimating = false
        }
    }


    fun uriToBitmap(context: Context,uri:Uri):Bitmap?{
        return try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                val source = ImageDecoder.createSource(context.contentResolver,uri)
                ImageDecoder.decodeBitmap(source)
            }
            else{
                MediaStore.Images.Media.getBitmap(context.contentResolver,uri)
            }
        } catch (e:Exception){
            e.printStackTrace()
            null
        }
    }


    fun processTextRecognition(
        bitmap: Bitmap,
        onResult:(String)->Unit,
        onError: (Exception)->Unit
    ){
        val image = InputImage.fromBitmap(bitmap,0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText->
                val fullText = visionText.text
                onResult(fullText)
            }
            .addOnFailureListener{e->
                onError(e)
            }
    }


    fun processQRCode(
        bitmap: Bitmap,
        onResult: (List<Barcode>) -> Unit,
        onError: (Exception) -> Unit
    ){
        val image = InputImage.fromBitmap(bitmap,0)
        val scanner = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build()
        )

        scanner.process(image)
            .addOnSuccessListener{barcodes->
                 onResult(barcodes)
            }
            .addOnFailureListener{e->
                onError(e)
            }
    }


    fun processObjectDetection(
        bitmap: Bitmap,
        onResult: (List<DetectedObject>) -> Unit,
        onError: (Exception) -> Unit
    ){
        val image = InputImage.fromBitmap(bitmap,0)
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()

        val detector = ObjectDetection.getClient(options)

        detector.process(image)
            .addOnSuccessListener { detectedObjects->
                onResult(detectedObjects)
            }
            .addOnFailureListener{e->
                onError(e)
            }
    }

    val languages = listOf(
        "Afrikaans",
        "Albanian",
        "Arabic",
        "Belarusian",
        "Bulgarian",
        "Bengali",
        "Catalan",
        "Chinese",
        "Croatian",
        "Czech",
        "Danish",
        "Dutch",
        "English",
        "Esperanto",
        "Estonian",
        "Finnish",
        "French",
        "Galician",
        "Georgian",
        "German",
        "Greek",
        "Gujarati",
        "Haitian Creole",
        "Hebrew",
        "Hindi",
        "Hungarian",
        "Icelandic",
        "Indonesian",
        "Irish",
        "Italian",
        "Japanese",
        "Kannada",
        "Korean",
        "Lithuanian",
        "Latvian",
        "Macedonian",
        "Marathi",
        "Malay",
        "Maltese",
        "Norwegian",
        "Persian",
        "Polish",
        "Portuguese",
        "Romanian",
        "Russian",
        "Slovak",
        "Slovenian",
        "Spanish",
        "Swedish",
        "Swahili",
        "Tagalog",
        "Tamil",
        "Telugu",
        "Thai",
        "Turkish",
        "Ukrainian",
        "Urdu",
        "Vietnamese",
        "Welsh"
    )

    val languageMap = mapOf(
        "Afrikaans" to TranslateLanguage.AFRIKAANS,
        "Albanian" to TranslateLanguage.ALBANIAN,
        "Arabic" to TranslateLanguage.ARABIC,
        "Belarusian" to TranslateLanguage.BELARUSIAN,
        "Bulgarian" to TranslateLanguage.BULGARIAN,
        "Bengali" to TranslateLanguage.BENGALI,
        "Catalan" to TranslateLanguage.CATALAN,
        "Chinese" to TranslateLanguage.CHINESE,
        "Croatian" to TranslateLanguage.CROATIAN,
        "Czech" to TranslateLanguage.CZECH,
        "Danish" to TranslateLanguage.DANISH,
        "Dutch" to TranslateLanguage.DUTCH,
        "English" to TranslateLanguage.ENGLISH,
        "Esperanto" to TranslateLanguage.ESPERANTO,
        "Estonian" to TranslateLanguage.ESTONIAN,
        "Finnish" to TranslateLanguage.FINNISH,
        "French" to TranslateLanguage.FRENCH,
        "Galician" to TranslateLanguage.GALICIAN,
        "Georgian" to TranslateLanguage.GEORGIAN,
        "German" to TranslateLanguage.GERMAN,
        "Greek" to TranslateLanguage.GREEK,
        "Gujarati" to TranslateLanguage.GUJARATI,
        "Haitian Creole" to TranslateLanguage.HAITIAN_CREOLE,
        "Hebrew" to TranslateLanguage.HEBREW,
        "Hindi" to TranslateLanguage.HINDI,
        "Hungarian" to TranslateLanguage.HUNGARIAN,
        "Icelandic" to TranslateLanguage.ICELANDIC,
        "Indonesian" to TranslateLanguage.INDONESIAN,
        "Irish" to TranslateLanguage.IRISH,
        "Italian" to TranslateLanguage.ITALIAN,
        "Japanese" to TranslateLanguage.JAPANESE,
        "Kannada" to TranslateLanguage.KANNADA,
        "Korean" to TranslateLanguage.KOREAN,
        "Lithuanian" to TranslateLanguage.LITHUANIAN,
        "Latvian" to TranslateLanguage.LATVIAN,
        "Macedonian" to TranslateLanguage.MACEDONIAN,
        "Marathi" to TranslateLanguage.MARATHI,
        "Malay" to TranslateLanguage.MALAY,
        "Maltese" to TranslateLanguage.MALTESE,
        "Norwegian" to TranslateLanguage.NORWEGIAN,
        "Persian" to TranslateLanguage.PERSIAN,
        "Polish" to TranslateLanguage.POLISH,
        "Portuguese" to TranslateLanguage.PORTUGUESE,
        "Romanian" to TranslateLanguage.ROMANIAN,
        "Russian" to TranslateLanguage.RUSSIAN,
        "Slovak" to TranslateLanguage.SLOVAK,
        "Slovenian" to TranslateLanguage.SLOVENIAN,
        "Spanish" to TranslateLanguage.SPANISH,
        "Swedish" to TranslateLanguage.SWEDISH,
        "Swahili" to TranslateLanguage.SWAHILI,
        "Tagalog" to TranslateLanguage.TAGALOG,
        "Tamil" to TranslateLanguage.TAMIL,
        "Telugu" to TranslateLanguage.TELUGU,
        "Thai" to TranslateLanguage.THAI,
        "Turkish" to TranslateLanguage.TURKISH,
        "Ukrainian" to TranslateLanguage.UKRAINIAN,
        "Urdu" to TranslateLanguage.URDU,
        "Vietnamese" to TranslateLanguage.VIETNAMESE,
        "Welsh" to TranslateLanguage.WELSH
    )

//    val reversedLanguageMap = languageMap.entries.associate { (name, code) -> code to name }
    val codeToNameMap = mapOf(
    "af" to "Afrikaans",
    "sq" to "Albanian",
    "ar" to "Arabic",
    "be" to "Belarusian",
    "bg" to "Bulgarian",
    "bn" to "Bengali",
    "ca" to "Catalan",
    "zh" to "Chinese",
    "hr" to "Croatian",
    "cs" to "Czech",
    "da" to "Danish",
    "nl" to "Dutch",
    "en" to "English",
    "eo" to "Esperanto",
    "et" to "Estonian",
    "fi" to "Finnish",
    "fr" to "French",
    "gl" to "Galician",
    "ka" to "Georgian",
    "de" to "German",
    "el" to "Greek",
    "gu" to "Gujarati",
    "ht" to "Haitian Creole",
    "he" to "Hebrew",
    "hi" to "Hindi",
    "hu" to "Hungarian",
    "is" to "Icelandic",
    "id" to "Indonesian",
    "ga" to "Irish",
    "it" to "Italian",
    "ja" to "Japanese",
    "kn" to "Kannada",
    "ko" to "Korean",
    "lt" to "Lithuanian",
    "lv" to "Latvian",
    "mk" to "Macedonian",
    "mr" to "Marathi",
    "ms" to "Malay",
    "mt" to "Maltese",
    "no" to "Norwegian",
    "fa" to "Persian",
    "pl" to "Polish",
    "pt" to "Portuguese",
    "ro" to "Romanian",
    "ru" to "Russian",
    "sk" to "Slovak",
    "sl" to "Slovenian",
    "es" to "Spanish",
    "sv" to "Swedish",
    "sw" to "Swahili",
    "tl" to "Tagalog",
    "ta" to "Tamil",
    "te" to "Telugu",
    "th" to "Thai",
    "tr" to "Turkish",
    "uk" to "Ukrainian",
    "ur" to "Urdu",
    "vi" to "Vietnamese",
    "cy" to "Welsh"
)


    fun processTranslation(
        bitmap: Bitmap,
        sourceLang:String = TranslateLanguage.ENGLISH,
        targetLang:String = TranslateLanguage.HINDI,
        onResult: (String,String) -> Unit,
        onError: (Exception) -> Unit
    ){
        val image = InputImage.fromBitmap(bitmap,0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText->
                val textToTranslate = visionText.text
                Log.d("Checking translate input","$textToTranslate")

                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(sourceLang)
                    .setTargetLanguage(targetLang)
                    .build()

                val translator = Translation.getClient(options)

                translator.downloadModelIfNeeded()
                    .addOnSuccessListener {
                        translator.translate(textToTranslate)
                            .addOnSuccessListener{translatedText->
                                onResult(textToTranslate,translatedText)
                                Log.d("Checking translate input","$translatedText")

                            }
                            .addOnFailureListener{e->
                                onError(e)
                            }
                    }
                    .addOnFailureListener { e->
                        onError(e)
                    }
            }
            .addOnFailureListener{e->
                onError(e)
            }

    }


    fun openQRContent(context: Context, qrText: String) {
        try {
            when {
                qrText.startsWith("http://") || qrText.startsWith("https://") -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(qrText))
                    context.startActivity(intent)
                }

                qrText.startsWith("tel:") -> {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse(qrText))
                    context.startActivity(intent)
                }

                qrText.startsWith("sms:") -> {
                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(qrText))
                    context.startActivity(intent)
                }

                qrText.startsWith("mailto:") -> {
                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(qrText))
                    context.startActivity(intent)
                }

                qrText.startsWith("geo:") -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(qrText))
                    context.startActivity(intent)
                }

                qrText.startsWith("upi:") -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(qrText))
                    context.startActivity(intent)
                }

                qrText.startsWith("whatsapp:") ||
                        qrText.startsWith("intent:") ||
                        qrText.contains("://") -> {
                    // Try opening app deep links or custom schemes
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(qrText))
                    context.startActivity(intent)
                }

                qrText.startsWith("BEGIN:VCARD") -> {
                    Toast.makeText(context, "Detected contact info (vCard).", Toast.LENGTH_LONG).show()
                    // Optionally parse and show contact info here
                }

                qrText.startsWith("WIFI:") -> {
                    Toast.makeText(context, "WiFi QR detected. Custom connection not supported via intent.", Toast.LENGTH_LONG).show()
                }

                else -> {
                    // Fallback: treat as plain text
                    Toast.makeText(context, "Content: $qrText", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Can't handle this QR content", Toast.LENGTH_SHORT).show()
        }
    }


    var detector:EfficientDetHelper? = null

    fun detect(context: Context,bitmap: Bitmap):List<Detection>{
        if(detector==null){
            detector = EfficientDetHelper(context)
        }
        val argbBitmap = if (bitmap.config != Bitmap.Config.ARGB_8888) {
            bitmap.copy(Bitmap.Config.ARGB_8888, true)
        } else {
            bitmap
        }
        val results = detector?.detect(argbBitmap)
        return results?: emptyList()

    }



}