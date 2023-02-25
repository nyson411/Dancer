package app.amano.nayu.dance

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.text.Transliterator
import android.media.audiofx.DynamicsProcessing
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Scene
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import app.amano.nayu.dance.databinding.ActivityMainBinding
import app.amano.nayu.dance.databinding.ViewCircleBinding
import com.google.android.material.animation.Positioning
import kotlin.math.max
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), View.OnTouchListener {
    private lateinit var binding: ActivityMainBinding
    private var pointScene: Int = 1
    val adapter = createSceneAdapter()

    val circleViews = mutableListOf<ViewCircleBinding>()

    lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = AppDatabase.getInstance(this.applicationContext)!!
        stageInsert(1)
        val backgroundList: List<String> = mutableListOf(
            "#dcdcdc", "#6495ed", "#20b2aa", "#008b8b", "#bdb76b", "#fffacd", "#f4a460",
            "#cd5c5c", "#ffa07a", "#ff6347", "#ff69b4", "#ffb6c1", "#da70d6", "#800080", "#9370db"
        )

        var maxScene = Math.max(1, db.stageDao().getUser(1).scene_count)
        Log.d("あ", "maxScene${maxScene}!")

        //Dancerの作成
        var dancers = mutableListOf<DancerEntity>()
        val dancerList: List<DancerEntity> = dancerGetAll()
        for (i in dancerList) {
            dancers.add(i)
        }
        Log.d("あ", dancers.toString())

        for (i in dancers) {
            val circleView = ViewCircleBinding.inflate(
                layoutInflater, binding.root, false
            ).apply {
                root.id = View.generateViewId()
                val position: List<PositionEntity> = getPositions(root.id)
                root.x = position[0].x
                root.y = position[0].y
                root.setOnTouchListener(this@MainActivity)

            }
            Log.d("あああ", i.color)
            circleView.root.backgroundTintList = ColorStateList.valueOf(Color.parseColor(i.color))
            circleViews.add(circleView)
            binding.root.addView(circleView.root)

        }


        //RecyclerViewの設定
        Log.d("ああ", dancers.toString())
        binding.recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerView.layoutManager = layoutManager
        for (i in 1..maxScene) {
            if (i != maxScene) {
                adapter.submitList(Scene(i, false))
            } else {
                adapter.submitList(Scene(i, true))
            }
        }


        //addPeople listener
        binding.addPeopleButton.setOnClickListener {
            var setColor: String = backgroundList.random().toString()
            val circleView = ViewCircleBinding.inflate(
                layoutInflater,
                binding.root,
                false,
            ).apply {
                root.id = View.generateViewId()
                root.x = 500.0f
                root.y = 500.0f


                root.backgroundTintList = ColorStateList.valueOf(Color.parseColor(setColor))
                root.setOnTouchListener(this@MainActivity)

            }
            circleViews.add(circleView)
            dancerInsert("name", setColor, 1)
            positionInsert(circleView.root.id, 500f, 500f)
            dancers = getDancers(1)
            for (i in 2..maxScene) {
                positionInsert(circleView.root.id, 500f, 500f)
            }
            binding.root.addView(circleView.root)

        }
        // データの追加
        binding.sendButton.setOnClickListener {
            val id: Int = binding.idText.text.toString().toInt()
            val x: Float = binding.textX.text.toString().toFloat()
            val y: Float = binding.textY.text.toString().toFloat()
            var positionList: List<PositionEntity> = getPositions(id)
            var position = positionList[pointScene - 1]
            position.x = x
            position.y = y
            positionUpdate(position)
            Log.d("あ", id.toString())
        }

        //startMoveToPointAnimを呼び出す
        binding.playButton.setOnClickListener {
            if (pointScene == maxScene) pointScene = 1
            else pointScene++
            dancers.forEach {
                val position: List<PositionEntity> = getPositions(it.uid)
                binding.root.getViewById(it.uid).startMoveToPointAnim(
                    position[pointScene - 1].x, position[pointScene - 1].y
                )
            }
            adapter.submitList(Scene(pointScene - 1, true))


        }


        //add_scene_buttonのリスナー
        binding.addSceneButton.setOnClickListener {
            maxScene++
            var stageAll: List<StageEntity> = stageGetAll()
            var stage: StageEntity = stageAll[0]
            stage.scene_count = maxScene
            stageUpdate(stage)
            adapter.submitList(Scene(maxScene, true))
            dancers.forEach {
                val lastPosition = getPositions(it.uid).last()
                positionInsert(it.uid, lastPosition.x, lastPosition.y)
            }
            pointScene = maxScene
            Log.d("あ", maxScene.toString())

        }


    }

    //adapterの作成、リスナーの設定
    private fun createSceneAdapter(): ListAdapter {
        val sceneAdapter = ListAdapter(
            this,
            object : ListAdapter.OnItemClickListener {
                override fun onItemClickListener(scene: Int) {
                    pointScene = scene
                    Log.d("aa", pointScene.toString())
                    adapter.submitList(Scene(pointScene - 1, true))
                    Log.d("あ", "pointScene${pointScene}")
                    var position: List<PositionEntity> = positionGetAll()
                    Log.d("あ", position.toString())
                    val dancers: MutableList<DancerEntity> = getDancers(1)
                    dancers.forEach {
                        val position: List<PositionEntity> = getPositions(it.uid)
                        Log.d("あ", position.toString())
                        binding.root.getViewById(it.uid).startMoveToPointAnim(
                            position[pointScene - 1].x, position[pointScene - 1].y
                        )
                    }
                }
            },
        )


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

    //dancerデータの追加
    private fun dancerInsert(name: String, color: String, stageId: Int) {
        val dancer: DancerEntity = DancerEntity(
            name = name,
            color = color,
            stageId = stageId,
        )

        db.dancerDao().insert(dancer)
    }

    //Positionデータの追加
    private fun positionInsert(dancerId: Int, x: Float, y: Float) {
        val position: PositionEntity = PositionEntity(
            dancerId = dancerId,
            x = x,
            y = y,
        )
        db.positionDao().insert(position)
    }

    //Stageデータの追加
    private fun stageInsert(stage_count: Int) {
        val stage: StageEntity = StageEntity(0, "name", stage_count)
        db.stageDao().insert(stage)
    }

    //Positionデータの全部取得
    private fun positionGetAll(): List<PositionEntity> {
        var positionList: List<PositionEntity> = emptyList()
        positionList = db.positionDao().getAll()
        return positionList
    }

    //stageデータの全部取得
    private fun stageGetAll(): List<StageEntity> {
        var stageList: List<StageEntity> = emptyList()
        stageList = db.stageDao().getAll()
        return stageList
    }

    //dancerデータの全部取得
    private fun dancerGetAll(): List<DancerEntity> {
        var dancerList: List<DancerEntity> = emptyList()
        dancerList = db.dancerDao().getAll()
        return dancerList
    }

    //dancer更新
    private fun dancerUpdate(dancer: DancerEntity) {

        db.dancerDao().update(dancer)

    }

    //  position更新
    private fun positionUpdate(position: PositionEntity) {
        db.positionDao().update(position)

    }

    //stage更新
    private fun stageUpdate(stage: StageEntity) {
        db.stageDao().update(stage)
    }

    //position 特定の条件
    private fun getPositions(id: Int): List<PositionEntity> {
        return db.positionDao().getPositions(id)
    }

    //dancer 特定の条件
    private fun getDancers(stageId: Int): MutableList<DancerEntity> {
        return db.dancerDao().getDancers(stageId)
    }


    private var preX by Delegates.notNull<Int>()
    private var preY by Delegates.notNull<Int>()

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        super.onTouchEvent(event)

        val newX = event.rawX
        val newY = event.rawY

        Log.d("ああああ", event.action.toString())

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                preX = circleViews[view.id - 1].root.left
                preY = circleViews[view.id - 1].root.top
            }


            MotionEvent.ACTION_MOVE -> {
                view.performClick()

                val dx = circleViews[view.id - 1].root.left + (newX - preX).toInt()
                val dy = circleViews[view.id - 1].root.top + (newY - preY).toInt()
                val width = dx + circleViews[view.id - 1].root.width
                val height = dy + circleViews[view.id - 1].root.height

                circleViews[view.id - 1].root.layout(dx, dy, width, height)
                Log.d("あああ", "dx${dx},dy${dy},width${width},height${height}")
            }

            MotionEvent.ACTION_UP -> {
                val dx = circleViews[view.id - 1].root.left + (newX - preX).toInt()
                val dy = circleViews[view.id - 1].root.top + (newY - preY).toInt()
                val width = dx + circleViews[view.id - 1].root.width
                val height = dy + circleViews[view.id - 1].root.height
                Log.d("あああ", "dx${dx},dy${dy},preX${preX},preY${preY}")

                val dragPositionList = getPositions(view.id)
                val positionEntity: PositionEntity = dragPositionList[pointScene - 1]
                positionEntity.x = (width - dx) / 2.toFloat()
                positionEntity.y = (height - dy) / 2.toFloat()
                positionUpdate(positionEntity)

                Log.d("あああ", "x${positionEntity.x},y${positionEntity.y}")
                Log.d("あああ", "rawX${event.rawX},rawY${event.rawY}")
            }
        }

        preX = newX.toInt()
        preY = newY.toInt()
        return true
    }


}