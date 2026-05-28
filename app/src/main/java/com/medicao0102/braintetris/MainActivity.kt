package com.medicao0102.braintetris

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets.Type
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.medicao0102.braintetris.ui.theme.BrainTetrisTheme
import androidx.core.content.edit
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class ItemRanking(
  val username: String,
  val score: Int
)

class MainActivity : ComponentActivity() {
  @SuppressLint("SourceLockedOrientationActivity")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    setContent {
      val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        windowInsetsController.hide(Type.systemBars())
      }
      windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

      BrainTetrisTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          Column(
            modifier = Modifier.padding(innerPadding)
          ) {

            val navController = rememberNavController()
            val ctx = LocalContext.current
            val sp = ctx.getSharedPreferences("prefs", MODE_PRIVATE)


            fun getRanking(): List<ItemRanking> {
              val string = sp.getString("ranking", "") ?: ""
              val list = try {
                Json.decodeFromString<List<ItemRanking>>(string)
              } catch (e: Exception) {
                emptyList<ItemRanking>()
              }
              return list
            }

            fun putItemRanking(item: ItemRanking) {
              val list = getRanking().toMutableList()
              list.add(item)
              sp.edit { putString("ranking", Json.encodeToString(list)) }
            }

            NavHost(navController, "splash") {
              composable("splash") {
                Splash({ navController.navigate("inicial") })
              }
              composable("inicial") {
                Inicial({
                  sp.edit { putString("username", it) }; navController.navigate("jogo")
                }, onNavigateToRanking = { navController.navigate("ranking") }, sessionUsername= sp.getString("username", "")?:"")
              }

              composable("jogo") {
                Jogo(ctx, navController, {
                  putItemRanking(
                    ItemRanking(
                      username = sp.getString("username", "Anonimo") ?: "",
                      score = it
                    )
                  )
                })
              }
              composable("ranking") {
                Ranking(navController, getRanking().sortedBy { it.score }.reversed())
              }
            }
          }
        }
      }
    }
  }
}

