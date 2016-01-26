package com.ag.common.provider;


import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.ag.common.other.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AGContacts {

    private final static String TAG="AGContacts";

    /*
     * 读取联系人的信息
     */
    public static List<ContactInfo> readAllContacts(Context context) {
        List<ContactInfo> list=new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;

        if(cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }
        while(cursor.moveToNext()) {
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);
            Log.i(TAG, contactId);
            Log.i(TAG, name);
            ContactInfo mContactInfo = new ContactInfo();
            mContactInfo.setLastName(name);

            // 查看联系人有多少个号码，如果没有号码，返回0
            int phoneCount = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (phoneCount <= 0) {
                list.add(mContactInfo);
                continue;
            }

            /*
             * 查找该联系人的phone信息
             */
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null, null);
            int phoneIndex = 0, photoType = 0;
            if (phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                photoType = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
            }
            while (phones.moveToNext()) {
                String phoneNumber = phones.getString(phoneIndex);
                String type = phones.getString(photoType);

                switch(StringUtils.SafeInt(type,0)){
                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                        mContactInfo.setHome(phoneNumber);
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                        mContactInfo.setMobile(phoneNumber);
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                        mContactInfo.setWork(phoneNumber);
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                        mContactInfo.setWorkfax(phoneNumber);
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                        mContactInfo.setHomefax(phoneNumber);
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                        mContactInfo.setPager(phoneNumber);
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                        mContactInfo.setOther(phoneNumber);
                        break;
                    case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                        mContactInfo.setMain(phoneNumber);
                        break;
                }

                Log.i(TAG, phoneNumber + ";type=" + type);
            }
            if(!phones.isClosed())
                phones.close();
            list.add(mContactInfo);

        }
        if(!cursor.isClosed())
            cursor.close();
        return list;
    }

    public static class ContactInfo implements Serializable{
        private String firstName;
        private String lastName;
        private String home;
        private String work;
        private String mobile;
        private String main;
        private String homefax;
        private String workfax;
        private String pager;
        private String other;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getHome() {
            return home;
        }

        public void setHome(String home) {
            this.home = home;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getHomefax() {
            return homefax;
        }

        public void setHomefax(String homefax) {
            this.homefax = homefax;
        }

        public String getWorkfax() {
            return workfax;
        }

        public void setWorkfax(String workfax) {
            this.workfax = workfax;
        }

        public String getPager() {
            return pager;
        }

        public void setPager(String pager) {
            this.pager = pager;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
            this.other = other;
        }
    }

}
