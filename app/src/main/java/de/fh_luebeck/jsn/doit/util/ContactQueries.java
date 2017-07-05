package de.fh_luebeck.jsn.doit.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by USER on 05.07.2017.
 */

public class ContactQueries {

    public static String queryName(Uri uri, ContentResolver contentResolver) {

        String id = uri.getLastPathSegment();

        // Telefon
        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                new String[]{id}, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        } else {
            return "";
        }
    }

    public static String queryPhone(Uri uri, ContentResolver contentResolver) {

        String id = uri.getLastPathSegment();
        // Telefon
        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                new String[]{id}, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        } else {
            return "";
        }
    }

    public static String queryMail(Uri uri, ContentResolver contentResolver) {

        String id = uri.getLastPathSegment();
        // E-Mail
        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?",
                new String[]{id}, null);
        if (cursor.moveToFirst()) {
            int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            return cursor.getString(emailIdx);
        } else {
            return "";
        }
    }
}
