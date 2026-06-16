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
