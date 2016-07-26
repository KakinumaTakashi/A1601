package jp.ecweb.homes.a1601;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Takashi Kakinuma on 2016/07/21.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "A1601.db";
	private static final int DATABASE_VERSION = 1;

	private static final String PRODUCT_TABLE_NAME = "HavingProduct";

	private static final String SQL_CREATE_HAVINGMATERIAL_TABLE =
			"CREATE TABLE " +
					PRODUCT_TABLE_NAME + " (" +
					"ProductID text primary key, " +
					"MaterialID text"
					+ ");";
	private static final String SQL_DROP_HAVINGMATERIAL_TABLE =
			"DROP TABLE " + PRODUCT_TABLE_NAME + ";";

	public MySQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(SQL_CREATE_HAVINGMATERIAL_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		sqLiteDatabase.execSQL(SQL_DROP_HAVINGMATERIAL_TABLE);
		onCreate(sqLiteDatabase);
	}

}
