package com.medicao0102.braintetris

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun Splash(n: () -> Unit) {


  val progress = remember { Animatable(0f) }
  LaunchedEffect(Unit) {
    progress.animateTo(
      1f, tween(3000, 200, LinearEasing)
    )
   n()
  }
  Column(
    Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {

    Text(
      "BRAINTETRIS",
      style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )
    Image(
      painterResource(R.drawable.logo),
      null,
      modifier = Modifier.fillMaxWidth(0.6f),
      contentScale = ContentScale.Crop
    )
    Spacer(Modifier.height(20.dp))
    LinearProgressIndicator(
      progress = progress.value,
      color = MaterialTheme.colorScheme.primary,
      trackColor = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier
        .fillMaxWidth(0.6f)
        .height(24.dp),
      strokeCap = StrokeCap.Butt
    )


  }

}