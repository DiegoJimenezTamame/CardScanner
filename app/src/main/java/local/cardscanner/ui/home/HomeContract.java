package local.cardscanner.ui.home;

public interface HomeContract {
    interface View {
        void showText(String text);
    }

    interface Presenter {
        void loadData();
    }
}
