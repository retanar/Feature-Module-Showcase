package com.featuremodule.homeImpl.barcode

import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
internal fun BarcodeResultScreen(barcode: String) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        SelectionContainer {
            Text(text = barcode)
        }

        Button(
            onClick = {
                clipboard.setText(AnnotatedString(barcode))
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                    Toast.makeText(context, "Copied $barcode", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.defaultMinSize(minWidth = 100.dp),
        ) {
            Text(text = "Copy")
        }

        Button(
            onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, barcode)
                }
                context.startActivity(Intent.createChooser(shareIntent, null))
            },
            modifier = Modifier.defaultMinSize(minWidth = 100.dp),
        ) {
            Text(text = "Share")
        }
    }
}
