package jj.home.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class PopUp extends AppCompatActivity {
    String TagiOrazNazwy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pop_up);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TagiOrazNazwy = extras.getString("TagiOrazNazwy");
        }
        DisplayMetrics rozmiarWyswietlacza = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(rozmiarWyswietlacza);

        int szerokosc = rozmiarWyswietlacza.widthPixels;
        int wysokosc = rozmiarWyswietlacza.heightPixels;

        getWindow().setLayout((int)(szerokosc*0.7),(int) (wysokosc*0.5));

        TextView text = findViewById(R.id.textView4);

        text.setText(TagiOrazNazwy);
    }
}
