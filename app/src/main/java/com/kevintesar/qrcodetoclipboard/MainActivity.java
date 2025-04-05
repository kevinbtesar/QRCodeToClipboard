package com.kevintesar.qrcodetoclipboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    Intent intentActionMain = new Intent(Intent.ACTION_MAIN);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intentActionMain.addCategory(Intent.CATEGORY_HOME);
        intentActionMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        new IntentIntegrator(this).setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class).initiateScan();

    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(result != null) {
            //If scan gets cancelled or if there's a problem scanning
            if(result.getContents() == null) {
                Log.d("MainAction", "Cancelled scan");
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {
                //the old way to put things in android's clipboard
                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(result.getContents());
                } else {
                    //modern approach of writing to clipboard
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", result.getContents());
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(this, "QR Code To Clipboard Successful! Clipboard: "+ result.getContents(), Toast.LENGTH_LONG).show();
                Log.d("MainActivity", "Scanned");
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, intent);
        }

        this.finish();
        startActivity(intentActionMain);
    }
}
