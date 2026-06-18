package com.example.liks_sports.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val PlayArrow: ImageVector
  get() {
    if (_PlayArrow != null) return _PlayArrow!!
    _PlayArrow =
      ImageVector.Builder(
          name = "play_arrow",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(8f, 19f)
            verticalLineTo(5f)
            lineToRelative(11f, 7f)
            lineTo(8f, 19f)
            close()
            moveToRelative(2f, -7f)
            close()
            moveToRelative(0f, 3.35f)
            lineTo(15.25f, 12f)
            lineTo(10f, 8.65f)
            verticalLineToRelative(6.7f)
            close()
          }
        }
        .build()
    return _PlayArrow!!
  }

private var _PlayArrow: ImageVector? = null

val Pause: ImageVector
  get() {
    if (_Pause != null) return _Pause!!
    _Pause =
      ImageVector.Builder(
          name = "pause",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(13f, 19f)
            verticalLineTo(5f)
            horizontalLineToRelative(6f)
            verticalLineTo(19f)
            horizontalLineTo(13f)
            close()
            moveTo(5f, 19f)
            verticalLineTo(5f)
            horizontalLineToRelative(6f)
            verticalLineTo(19f)
            horizontalLineTo(5f)
            close()
            moveTo(15f, 17f)
            horizontalLineToRelative(2f)
            verticalLineTo(7f)
            horizontalLineTo(15f)
            verticalLineTo(17f)
            close()
            moveTo(7f, 17f)
            horizontalLineTo(9f)
            verticalLineTo(7f)
            horizontalLineTo(7f)
            verticalLineTo(17f)
            close()
          }
        }
        .build()
    return _Pause!!
  }

private var _Pause: ImageVector? = null

val SkipNext: ImageVector
  get() {
    if (_SkipNext != null) return _SkipNext!!
    _SkipNext =
      ImageVector.Builder(
          name = "skip_next",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(16.5f, 18f)
            verticalLineTo(6f)
            horizontalLineToRelative(2f)
            verticalLineTo(18f)
            horizontalLineToRelative(-2f)
            close()
            moveToRelative(-11f, 0f)
            verticalLineTo(6f)
            lineToRelative(9f, 6f)
            lineToRelative(-9f, 6f)
            close()
            moveToRelative(2f, -6f)
            close()
            moveToRelative(0f, 2.25f)
            lineTo(10.9f, 12f)
            lineTo(7.5f, 9.75f)
            verticalLineToRelative(4.5f)
            close()
          }
        }
        .build()
    return _SkipNext!!
  }

private var _SkipNext: ImageVector? = null

val Stop: ImageVector
  get() {
    if (_Stop != null) return _Stop!!
    _Stop =
      ImageVector.Builder(
          name = "stop",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(6f, 18f)
            verticalLineTo(6f)
            horizontalLineTo(18f)
            verticalLineTo(18f)
            horizontalLineTo(6f)
            close()
            moveTo(8f, 16f)
            horizontalLineToRelative(8f)
            verticalLineTo(8f)
            horizontalLineTo(8f)
            verticalLineToRelative(8f)
            close()
          }
        }
        .build()
    return _Stop!!
  }

private var _Stop: ImageVector? = null

val ArrowBack: ImageVector
  get() {
    if (_ArrowBack != null) return _ArrowBack!!
    _ArrowBack =
      ImageVector.Builder(
          name = "arrow_back",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(7.83f, 13f)
            lineToRelative(5.6f, 5.6f)
            lineTo(12f, 20f)
            lineTo(4f, 12f)
            lineTo(12f, 4f)
            lineToRelative(1.43f, 1.4f)
            lineTo(7.83f, 11f)
            horizontalLineTo(20f)
            verticalLineToRelative(2f)
            horizontalLineTo(7.83f)
            close()
          }
        }
        .build()
    return _ArrowBack!!
  }

private var _ArrowBack: ImageVector? = null

val FitnessCenter: ImageVector
  get() {
    if (_FitnessCenter != null) return _FitnessCenter!!
    _FitnessCenter =
      ImageVector.Builder(
          name = "fitness_center",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(13.4f, 21.9f)
            lineTo(12f, 20.5f)
            lineToRelative(3.55f, -3.55f)
            lineTo(7.05f, 8.45f)
            lineTo(3.5f, 12f)
            lineTo(2.1f, 10.6f)
            lineTo(3.5f, 9.15f)
            lineTo(2.1f, 7.75f)
            lineTo(4.2f, 5.65f)
            lineTo(2.8f, 4.2f)
            lineTo(4.2f, 2.8f)
            lineTo(5.65f, 4.2f)
            lineTo(7.75f, 2.1f)
            lineToRelative(1.4f, 1.4f)
            lineTo(10.6f, 2.1f)
            lineTo(12f, 3.5f)
            lineTo(8.45f, 7.05f)
            lineToRelative(8.5f, 8.5f)
            lineTo(20.5f, 12f)
            lineToRelative(1.4f, 1.4f)
            lineToRelative(-1.4f, 1.45f)
            lineToRelative(1.4f, 1.4f)
            lineToRelative(-2.1f, 2.1f)
            lineToRelative(1.4f, 1.45f)
            lineToRelative(-1.4f, 1.4f)
            lineTo(18.35f, 19.8f)
            lineToRelative(-2.1f, 2.1f)
            lineToRelative(-1.4f, -1.4f)
            lineTo(13.4f, 21.9f)
            close()
          }
        }
        .build()
    return _FitnessCenter!!
  }

private var _FitnessCenter: ImageVector? = null

val Timer: ImageVector
  get() {
    if (_Timer != null) return _Timer!!
    _Timer =
      ImageVector.Builder(
          name = "timer",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(9f, 3f)
            verticalLineTo(1f)
            horizontalLineToRelative(6f)
            verticalLineTo(3f)
            horizontalLineTo(9f)
            close()
            moveToRelative(2f, 11f)
            horizontalLineToRelative(2f)
            verticalLineTo(8f)
            horizontalLineTo(11f)
            verticalLineToRelative(6f)
            close()
            moveTo(8.51f, 21.29f)
            quadTo(6.88f, 20.58f, 5.65f, 19.35f)
            reflectiveQuadTo(3.71f, 16.49f)
            reflectiveQuadTo(3f, 13f)
            reflectiveQuadTo(3.71f, 9.51f)
            reflectiveQuadTo(5.65f, 6.65f)
            quadTo(6.88f, 5.43f, 8.51f, 4.71f)
            reflectiveQuadTo(12f, 4f)
            quadToRelative(1.55f, 0f, 2.98f, 0.5f)
            reflectiveQuadToRelative(2.68f, 1.45f)
            lineToRelative(1.4f, -1.4f)
            lineToRelative(1.4f, 1.4f)
            lineToRelative(-1.4f, 1.4f)
            quadTo(20f, 8.6f, 20.5f, 10.02f)
            reflectiveQuadTo(21f, 13f)
            quadToRelative(0f, 1.85f, -0.71f, 3.49f)
            reflectiveQuadToRelative(-1.94f, 2.86f)
            reflectiveQuadToRelative(-2.86f, 1.94f)
            reflectiveQuadTo(12f, 22f)
            reflectiveQuadTo(8.51f, 21.29f)
            close()
            moveToRelative(8.44f, -3.34f)
            quadTo(19f, 15.9f, 19f, 13f)
            reflectiveQuadTo(16.95f, 8.05f)
            reflectiveQuadTo(12f, 6f)
            reflectiveQuadTo(7.05f, 8.05f)
            reflectiveQuadTo(5f, 13f)
            reflectiveQuadToRelative(2.05f, 4.95f)
            reflectiveQuadTo(12f, 20f)
            reflectiveQuadToRelative(4.95f, -2.05f)
            close()
          }
        }
        .build()
    return _Timer!!
  }

private var _Timer: ImageVector? = null

val CheckCircle: ImageVector
  get() {
    if (_CheckCircle != null) return _CheckCircle!!
    _CheckCircle =
      ImageVector.Builder(
          name = "check_circle",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(10.6f, 16.6f)
            lineTo(17.65f, 9.55f)
            lineToRelative(-1.4f, -1.4f)
            lineTo(10.6f, 13.8f)
            lineTo(7.75f, 10.95f)
            lineToRelative(-1.4f, 1.4f)
            lineTo(10.6f, 16.6f)
            close()
            moveTo(12f, 22f)
            quadTo(9.93f, 22f, 8.1f, 21.21f)
            quadTo(6.28f, 20.43f, 4.93f, 19.08f)
            quadTo(3.58f, 17.73f, 2.79f, 15.9f)
            reflectiveQuadTo(2f, 12f)
            quadTo(2f, 9.92f, 2.79f, 8.1f)
            quadTo(3.58f, 6.27f, 4.93f, 4.93f)
            quadTo(6.28f, 3.57f, 8.1f, 2.79f)
            quadTo(9.93f, 2f, 12f, 2f)
            reflectiveQuadToRelative(3.9f, 0.79f)
            reflectiveQuadToRelative(3.17f, 2.14f)
            quadToRelative(1.35f, 1.35f, 2.14f, 3.17f)
            quadTo(22f, 9.92f, 22f, 12f)
            reflectiveQuadToRelative(-0.79f, 3.9f)
            reflectiveQuadToRelative(-2.14f, 3.17f)
            quadToRelative(-1.35f, 1.35f, -3.17f, 2.14f)
            reflectiveQuadTo(12f, 22f)
            close()
            moveToRelative(0f, -2f)
            quadToRelative(3.35f, 0f, 5.68f, -2.32f)
            reflectiveQuadTo(20f, 12f)
            reflectiveQuadTo(17.68f, 6.32f)
            reflectiveQuadTo(12f, 4f)
            reflectiveQuadTo(6.33f, 6.32f)
            reflectiveQuadTo(4f, 12f)
            reflectiveQuadToRelative(2.33f, 5.68f)
            reflectiveQuadTo(12f, 20f)
            close()
          }
        }
        .build()
    return _CheckCircle!!
  }

private var _CheckCircle: ImageVector? = null

val Edit: ImageVector
  get() {
    if (_Edit != null) return _Edit!!
    _Edit =
      ImageVector.Builder(
          name = "edit",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(5f, 19f)
            horizontalLineTo(6.43f)
            lineTo(16.2f, 9.23f)
            lineTo(14.78f, 7.8f)
            lineTo(5f, 17.58f)
            verticalLineTo(19f)
            close()
            moveTo(3f, 21f)
            verticalLineTo(16.75f)
            lineTo(16.2f, 3.57f)
            quadTo(16.5f, 3.3f, 16.86f, 3.15f)
            reflectiveQuadTo(17.63f, 3f)
            quadToRelative(0.4f, 0f, 0.78f, 0.15f)
            reflectiveQuadTo(19.05f, 3.6f)
            lineTo(20.43f, 5f)
            quadToRelative(0.3f, 0.27f, 0.44f, 0.65f)
            reflectiveQuadTo(21f, 6.4f)
            quadToRelative(0f, 0.4f, -0.14f, 0.76f)
            reflectiveQuadTo(20.43f, 7.82f)
            lineTo(7.25f, 21f)
            horizontalLineTo(3f)
            close()
          }
        }
        .build()
    return _Edit!!
  }

private var _Edit: ImageVector? = null

val ExpandLess: ImageVector
  get() {
    if (_ExpandLess != null) return _ExpandLess!!
    _ExpandLess =
      ImageVector.Builder(
          name = "expand_less",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(7.4f, 15.38f)
            lineTo(6f, 13.98f)
            lineToRelative(6f, -6f)
            lineToRelative(6f, 6f)
            lineToRelative(-1.4f, 1.4f)
            lineTo(12f, 10.77f)
            lineToRelative(-4.6f, 4.6f)
            close()
          }
        }
        .build()
    return _ExpandLess!!
  }

private var _ExpandLess: ImageVector? = null

val ExpandMore: ImageVector
  get() {
    if (_ExpandMore != null) return _ExpandMore!!
    _ExpandMore =
      ImageVector.Builder(
          name = "expand_more",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(12f, 15.38f)
            lineToRelative(-6f, -6f)
            lineTo(7.4f, 7.97f)
            lineToRelative(4.6f, 4.6f)
            lineToRelative(4.6f, -4.6f)
            lineTo(18f, 9.38f)
            lineToRelative(-6f, 6f)
            close()
          }
        }
        .build()
    return _ExpandMore!!
  }

private var _ExpandMore: ImageVector? = null

val Check: ImageVector
  get() {
    if (_Check != null) return _Check!!
    _Check =
      ImageVector.Builder(
          name = "check",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(9.55f, 18f)
            lineTo(3.85f, 12.3f)
            lineTo(5.28f, 10.88f)
            lineToRelative(4.28f, 4.28f)
            lineTo(18.73f, 5.97f)
            lineTo(20.15f, 7.4f)
            lineTo(9.55f, 18f)
            close()
          }
        }
        .build()
    return _Check!!
  }

private var _Check: ImageVector? = null

val Close: ImageVector
  get() {
    if (_Close != null) return _Close!!
    _Close =
      ImageVector.Builder(
          name = "close",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(6.4f, 19f)
            lineTo(5f, 17.6f)
            lineTo(10.6f, 12f)
            lineTo(5f, 6.4f)
            lineTo(6.4f, 5f)
            lineTo(12f, 10.6f)
            lineTo(17.6f, 5f)
            lineTo(19f, 6.4f)
            lineTo(13.4f, 12f)
            lineTo(19f, 17.6f)
            lineTo(17.6f, 19f)
            lineTo(12f, 13.4f)
            lineTo(6.4f, 19f)
            close()
          }
        }
        .build()
    return _Close!!
  }

private var _Close: ImageVector? = null

val Add: ImageVector
  get() {
    if (_Add != null) return _Add!!
    _Add =
      ImageVector.Builder(
          name = "add",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(11f, 13f)
            horizontalLineTo(5f)
            verticalLineTo(11f)
            horizontalLineToRelative(6f)
            verticalLineTo(5f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(6f)
            horizontalLineToRelative(6f)
            verticalLineToRelative(2f)
            horizontalLineTo(13f)
            verticalLineToRelative(6f)
            horizontalLineTo(11f)
            verticalLineTo(13f)
            close()
          }
        }
        .build()
    return _Add!!
  }

private var _Add: ImageVector? = null

val Delete: ImageVector
  get() {
    if (_Delete != null) return _Delete!!
    _Delete =
      ImageVector.Builder(
          name = "delete",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(7f, 21f)
            quadTo(6.18f, 21f, 5.59f, 20.41f)
            reflectiveQuadTo(5f, 19f)
            verticalLineTo(6f)
            horizontalLineTo(4f)
            verticalLineTo(4f)
            horizontalLineTo(9f)
            verticalLineTo(3f)
            horizontalLineToRelative(6f)
            verticalLineTo(4f)
            horizontalLineToRelative(5f)
            verticalLineTo(6f)
            horizontalLineTo(19f)
            verticalLineTo(19f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(17f, 21f)
            horizontalLineTo(7f)
            close()
            moveTo(17f, 6f)
            horizontalLineTo(7f)
            verticalLineTo(19f)
            horizontalLineTo(17f)
            verticalLineTo(6f)
            close()
            moveTo(9f, 17f)
            horizontalLineToRelative(2f)
            verticalLineTo(8f)
            horizontalLineTo(9f)
            verticalLineToRelative(9f)
            close()
            moveToRelative(4f, 0f)
            horizontalLineToRelative(2f)
            verticalLineTo(8f)
            horizontalLineTo(13f)
            verticalLineToRelative(9f)
            close()
          }
        }
        .build()
    return _Delete!!
  }

private var _Delete: ImageVector? = null

val Settings: ImageVector
  get() {
    if (_Settings != null) return _Settings!!
    _Settings =
      ImageVector.Builder(
          name = "settings",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(9.25f, 22f)
            lineTo(8.85f, 18.8f)
            quadTo(8.53f, 18.68f, 8.24f, 18.5f)
            reflectiveQuadTo(7.68f, 18.13f)
            lineTo(4.7f, 19.38f)
            lineTo(1.95f, 14.63f)
            lineTo(4.53f, 12.68f)
            quadTo(4.5f, 12.5f, 4.5f, 12.34f)
            quadToRelative(0f, -0.16f, 0f, -0.34f)
            reflectiveQuadToRelative(0f, -0.34f)
            reflectiveQuadTo(4.53f, 11.33f)
            lineTo(1.95f, 9.38f)
            lineTo(4.7f, 4.63f)
            lineTo(7.68f, 5.88f)
            quadTo(7.95f, 5.68f, 8.25f, 5.5f)
            reflectiveQuadTo(8.85f, 5.2f)
            lineTo(9.25f, 2f)
            horizontalLineToRelative(5.5f)
            lineToRelative(0.4f, 3.2f)
            quadToRelative(0.33f, 0.13f, 0.61f, 0.3f)
            reflectiveQuadToRelative(0.56f, 0.38f)
            lineTo(19.3f, 4.63f)
            lineToRelative(2.75f, 4.75f)
            lineToRelative(-2.57f, 1.95f)
            quadToRelative(0.02f, 0.18f, 0.02f, 0.34f)
            reflectiveQuadToRelative(0f, 0.34f)
            reflectiveQuadToRelative(0f, 0.34f)
            reflectiveQuadToRelative(-0.05f, 0.34f)
            lineToRelative(2.57f, 1.95f)
            lineToRelative(-2.75f, 4.75f)
            lineTo(16.33f, 18.13f)
            quadToRelative(-0.27f, 0.2f, -0.57f, 0.38f)
            reflectiveQuadToRelative(-0.6f, 0.3f)
            lineTo(14.75f, 22f)
            horizontalLineTo(9.25f)
            close()
            moveTo(11f, 20f)
            horizontalLineToRelative(1.98f)
            lineToRelative(0.35f, -2.65f)
            quadToRelative(0.78f, -0.2f, 1.44f, -0.59f)
            reflectiveQuadToRelative(1.21f, -0.94f)
            lineToRelative(2.47f, 1.03f)
            lineToRelative(0.98f, -1.7f)
            lineTo(17.28f, 13.52f)
            quadToRelative(0.13f, -0.35f, 0.17f, -0.74f)
            reflectiveQuadTo(17.5f, 12f)
            reflectiveQuadTo(17.45f, 11.21f)
            quadTo(17.4f, 10.83f, 17.28f, 10.48f)
            lineTo(19.43f, 8.85f)
            lineTo(18.45f, 7.15f)
            lineTo(15.98f, 8.2f)
            quadTo(15.43f, 7.63f, 14.76f, 7.24f)
            reflectiveQuadTo(13.33f, 6.65f)
            lineTo(13f, 4f)
            horizontalLineTo(11.03f)
            lineTo(10.68f, 6.65f)
            quadTo(9.9f, 6.85f, 9.24f, 7.24f)
            reflectiveQuadTo(8.03f, 8.17f)
            lineTo(5.55f, 7.15f)
            lineTo(4.58f, 8.85f)
            lineToRelative(2.15f, 1.6f)
            quadTo(6.6f, 10.83f, 6.55f, 11.2f)
            reflectiveQuadTo(6.5f, 12f)
            quadToRelative(0f, 0.4f, 0.05f, 0.77f)
            reflectiveQuadToRelative(0.17f, 0.75f)
            lineTo(4.58f, 15.15f)
            lineToRelative(0.98f, 1.7f)
            lineTo(8.03f, 15.8f)
            quadToRelative(0.55f, 0.58f, 1.21f, 0.96f)
            reflectiveQuadToRelative(1.44f, 0.59f)
            lineTo(11f, 20f)
            close()
            moveToRelative(1.05f, -4.5f)
            quadToRelative(1.45f, 0f, 2.47f, -1.03f)
            reflectiveQuadTo(15.55f, 12f)
            reflectiveQuadTo(14.53f, 9.52f)
            reflectiveQuadTo(12.05f, 8.5f)
            quadToRelative(-1.47f, 0f, -2.49f, 1.02f)
            reflectiveQuadTo(8.55f, 12f)
            reflectiveQuadToRelative(1.01f, 2.47f)
            reflectiveQuadToRelative(2.49f, 1.03f)
            close()
            moveTo(12f, 12f)
            close()
          }
        }
        .build()
    return _Settings!!
  }

private var _Settings: ImageVector? = null

val AutoAwesome: ImageVector
  get() {
    if (_AutoAwesome != null) return _AutoAwesome!!
    _AutoAwesome =
      ImageVector.Builder(
          name = "auto_awesome",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(19f, 9f)
            lineTo(17.75f, 6.25f)
            lineTo(15f, 5f)
            lineTo(17.75f, 3.75f)
            lineTo(19f, 1f)
            lineToRelative(1.25f, 2.75f)
            lineTo(23f, 5f)
            lineTo(20.25f, 6.25f)
            lineTo(19f, 9f)
            close()
            moveToRelative(0f, 14f)
            lineTo(17.75f, 20.25f)
            lineTo(15f, 19f)
            lineToRelative(2.75f, -1.25f)
            lineTo(19f, 15f)
            lineToRelative(1.25f, 2.75f)
            lineTo(23f, 19f)
            lineToRelative(-2.75f, 1.25f)
            lineTo(19f, 23f)
            close()
            moveTo(9f, 20f)
            lineTo(6.5f, 14.5f)
            lineTo(1f, 12f)
            lineTo(6.5f, 9.5f)
            lineTo(9f, 4f)
            lineToRelative(2.5f, 5.5f)
            lineTo(17f, 12f)
            lineToRelative(-5.5f, 2.5f)
            lineTo(9f, 20f)
            close()
            moveTo(9f, 15.15f)
            lineTo(10f, 13f)
            lineToRelative(2.15f, -1f)
            lineTo(10f, 11f)
            lineTo(9f, 8.85f)
            lineTo(8f, 11f)
            lineTo(5.85f, 12f)
            lineTo(8f, 13f)
            lineToRelative(1f, 2.15f)
            close()
            moveTo(9f, 12f)
            close()
          }
        }
        .build()
    return _AutoAwesome!!
  }

private var _AutoAwesome: ImageVector? = null

val Send: ImageVector
  get() {
    if (_Send != null) return _Send!!
    _Send =
      ImageVector.Builder(
          name = "send",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(3f, 20f)
            verticalLineTo(4f)
            lineToRelative(19f, 8f)
            lineTo(3f, 20f)
            close()
            moveTo(5f, 17f)
            lineTo(16.85f, 12f)
            lineTo(5f, 7f)
            verticalLineToRelative(3.5f)
            lineTo(11f, 12f)
            lineTo(5f, 13.5f)
            verticalLineTo(17f)
            close()
            moveToRelative(0f, 0f)
            verticalLineTo(12f)
            verticalLineTo(7f)
            verticalLineToRelative(3.5f)
            verticalLineToRelative(3f)
            verticalLineTo(17f)
            close()
          }
        }
        .build()
    return _Send!!
  }

private var _Send: ImageVector? = null

val ArrowDropUp: ImageVector
    get() {
        if (_ArrowDropUp != null) return _ArrowDropUp!!
        _ArrowDropUp = ImageVector.Builder(
            name = "arrow_drop_up",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Bevel,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(12f, 8f)
                lineTo(20f, 18f)
                lineTo(4f, 18f)
                close()
            }
        }.build()
        return _ArrowDropUp!!
    }

private var _ArrowDropUp: ImageVector? = null

val ArrowDropDown: ImageVector
    get() {
        if (_ArrowDropDown != null) return _ArrowDropDown!!
        _ArrowDropDown = ImageVector.Builder(
            name = "arrow_drop_down",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Bevel,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero,
            ) {
                moveTo(12f, 18f)
                lineTo(20f, 8f)
                lineTo(4f, 8f)
                close()
            }
        }.build()
        return _ArrowDropDown!!
    }

private var _ArrowDropDown: ImageVector? = null

val Cloud: ImageVector
  get() {
    if (_Cloud != null) return _Cloud!!
    _Cloud =
      ImageVector.Builder(
          name = "cloud",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(6.5f, 20f)
            quadTo(4.23f, 20f, 2.61f, 18.43f)
            reflectiveQuadTo(1f, 14.58f)
            quadTo(1f, 12.63f, 2.18f, 11.1f)
            reflectiveQuadTo(5.25f, 9.15f)
            quadTo(5.88f, 6.85f, 7.75f, 5.43f)
            reflectiveQuadTo(12f, 4f)
            quadToRelative(2.93f, 0f, 4.96f, 2.04f)
            reflectiveQuadTo(19f, 11f)
            quadToRelative(1.73f, 0.2f, 2.86f, 1.49f)
            reflectiveQuadTo(23f, 15.5f)
            quadToRelative(0f, 1.88f, -1.31f, 3.19f)
            reflectiveQuadTo(18.5f, 20f)
            horizontalLineTo(6.5f)
            close()
            moveToRelative(0f, -2f)
            horizontalLineToRelative(12f)
            quadToRelative(1.05f, 0f, 1.78f, -0.73f)
            reflectiveQuadTo(21f, 15.5f)
            reflectiveQuadTo(20.28f, 13.73f)
            reflectiveQuadTo(18.5f, 13f)
            horizontalLineTo(17f)
            verticalLineTo(11f)
            quadTo(17f, 8.92f, 15.54f, 7.46f)
            reflectiveQuadTo(12f, 6f)
            quadTo(9.93f, 6f, 8.46f, 7.46f)
            reflectiveQuadTo(7f, 11f)
            horizontalLineTo(6.5f)
            quadTo(5.05f, 11f, 4.03f, 12.02f)
            reflectiveQuadTo(3f, 14.5f)
            reflectiveQuadToRelative(1.03f, 2.48f)
            reflectiveQuadTo(6.5f, 18f)
            close()
            moveTo(12f, 12f)
            close()
          }
        }
        .build()
    return _Cloud!!
  }

private var _Cloud: ImageVector? = null

val Download: ImageVector
  get() {
    if (_Download != null) return _Download!!
    _Download =
      ImageVector.Builder(
          name = "download",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(12f, 16f)
            lineTo(7f, 11f)
            lineTo(8.4f, 9.55f)
            lineToRelative(2.6f, 2.6f)
            verticalLineTo(4f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(8.15f)
            lineToRelative(2.6f, -2.6f)
            lineTo(17f, 11f)
            lineToRelative(-5f, 5f)
            close()
            moveTo(6f, 20f)
            quadTo(5.18f, 20f, 4.59f, 19.41f)
            reflectiveQuadTo(4f, 18f)
            verticalLineTo(15f)
            horizontalLineTo(6f)
            verticalLineToRelative(3f)
            horizontalLineTo(18f)
            verticalLineTo(15f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(3f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(18f, 20f)
            horizontalLineTo(6f)
            close()
          }
        }
        .build()
    return _Download!!
  }

private var _Download: ImageVector? = null

val Refresh: ImageVector
  get() {
    if (_Refresh != null) return _Refresh!!
    _Refresh =
      ImageVector.Builder(
          name = "refresh",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(12f, 20f)
            quadTo(8.65f, 20f, 6.33f, 17.68f)
            reflectiveQuadTo(4f, 12f)
            reflectiveQuadTo(6.33f, 6.32f)
            reflectiveQuadTo(12f, 4f)
            quadToRelative(1.73f, 0f, 3.3f, 0.71f)
            quadTo(16.88f, 5.43f, 18f, 6.75f)
            verticalLineTo(4f)
            horizontalLineToRelative(2f)
            verticalLineTo(7f)
            horizontalLineTo(13f)
            verticalLineTo(9f)
            horizontalLineToRelative(4.2f)
            quadTo(16.4f, 7.6f, 15.01f, 6.8f)
            reflectiveQuadTo(12f, 6f)
            quadTo(9.5f, 6f, 7.75f, 7.75f)
            reflectiveQuadTo(6f, 12f)
            reflectiveQuadToRelative(1.75f, 4.25f)
            reflectiveQuadTo(12f, 18f)
            quadToRelative(1.93f, 0f, 3.48f, -1.1f)
            reflectiveQuadTo(17.65f, 14f)
            horizontalLineTo(19.75f)
            quadTo(19.05f, 16.65f, 16.9f, 18.32f)
            reflectiveQuadTo(12f, 20f)
            close()
          }
        }
        .build()
    return _Refresh!!
  }

private var _Refresh: ImageVector? = null

val Memory: ImageVector
  get() {
    if (_Memory != null) return _Memory!!
    _Memory =
      ImageVector.Builder(
          name = "memory",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(9f, 15f)
            verticalLineTo(9f)
            horizontalLineToRelative(6f)
            verticalLineToRelative(6f)
            horizontalLineTo(9f)
            close()
            moveToRelative(2f, -2f)
            horizontalLineToRelative(2f)
            verticalLineTo(11f)
            horizontalLineTo(11f)
            verticalLineToRelative(2f)
            close()
            moveTo(9f, 21f)
            verticalLineTo(19f)
            horizontalLineTo(7f)
            quadTo(6.18f, 19f, 5.59f, 18.41f)
            reflectiveQuadTo(5f, 17f)
            verticalLineTo(15f)
            horizontalLineTo(3f)
            verticalLineTo(13f)
            horizontalLineTo(5f)
            verticalLineTo(11f)
            horizontalLineTo(3f)
            verticalLineTo(9f)
            horizontalLineTo(5f)
            verticalLineTo(7f)
            quadTo(5f, 6.18f, 5.59f, 5.59f)
            reflectiveQuadTo(7f, 5f)
            horizontalLineTo(9f)
            verticalLineTo(3f)
            horizontalLineToRelative(2f)
            verticalLineTo(5f)
            horizontalLineToRelative(2f)
            verticalLineTo(3f)
            horizontalLineToRelative(2f)
            verticalLineTo(5f)
            horizontalLineToRelative(2f)
            quadToRelative(0.82f, 0f, 1.41f, 0.59f)
            quadTo(19f, 6.18f, 19f, 7f)
            verticalLineTo(9f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            horizontalLineTo(19f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            horizontalLineTo(19f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(2f)
            horizontalLineTo(19f)
            verticalLineToRelative(2f)
            quadToRelative(0f, 0.82f, -0.59f, 1.41f)
            reflectiveQuadTo(17f, 19f)
            horizontalLineTo(15f)
            verticalLineToRelative(2f)
            horizontalLineTo(13f)
            verticalLineTo(19f)
            horizontalLineTo(11f)
            verticalLineToRelative(2f)
            horizontalLineTo(9f)
            close()
            moveToRelative(8f, -4f)
            verticalLineTo(7f)
            horizontalLineTo(7f)
            verticalLineTo(17f)
            horizontalLineTo(17f)
            close()
            moveTo(12f, 12f)
            close()
          }
        }
        .build()
    return _Memory!!
  }

private var _Memory: ImageVector? = null

val Database: ImageVector
  get() {
    if (_Database != null) return _Database!!
    _Database =
      ImageVector.Builder(
          name = "database",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(12f, 21f)
            quadTo(8.23f, 21f, 5.61f, 19.84f)
            quadTo(3f, 18.68f, 3f, 17f)
            verticalLineTo(7f)
            quadTo(3f, 5.35f, 5.64f, 4.17f)
            reflectiveQuadTo(12f, 3f)
            reflectiveQuadToRelative(6.36f, 1.17f)
            reflectiveQuadTo(21f, 7f)
            verticalLineTo(17f)
            quadToRelative(0f, 1.68f, -2.61f, 2.84f)
            quadTo(15.78f, 21f, 12f, 21f)
            close()
            moveTo(12f, 9.02f)
            quadToRelative(2.23f, 0f, 4.48f, -0.64f)
            reflectiveQuadTo(19f, 7.02f)
            quadTo(18.73f, 6.3f, 16.49f, 5.65f)
            reflectiveQuadTo(12f, 5f)
            quadTo(9.73f, 5f, 7.54f, 5.64f)
            reflectiveQuadTo(5f, 7.02f)
            quadTo(5.35f, 7.77f, 7.54f, 8.4f)
            reflectiveQuadTo(12f, 9.02f)
            close()
            moveTo(12f, 14f)
            quadToRelative(1.05f, 0f, 2.03f, -0.1f)
            reflectiveQuadToRelative(1.86f, -0.29f)
            reflectiveQuadToRelative(1.67f, -0.46f)
            quadTo(18.35f, 12.88f, 19f, 12.52f)
            verticalLineToRelative(-3f)
            quadToRelative(-0.65f, 0.35f, -1.44f, 0.63f)
            quadToRelative(-0.79f, 0.28f, -1.67f, 0.46f)
            reflectiveQuadTo(14.03f, 10.9f)
            reflectiveQuadTo(12f, 11f)
            reflectiveQuadTo(9.95f, 10.9f)
            reflectiveQuadTo(8.06f, 10.61f)
            quadTo(7.18f, 10.43f, 6.4f, 10.15f)
            quadTo(5.63f, 9.88f, 5f, 9.52f)
            verticalLineToRelative(3f)
            quadToRelative(0.63f, 0.35f, 1.4f, 0.63f)
            quadToRelative(0.78f, 0.28f, 1.66f, 0.46f)
            reflectiveQuadTo(9.95f, 13.9f)
            reflectiveQuadTo(12f, 14f)
            close()
            moveToRelative(0f, 5f)
            quadToRelative(1.15f, 0f, 2.34f, -0.18f)
            reflectiveQuadToRelative(2.19f, -0.46f)
            reflectiveQuadTo(18.2f, 17.71f)
            quadTo(18.88f, 17.35f, 19f, 16.98f)
            verticalLineTo(14.53f)
            quadToRelative(-0.65f, 0.35f, -1.44f, 0.63f)
            quadToRelative(-0.79f, 0.27f, -1.67f, 0.46f)
            reflectiveQuadTo(14.03f, 15.9f)
            reflectiveQuadTo(12f, 16f)
            reflectiveQuadTo(9.95f, 15.9f)
            reflectiveQuadTo(8.06f, 15.61f)
            quadTo(7.18f, 15.43f, 6.4f, 15.15f)
            reflectiveQuadTo(5f, 14.53f)
            verticalLineTo(17f)
            quadToRelative(0.13f, 0.38f, 0.79f, 0.73f)
            reflectiveQuadToRelative(1.66f, 0.64f)
            reflectiveQuadToRelative(2.2f, 0.46f)
            reflectiveQuadTo(12f, 19f)
            close()
          }
        }
        .build()
    return _Database!!
  }

private var _Database: ImageVector? = null

val ClearAll: ImageVector
  get() {
    if (_ClearAll != null) return _ClearAll!!
    _ClearAll =
      ImageVector.Builder(
          name = "clear_all",
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
            pathFillType = PathFillType.NonZero,
          ) {
            moveTo(3f, 17f)
            verticalLineTo(15f)
            horizontalLineTo(17f)
            verticalLineToRelative(2f)
            horizontalLineTo(3f)
            close()
            moveTo(5f, 13f)
            verticalLineTo(11f)
            horizontalLineTo(19f)
            verticalLineToRelative(2f)
            horizontalLineTo(5f)
            close()
            moveTo(7f, 9f)
            verticalLineTo(7f)
            horizontalLineTo(21f)
            verticalLineTo(9f)
            horizontalLineTo(7f)
            close()
          }
        }
        .build()
    return _ClearAll!!
  }

private var _ClearAll: ImageVector? = null
