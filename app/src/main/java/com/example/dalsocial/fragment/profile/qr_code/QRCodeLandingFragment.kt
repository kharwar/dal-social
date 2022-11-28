package com.example.dalsocial.fragment.profile.qr_code

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dalsocial.R
import com.example.dalsocial.model.user.UserManagement
import io.github.g0dkar.qrcode.QRCode

class QRCodeLanding : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //reference: https://github.com/g0dkar/qrcode-kotlin/blob/main/examples/android/src/main/java/io/github/g0dkar/qrcode/NewQRCodeActivity.kt
        // https://qrcodekotlin.com/

        val view = inflater.inflate(R.layout.fragment_qr_code_landing, container, false)

        val qrCode: Bitmap =
            QRCode(UserManagement().getFirebaseUserID()!!).render().nativeImage() as Bitmap
        val ivQrCode = view.findViewById<ImageView>(R.id.ivQRCode)
        ivQrCode.setImageBitmap(qrCode)

        val btnScanQRCode = view.findViewById<Button>(R.id.btnScanQRCode)
        btnScanQRCode.setOnClickListener {
            findNavController().navigate(R.id.action_QRCodeLanding_to_QRCodeScanFragment)

        }

        // reference: https://stackoverflow.com/questions/5448653/how-to-implement-onbackpressed-in-fragments
        val btnBack = view.findViewById<ImageView>(R.id.backQrCodeLandingBtn)
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

}