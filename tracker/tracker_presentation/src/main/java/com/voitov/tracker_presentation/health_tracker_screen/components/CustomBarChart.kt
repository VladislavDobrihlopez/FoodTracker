package com.voitov.tracker_presentation.health_tracker_screen.components

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import com.voitov.common_ui.CarbColor
import com.voitov.common_ui.FatColor
import com.voitov.common_ui.ProteinColor
import com.voitov.tracker_presentation.health_tracker_screen.components.CustomBarChartState.Companion.MAXIMUM_VISIBLE_BARS_TO_HIDE_TEXT
import com.voitov.tracker_presentation.health_tracker_screen.components.CustomBarChartState.Companion.MINIMUM_VISIBLE_BARS
import com.voitov.tracker_presentation.health_tracker_screen.model.TimePointResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@OptIn(ExperimentalTextApi::class)
@Composable
fun CustomBarChart(
    nutrientGoalInKkal: Int,
    items: List<TimePointResult>,
    shouldDisplayInZoneArea: () -> Boolean,
    shouldDisplayExceededArea: () -> Boolean,
    modifier: Modifier = Modifier,
    chartState: CustomBarChartState = CustomBarChartState(items)
) {
    var state by rememberCustomBarChartState(chartState)

    state = state.copy(
        timePointResults = items,
        lineConfig = state.lineConfig.copy(
            shouldDisplayGoalLine = shouldDisplayExceededArea(),
            shouldDisplayAvgValue = shouldDisplayInZoneArea()
        )
    )

    Log.d("CHANGE_STATE", state.toString())

    val brush = remember {
        Brush.linearGradient(colors = listOf(Color.Cyan, Color.Green))
    }

    val scope = rememberCoroutineScope()

    val anim by remember {
        mutableStateOf(Animatable(0f))
    }

    val animReverse by remember {
        mutableStateOf(Animatable(1f))
    }

    Log.d("TEST_RECOMPOSITION", "change anim: ${anim.value}, ${animReverse.value}")
    Log.d(
        "TEST_RECOMPOSITION",
        "${state.lineConfig.shouldDisplayGoalLine} ${state.lineConfig.shouldDisplayAvgValue}"
    )

    LaunchedEffect(key1 = state.mode) {
        Log.d("TEST_RECOMPOSITION", "change state mode: ${state.mode}")
        anim.snapTo(0f)
        animReverse.snapTo(1f)
        anim.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy))
    }

    val transformable = rememberTransformableState(onTransformation = { zoomChange, panChange, _ ->
        Log.d("TEST_RECOMPOSITION", "transformable: $zoomChange ${panChange.x}")

        val newVisibleItems = (state.visibleBarsCount / zoomChange).roundToInt().coerceIn(
            MINIMUM_VISIBLE_BARS..items.size
        )

        state = state.copy(visibleBarsCount = newVisibleItems)

        val nesScrolledValue = (state.scrolledBy + panChange.x)
            .coerceIn(0f..(state.barWidth * items.size + state.barWidth / 2 - state.componentWidth))
        state = state.copy(scrolledBy = nesScrolledValue.toFloat())
    })

    val body = MaterialTheme.typography.h4

    if (items.isEmpty() || items.size < MINIMUM_VISIBLE_BARS) {
        Text(text = "Nothing to show", style = MaterialTheme.typography.h2)
    } else {
        val textMeasure = rememberTextMeasurer()
        Canvas(modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    Log.d("TEST_RECOMPOSITION", "long press")
                    scope.launch {
                        animReverse.snapTo(1f)
                        animReverse.animateTo(0f, animationSpec = tween(100))
                        delay(100)
                        state = state.copy(
                            mode =
                            if (state.mode == CustomBarChartState.Mode.NORMAL) {
                                CustomBarChartState.Mode.DETAILED
                            } else {
                                CustomBarChartState.Mode.NORMAL
                            }
                        )
                    }
                })
            }
            .transformable(transformable)
            .onSizeChanged {
                state = state.copy(componentWidth = it.width.toFloat())
            }
            .drawBehind {
                val maximal = state.visibleItems.maxOf { it.inTotalKkal }
                val pxPerPoint = size.height * 0.95f / maximal
                val alphaForAvg = if (state.lineConfig.shouldDisplayAvgValue) 0.15f else 0f
                val alphaForExceeding = if (state.lineConfig.shouldDisplayGoalLine) 0.15f else 0f
                drawRect(
                    alpha = alphaForExceeding,
                    color = state.lineConfig.exceededColor,
                    topLeft = Offset(0f, 0f),
                    size = Size(
                        state.barWidth * items.size,
                        size.height - pxPerPoint * nutrientGoalInKkal
                    )
                )

                drawRect(
                    alpha = alphaForAvg,
                    brush = Brush.linearGradient(
                        listOf(
                            state.lineConfig.inZoneColor,
                            Color.White
                        )
                    ),
                    topLeft = Offset(
                        0f,
                        size.height - pxPerPoint * items.fold(0f) { sum, current ->
                            sum + current.inTotalKkal
                        } / items.size),
                    size = Size(
                        state.barWidth * items.size,
                        size.height
                    )
                )
            }) {
            Log.d("TEST_RECOMPOSITION", "canvas changes")
            val minimal = state.visibleItems.minOf { it.inTotalKkal }
            val maximal = state.visibleItems.maxOf { it.inTotalKkal }
            val barWidth = state.barWidth

            translate(left = state.scrolledBy) {
                state.timePointResults.forEachIndexed { index, dayResult ->
                    val toDraw = dayResult.date.format(DateTimeFormatter.ofPattern("dd.MM"))
                    val res = textMeasure.measure(
                        toDraw,
                        style = body,
                        overflow = TextOverflow.Ellipsis
                    )
                    Log.d("TEST_ONE_ELEMENT", "$maximal $index")
                    val pxPerPoint = (size.height * 0.95f - res.size.height) / maximal

                    val fatsProportion = dayResult.kkalInFats / dayResult.inTotalKkal.toFloat()
                    val proteinsProportion = dayResult.kkalInProteins / dayResult.inTotalKkal.toFloat()
                    val carbsProportion = dayResult.kkalInCarbs / dayResult.inTotalKkal.toFloat()
                    val carbsHeight = dayResult.kkalInCarbs * pxPerPoint
                    val fatsHeight = dayResult.kkalInFats * pxPerPoint
                    val proteinsHeight = dayResult.kkalInProteins * pxPerPoint

                    val rightToLeft = size.width - (index + 1) * barWidth
                    val cornerRadius = CornerRadius(24f, 24f)

                    if (state.visibleBarsCount <= MAXIMUM_VISIBLE_BARS_TO_HIDE_TEXT) {
                        drawText(
                            textMeasure,
                            toDraw,
                            style = body,
                            topLeft = Offset(
                                rightToLeft + barWidth / 4,
                                size.height - dayResult.inTotalKkal * pxPerPoint - res.size.height * 1.25f
                            )
                        )
                    }

                    when (state.mode) {
                        CustomBarChartState.Mode.NORMAL -> {
                            drawRoundRect(
                                cornerRadius = cornerRadius,
                                brush = brush,
                                topLeft = Offset(
                                    rightToLeft,
                                    size.height - dayResult.inTotalKkal * pxPerPoint * anim.value * animReverse.value
                                ),
                                size = Size(barWidth * 7 / 8, dayResult.inTotalKkal * pxPerPoint)
                            )
                        }

                        CustomBarChartState.Mode.DETAILED -> {
                            drawPath(color = CarbColor, path = Path().apply {
                                addRoundRect(
                                    RoundRect(
                                        rect = Rect(
                                            offset = Offset(
                                                rightToLeft,
                                                size.height - dayResult.inTotalKkal * pxPerPoint * anim.value * animReverse.value
                                            ),
                                            size = Size(barWidth * 7 / 8, carbsHeight)
                                        ),
                                        topLeft = cornerRadius,
                                        topRight = cornerRadius
                                    )
                                )
                            })
                            drawRect(
                                color = FatColor,
                                topLeft = Offset(
                                    rightToLeft,
                                    size.height + carbsHeight - (dayResult.inTotalKkal * pxPerPoint * anim.value) * animReverse.value
                                ),
                                size = Size(barWidth * 7 / 8, fatsHeight)
                            )
                            drawPath(color = ProteinColor, path = Path().apply {
                                addRoundRect(
                                    RoundRect(
                                        rect = Rect(
                                            offset = Offset(
                                                rightToLeft,
                                                size.height + carbsHeight + fatsHeight - (dayResult.inTotalKkal * pxPerPoint * anim.value) * animReverse.value
                                            ),
                                            size = Size(barWidth * 7 / 8, proteinsHeight)
                                        ),
                                        bottomLeft = cornerRadius,
                                        bottomRight = cornerRadius
                                    )
                                )
                            })
                        }
                    }
                }
            }
        }
    }
}

data class CustomBarChartState(
    val timePointResults: List<TimePointResult>,
    val componentWidth: Float = 0f,
    val visibleBarsCount: Int = MINIMUM_VISIBLE_BARS,
    val scrolledBy: Float = 0f,
    val mode: Mode = Mode.NORMAL,
    val lineConfig: LineConfig = LineConfig(),
) {
    val barWidth: Float
        get() = componentWidth / min(timePointResults.size, visibleBarsCount)

    val visibleItems: List<TimePointResult>
        get() {
            val outsidePointOfView = (abs(scrolledBy) / barWidth)
            val lastIndexOfItemToTake =
                (outsidePointOfView.toInt() + visibleBarsCount).coerceIn(0..timePointResults.size)
            Log.d("TEST_ONE_ELEMENT", "$outsidePointOfView $lastIndexOfItemToTake")
            return timePointResults.subList(
                min(outsidePointOfView.toInt(), lastIndexOfItemToTake),
                max(outsidePointOfView.toInt(), lastIndexOfItemToTake)
            )
        }

    companion object {
        const val MINIMUM_VISIBLE_BARS = 2
        const val MAXIMUM_VISIBLE_BARS_TO_HIDE_TEXT = 5

        val saver: Saver<MutableState<CustomBarChartState>, Any> = listSaver(save = {
            val stateValue = it.value
            listOf(
                stateValue.timePointResults,
                stateValue.componentWidth,
                stateValue.visibleBarsCount,
                stateValue.scrolledBy,
                stateValue.mode
            )
        }, restore = {
            val timePointResult = it[0] as List<TimePointResult>
            val componentWidth = it[1] as Float
            val visibleBarsCount = it[2] as Int
            val scrolledBy = it[3] as Float
            val mode = it[4] as Mode
            Log.d(
                "TEST_RESTORING",
                "$scrolledBy ${componentWidth - componentWidth / visibleBarsCount}"
            )
            mutableStateOf(
                CustomBarChartState(
                    timePointResult,
                    componentWidth,
                    visibleBarsCount,
                    min(abs(scrolledBy), componentWidth - componentWidth / visibleBarsCount),
                    mode
                )
            )
        })
    }

    data class LineConfig(
        val shouldDisplayGoalLine: Boolean = false,
        val exceededColor: Color = Color.Red,
        val shouldDisplayAvgValue: Boolean = false,
        val inZoneColor: Color = Color.Blue
    )

    @Immutable
    enum class Mode {
        NORMAL,
        DETAILED
    }
}

@Composable
fun rememberCustomBarChartState(items: List<TimePointResult>) = remember {
    mutableStateOf(CustomBarChartState(items))
}

@Composable
fun rememberCustomBarChartState(state: CustomBarChartState) =
    rememberSaveable(saver = CustomBarChartState.saver) {
        mutableStateOf(state)
    }


