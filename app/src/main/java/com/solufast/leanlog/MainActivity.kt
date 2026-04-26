package com.solufast.leanlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.solufast.leanlog.data.seedIfEmpty
import com.solufast.leanlog.data.repository.BodyMetricRepository
import com.solufast.leanlog.data.repository.WorkoutRepository
import com.solufast.leanlog.ui.navigation.LeanLogNavGraph
import com.solufast.leanlog.ui.theme.LeanLogTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Seed representative data on first launch (no-op if data already exists)
        lifecycleScope.launch {
            seedIfEmpty(
                WorkoutRepository(applicationContext),
                BodyMetricRepository(applicationContext)
            )
        }

        setContent {
            LeanLogTheme {
                LeanLogNavGraph()
            }
        }
    }
}
