package local.cardscanner.ui.home;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;
    private HomeModel model;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
        model = new HomeModel();
    }

    @Override
    public void loadData() {
        String data = model.fetchData();
        view.showText(data); // Pass the data to the View
    }
}
