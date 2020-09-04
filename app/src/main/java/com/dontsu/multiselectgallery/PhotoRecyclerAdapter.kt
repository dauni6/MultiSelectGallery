package com.dontsu.multiselectgallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_item_recycler.view.*

class PhotoRecyclerAdapter : RecyclerView.Adapter<PhotoRecyclerAdapter.PhotoViewHolder>() {
    var photoList = arrayListOf<Uri>()

    fun updatePhotoList(newList: ArrayList<Uri>) {
        photoList.clear()
        photoList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_item_recycler, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val image = photoList[position]
        holder.setImage(image)
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var currentUri: Uri? = null

        init {
            itemView.deleteBtn.setOnClickListener {
                //해당 이미지 삭제
                if (photoList.contains(currentUri)) {
                    photoList.remove(currentUri)
                    notifyDataSetChanged()
                }
            }
        }

        fun setImage(uri: Uri) {
            itemView.itemImage.loadUrl(uri)
            currentUri = uri
        }

    }


}

