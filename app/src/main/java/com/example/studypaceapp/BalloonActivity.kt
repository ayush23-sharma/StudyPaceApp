package com.example.studypaceapp

import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class BalloonActivity : AppCompatActivity() {

    private var currentLevel = 1
    private lateinit var balloonImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balloon)

        balloonImageView = findViewById(R.id.balloonImageView)

        updateBalloonImage()

        findViewById<Button>(R.id.doTasksButton).setOnClickListener {
            startActivity(Intent(this, TodoListActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val completedTasks = TaskManager.getTaskStatus().count { it }

        if (completedTasks >= 8) { // Check for completion of 8 tasks
            triggerBurstEffect()
        } else {
            currentLevel = completedTasks + 1
            updateBalloonImage()
        }
    }

    private fun updateBalloonImage() {
        val imageRes = when (currentLevel) {
            1 -> R.drawable.balloon_level1
            2 -> R.drawable.balloon_level2
            3 -> R.drawable.balloon_level3
            else -> R.drawable.balloon_level4
        }

        balloonImageView.setImageResource(imageRes)
        resizeBalloonImage()
    }

    private fun resizeBalloonImage() {
        val newSize = when (currentLevel) {
            1 -> 150
            2 -> 200
            3 -> 200
            4 -> 200
            5 -> 250
            6 -> 300
            7 -> 350
            else -> 350
        }

        val density = resources.displayMetrics.density
        val pixelSize = (newSize * density).toInt()

        val layoutParams = balloonImageView.layoutParams
        layoutParams.width = pixelSize
        layoutParams.height = pixelSize
        balloonImageView.layoutParams = layoutParams
    }

    private fun triggerBurstEffect() {
        // Play distorted balloon sequence before bursting
        val distortedImages = listOf(
            R.drawable.balloon_fragment1, // Highly distorted
            R.drawable.balloon_fragment2  // About to burst
        )

        var index = 0
        val handler = Handler(Looper.getMainLooper())
        val distortedRunnable = object : Runnable {
            override fun run() {
                if (index < distortedImages.size) {
                    balloonImageView.setImageResource(distortedImages[index])
                    index++
                    handler.postDelayed(this, 200) // Show each distorted balloon for 200ms
                } else {
                    // Show the burst fragments
                    playFragmentAnimation()
                }
            }
        }
        handler.post(distortedRunnable)
    }

    private fun playFragmentAnimation() {
        balloonImageView.setImageResource(R.drawable.balloon_fragment3) // Show fragments

        // Shrink effect to simulate burst
        val shrinkAnimatorX = ObjectAnimator.ofFloat(balloonImageView, "scaleX", 1f, 0.2f)
        val shrinkAnimatorY = ObjectAnimator.ofFloat(balloonImageView, "scaleY", 1f, 0.2f)
        val animatorSet = AnimatorSet()

        animatorSet.playTogether(shrinkAnimatorX, shrinkAnimatorY)
        animatorSet.duration = 500 // Shrink over 500ms
        animatorSet.start()

        // Transition to the reward screen after showing fragments
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, RewardActivity::class.java))
            finish()
        }, 800) // Wait 800ms to let the burst effect play out
    }
}





