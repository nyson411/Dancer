package app.amano.nayu.dance

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Scene
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import app.amano.nayu.dance.databinding.ActivityMainBinding
import app.amano.nayu.dance.databinding.ViewCircleBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Dancerの作成
        val dancers = mutableListOf<Dancer>()
        binding.addPeopleButton.setOnClickListener {
            val circleView = ViewCircleBinding.inflate(
                layoutInflater,
                binding.root,
                false,
            ).apply {
                root.id = View.generateViewId()
                root.x = 100.0f * root.id
                root.y = 100.0f * root.id
                root.setOnClickListener {
                    Log.d("あ", "${root.id} CLICK")
                }

            }
            val positionList = mutableListOf<Position>(
                Position(circleView.root.x, circleView.root.y),
                Position(190f, 100f)
            )

            dancers.add(Dancer(circleView.root.id, "あ", positionList))
            binding.root.addView(circleView.root)
        }
        // データの追加
        binding.sendButton.setOnClickListener {
            val id: Int = binding.idText.text.toString().toInt()
            val x: Float = binding.textX.text.toString().toFloat()
            val y: Float = binding.textY.text.toString().toFloat()
            dancers[id].positionList.add(Position(x, y))

        }

        //startMoveToPointAnimを呼び出す
        var currentScene = 1
        binding.playButton.setOnClickListener {
            var max: Int = 0;
            dancers.forEach() {
                if (max < it.positionList.size) {
                    max = it.positionList.size;
                }
            }
            dancers.forEach {
                var eachScene: Int = 0;
                if (currentScene > it.positionList.size - 1) {
                    eachScene = it.positionList.size - 1
                } else {
                    eachScene = currentScene;
                }
                binding.root.getViewById(it.id).startMoveToPointAnim(
                    it.positionList[eachScene].x, it.positionList[eachScene].y
                )
            }
            currentScene++
            if (currentScene == max) currentScene = 0


        }
        //RecyclerViewの設定
        binding.recyclerView.adapter =
            ListAdapter(this, object : ListAdapter.OnItemClickListener {
            override fun onItemClickListener(scene: Int) {
                dancers.forEach {
                    binding.root.getViewById(it.id).startMoveToPointAnim(
                        it.positionList[scene].x, it.positionList[scene].y
                    )
                }
            }
        })
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerView.layoutManager = layoutManager


    }


    private fun View.startMoveToPointAnim(transX: Float, transY: Float) {
        Log.d("あ", "startMoveToPointAnim")
        val translationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, transX)
        val translationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, transY)
        val animator =
            ObjectAnimator.ofPropertyValuesHolder(this, translationX, translationY).apply {
                duration = 500
                interpolator = DecelerateInterpolator()
            }
        animator.start()
    }

}