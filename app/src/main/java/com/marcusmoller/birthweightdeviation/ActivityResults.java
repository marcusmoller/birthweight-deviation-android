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
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

public class ActivityResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setTitle(getResources().getString(R.string.title_results));

        // insert data from main activity
        Intent intent = getIntent();

        TextView text_sex = findViewById(R.id.text_res_sex); text_sex.setText(intent.getStringExtra("com.marcusmoller.birthweightdeviation.SEX").toLowerCase());
        TextView text_bw_input = findViewById(R.id.text_res_bw_input); text_bw_input.setText(intent.getStringExtra("com.marcusmoller.birthweightdeviation.BW_INPUT") + " " +getResources().getString(R.string.bw_unit));
        TextView text_ga_days = findViewById(R.id.text_res_ga); text_ga_days.setText(intent.getStringExtra("com.marcusmoller.birthweightdeviation.GA_WEEKS") + " (" + intent.getStringExtra("com.marcusmoller.birthweightdeviation.GA_DAYS") + " days)"); // TODO: trans

        TextView text_bw_mean = findViewById(R.id.text_res_bw_mean); text_bw_mean.setText(intent.getStringExtra("com.marcusmoller.birthweightdeviation.BW_MEAN") + " " + getResources().getString(R.string.bw_unit));
        TextView text_perc_dev = findViewById(R.id.text_result_percent); text_perc_dev.setText(intent.getStringExtra("com.marcusmoller.birthweightdeviation.PERC_DEV") + " %");
        TextView text_s_dev = findViewById(R.id.text_res_sd); text_s_dev.setText(intent.getStringExtra("com.marcusmoller.birthweightdeviation.S_DEV"));

        TextView text_bw_status = findViewById(R.id.text_res_status); text_bw_status.setText(intent.getStringExtra("com.marcusmoller.birthweightdeviation.BW_STATUS"));
        if (intent.getStringExtra("com.marcusmoller.birthweightdeviation.BW_STATUS").equals("SGA") || intent.getStringExtra("com.marcusmoller.birthweightdeviation.BW_STATUS").equals("LGA")) {
            text_perc_dev.setTextColor(Color.RED);
            text_s_dev.setTextColor(Color.RED);
            text_bw_status.setTextColor(Color.RED);
        }

        // todo: do HTML formatting and add note characters
    }
}