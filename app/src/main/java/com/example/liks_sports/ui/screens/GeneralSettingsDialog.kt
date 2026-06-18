package com.example.liks_sports.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.liks_sports.R
import com.example.liks_sports.data.SettingsStore.Language
import com.example.liks_sports.data.SettingsStore.ThemePalette
import com.example.liks_sports.ui.icons.Close
import com.example.liks_sports.ui.icons.Settings

@Composable
fun GeneralSettingsDialog(
    currentPalette: ThemePalette,
    currentLanguage: Language,
    onSave: (ThemePalette, Language) -> Unit,
    onDismiss: () -> Unit,
) {
    var palette by remember { mutableStateOf(currentPalette) }
    var language by remember { mutableStateOf(currentLanguage) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .heightIn(max = 620.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                GeneralSettingsHeader(onDismiss = onDismiss)

                HorizontalDivider()

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                ) {
                    SectionLabel(text = stringResource(R.string.theme_section))
                    Spacer(modifier = Modifier.height(10.dp))
                    PaletteCard(
                        title = stringResource(R.string.theme_palette_material_you),
                        description = stringResource(R.string.theme_palette_material_you_desc),
                        swatches = listOf(
                            Color(0xFF6650a4),
                            Color(0xFF625b71),
                            Color(0xFF7D5260),
                        ),
                        selected = palette == ThemePalette.MATERIAL_YOU,
                        onClick = { palette = ThemePalette.MATERIAL_YOU },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PaletteCard(
                        title = stringResource(R.string.theme_palette_kova),
                        description = stringResource(R.string.theme_palette_kova_desc),
                        swatches = listOf(
                            Color(0xFF04342C),
                            Color(0xFF1D9E75),
                            Color(0xFF9FE1CB),
                            Color(0xFFEF9F27),
                        ),
                        selected = palette == ThemePalette.KOVA,
                        onClick = { palette = ThemePalette.KOVA },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PaletteCard(
                        title = stringResource(R.string.theme_palette_liks),
                        description = stringResource(R.string.theme_palette_liks_desc),
                        swatches = listOf(
                            Color(0xFF1565C0),
                            Color(0xFF2E7D32),
                            Color(0xFFF9A825),
                            Color(0xFFC62828),
                        ),
                        selected = palette == ThemePalette.LIKS,
                        onClick = { palette = ThemePalette.LIKS },
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(20.dp))

                    SectionLabel(text = stringResource(R.string.language_section))
                    Spacer(modifier = Modifier.height(10.dp))
                    LanguageRow(
                        title = stringResource(R.string.language_system),
                        description = stringResource(R.string.language_system_desc),
                        selected = language == Language.SYSTEM,
                        onClick = { language = Language.SYSTEM },
                    )
                    LanguageRow(
                        title = stringResource(R.string.language_english),
                        description = stringResource(R.string.language_english_desc),
                        selected = language == Language.ENGLISH,
                        onClick = { language = Language.ENGLISH },
                    )
                    LanguageRow(
                        title = stringResource(R.string.language_spanish),
                        description = stringResource(R.string.language_spanish_desc),
                        selected = language == Language.SPANISH,
                        onClick = { language = Language.SPANISH },
                    )
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSave(palette, language)
                        }
                    ) { Text(stringResource(R.string.save)) }
                }
            }
        }
    }
}

@Composable
private fun GeneralSettingsHeader(onDismiss: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = CircleShape,
            modifier = Modifier.size(40.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.general_settings_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(R.string.general_settings_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Close,
                contentDescription = stringResource(R.string.general_settings_close_desc),
            )
        }
    }
}

@Composable
private fun PaletteCard(
    title: String,
    description: String,
    swatches: List<Color>,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val container = if (selected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant
    val onContainer = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurfaceVariant
    val border = if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outlineVariant
    Surface(
        color = container,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, border),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selected) onContainer else MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = onContainer,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    swatches.forEach { color ->
                        Surface(
                            color = color,
                            shape = CircleShape,
                            modifier = Modifier.size(20.dp),
                        ) {}
                    }
                }
            }
            RadioButton(
                selected = selected,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun LanguageRow(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        color = if (selected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            1.dp,
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            RadioButton(
                selected = selected,
                onClick = onClick,
            )
        }
    }
}
