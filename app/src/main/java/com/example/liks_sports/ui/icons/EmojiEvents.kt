package com.example.liks_sports.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val EmojiEvents: ImageVector
  get() {
    if (_EmojiEvents != null) {
      return _EmojiEvents!!
    }
    _EmojiEvents =
      ImageVector.Builder(
          name = "emoji_events",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(7f, 21f)
            verticalLineTo(19f)
            horizontalLineToRelative(4f)
            verticalLineTo(15.9f)
            quadTo(9.78f, 15.63f, 8.81f, 14.86f)
            reflectiveQuadTo(7.4f, 12.95f)
            quadTo(5.53f, 12.73f, 4.26f, 11.31f)
            reflectiveQuadTo(3f, 8f)
            verticalLineTo(7f)
            quadTo(3f, 6.18f, 3.59f, 5.59f)
            reflectiveQuadTo(5f, 5f)
            horizontalLineTo(7f)
            verticalLineTo(3f)
            horizontalLineTo(17f)
            verticalLineTo(5f)
            horizontalLineToRelative(2f)
            quadToRelative(0.83f, 0f, 1.41f, 0.59f)
            quadTo(21f, 6.18f, 21f, 7f)
            verticalLineTo(8f)
            quadToRelative(0f, 1.9f, -1.26f, 3.31f)
            reflectiveQuadTo(16.6f, 12.95f)
            quadToRelative(-0.45f, 1.15f, -1.41f, 1.91f)
            quadTo(14.23f, 15.63f, 13f, 15.9f)
            verticalLineTo(19f)
            horizontalLineToRelative(4f)
            verticalLineToRelative(2f)
            horizontalLineTo(7f)
            close()
            moveTo(7f, 10.8f)
            verticalLineTo(7f)
            horizontalLineTo(5f)
            verticalLineTo(8f)
            quadTo(5f, 8.95f, 5.55f, 9.71f)
            quadTo(6.1f, 10.48f, 7f, 10.8f)
            close()
            moveToRelative(7.13f, 2.33f)
            quadTo(15f, 12.25f, 15f, 11f)
            verticalLineTo(5f)
            horizontalLineTo(9f)
            verticalLineToRelative(6f)
            quadToRelative(0f, 1.25f, 0.88f, 2.13f)
            reflectiveQuadTo(12f, 14f)
            reflectiveQuadToRelative(2.13f, -0.88f)
            close()
            moveTo(17f, 10.8f)
            quadToRelative(0.9f, -0.32f, 1.45f, -1.09f)
            reflectiveQuadTo(19f, 8f)
            verticalLineTo(7f)
            horizontalLineTo(17f)
            verticalLineToRelative(3.8f)
            close()
            moveTo(12f, 9.5f)
            close()
          }
        }
        .build()
    return _EmojiEvents!!
  }

private var _EmojiEvents: ImageVector? = null
