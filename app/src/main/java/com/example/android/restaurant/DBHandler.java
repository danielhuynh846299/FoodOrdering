package com.example.android.restaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "restaurantManager";
    private static final String TABLE_MENU = "menu";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_FAVOURITE = "favourite";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_IMAGE = "image";
    private static final String TABLE_ORDER = "my_order";
    private static final String KEY_QUANTITY = "quantity";


    public DBHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MENU_TABLE = "CREATE TABLE " + TABLE_MENU + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PRICE + " TEXT," + KEY_FAVOURITE + " TEXT," + KEY_CATEGORY + " TEXT," + KEY_IMAGE + " TEXT" + ")";

        String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_ORDER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_QUANTITY + " TEXT" + ")";
        db.execSQL(CREATE_MENU_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        // Create table again
        onCreate(db);
    }
    //adding new menu

    void addMenu(Item menu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, menu.getName());
        values.put(KEY_PRICE, menu.getPrice());
        values.put(KEY_FAVOURITE, menu.getFavourite());
        values.put(KEY_CATEGORY, menu.getCategory());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        menu.getImage().compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String image = Base64.encodeToString(b, Base64.DEFAULT);
        values.put(KEY_IMAGE, image);

        db.insert(TABLE_MENU, null, values);
        db.close();

    }

    void addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, order.getItem().getName());
        values.put(KEY_QUANTITY, order.getQuantity());
        db.insert(TABLE_ORDER, null, values);
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MENU, null, null);
        db.delete(TABLE_ORDER, null, null);
    }

    public void deleteAllOrder() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDER, null, null);
    }

    //get order

    public List<Order> getAllOrders() {
        List<Item> menuList = getAllMenus();
        List<Order> listOrder = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                for (Item i : menuList) {
                    if (i.getName().equalsIgnoreCase(cursor.getString(1))) {
                        listOrder.add(new Order(i, cursor.getString(2)));
                    }
                }
            } while (cursor.moveToNext());
        }
        return listOrder;
    }

    public List<Item> getAllMenus() {
        List<Item> menuList = new ArrayList<>();
        //Select all query
        String selectQuery = "SELECT * FROM " + TABLE_MENU;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //looping through all rows
        if (cursor.moveToFirst()) {
            try {
                do {

                    byte[] encodeByte = Base64.decode(cursor.getString(5), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                            encodeByte.length);

                    Item item = new Item(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), bitmap);
                    menuList.add(item);
                } while (cursor.moveToNext());

            } catch (Exception e) {
                e.getMessage();
                return null;
            }

        }
        return menuList;
    }

}

