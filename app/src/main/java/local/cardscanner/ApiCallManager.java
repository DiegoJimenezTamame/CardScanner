package local.cardscanner;

public class ApiCallManager {
    public void searchCard(String exampleCard, ApiCallManager.ApiCallback apiCallback) {
    }

    public interface ApiCallback {
        void onSuccess(CardResponse card);

        void onError(String error);
    }
}
