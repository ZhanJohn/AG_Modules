package com.ag.common.webview.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * 数据库帮助类
 *
 */
public class AGOrmliteHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "agwebview.db";
	public static int DATABASE_VERSION = 1;

	private Dao<AGCacheInfo, Integer> dao = null;
	private Dao<AGHeaderInfo, Integer> headerDao=null;

	public AGOrmliteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	private static AGOrmliteHelper instance;

	/**
	 * 单例获取该Helper
	 *
	 * @param context
	 * @return
	 */
	public static synchronized AGOrmliteHelper getHelper(Context context)
	{
		if (instance == null)
		{
			synchronized (AGOrmliteHelper.class)
			{
				if (instance == null)
					instance = new AGOrmliteHelper(context);
			}
		}

		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.e(AGOrmliteHelper.class.getName(), "开始创建数据库");
			try {
				TableUtils.createTable(connectionSource, AGCacheInfo.class);
				TableUtils.createTable(connectionSource, AGHeaderInfo.class);
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
			Log.e(AGOrmliteHelper.class.getName(), "创建数据库成功");
		} catch (SQLException e) {
			Log.e(AGOrmliteHelper.class.getName(), "创建数据库失败", e);
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int arg2,
						  int arg3) {
		System.out.println("onUpgrade()...");
		try {
			try {
				TableUtils.dropTable(connectionSource, AGCacheInfo.class, true);
				TableUtils.dropTable(connectionSource, AGHeaderInfo.class, true);
			} catch (java.sql.SQLException e) {
				e.printStackTrace();
			}
			onCreate(db, connectionSource);
			Log.e(AGOrmliteHelper.class.getName(), "更新数据库成功");
		} catch (SQLException e) {
			Log.e(AGOrmliteHelper.class.getName(), "更新数据库失败", e);
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		super.close();
		dao=null;
		headerDao=null;
	}

	public Dao<AGCacheInfo, Integer> getCacheDao() throws java.sql.SQLException {
		if (dao == null) {
			dao = getDao(AGCacheInfo.class);
		}
		return dao;
	}

	public Dao<AGHeaderInfo, Integer> getHeaderDao() throws java.sql.SQLException {
		if (headerDao == null) {
			headerDao = getDao(AGHeaderInfo.class);
		}
		return headerDao;
	}

}
