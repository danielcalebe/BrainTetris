package com.medicao0102.braintetris

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun Ranking(navController: NavHostController, ranking: List<ItemRanking>) {
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
        Row(
          Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {

          Text(
            "BRAINTETRIS",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Image(
            painterResource(R.drawable.logo),
            null,
            modifier = Modifier.size(60.dp),
            contentScale = ContentScale.Crop
          )


        }


        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          itemsIndexed(ranking) { index, item ->

          val color =  when(index+1){
              1 -> Color(0xFFB09650)
              2 -> Color(0xFF7E7B78)
              3 -> Color(0xFF864B3B)
              else -> Color(0xFF333333)
            }
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(Color.Transparent),
              shape = RoundedCornerShape(12.dp)
            ) {
              Row(
                Modifier
                  .fillMaxWidth()
                  .background(
                    Brush.horizontalGradient(
                      colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                      )
                    )
                  )
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Box(
                    Modifier
                      .clip(CircleShape)
                      .size(32.dp)
                      .background(color)
                      .padding(),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      (index + 1).toString() + "º",
                      color = MaterialTheme.colorScheme.background,
                      textAlign = TextAlign.Center,
                    )
                  }
                  Text(
                    item.username,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                  )
                }


                Text("${item.score} PTS", fontWeight = FontWeight.Bold)
              }
            }

          }
        }
        Button(
          onClick = {
            navController.navigate("inicial")
          },
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onSurface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            "VOLTAR À TELA INICIAL",
            modifier = Modifier.padding(6.dp),
            color = MaterialTheme.colorScheme.background
          )
        }

      }
    }
  }

}