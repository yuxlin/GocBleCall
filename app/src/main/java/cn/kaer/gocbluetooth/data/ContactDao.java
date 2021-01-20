package cn.kaer.gocbluetooth.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

/**
 * User: yxl
 * Date: 2020/12/17
 */
public class ContactDao {
    public static String getContactNameByNumb(Context context, String number) {
        String name = "";


        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                "replace(data1,' ','') = ?", new String[]{number}, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                name = cursor.getString(0);
            }
            cursor.close();
        }
        return name;
    }

    public static String getEnterpriseContactName(Context context, String numberparm) {
        String contactName = "";
        if (!TextUtils.isEmpty(numberparm)) {
            try {
                numberparm = numberparm.replaceAll(" ", "");
                Uri remoteuri_phonetable = Uri.parse("content://com.kaer.contact/" + "tbl_phone");
                Cursor cursor = context.getContentResolver().query(remoteuri_phonetable, null,
                        " phoneNO = '" + numberparm + "'", null, null);
                // tbl_phone phoneID|phoneNO|personID|phoneType|rootid
                // tbl_companys ：
                // companyid|companyName|companyAddress|companyTel|parentCompanyID|maxPersonNO|updateChildren|updateParent|rootid
                // tbl_person
                // ：personID|companyID|personName|homeAddress|nickName|email|memo|IMNO|passWord|empID|position|rootid|groupid

                if (cursor == null) {
                    //empty contact

                    return "";
                }


                if (cursor.moveToFirst()) {
                    int personid = cursor.getInt(2);
                    int phoneType = cursor.getInt(3);

                    Uri remoteuri_persontable = Uri.parse("content://com.kaer.contact/" + "tbl_person");
                    Cursor personcursor = context.getContentResolver().query(remoteuri_persontable, null,
                            " personID = " + personid, null, null);
                    if (personcursor != null) {
                        if (personcursor.moveToFirst()) {
                            contactName = personcursor.getString(2);
                        }
                        personcursor.close();
                    }
                }
                cursor.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return contactName;
    }
}
