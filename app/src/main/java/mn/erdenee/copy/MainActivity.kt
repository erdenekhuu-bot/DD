package mn.erdenee.copy

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var music: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        music = MediaPlayer.create(this, R.raw.background)
        val btnPlayPause = findViewById<ImageView>(R.id.btnPause)

        btnPlayPause?.setOnClickListener {
            play()
        }
    }

    fun play() {
        music?.let { player ->
            val btnPlayPause = findViewById<ImageView>(R.id.btnPause)
            if (player.isPlaying) {
                player.pause()
                btnPlayPause?.setImageResource(R.drawable.ic_play)
            } else {
                player.start()
                btnPlayPause?.setImageResource(R.drawable.ic_pause)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        music?.release()
        music = null
    }
}
