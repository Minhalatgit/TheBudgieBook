package com.koders.budgie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.koders.budgie.model.BirdInfo;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bird";
    public static final String TABLE_NAME = "bird_info";
    public static final String ID_COL = "ID";
    public static final String RING_NUM_COL = "ring_num";
    public static final String SEX_COL = "sex";
    public static final String HATCH_DATE_COL = "hatch_date";
    public static final String ARRIVAL_DATE_COL = "arrival_date";
    public static final String APPROX_AGE_COL = "approx_age";
    public static final String SIZE_COL = "size";
    public static final String COLOR_COL = "color";
    public static final String CRESTED_COL = "crested";
    public static final String FATHER_COL = "father";
    public static final String MOTHER_COL = "mother";
    public static final String STATUS_COL = "status";
    public static final String CAGE_NUM_COL = "cage_num";
    public static final String RING_OWNER_NAME_COL = "ring_owner_name";
    public static final String PURCHASED_PRICE_COL = "purchased_price";
    public static final String TAKEN_FROM_COL = "taken_from";
    public static final String TAKEN_DATE_COL = "taken_date";
    public static final String SELLER_NUM_COL = "seller_num";
    public static final String SELLER_LOCATION_COL = "seller_location";
    public static final String SELLING_PRICE_COL = "selling_price";
    public static final String GIVEN_TO_COL = "given_to";
    public static final String GIVEN_DATE_COL = "given_date";
    public static final String BUYER_NUM_COL = "buyer_num";
    public static final String BUYER_LOCATION_COL = "buyer_location";
    public static final String WITH_PARTNERSHIP_COL = "with_partnership";
    public static final String IMAGE_COL = "image";
    public static final String MUTATION_COL = "mutation";


    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT ," + RING_NUM_COL + " TEXT," + SEX_COL + " TEXT,"
                + HATCH_DATE_COL + " TEXT ," + ARRIVAL_DATE_COL + " TEXT ," + APPROX_AGE_COL + " TEXT," + SIZE_COL + " TEXT," + COLOR_COL + " TEXT,"
                + CRESTED_COL + " TEXT," + FATHER_COL + " TEXT," + MOTHER_COL + " TEXT," + STATUS_COL + " TEXT," + CAGE_NUM_COL + " TEXT," + RING_OWNER_NAME_COL + " TEXT,"
                + PURCHASED_PRICE_COL + " TEXT," + TAKEN_FROM_COL + " TEXT," + TAKEN_DATE_COL + " TEXT," + SELLER_NUM_COL + " TEXT," + SELLER_LOCATION_COL + " TEXT,"
                + SELLING_PRICE_COL + " TEXT," + GIVEN_TO_COL + " TEXT," + GIVEN_DATE_COL + " TEXT," + BUYER_NUM_COL + " TEXT, " + BUYER_LOCATION_COL + " TEXT,"
                + WITH_PARTNERSHIP_COL + " TEXT," + IMAGE_COL + " TEXT, " + MUTATION_COL + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertBirdInfo(List<BirdInfo> birdInfoList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (BirdInfo birdInfo : birdInfoList) {
            values.put(RING_NUM_COL, birdInfo.getRingNumber());
            values.put(SEX_COL, birdInfo.getSex());
            values.put(HATCH_DATE_COL, birdInfo.getHatchDate());
            values.put(ARRIVAL_DATE_COL, birdInfo.getArrivalDate());
            values.put(APPROX_AGE_COL, birdInfo.getApproxAge());
            values.put(SIZE_COL, birdInfo.getSize());
            values.put(COLOR_COL, birdInfo.getColor());
            values.put(CRESTED_COL, birdInfo.getCrested());
            values.put(FATHER_COL, birdInfo.getFather());
            values.put(MOTHER_COL, birdInfo.getMother());
            values.put(STATUS_COL, birdInfo.getStatus());
            values.put(CAGE_NUM_COL, birdInfo.getCageNumber());
            values.put(RING_OWNER_NAME_COL, birdInfo.getRingOwnerName());
            values.put(PURCHASED_PRICE_COL, birdInfo.getPurchasedPrice());
            values.put(TAKEN_FROM_COL, birdInfo.getTakenFrom());
            values.put(TAKEN_DATE_COL, birdInfo.getTakenDate());
            values.put(SELLER_NUM_COL, birdInfo.getSellerNumber());
            values.put(SELLER_LOCATION_COL, birdInfo.getSellerLocation());
            values.put(SELLING_PRICE_COL, birdInfo.getSellingPrice());
            values.put(GIVEN_TO_COL, birdInfo.getGivenTo());
            values.put(GIVEN_DATE_COL, birdInfo.getGivenDate());
            values.put(BUYER_NUM_COL, birdInfo.getBuyerNumber());
            values.put(BUYER_LOCATION_COL, birdInfo.getBuyerLocation());
            values.put(WITH_PARTNERSHIP_COL, birdInfo.getWithPartnership());
            values.put(IMAGE_COL, birdInfo.getImage());
            values.put(MUTATION_COL, birdInfo.getMutation());

            long temp = db.insert(TABLE_NAME, null, values);
            Log.d("DB", temp + "");
        }
    }

    public List<BirdInfo> getAllBirds() {
        List<BirdInfo> birdInfoList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                BirdInfo birdInfo = new BirdInfo();
                birdInfo.setRingNumber(cursor.getString(1));
                birdInfo.setSex(cursor.getString(2));
                birdInfo.setHatchDate(cursor.getString(3));
                birdInfo.setArrivalDate(cursor.getString(4));
                birdInfo.setApproxAge(cursor.getString(5));
                birdInfo.setSize(cursor.getString(6));
                birdInfo.setColor(cursor.getString(7));
                birdInfo.setCrested(cursor.getString(8));
                birdInfo.setFather(cursor.getString(9));
                birdInfo.setMother(cursor.getString(10));
                birdInfo.setStatus(cursor.getString(11));
                birdInfo.setCageNumber(cursor.getString(12));
                birdInfo.setRingNumber(cursor.getString(13));
                birdInfo.setPurchasedPrice(cursor.getString(14));
                birdInfo.setTakenFrom(cursor.getString(15));
                birdInfo.setTakenDate(cursor.getString(16));
                birdInfo.setSellerNumber(cursor.getString(17));
                birdInfo.setSellerLocation(cursor.getString(18));
                birdInfo.setSellingPrice(cursor.getString(19));
                birdInfo.setGivenTo(cursor.getString(20));
                birdInfo.setGivenDate(cursor.getString(21));
                birdInfo.setBuyerNumber(cursor.getString(22));
                birdInfo.setBuyerLocation(cursor.getString(23));
                birdInfo.setWithPartnership(cursor.getString(24));
                birdInfo.setImage(cursor.getString(25));
                birdInfo.setMutation(cursor.getString(26));

                birdInfoList.add(birdInfo);
            } while (cursor.moveToNext());
        }

        return birdInfoList;
    }
}
