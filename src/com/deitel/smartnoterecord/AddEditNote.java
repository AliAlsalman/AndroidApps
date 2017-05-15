package com.deitel.smartnoterecord;

import java.util.Calendar;

import com.deitel.smartnoterecord.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddEditNote extends Activity{
	
	private long rowID;
	static final int DATE_DIALOG_ID = 0;
	private int mYear;
	private int mMonth;
	private int mDay;
	
	private EditText nameEditText;
	private EditText noteEditText;
	private static EditText dateEditText;
	private EditText detailsEditText;
	private EditText priorityEditText;
	private Button pick;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_note);
		
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		noteEditText = (EditText) findViewById(R.id.noteEditText);
		dateEditText = (EditText) findViewById(R.id.dateEditText);
		detailsEditText = (EditText) findViewById(R.id.detailsEditText);
		priorityEditText = (EditText) findViewById(R.id.priorityEditText);
		
		pick = (Button) findViewById(R.id.pick);
		pick.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null){
			rowID = extras.getLong("row_id");
			nameEditText.setText(extras.getString("name"));
			noteEditText.setText(extras.getString("note"));
			dateEditText.setText(extras.getString("date"));
			detailsEditText.setText(extras.getString("details"));
			priorityEditText.setText(extras.getString("priority"));
		}
		
		Button saveNoteButton = (Button) findViewById(R.id.saveNoteButton);
		saveNoteButton.setOnClickListener(saveNoteButtonClicked);
	}
	
	OnClickListener saveNoteButtonClicked = new OnClickListener(){
		
		@Override
		public void onClick(View v){
			if (nameEditText.getText().length() != 0){
				AsyncTask<Object, Object, Object> saveNoteTask = new AsyncTask<Object, Object, Object>(){
					
					@Override
					protected Object doInBackground(Object... params){
						saveNote();
						return null;
					}
					
					@Override
					protected void onPostExecute(Object result){
						finish();
					}
				};
				saveNoteTask.execute((Object[]) null);
			}
			else{
				AlertDialog.Builder builder = new AlertDialog.Builder(AddEditNote.this);
				
				builder.setTitle(R.string.errorTitle);
				builder.setMessage(R.string.errorMessage);
				builder.setPositiveButton(R.string.errorButton, null);
				builder.show();
			}
		}
	};
	
	
	private void saveNote(){
		DatabaseConnector databaseConnector = new DatabaseConnector(this);
		
		if (getIntent().getExtras() == null){
			databaseConnector.insertNote(
					nameEditText.getText().toString(),
					noteEditText.getText().toString(),
					dateEditText.getText().toString(),
					detailsEditText.getText().toString(),
					priorityEditText.getText().toString());
		}
		
		else{
			databaseConnector.updateNote(rowID,
					nameEditText.getText().toString(),
					noteEditText.getText().toString(),
					dateEditText.getText().toString(),
					detailsEditText.getText().toString(),
					priorityEditText.getText().toString());
		}
	}

	
	private void updateDisplay() {
	    this.dateEditText.setText(
	        new StringBuilder()
	                .append(mMonth + 1).append("-")
	                .append(mDay).append("-")
	                .append(mYear).append(" "));
	    
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
				@SuppressWarnings("unused")
				public void onDateSet1(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		            mYear = year;
		            mMonth = monthOfYear;
		            mDay = dayOfMonth;
		            
		            updateDisplay();
		        }

				@Override
				public void onDateSet(android.widget.DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					// TODO Auto-generated method stub
					 	mYear = year;
			            mMonth = monthOfYear;
			            mDay = dayOfMonth;
			            updateDisplay();
				}
		    };
		    
		    @Override
		    protected Dialog onCreateDialog(int id) {
		    	final Calendar c = Calendar.getInstance();
				mYear = c.get(Calendar.YEAR);
				mMonth = c.get(Calendar.MONTH);
				mDay = c.get(Calendar.DAY_OF_MONTH);
				updateDisplay();
		       switch (id) {
		       case DATE_DIALOG_ID:
		          return new DatePickerDialog(this,mDateSetListener,mYear, mMonth, mDay);		
		       }
		       return null;
		    }
	
}
