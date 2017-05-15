package com.deitel.smartnoterecord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnector {
	
	private static final String DATABASE_NAME = "UserContacts";
	private SQLiteDatabase database;
	private DatabaseOpenHelper databaseOpenHelper;
	
	public DatabaseConnector(Context context){
		databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
	}
	
	public void open() throws SQLException{
		database = databaseOpenHelper.getWritableDatabase();
	}
	
	public void close(){
		if (database != null)
			database.close();
	}
	
	public void insertNote(String name, String note, String date, String details, String priority){
		ContentValues newNote = new ContentValues();
		newNote.put("name", name);
		newNote.put("note", note);
		newNote.put("date", date);
		newNote.put("details", details);
		newNote.put("priority", priority);
		
		open();
		database.insert("contacts", null, newNote);
		close();
	}
	
	public void updateNote(long id, String name, String note, String date, String details, String priority){
		ContentValues editNote = new ContentValues();
		editNote.put("name", name);
		editNote.put("note", note);
		editNote.put("date", date);
		editNote.put("details", details);
		editNote.put("priority", priority);
		
		open();
		database.update("contacts", editNote, "_id=" + id, null);
		close();
	}
	
	public Cursor getAllNotes(){
		return database.query("contacts", new String[] {"_id", "name"}, null, null, null, null, "name");
	}
	
	public Cursor getOneNote(long id){
		return database.query("contacts", null, "_id=" + id, null, null, null, null);
	}
	
	public void deleteNote(long id){
		open();
		database.delete("contacts", "_id=" + id, null);
		close();
	}
	
	private class DatabaseOpenHelper extends SQLiteOpenHelper{
		
		public DatabaseOpenHelper(Context context, String name, CursorFactory factory, int version){
			super(context, name, factory, version);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db){
			String createQuery = "CREATE TABLE contacts" +
					"(_id integer primary key autoincrement," +
					"name TEXT, note TEXT, date TEXT," +
					"details TEXT, priority TEXT);";
			
			db.execSQL(createQuery);		
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			
		}
	}

}
