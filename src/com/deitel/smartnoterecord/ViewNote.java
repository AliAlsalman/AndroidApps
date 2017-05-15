package com.deitel.smartnoterecord;

import com.deitel.smartnoterecord.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewNote extends Activity{
	
	private long rowID;
	private TextView nameTextView;
	private TextView noteTextView;
	private TextView dateTextView;
	private TextView detailsTextView;
	private TextView priorityTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_note);
		
		nameTextView = (TextView) findViewById(R.id.nameTextView);
		noteTextView = (TextView) findViewById(R.id.noteTextView);
		dateTextView = (TextView) findViewById(R.id.dateTextView);
		detailsTextView = (TextView) findViewById(R.id.detailsTextView);
		priorityTextView = (TextView) findViewById(R.id.priorityTextView);
		
		Bundle extras = getIntent().getExtras();
		rowID = extras.getLong("row_id");
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		new LoadNote().execute(rowID);
	}
	
	private class LoadNote extends AsyncTask<Long, Object, Cursor>{
		DatabaseConnector databaseConnector = new DatabaseConnector(ViewNote.this);
		
		@Override
		protected Cursor doInBackground(Long... params){
			databaseConnector.open();
			return databaseConnector.getOneNote(params[0]);
		}
		
		protected void onPostExecute(Cursor result){
			super.onPostExecute(result);
			result.moveToFirst();
			
			int nameIndex = result.getColumnIndex("name");
			int noteIndex = result.getColumnIndex("note");
			int dateIndex = result.getColumnIndex("date");
			int detailsIndex = result.getColumnIndex("details");
			int priorityIndex = result.getColumnIndex("priority");
			
			nameTextView.setText(result.getString(nameIndex));
			noteTextView.setText(result.getString(noteIndex));
			dateTextView.setText(result.getString(dateIndex));
			detailsTextView.setText(result.getString(detailsIndex));
			priorityTextView.setText(result.getString(priorityIndex));
			
			result.close();
			databaseConnector.close();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.viewnoteactivitymenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
		
			case R.id.editItem:
				Intent addEditNote = new Intent(this, AddEditNote.class);
				addEditNote.putExtra("row_id", rowID);
				addEditNote.putExtra("name", nameTextView.getText());
				addEditNote.putExtra("note", noteTextView.getText());
				addEditNote.putExtra("date", dateTextView.getText());
				addEditNote.putExtra("details", detailsTextView.getText());
				addEditNote.putExtra("priority", priorityTextView.getText());
				startActivity(addEditNote);
				return true;
				
			case R.id.deleteItem:
				deleteNote();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void deleteNote(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(ViewNote.this);
		
		builder.setTitle(R.string.confirmTitle);
		builder.setMessage(R.string.confirmMessage);
		
		builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int button) {
				// TODO Auto-generated method stub
				final DatabaseConnector databaseConnector = new DatabaseConnector(ViewNote.this);
				
				AsyncTask<Long, Object, Object> deleteNote = new AsyncTask<Long, Object, Object>(){
					@Override
					protected Object doInBackground(Long... params){
						databaseConnector.deleteNote(params[0]);
						return null;
					}
					
					@Override
					protected void onPostExecute(Object result){
						finish();
					}
				};
				
				deleteNote.execute(new Long[] { rowID });
			}
		
		});
		builder.setNegativeButton(R.string.button_cancel, null);
		builder.show();
	}
}
