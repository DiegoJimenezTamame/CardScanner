package local.cardscanner;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class CardDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        TextView cardNameTextView = findViewById(R.id.cardNameTextView);
        TextView cardTextTextView = findViewById(R.id.cardTextTextView);
        ImageView cardImageView = findViewById(R.id.cardImageView);

        CardResponse card = (CardResponse) getIntent().getSerializableExtra("CARD_RESPONSE");
        if (card != null) {
            // Display card details
            cardNameTextView.setText(card.getName());
            cardTextTextView.setText(card.getText());
            if (card.getImageUrl() != null) {
                Glide.with(this).load(card.getImageUrl()).into(cardImageView);
            }
        }
    }
}

