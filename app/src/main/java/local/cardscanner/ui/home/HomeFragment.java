package local.cardscanner.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import local.cardscanner.R;
import local.cardscanner.databinding.FragmentHomeBinding;
public class HomeFragment extends Fragment implements HomeContract.View {

    private TextView textView;
    private HomePresenter presenter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        textView = rootView.findViewById(R.id.textHome);
        presenter = new HomePresenter(this); // Pass this Fragment as the View
        presenter.loadData();
        return rootView;
    }

    @Override
    public void showText(String text) {
        textView.setText(text);
    }
}

