package app.amano.nayu.dance

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.amano.nayu.dance.databinding.ListItemBinding
import kotlin.math.max

class ListAdapter(
    context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<FlowerListViewHolder>() {
    val sceneList = mutableListOf<Scene>()

    //作成
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FlowerListViewHolder {
        val view: ListItemBinding =
            ListItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return FlowerListViewHolder(view)
    }

    // ViewHolderの設定
    override fun onBindViewHolder(holder: FlowerListViewHolder, position: Int) {
        Log.d("ああ",position.toString())
        Log.d("ああa",sceneList[position].id.toString())
        if (sceneList[position].isSelected) {
            holder.binding.root.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#D7B2C1"))
        } else {
            holder.binding.root.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#F2F2F2"))

        }
        holder.binding.root.setOnClickListener {
            listener.onItemClickListener(
                scene = position + 1


            )
        }
        holder.binding.sceneText.text = sceneList[position].id.toString()
    }

    // ViewHolderの数
    override fun getItemCount(): Int {
        return sceneList.size
    }

    //リスナーの設定
    interface OnItemClickListener {
        fun onItemClickListener(scene: Int)
    }

    // 新しいリストを受け取り、sceneList にセットする
    fun submitList(maxScene: Scene) {
        for (scene in sceneList) {
            scene.isSelected = false
            Log.d("あ", sceneList.toString())
        }
        if (maxScene.id > sceneList.size) {
            sceneList.add(maxScene)
        } else {
            sceneList[maxScene.id] = Scene(maxScene.id+1,maxScene.isSelected)
        }

        notifyDataSetChanged()
        Log.d("あ", "ListAdapter.submitList")
    }

}