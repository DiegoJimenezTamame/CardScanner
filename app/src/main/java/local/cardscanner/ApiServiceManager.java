package local.cardscanner;

public class ApiServiceManager {
    private static ScryfallApiService apiService;

    public static ScryfallApiService getScryfallApiService() {
        if (apiService == null) {
            apiService = RetrofitClient.getClient().create(ScryfallApiService.class);
        }
        return apiService;
    }
}
