package com.dontsu.multiselectgallery

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dontsu.multiselectgallery.databinding.ActivityMainBinding
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

/**
 * 실행순서
 * 1. 이미지추가 아이콘을 클릭한다.
 * 2. 클릭하면 권한설정을 요구한다.
 * 3. 여러개의 이미지를 선택할 수 있도록 한다.
 * 4. 받은 여러개의 사진 URI를 RecyclerView에 보내어 이미지를 출력시킨다
 * 5. 뷰홀더의 뷰에서 x뷰를 클릭하면 해당 이미지는 사라진다.
 * */

class MainActivity : AppCompatActivity() {

    lateinit var photoVM: PhotoViewModel

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private lateinit var adapter: PhotoRecyclerAdapter

    init{
        //timber initialize
        Timber.plant(Timber.DebugTree())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        binding.lifecycleOwner = this@MainActivity
        photoVM = ViewModelProvider(this@MainActivity, ViewModelProvider.NewInstanceFactory()).get(PhotoViewModel::class.java)
        binding.viewModel = photoVM

        adapter = PhotoRecyclerAdapter()
        recycler_photo.adapter = adapter
        adapter.photoVM = photoVM

        addBtn.setOnClickListener {
            if (checkPermission()) {
                openGallery()
            }
        }

    }

    private fun openGallery() {
        /*val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, 200)*/
        Matisse.from(this@MainActivity)
            .choose(MimeType.ofAll())
            .countable(true)
            .maxSelectable(10)
            .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.album_item_height))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .showPreview(false)
            .forResult(200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 200) {
            data?.let {
                //리사이클러뷰에 보내기
                val selected = Matisse.obtainResult(data) as ArrayList<Uri>
                adapter.updatePhotoList(selected)
                photo_count.text = "${selected.size}/10"
            }
        }
    }

    private fun checkPermission() : Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                val permissionState = ContextCompat.checkSelfPermission(this@MainActivity, permission)
                if (permissionState != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this@MainActivity, permissions, 369)
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            369 -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "저장소 권한을 승인해야 이미지 등록이 가능합니다", Toast.LENGTH_SHORT).show()
                    return
                }
                openGallery()
            }
        }
    }

    fun showImageListCount(count: String) {
        photo_count.text = count
    }

}
