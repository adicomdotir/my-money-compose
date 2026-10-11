package ir.adicom.samples

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import ir.adicom.mymoney.ui.components.AppIconButton
import ir.adicom.mymoney.ui.components.AppIconButtonSize
import ir.adicom.mymoney.ui.components.AppIconButtonVariant

@Composable
fun AppIconButtonSamples() {
    // Simple delete icon
    AppIconButton(
        icon = Icons.Default.Delete,
        contentDescription = "Delete",
        tint = MaterialTheme.colorScheme.error,
        onClick = {  }
    )

    // Tonal "Add" button in a toolbar
    AppIconButton(
        icon = Icons.Outlined.Add,
        contentDescription = "Add",
        variant = AppIconButtonVariant.Tonal,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        tint = MaterialTheme.colorScheme.onPrimaryContainer,
        onClick = {  }
    )

    // Outlined, small, in a row of actions
    AppIconButton(
        icon = Icons.Outlined.Edit,
        contentDescription = "Edit",
        variant = AppIconButtonVariant.Outlined,
        size = AppIconButtonSize.Small,
        onClick = {  }
    )

    // Disabled
    AppIconButton(
        icon = Icons.Outlined.Share,
        contentDescription = "Share",
        enabled = false,
        onClick = {}
    )
}