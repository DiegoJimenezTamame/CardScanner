package local.cardscanner;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CardScannerViewModel extends ViewModel {
    private MutableLiveData<CardResponse> recognizedCard = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private ApiCallManager apiCallManager = new ApiCallManager();

    public void processCardImage(Bitmap cardImage) {
        // Placeholder for image recognition logic
        // This would typically involve ML Kit or another image recognition service
        apiCallManager.searchCard("Example Card", new ApiCallManager.ApiCallback() {
            @Override
            public void onSuccess(CardResponse card) {
                recognizedCard.setValue(card);
            }

            @Override
            public void onError(String error) {
                errorMessage.setValue(error);
            }
        });
    }

    public LiveData<CardResponse> getRecognizedCard() {
        return recognizedCard;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}