package com.example.portfolioapp;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CompoundButton;

        import com.google.android.material.chip.Chip;

        import java.sql.Array;
        import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    private Chip chip4,chip5,chip6,chip7,chip8,chip9,chip10,chip11,chip12,chip13;
    private Button Apply;
    private ArrayList<String> selectedChipData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        chip4 = findViewById(R.id.chip4);
        chip5 = findViewById(R.id.chip5);
        chip6 = findViewById(R.id.chip6);
        chip7 = findViewById(R.id.chip7);
        chip8 = findViewById(R.id.chip8);
        chip9 = findViewById(R.id.chip9);
        chip10 = findViewById(R.id.chip10);
        chip11 = findViewById(R.id.chip11);
        chip12 = findViewById(R.id.chip12);
        chip13 = findViewById(R.id.chip13);

        selectedChipData = new ArrayList<>();

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    selectedChipData.add(buttonView.getText().toString());
                }
                else
                {
                    selectedChipData.remove(buttonView.getText().toString());
                }
            }
        };

        chip4.setOnCheckedChangeListener(checkedChangeListener);
        chip5.setOnCheckedChangeListener(checkedChangeListener);
        chip6.setOnCheckedChangeListener(checkedChangeListener);
        chip7.setOnCheckedChangeListener(checkedChangeListener);
        chip8.setOnCheckedChangeListener(checkedChangeListener);
        chip9.setOnCheckedChangeListener(checkedChangeListener);
        chip10.setOnCheckedChangeListener(checkedChangeListener);
        chip11.setOnCheckedChangeListener(checkedChangeListener);
        chip12.setOnCheckedChangeListener(checkedChangeListener);
        chip13.setOnCheckedChangeListener(checkedChangeListener);

        Apply = findViewById(R.id.Apply);
        Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("data",selectedChipData.toString());
                setResult(101,resultIntent);
                finish();
            }
        });
    }
}