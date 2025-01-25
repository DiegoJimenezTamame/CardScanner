package local.cardscanner;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
//import com.google.mlkit.vision.label.ImageLabelerOptions;

public class CardScannerViewModel extends ViewModel {
    private final MutableLiveData<CardResponse> recognizedCard = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private Bitmap bitmap;

    public LiveData<CardResponse> getRecognizedCard() {
        return recognizedCard;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void processCardImage() {
        // Convert Bitmap to ML Kit InputImage
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        // Use ML Kit's default image labeling model
        /*ImageLabelerOptions options = new ImageLabelerOptions.Builder()
                .setConfidenceThreshold(0.7f)
                .build();
        ImageLabeler labeler = ImageLabeling.getClient(options);*/

        // Process the image
        /*labeler.process(image)
                .addOnSuccessListener(labels -> {
                    for (ImageLabel label : labels) {
                        String labelText = label.getText();
                        float confidence = label.getConfidence();
                        Log.d("CardScanner", "Label: " + labelText + " Confidence: " + confidence);

                        // Example: Match the label to the Scryfall API
                        if (confidence > 0.8) { // Confidence threshold
                            fetchCardDetailsFromScryfall(labelText);
                            break;
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("CardScanner", "Labeling failed", e);
                    errorMessage.setValue("Card recognition failed. Please try again.");
                });*/
    }

    private void fetchCardDetailsFromScryfall(String cardName) {
        // Use RetrofitClient to call Scryfall API
        ScryfallApiService apiService = RetrofitClient.getClient().create(ScryfallApiService.class);

        apiService.getCardByName(cardName).enqueue(new retrofit2.Callback<CardResponse>() {
            @Override
            public void onResponse(@NonNull retrofit2.Call<CardResponse> call, @NonNull retrofit2.Response<CardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recognizedCard.setValue(response.body());
                } else {
                    errorMessage.setValue("No card data found.");
                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<CardResponse> call, @NonNull Throwable t) {
                Log.e("CardScanner", "API call failed", t);
                errorMessage.setValue("Failed to fetch card data.");
            }
        });
    }

    public LiveData<Object> getRecognizedCardName() {
        return null;
    }
}
