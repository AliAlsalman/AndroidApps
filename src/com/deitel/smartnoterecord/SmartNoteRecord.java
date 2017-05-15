package com.deitel.smartnoterecord;

import com.deitel.smartnoterecord.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.os.Build;

public class SmartNoteRecord extends ListActivity {
	
	public static final String ROW_ID = "row_id";
	private ListView noteListView;
	private CursorAdapter contactAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		noteListView = getListView();
		noteListView.setOnItemClickListener(viewNoteListener);
		
		String[] from = new String[] { "name" };
		int[] to = new int[] { R.id.noteTextView };
		contactAdapter = new SimpleCursorAdapter(
				SmartNoteRecord.this, R.layout.note_list_item, null, from, to);
		setListAdapter(contactAdapter);
	}

	@Override
	protected void onResume(){
		super.onResume();
		new GetNote().execute((Object[]) null);
	}
	
	@Override
	protected void onStop(){
		Cursor cursor = contactAdapter.getCursor();
		if (cursor != null)
			cursor.deactivate();
		
		contactAdapter.changeCursor(null);
		super.onStop();
	}
	
	private class GetNote extends AsyncTask<Object, Object, Cursor>{
		DatabaseConnector databaseConnector = new DatabaseConnector(SmartNoteRecord.this);
		
		@Override
		protected Cursor doInBackground(Object... params){
			databaseConnector.open();
			return databaseConnector.getAllNotes();
		}
		
		@Override
		protected void onPostExecute(Cursor result){
			contactAdapter.changeCursor(result);
			databaseConnector.close();
		}
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.noteactivitymenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Intent addNewNote = new Intent(SmartNoteRecord.this, AddEditNote.class);
		startActivity(addNewNote);
		return super.onOptionsItemSelected(item);
	}
	
	OnItemClickListener viewNoteListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
			Intent viewNote = new Intent(SmartNoteRecord.this, ViewNote.class);
			viewNote.putExtra(ROW_ID, arg3);
			startActivity(viewNote);
		}
	};
}