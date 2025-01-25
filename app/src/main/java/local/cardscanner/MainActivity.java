package local.cardscanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import retrofit2.Call;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private ImageView capturedCardImageView;
    private final CardScannerViewModel viewModel;

    // Permission and camera launchers
    private ActivityResultLauncher<String> permissionLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    public MainActivity(ImageView capturedCardImageView, CardScannerViewModel viewModel) {
        this.capturedCardImageView = capturedCardImageView;
        this.viewModel = viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        capturedCardImageView = findViewById(R.id.capturedCardImageView);
        Button scanCardButton = findViewById(R.id.scanCardButton);

        // Initialize ViewModel
        //viewModel = new ViewModelProvider(this).get(CardScannerViewModel.class);

        // Initialize camera launcher
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getExtras() != null) {
                            Bitmap cardImage = (Bitmap) data.getExtras().get("data");
                            capturedCardImageView.setImageBitmap(cardImage);
                            viewModel.processCardImage();
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

    ProgressBar progressBar = findViewById(R.id.progressBar);
    private void observeCardRecognitionResults() {
        progressBar.setVisibility(View.VISIBLE);
        viewModel.getRecognizedCardName().observe(this, cardName -> {
            progressBar.setVisibility(View.GONE);
            if (cardName != null) {
                fetchCardDetails((String) cardName);
            }
        });

        viewModel.getErrorMessage().observe(this, errorMessage -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchCardDetails(String cardName) {
        ScryfallApiService apiService = ApiServiceManager.getScryfallApiService();
        apiService.searchCards(cardName, "exact").enqueue(new retrofit2.Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent detailIntent = new Intent(MainActivity.this, CardDetailActivity.class);
                    detailIntent.putExtra("CARD_RESPONSE", (CharSequence) response.body());
                    startActivity(detailIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Card not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





}
