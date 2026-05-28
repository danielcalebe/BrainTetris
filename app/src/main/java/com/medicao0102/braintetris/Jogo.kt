package com.medicao0102.braintetris

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlin.collections.listOf


const val GW = 6
const val GH = 10

data class Piece(
  val shape: List<Pair<Int, Int>>, val x: Int, val y: Int
)


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun Jogo(ctx: Context, navController: NavHostController, onMatchSave: (Int) -> Unit) {
  var board = remember { Array(GH) { IntArray(GW) } }
  var timer by remember { mutableStateOf(3) }
  var velocity by remember { mutableStateOf(500L) }
  var score by remember { mutableStateOf(0) }
  var gameOver by remember { mutableStateOf(false) }

  val shapes = remember {
    listOf(
      listOf(
        0 to 0, 0 to 1, 0 to 2, 0 to 3
      ),
      listOf(0 to 1, 1 to 1, 2 to 1, 1 to 0),
      listOf(0 to 1, 0 to 2, 1 to 0, 1 to 1),
      listOf(
        0 to 1,
        1 to 1,
        2 to 1,
        2 to 0
      )
    )
  }

  fun randomPiece(): Piece = Piece(
    shape = shapes.random(), x = GW / 2 - 1, y = 0
  )

  var currentPiece by remember { mutableStateOf(randomPiece()) }

  fun canMove(
    piece: Piece, newX: Int, newY: Int
  ): Boolean {
    piece.shape.forEach { (dx, dy) ->
      val x = dx + newX
      val y = dy + newY

      if (x < 0 || x >= GW || y < 0 || y >= GH || board[y][x] == 1) {
        return false
      }
    }
    return true
  }


  fun fixPiece() {
    currentPiece.shape.forEach { (dx, dy) ->
      val x = currentPiece.x + dx
      val y = currentPiece.y + dy
      board[y][x] = 1
    }
    val newPiece = randomPiece()
    if (!canMove(newPiece, newPiece.x, newPiece.y)) {

      gameOver = true
      onMatchSave(score)
    } else {
      score++
      currentPiece = newPiece
    }
  }

  fun moveDown() {
    if (canMove(
        currentPiece, currentPiece.x, currentPiece.y + 1
      )
    ) {
      currentPiece = currentPiece.copy(y = currentPiece.y + 1)
    } else {
      fixPiece()
    }
  }

  fun moveRight() {

    if (canMove(
        currentPiece, currentPiece.x + 1, currentPiece.y
      )
    ) {
      currentPiece = currentPiece.copy(x = currentPiece.x + 1)
    }
  }


  fun moveLeft() {

    if (canMove(
        currentPiece, currentPiece.x - 1, currentPiece.y
      )
    ) {
      currentPiece = currentPiece.copy(x = currentPiece.x - 1)
    }
  }


  LaunchedEffect(Unit) {
    while (timer > 0) {
      delay(1000)
      timer--
    }
  }


  LaunchedEffect(timer) {
    if (timer == 0) {
      while (!gameOver) {
        delay(velocity)
        moveDown()
      }
    }
  }

  var moved by remember { mutableStateOf(false) }

  DisposableEffect(Unit) {
    val sm = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val acc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    val listener = object : SensorEventListener {
      override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
      }

      override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
          val x = event.values[0]
          val z = event.values[2]

          if (!gameOver && timer == 0) {
            if (z < 3f) {
              velocity = 100L
            } else {
              velocity = 500L
            }

            if (x in -1f..1f) {
              moved = false
            }
            if (x < -3f && !moved) {
              moveRight()
              moved = true
            }

            if (x > 3f && !moved) {
              moveLeft()
              moved = true
            }
          }
        }
      }
    }

    sm.registerListener(listener, acc, SensorManager.SENSOR_DELAY_UI)
    onDispose { sm.unregisterListener(listener) }
  }
  Column(
    Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
  ) {


    Box(
      modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
      Image(
        painterResource(R.drawable.bg),
        null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )

      Column(
        Modifier
          .fillMaxSize()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {

        if (gameOver) {
          AlertDialog(
            onDismissRequest = { navController.navigate("inicial") },
            confirmButton = {},
            containerColor = MaterialTheme.colorScheme.background,
            title = { Text("Fim de jogo") },
            text = {
              Column() {
                Text(
                  if (timer == 0) "PONTUAÇÃO" else "",
                  style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Box(
                  Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(vertical = 8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    if (timer == 0) "$score PTS" else timer.toString(),
                    style = MaterialTheme.typography.titleLarge
                  )
                }


                Spacer(Modifier.height(16.dp))
                Button(
                  onClick = {
                    navController.navigate("inicial")
                  },
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSurface),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text(
                    "IR PARA A TELA INICIAL",
                    modifier = Modifier.padding(6.dp),
                    color = MaterialTheme.colorScheme.background
                  )
                }

              }
            }

          )
        }

        Column(
          Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            if (timer == 0) "PONTUAÇÃO" else "",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Box(
            Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.secondary)
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              if (timer == 0) "$score PTS" else timer.toString(),
              style = MaterialTheme.typography.titleLarge
            )
          }
        }
        BoxWithConstraints(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp), contentAlignment = Alignment.Center
        ) {
          val ch = maxHeight / GH
          val cw = maxWidth / GW
          val cellSize = min(cw, ch)

          Canvas(
            modifier = Modifier.size(
              width = cellSize * GW, height = cellSize * GH
            )
          ) {
            val cellPx = cellSize.toPx()
            for (y in 0 until GH) {
              for (x in 0 until GW) {
                drawRect(
                  color = if (board[y][x] == 0) Color(0xFFEDEDED) else Color.LightGray,
                  size = Size(cellPx, cellPx),
                  topLeft = Offset(
                    x = cellPx * x, y = cellPx * y
                  )
                )
                drawRect(
                  color = Color(0xFF333333), size = Size(cellPx, cellPx), topLeft = Offset(
                    x = cellPx * x, y = cellPx * y
                  ), style = Stroke(3f)
                )
              }

            }

            if (timer == 0)
              currentPiece.shape.forEach { (dx, dy) ->
                val x = dx + currentPiece.x
                val y = dy + currentPiece.y
                drawRect(
                  color = Color(0xFFAED6F1), size = Size(cellPx, cellPx), topLeft = Offset(
                    x = cellPx * x, y = cellPx * y
                  )
                )
                drawRect(
                  color = Color(0xFF333333), size = Size(cellPx, cellPx), topLeft = Offset(
                    x = cellPx * x, y = cellPx * y
                  ), style = Stroke(3f)
                )
              }
          }
        }


        Column(
          Modifier
            .fillMaxWidth()
            .padding(12.dp)
        ) {

          Button(
            onClick = {
              gameOver = true
              onMatchSave(score)
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              "ENCERRAR",
              modifier = Modifier.padding(6.dp),
              color = MaterialTheme.colorScheme.background
            )
          }


          Spacer(Modifier.height(12.dp))
          Text(
            "Dica: Incline o dispositivo para controlar as peças",
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic
          )

        }


      }
    }
  }

}