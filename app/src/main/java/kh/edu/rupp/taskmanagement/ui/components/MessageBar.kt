package kh.edu.rupp.taskmanagement.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import kh.edu.rupp.taskmanagement.R

// nothing here can count the seconds yet, so the bar stays until the user dismisses it
@Composable
fun MessageBar(
    message: Int?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (message != null) {
        Snackbar(
            modifier = modifier.padding(dimensionResource(R.dimen.list_padding)),
            action = {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = SnackbarDefaults.actionColor
                    )
                ) {
                    Text(stringResource(R.string.dismiss))
                }
            }
        ) {
            Text(stringResource(message))
        }
    }
}
