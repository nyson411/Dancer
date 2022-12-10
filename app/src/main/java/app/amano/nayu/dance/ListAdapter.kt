package app.amano.nayu.dance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.amano.nayu.dance.databinding.ListItemBinding

class ListAdapter(
    context: Context,
    private val listener :OnItemClickListener
) : RecyclerView.Adapter<FlowerListViewHolder>() {
    val sceneList = mutableListOf(1);

    //作成
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FlowerListViewHolder {
        val view: ListItemBinding =
            ListItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return FlowerListViewHolder(view)
    }

    // ViewHolderの設定
    override fun onBindViewHolder(holder: FlowerListViewHolder, position: Int) {
        holder.binding.root.setOnClickListener {
            listener.onItemClickListener(
                scene = position
            )
        }
        holder.binding.sceneText.text=sceneList[position].toString()
    }

    // ViewHolderの数
    override fun getItemCount(): Int {
        return sceneList.size
    }
    //リスナーの設定
    interface OnItemClickListener {
        fun onItemClickListener(scene: Int)
    }

}