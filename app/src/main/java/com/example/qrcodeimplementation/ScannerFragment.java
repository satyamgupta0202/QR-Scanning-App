package com.example.qrcodeimplementation;
import android.Manifest;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.budiyev.android.codescanner.CodeScanner;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.GnssAntennaInfo;
import android.location.GpsStatus;
import android.net.sip.SipAudioCall;
import android.net.sip.SipSession;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.VIBRATE;


public class ScannerFragment extends Fragment {
        private CodeScanner mCodeScanner;
        private int REQUEST_CODE_CAMERA_PERMISSION = 1001;
        private boolean mPermissionGranted;
        String lastScannedValue;
        //
      // SipSession.Listener listener;

        //keep empty as Alder App
        public ScannerFragment() {

        }

//        Doubt in the Sipsession.Listener
//        public ScannerFragment(SipSession.Listener listener) {
//            this.listener = listener;
//        }

        public static ScannerFragment newInstance(Bundle args) {
            ScannerFragment fragment = new ScannerFragment();
            fragment.setArguments(args);
            return fragment;
        }



    private boolean checkPermission() {
        int camera_permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             camera_permission = ContextCompat.checkSelfPermission(getActivity(), CAMERA);
            return camera_permission == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

//  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//       if (ContextCompat.checkSelfPermission(getActivity(), CAMERA)!=PackageManager.PERMISSION_GRANTED) {
//            mPermissionGranted = false;
//            requestPermissions(new String[] {Manifest.permission.CAMERA},);
//        } else {
//            mPermissionGranted = true;
//        }
//    }
//   else {
//        mPermissionGranted =false;
//    }

    private void requestPermission() {
        // this method is to request
        // the runtime permission.
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }
//
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // this method is called when user
        // allows the permission to use camera.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean cameraaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (cameraaccepted) {
                Toast.makeText(getActivity(), "Permission granted..", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission Denined \n You cannot use app without providing permssion", Toast.LENGTH_SHORT).show();
            }
        }
    }





        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            if (checkPermission()) {
                // if permission is already granted display a toast message
                Toast.makeText(getActivity(), "Permission Granted.", Toast.LENGTH_SHORT).show();
            } else {
                requestPermission();
            }
            final Activity activity = getActivity();
            View root = inflater.inflate(R.layout.fragment_scanner, container, false);
            CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
            mCodeScanner = new CodeScanner(activity, scannerView);


            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCodeScanner.startPreview();
                }
            });
            return root;
        }




    @Override
        public void onResume() {
            super.onResume();
            if(mPermissionGranted)
            mCodeScanner.startPreview();
        }

        @Override
        public void onPause() {
            mCodeScanner.releaseResources();
            super.onPause();
        }

    }

