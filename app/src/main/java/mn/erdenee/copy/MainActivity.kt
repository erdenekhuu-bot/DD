package mn.erdenee.copy

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var music: MediaPlayer? = null
    private var currentResId: Int? = null
    private var currentArtist: String = ""
    private lateinit var miniPlayer: MaterialCardView
    private lateinit var podCast: MaterialCardView
    private lateinit var steaveCard: MaterialCardView
    private lateinit var playerProgress: ProgressBar
    private lateinit var btnPlayPause: ImageView
    private lateinit var playTitle: TextView
    private lateinit var playSub: TextView

    private val progressHandler = Handler(Looper.getMainLooper())
    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            music?.let { player ->
                if (player.isPlaying) {
                    playerProgress.progress = player.currentPosition
                    updateTimeDisplay(player.currentPosition, player.duration)
                    progressHandler.postDelayed(this, 500)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        miniPlayer = findViewById(R.id.miniPlayerCard)
        podCast = findViewById(R.id.podcastCard)
        steaveCard = findViewById(R.id.steaveCard)
        playerProgress = findViewById(R.id.playerProgressBar)
        btnPlayPause = findViewById(R.id.btnPause)
        playTitle = findViewById(R.id.tvPlayerTitle)
        playSub = findViewById(R.id.tvPlayerSubtitle)

        podCast.setOnClickListener {
            playTrack(
                resId = R.raw.taro,
                title = getString(R.string.ep1_title),
                artist = "Julian Vane",
            )
        }

        steaveCard.setOnClickListener {
            playTrack(
                resId = R.raw.steve,
                title = getString(R.string.ep2_title),
                artist = "Steve Jobs",
            )
        }

        btnPlayPause.setOnClickListener {
            togglePlayPause()
        }
    }

    private fun playTrack(resId: Int, title: String, artist: String) {
        miniPlayer.visibility = View.VISIBLE

        if ((currentResId == resId) && (music != null)) {
            if (music?.isPlaying == false) {
                music?.start()
                btnPlayPause.setImageResource(R.drawable.ic_pause)
                progressHandler.post(updateProgressRunnable)
            }
            return
        }

        stopAndReleasePlayer()
        currentResId = resId
        currentArtist = artist
        playTitle.text = title
        music = MediaPlayer.create(this, resId).apply {
            playerProgress.max = duration
            playerProgress.progress = 0
            updateTimeDisplay(0, duration)
            setOnCompletionListener {
                btnPlayPause.setImageResource(R.drawable.ic_play)
                playerProgress.progress = 0
                updateTimeDisplay(0, duration)
                progressHandler.removeCallbacks(updateProgressRunnable)
            }
            start()
            btnPlayPause.setImageResource(R.drawable.ic_pause)
            progressHandler.post(updateProgressRunnable)
        }
    }

    private fun togglePlayPause() {
        music?.let { player ->
            if (player.isPlaying) {
                player.pause()
                btnPlayPause.setImageResource(R.drawable.ic_play)
                progressHandler.removeCallbacks(updateProgressRunnable)
            } else {
                player.start()
                btnPlayPause.setImageResource(R.drawable.ic_pause)
                progressHandler.post(updateProgressRunnable)
            }
        }
    }

    private fun updateTimeDisplay(currentMs: Int, totalMs: Int) {
        val currentStr = formatTime(currentMs)
        val totalStr = formatTime(totalMs)
        playSub.text = getString(R.string.player_subtitle_format, currentArtist, currentStr, totalStr)
    }

    private fun formatTime(millis: Int): String {
        val totalSeconds = (millis / 1000).coerceAtLeast(0)
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    private fun stopAndReleasePlayer() {
        progressHandler.removeCallbacks(updateProgressRunnable)
        music?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        music = null
    }

    override fun onStart() {
        super.onStart()
        if (music == null) {
            miniPlayer.visibility = View.INVISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        progressHandler.removeCallbacks(updateProgressRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAndReleasePlayer()
    }
}
