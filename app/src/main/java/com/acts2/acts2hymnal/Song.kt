@file:OptIn(ExperimentalMaterial3Api::class)

package com.acts2.acts2hymnal

//import androidx.compose.material3.ExperimentalMaterial3Api
import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path


//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongScreen(song: SongData,
               navController: NavHostController,
               context: Context
) {

    var isPlaying by remember { mutableStateOf(false) }
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null)}

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            SongAppBar(song = song, navController = navController, isPlaying = isPlaying) {
                if (mediaPlayer == null) {
                    val afd = context.assets.openFd(song.audio_file)
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                        prepare()
                        setOnCompletionListener { isPlaying = false }
                    }
                }
                if (isPlaying) {
                    mediaPlayer?.pause()
                } else {
                    mediaPlayer?.start()
                }
                isPlaying = !isPlaying
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(WindowInsets.systemBars.asPaddingValues())
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val styledText = buildAnnotatedString {
                song.chunks.forEachIndexed { idx, chunk ->
                    val emphasize = song.isChorus.getOrNull(idx) == true

                    if (emphasize) {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            )
                        ) {
                            append(chunk)
                        }
                    } else {
                        append(chunk)
                    }
                    append("\n")
                }
                append("\n")
                append(song.buildMetadata())
            }
            Text(
                text = styledText,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp
            )
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}


@Composable
fun SongAppBar(
    song: SongData,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit
) {
    TopAppBar(
        title = { Text(text = song.name, fontSize = 24.sp)},
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
            }
        },
        actions = {
            if (song.audio_file != "") {
                IconButton(onClick = onTogglePlay) {
                    Icon(
                        imageVector = if (isPlaying) {
                            PauseCircle24dp
                        } else {
                            Icons.Default.PlayArrow
                        },
                        contentDescription = if (isPlaying) "Pause" else "Play"
                    )
                }
            }
        }

    )
}


val PauseCircle24dp: ImageVector
    get() = ImageVector.Builder(
        name = "PauseCircle24dp", defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color(0xFFE3E3E3)),
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(9f, 16f)
            lineTo(11f, 16f)
            lineTo(11f, 8f)
            lineTo(9f, 8f)
            close()

            moveTo(12f, 2f)
            curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
            reflectiveCurveTo(6.48f, 22f, 12f, 22f)
            reflectiveCurveTo(22f, 17.52f, 22f, 12f)
            reflectiveCurveTo(17.52f, 2f, 12f, 2f)
            close()

            moveTo(12f, 20f)
            curveTo(7.59f, 20f, 4f, 16.41f, 4f, 12f)
            reflectiveCurveTo(7.59f, 4f, 12f, 4f)
            reflectiveCurveTo(20f, 7.59f, 20f, 12f)
            reflectiveCurveTo(16.41f, 20f, 12f, 20f)
            close()

            moveTo(13f, 16f)
            lineTo(15f, 16f)
            lineTo(15f, 8f)
            lineTo(13f, 8f)
            close()
        }
    }.build()


