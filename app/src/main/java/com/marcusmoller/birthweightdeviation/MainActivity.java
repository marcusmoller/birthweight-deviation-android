/*
 * Copyright (C) Marcus Møller 2020.
 *
 * Birth weight deviation is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Birth weight deviation is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Birth weight deviation.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.marcusmoller.birthweightdeviation;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import android.os.Bundle;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private NumberPicker picker_ga_week;
    private NumberPicker picker_ga_day;
    private EditText input_bw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init input
        initNumberPickers();
        input_bw = findViewById(R.id.input_bw);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent intent = new Intent(this, ActivityAbout.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initNumberPickers() {
        /**
         * Initialize numberpickers for gestational age week and day
         */
        picker_ga_week = findViewById(R.id.input_ga_week);
        picker_ga_day = findViewById(R.id.input_ga_day);

        picker_ga_week.setMinValue(22);
        picker_ga_week.setMaxValue(44);
        picker_ga_week.setValue(33);
        picker_ga_week.setWrapSelectorWheel(false);


        picker_ga_day.setMaxValue(6);
        picker_ga_day.setMinValue(0);
        picker_ga_day.setValue(0);
        picker_ga_day.setWrapSelectorWheel(false);
    }

    public void calculate(View view) {
        /**
         * Do calculations
         */
        Integer ga_weeks = picker_ga_week.getValue();
        Integer ga_days = picker_ga_day.getValue();

        // check if input is correct
        Integer bw_input;
        if (TextUtils.isEmpty(input_bw.getText())) {
            input_bw.setError(getResources().getString(R.string.error_bw_null));
            return;
        } else {
            bw_input = Integer.parseInt(String.valueOf(input_bw.getText()));
            if (bw_input < 300 || bw_input > 7000) {
                input_bw.setError(getResources().getString(R.string.error_bw_interval));
                return;
            }
        }

        // calculate gestational age in days
        Integer ga = (ga_weeks * 7) + ga_days;

        // calculate mean birth weight based on sex
        // based on calculations by Marsál K et al 1996 (https://pubmed.ncbi.nlm.nih.gov/8819552/)
        RadioGroup radiogroup_sex = (RadioGroup) findViewById(R.id.radio_sex);
        int radio_button_id = radiogroup_sex.getCheckedRadioButtonId();
        Double bw;
        if (radio_button_id == R.id.rbtn_male) { // MALE
            bw = -1.907345e-6 * Math.pow(ga, 4) +
                    1.140644e-3 * Math.pow(ga, 3) +
                    -1.336265e-1 * Math.pow(ga, 2) +
                    (1.976961e+0) * ga +
                    2.410053e+2;
        } else { // FEMALE
            bw = -2.761948e-6*Math.pow(ga, 4) +
                    1.744841e-3*Math.pow(ga, 3) +
                    -2.893626e-1*Math.pow(ga, 2) +
                    (1.891197e+1)*ga +
                    -4.135122e+2;
        }

        // calculate birth weight deviation from mean birth weight at given sex and gestational age
        Double percent_deviation = ((bw_input/bw)-1)*100;
        Double sdev              = percent_deviation/12;

        // send results to next activity
        Intent intent = new Intent(this, ActivityResults.class);

        if (radio_button_id == R.id.rbtn_male) {
            intent.putExtra("com.marcusmoller.birthweightdeviation.SEX", getResources().getString(R.string.gender_male));
        } else {
            intent.putExtra("com.marcusmoller.birthweightdeviation.SEX", getResources().getString(R.string.gender_female));
        }
        intent.putExtra("com.marcusmoller.birthweightdeviation.BW_INPUT", Integer.toString(bw_input));

        intent.putExtra("com.marcusmoller.birthweightdeviation.GA_DAYS", Integer.toString(ga));
        intent.putExtra("com.marcusmoller.birthweightdeviation.GA_WEEKS", Integer.toString(ga_weeks) + "+" + Integer.toString(ga_days));
        intent.putExtra("com.marcusmoller.birthweightdeviation.BW_MEAN", String.format("%.0f", bw));
        intent.putExtra("com.marcusmoller.birthweightdeviation.PERC_DEV", String.format("%.1f", percent_deviation));
        intent.putExtra("com.marcusmoller.birthweightdeviation.S_DEV", String.format("%.2f", sdev));
        if (sdev <= -2) {           /* SGA */
            intent.putExtra("com.marcusmoller.birthweightdeviation.BW_STATUS", "SGA");
        } else if (sdev >= 2) {     /* LGA */
            intent.putExtra("com.marcusmoller.birthweightdeviation.BW_STATUS", "LGA");
        } else {                    /* AGA */
            intent.putExtra("com.marcusmoller.birthweightdeviation.BW_STATUS", "AGA");
        }
        startActivity(intent);
    }


}

