package com.debduttapanda.customshadow

import android.graphics.BlurMaskFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.debduttapanda.customshadow.ui.theme.CustomShadowTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomShadowTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val height = remember {
                        mutableStateOf(80f)
                    }
                    val radius = remember {
                        mutableStateOf(22f)
                    }
                    val shadowBorderRadius = remember {
                        mutableStateOf(22f)
                    }
                    val offsetX = remember {
                        mutableStateOf(7f)
                    }
                    val offsetY = remember {
                        mutableStateOf(7f)
                    }
                    val spread = remember {
                        mutableStateOf(7f)
                    }
                    val blurRadius = remember {
                        mutableStateOf(22f)
                    }
                    val shadowColor = remember {
                        mutableStateOf(Color.Black)
                    }
                    Column(
                        modifier = Modifier
                    ){
                        Box(
                            modifier = Modifier
                                .padding(40.dp)
                                .shadow(
                                    shadowColor.value,
                                    borderRadius = shadowBorderRadius.value.dp,
                                    offsetX = offsetX.value.dp,
                                    offsetY = offsetY.value.dp,
                                    spread = spread.value.dp,
                                    blurRadius = blurRadius.value.dp
                                )
                                .fillMaxWidth()
                                .height(height.value.dp)
                                .clip(RoundedCornerShape(radius.value.dp))
                                .background(Color.White)
                        ){

                        }
                        LazyColumn(
                            modifier = Modifier
                                .padding(50.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ){
                            stickyHeader {
                                MyHeader("Geometry")
                            }
                            item{
                                MySlider(
                                    title = "Width",
                                    from = 0f,
                                    to = 200f,
                                    value = height
                                )
                            }
                            item{
                                MySlider(
                                    title = "Radius",
                                    from = 0f,
                                    to = 80f,
                                    value = radius
                                )
                            }
                            stickyHeader {
                                MyHeader("Shadow")
                            }
                            item{
                                MySlider(
                                    title = "Radius",
                                    from = 0f,
                                    to = 80f,
                                    value = shadowBorderRadius
                                )
                            }
                            item{
                                MySlider(
                                    title = "OffsetX",
                                    from = 0f,
                                    to = 80f,
                                    value = offsetX
                                )
                            }
                            item{
                                MySlider(
                                    title = "OffsetY",
                                    from = 0f,
                                    to = 80f,
                                    value = offsetY
                                )
                            }
                            item{
                                MySlider(
                                    title = "Spread",
                                    from = 0f,
                                    to = 80f,
                                    value = spread
                                )
                            }
                            item{
                                MySlider(
                                    title = "Blur Radius",
                                    from = 0f,
                                    to = 80f,
                                    value = blurRadius
                                )
                            }
                            stickyHeader {
                                MyHeader("Shadow Color")
                            }
                            item{
                                ColorPicker{
                                    shadowColor.value = it
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyHeader(
    title: String
){
    Column(){
        Text(
            title,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(4.dp)
        )
    }
    Divider()
}


@OptIn(ExperimentalGraphicsApi::class)
@Composable
fun ColorPicker(
    onColorChange: (Color)->Unit
){
    val hue = remember {
        mutableStateOf(0f)
    }
    val sat = remember {
        mutableStateOf(0f)
    }
    val value = remember {
        mutableStateOf(0f)
    }
    val alpha = remember {
        mutableStateOf(1f)
    }
    val color = remember {
        derivedStateOf {
            val c = Color.hsv(hue.value,sat.value,value.value,alpha.value)
            onColorChange(c)
            c
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth()
    ){

    }
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(color.value)
    )
    MySlider(title = "Hue", value = hue, to = 360f)
    MySlider(title = "Sat", value = sat)
    MySlider(title = "Val", value = value)
    MySlider(title = "Alpha", value = alpha)
}

@Composable
fun MySlider(
    title: String,
    from: Float = 0f,
    to: Float = 1f,
    value: MutableState<Float>
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Text("$title:${value.value}")
        Slider(
            value = value.value,
            onValueChange = {
                value.value = it
            },
            valueRange = from..to
        )
    }
}

fun Modifier.shadow(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0f.dp,
    modifier: Modifier = Modifier
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint
            )
        }
    }
)