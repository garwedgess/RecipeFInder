package eu.wedgess.recipefinder.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.wedgess.recipefinder.R

@Composable
fun ErrorSection(errorMessage: String?, onRetry: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("ErrorSectionTag"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
                .testTag("ErrorMessageTag"),
            textAlign = TextAlign.Center,
            text = errorMessage ?: stringResource(R.string.error_msg_unknown),
            style = MaterialTheme.typography.h6.copy(color = Color.Red)
        )
        if (onRetry != null) {
            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
                    .testTag("ErrorRetryButtonTag"),
                onClick = { onRetry() }
            ) {
                Text(stringResource(R.string.btn_retry))
            }
        }
    }
}