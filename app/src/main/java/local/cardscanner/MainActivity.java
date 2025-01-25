package local.cardscanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private ImageView capturedCardImageView;
    private CardScannerViewModel viewModel;

    // Permission and camera launchers
    private ActivityResultLauncher<String> permissionLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        capturedCardImageView = findViewById(R.id.capturedCardImageView);
        Button scanCardButton = findViewById(R.id.scanCardButton);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CardScannerViewModel.class);

        // Initialize camera launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getExtras() != null) {
                            Bitmap cardImage = (Bitmap) data.getExtras().get("data");
                            capturedCardImageView.setImageBitmap(cardImage);
                            viewModel.processCardImage(cardImage);
                        }
                    }
                }
        );

        // Initialize permission launcher
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Permission granted, open the camera
                        openCamera();
                    } else {
                        // Permission denied, show a toast
                        Toast.makeText(this, "Camera permission is required to scan cards.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Setup scan button
        scanCardButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, open the camera
                openCamera();
            } else {
                // Request camera permission
                permissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        // Observe card recognition results
        observeCardRecognitionResults();
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(cameraIntent);
        } else {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    private void observeCardRecognitionResults() {
        viewModel.getRecognizedCard().observe(this, cardResponse -> {
            if (cardResponse != null) {
                Intent detailIntent = new Intent(this, CardDetailActivity.class);
                detailIntent.putExtra("CARD_NAME", cardResponse.getName());
                startActivity(detailIntent);
            }
        });

        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
