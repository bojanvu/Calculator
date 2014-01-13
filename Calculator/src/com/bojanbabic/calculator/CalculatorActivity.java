package com.bojanbabic.calculator;

import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CalculatorActivity extends Activity implements OnClickListener {

	private TextView mCalculatorDisplay;
	private Boolean userIsInTheMiddleOfTypingANumber = false;
	private CalculatorBrain mCalculatorBrain;
	private static final String DIGITS = "0123456789.";
	DecimalFormat df = new DecimalFormat("@###########");

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// hide the status bar and other OS-level chrome
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculator);

		mCalculatorBrain = new CalculatorBrain();
		mCalculatorDisplay = (TextView) findViewById(R.id.textview_display);

		df.setMinimumFractionDigits(0);
		df.setMinimumIntegerDigits(1);
		df.setMaximumIntegerDigits(8);

		findViewById(R.id.button_zero).setOnClickListener(this);
		findViewById(R.id.button_one).setOnClickListener(this);
		findViewById(R.id.button_two).setOnClickListener(this);
		findViewById(R.id.button_three).setOnClickListener(this);
		findViewById(R.id.button_four).setOnClickListener(this);
		findViewById(R.id.button_five).setOnClickListener(this);
		findViewById(R.id.button_six).setOnClickListener(this);
		findViewById(R.id.button_seven).setOnClickListener(this);
		findViewById(R.id.button_eight).setOnClickListener(this);
		findViewById(R.id.button_nine).setOnClickListener(this);

		findViewById(R.id.button_add).setOnClickListener(this);
		findViewById(R.id.button_subtract).setOnClickListener(this);
		findViewById(R.id.button_multiply).setOnClickListener(this);
		findViewById(R.id.button_divide).setOnClickListener(this);
		findViewById(R.id.button_toggle_sign).setOnClickListener(this);
		findViewById(R.id.button_decimal_point).setOnClickListener(this);
		findViewById(R.id.button_equals).setOnClickListener(this);
		findViewById(R.id.button_clear).setOnClickListener(this);
		findViewById(R.id.button_clear_memory).setOnClickListener(this);
		findViewById(R.id.button_add_to_memory).setOnClickListener(this);
		findViewById(R.id.button_subtract_from_memory).setOnClickListener(this);
		findViewById(R.id.button_recall_memory).setOnClickListener(this);
		findViewById(R.id.button_backspace).setOnClickListener(this);

		// The following buttons only exist in layout-land (Landscape mode)
		if (findViewById(R.id.button_square_root) != null) {
			findViewById(R.id.button_square_root).setOnClickListener(this);
		}
		if (findViewById(R.id.button_squared) != null) {
			findViewById(R.id.button_squared).setOnClickListener(this);
		}
		if (findViewById(R.id.button_invert) != null) {
			findViewById(R.id.button_invert).setOnClickListener(this);
		}
		if (findViewById(R.id.button_sine) != null) {
			findViewById(R.id.button_sine).setOnClickListener(this);
		}
		if (findViewById(R.id.button_cosine) != null) {
			findViewById(R.id.button_cosine).setOnClickListener(this);
		}
		if (findViewById(R.id.button_tangent) != null) {
			findViewById(R.id.button_tangent).setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {

		String buttonPressed = ((Button) v).getText().toString();

		if (DIGITS.contains(buttonPressed)) {

			// digit was pressed
			if (userIsInTheMiddleOfTypingANumber) {

				if (buttonPressed.equals(".")
						&& mCalculatorDisplay.getText().toString()
								.contains(".")) {
				} else {
					mCalculatorDisplay.append(buttonPressed);
				}

			} else {

				if (buttonPressed.equals(".")) {
					mCalculatorDisplay.setText(0 + buttonPressed);
				} else {
					mCalculatorDisplay.setText(buttonPressed);
				}

				userIsInTheMiddleOfTypingANumber = true;
			}

		} else {
			// operation was pressed
			if (userIsInTheMiddleOfTypingANumber) {

				mCalculatorBrain.setOperand(Double
						.parseDouble(mCalculatorDisplay.getText().toString()));
				userIsInTheMiddleOfTypingANumber = false;
			}

			mCalculatorBrain.performOperation(buttonPressed);
			setNull();
		}
		switch (v.getId()) {
		case R.id.button_backspace:
			String str = mCalculatorDisplay.getText().toString();
			if (str.length() > 1) {
				str = str.substring(0, str.length() - 1);
				mCalculatorDisplay.setText(str);
			} else {
				mCalculatorDisplay.setText("0");
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save variables on screen orientation change
		outState.putDouble("OPERAND", mCalculatorBrain.getResult());
		outState.putDouble("MEMORY", mCalculatorBrain.getMemory());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Restore variables on screen orientation change
		mCalculatorBrain.setOperand(savedInstanceState.getDouble("OPERAND"));
		mCalculatorBrain.setMemory(savedInstanceState.getDouble("MEMORY"));
		setNull();
	}

	@SuppressLint("UseValueOf")
	protected void setNull() {

		if (new Double(mCalculatorBrain.getResult()).equals(0.0)) {
			mCalculatorDisplay.setText("" + 0);
		} else {
			mCalculatorDisplay.setText(df.format(mCalculatorBrain.getResult()));
		}
	}
}