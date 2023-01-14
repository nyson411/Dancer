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
    private var pointScene:Int =1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var maxScene =1

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
            val positionList = mutableListOf<Position>()
            dancers.add(Dancer(circleView.root.id, "あ", positionList))
            for(i in 0..maxScene) {
                positionList.add(Position(circleView.root.x,circleView.root.y))
            }
            binding.root.addView(circleView.root)
        }
        // データの追加
        binding.sendButton.setOnClickListener {
            val id: Int = binding.idText.text.toString().toInt()
            val x: Float = binding.textX.text.toString().toFloat()
            val y: Float = binding.textY.text.toString().toFloat()
            dancers[id].positionList[pointScene-1]=Position(x, y)

        }

        //startMoveToPointAnimを呼び出す
        var currentScene = 0
        binding.playButton.setOnClickListener {

            dancers.forEach {
                binding.root.getViewById(it.id).startMoveToPointAnim(
                    it.positionList[currentScene].x, it.positionList[currentScene].y
                )
            }
            currentScene++
            if(currentScene==maxScene) currentScene=0;
            Log.d("あ",currentScene.toString())


        }
        //RecyclerViewの設定

        val adapter = createSceneAdapter(dancers)
        binding.recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerView.layoutManager = layoutManager
        adapter.submitList(maxScene)


        //add_scene_buttonのリスナー
        binding.addSceneButton.setOnClickListener{
            Log.d("あ","addSceneButton")
            maxScene++
            adapter.submitList(maxScene)
            dancers.forEach{
                it.positionList.add(it.positionList[maxScene-2])
            }
            pointScene=maxScene
            Log.d("あ",maxScene.toString())

        }


    }
    //adapterの作成、リスナーの設定
    private fun createSceneAdapter(dancers:List<Dancer>):ListAdapter{
        val sceneAdapter=ListAdapter(this, object : ListAdapter.OnItemClickListener {
              override fun onItemClickListener(scene: Int) {
                  pointScene=scene

                 dancers.forEach {
                      binding.root.getViewById(it.id).startMoveToPointAnim(
                         it.positionList[scene-1].x, it.positionList[scene-1].y
                      )
                 }
              }
         }, )


        return sceneAdapter
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