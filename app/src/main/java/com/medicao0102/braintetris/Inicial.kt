package com.medicao0102.braintetris

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun Inicial(
  onSaveUserName: (String) -> Unit,
  onNavigateToRanking: () -> Unit,
  sessionUsername: String
) {


  var username by remember { mutableStateOf(sessionUsername) }
  var isError by remember { mutableStateOf(false) }
  Column(
    Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {


    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Image(
        painterResource(R.drawable.bg),
        null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )

      Column(
        Modifier.fillMaxSize(),
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
          modifier = Modifier.size(200.dp),
          contentScale = ContentScale.Crop
        )
        Column(
          modifier = Modifier.fillMaxWidth(0.7f),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Informe seu nome") },
            modifier = Modifier.fillMaxWidth(),
            keyboardActions = KeyboardActions(onGo = {

              if (username.isEmpty()) {
                isError = true
              } else {
                isError = false
                onSaveUserName(username)

              }
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go)

          )
          Button(
            onClick = {
              if (username.isEmpty()) {
                isError = true
              } else {
                isError = false
                onSaveUserName(username)

              }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              "INICIAR",
              modifier = Modifier.padding(6.dp),
              color = MaterialTheme.colorScheme.onSurface
            )
          }



          Button(
            onClick = {
              onNavigateToRanking()

            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              "RANKING",
              modifier = Modifier.padding(6.dp),
              color = MaterialTheme.colorScheme.onSurface
            )
          }



          AnimatedVisibility(isError) {
            Box(
              Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.error)
                .padding(12.dp), contentAlignment = Alignment.Center
            ) {
              Text(
                "O nome deve ser preenchido",
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }


    }


  }

}