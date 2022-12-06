package com.example.jubgging_nav

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.jubgging_nav.databinding.FragmentCameraBinding
import com.google.firebase.database.FirebaseDatabase

class CameraFragment : Fragment() {

    lateinit var binding: FragmentCameraBinding

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference

    private val IMAGE_CAPTURE_CODE = 1001
    private var imageUri: Uri? = null
    private var imageView: ImageView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = binding.imageviewPicture


        binding.buttonTakePicture.setOnClickListener {
            // 카메라 실행
                openCameraInterface()
        }

        binding.btnPictureYes.setOnClickListener {
            // firebase에 score 1 추가 후 plogging화면전환
           databaseReference.child("User").child(i.toString()).child("score").setValue(++score)
           findNavController().navigate(R.id.action_cameraFragment_to_PloggingMapsFragment)
        }

        binding.btnPictureNo.setOnClickListener {
            // plogging화면전환
            findNavController().navigate(R.id.action_cameraFragment_to_PloggingMapsFragment)
        }


    }

    private fun openCameraInterface() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, R.string.take_picture)
        values.put(MediaStore.Images.Media.DESCRIPTION, R.string.take_picture_description)
        imageUri = activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        // 카메라 인텐트 생성
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        // 인텐트 실행
        startActivityForResult(intent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 카메라 인텐트 콜백
        if (resultCode == Activity.RESULT_OK){
            // 촬영성공시
            imageView?.setImageURI(imageUri)
        }
        else {
            // 촬영실패시 경고
            showAlert("사진 촬영에 실패했습니다.")
        }
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(activity as Context)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.ok_button_title, null)

        val dialog = builder.create()
        dialog.show()
    }
}