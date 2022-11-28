package com.example.dalsocial.fragment.profile.qr_code

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.dalsocial.R

class QRCodeScanFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_q_r_code_scan, container, false)

        //reference: https://github.com/yuriy-budiyev/code-scanner

        val qrCodeScanner = view.findViewById<CodeScannerView>(R.id.qrCodeScanner)
        val codeScanner = CodeScanner(requireContext(), qrCodeScanner)

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.scanMode = ScanMode.CONTINUOUS

        codeScanner.startPreview()

        // reference: https://stackoverflow.com/questions/16425146/runonuithread-in-fragment
        codeScanner.decodeCallback = DecodeCallback {
            requireActivity().runOnUiThread {
                val bundle = Bundle()
                bundle.putString("userId", it.text)
                findNavController().navigate(R.id.action_QRCodeScanFragment_to_QRCodeScannedFragment2, bundle)
            }
        }

        return view
    }
}