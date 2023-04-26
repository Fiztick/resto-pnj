package com.pnj.resto.data.makanan

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pnj.resto.MakananActivity
import com.pnj.resto.R
import com.pnj.resto.data.RestoDatabase
import com.pnj.resto.databinding.FragmentAddMakananBinding
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.security.auth.callback.PasswordCallback

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddMakananFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddMakananFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentAddMakananBinding? = null
    private val binding get() = _binding!!

    private val REQ_CAM = 100
    private var dataGambar: Bitmap? = null
    private var saved_image_url: String = ""

    private val STORAGE_PERMISSION_CODE = 102
    private val TAG = "PERMISSION_TAG"

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddMakananBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun addMakanan() {
        val nama_makanan = binding.TxtNama.text.toString()
        val jenis = binding.TxtJenis.text.toString()
        val harga = binding.TxtHarga.text.toString().toDouble()

        lifecycleScope.launch {
            val makanan = Makanan(nama_makanan, jenis, harga, saved_image_url)
            RestoDatabase(requireContext()).getMakananDao().addMakanan(makanan)
        }
        dismiss()
    }

    fun saveMediaToStorage(bitmap: Bitmap): String {
        //Generate Nama File
        val filename = "${System.currentTimeMillis()}.jpg"
        //Output Stream
        var fos: OutputStream? = null
        var image_save = ""

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getting the contentResolver
            activity?.contentResolver?.also { resolver ->
                //Content resolver will process the contentValues
                val contentValues = ContentValues().apply {
                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
            }
        }
        else {
            //These for devices running on android < Q
            val permission = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE)
            }

            val imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imageDir, filename)
            fos = FileOutputStream(image)

            image_save = "${Environment.DIRECTORY_PICTURES}/${filename}"
        }

        fos?.use {bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)}
        return image_save
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CAM && resultCode == AppCompatActivity.RESULT_OK) {
            dataGambar = data?.extras?.get("data") as Bitmap
            val image_save_uri: String = saveMediaToStorage(dataGambar!!)
            binding.BtnImgMakanan.setImageBitmap(dataGambar)
            saved_image_url = image_save_uri
        }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            this.activity?.packageManager?.let {
                intent?.resolveActivity(it).also {
                    startActivityForResult(intent, REQ_CAM)
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MakananActivity?)?.loadDataMakanan()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.BtnImgMakanan.setOnClickListener {
            openCamera()
        }

        binding.BtnAddMakanan.setOnClickListener {
            if(saved_image_url != "") {
                addMakanan()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddMakananFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddMakananFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}