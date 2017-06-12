package es.dmoral.coloromatic.sample;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import es.dmoral.coloromatic.ColorOMaticDialog;
import es.dmoral.coloromatic.ColorOMaticUtil;
import es.dmoral.coloromatic.IndicatorMode;
import es.dmoral.coloromatic.OnColorSelectedListener;
import es.dmoral.coloromatic.colormode.ColorMode;

public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_COLOR = "extra_color";
    private static final String EXTRA_MODE = "extra_MODE";

    private Spinner spinner;
    private TextView textView;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private CheckBox cbShowTextIndicator;
    private LinearLayout layoutShowTextIndicator;

    private int color;
    private ColorMode mode;
    private boolean showTextIndicator = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textView = (TextView) findViewById(R.id.text_view);
        spinner = (Spinner) findViewById(R.id.spinner);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        cbShowTextIndicator = (CheckBox) findViewById(R.id.cb_text_indicator);
        layoutShowTextIndicator = (LinearLayout) findViewById(R.id.show_text_indicator_container);

        cbShowTextIndicator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showTextIndicator = isChecked;
            }
        });

        layoutShowTextIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbShowTextIndicator.setChecked(!cbShowTextIndicator.isChecked());
            }
        });

        if(savedInstanceState == null) {
            color = ContextCompat.getColor(this, R.color.colorPrimary);
            mode = ColorMode.RGB;

        }
        else {
            color = savedInstanceState.getInt(EXTRA_COLOR);
            mode = ColorMode.values()[savedInstanceState.getInt(EXTRA_MODE)];
        }

        setSupportActionBar(toolbar);

        updateTextView(color);
        updateToolbar(color, color);

        setupSpinner();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(darkenColor(color));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });
    }

    void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        for(ColorMode m : ColorMode.values()) {
            adapter.add(m.name());
        }
        adapter.notifyDataSetChanged();

        spinner.setAdapter(adapter);
        spinner.setSelection(mode.ordinal());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode = ColorMode.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    void updateTextView(int newColor) {
        textView.setText(ColorOMaticUtil.getFormattedColorString(newColor, mode == ColorMode.ARGB));
        textView.setTextColor(newColor);
    }

    void updateToolbar(int oldColor, int newColor) {
        final TransitionDrawable transition = new TransitionDrawable(new ColorDrawable[]{
                new ColorDrawable(oldColor), new ColorDrawable(newColor)
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            toolbar.setBackground(transition);
        } else {
            toolbar.setBackgroundDrawable(transition);
        }

        transition.startTransition(300);
    }

    int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

    private void showColorPickerDialog() {
        IndicatorMode indicatorMode = IndicatorMode.HEX;
        if(mode == ColorMode.HSV) indicatorMode = IndicatorMode.DECIMAL; // cuz HEX is dumb for those

        new ColorOMaticDialog.Builder()
            .initialColor(color)
            .colorMode(mode)
            .indicatorMode(indicatorMode) //HEX or DECIMAL;
            .showColorIndicator(showTextIndicator)
                .isColorIndicatorEditable(true)
            .onColorSelected(new OnColorSelectedListener() {
                @Override public void onColorSelected(int newColor) {
                    updateTextView(newColor);
                    updateToolbar(color, newColor);
                    color = newColor;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(darkenColor(newColor));
                    }
                }
            })
            .create()
            .show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_COLOR, color);
        outState.putInt(EXTRA_MODE, mode.ordinal());
        super.onSaveInstanceState(outState);
    }
}
