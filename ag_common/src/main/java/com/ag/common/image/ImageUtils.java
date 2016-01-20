package com.ag.common.image;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.ag.common.other.CommonUtil;
import com.ag.common.other.StringUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片锟斤拷锟斤拷锟斤拷
 *
 * @author JohnZhan
 */
public class ImageUtils {

    /**
     * 获取图片的完整路径
     *
     * @param rootUrl
     * @param img
     * @return
     */
    public static String getFullImagePath(String rootUrl, String img) {
        String imgUrl = "";
        if (!img.startsWith("http://")) {
            imgUrl = rootUrl + img;
        } else {
            imgUrl = img;
        }
        return imgUrl;
    }

    @SuppressWarnings("deprecation")
    public static String getLocalImagePath(Context context, String imgPath, String imageLoadPath, String rootUrl, String shareImgName, int shareIconId) {

        if (!StringUtils.isEmpty(imgPath)) {
            if (!imgPath.startsWith("http://")) {
                imgPath = rootUrl + imgPath;
            }
//			String path = ImageLoader.getInstance().getDiskCache().get(imgPath).getPath();
            if (StringUtils.isEmpty(imageLoadPath)) {
                return getShareIcon(context, shareImgName, shareIconId);
            } else {
                File file = new File(imageLoadPath);
                if (file.exists()) {
                    return imageLoadPath;
                } else {
                    return getShareIcon(context, shareImgName, shareIconId);
                }
            }
        } else
            return getShareIcon(context, shareImgName, shareIconId);
    }

    public static String getShareIcon(Context context, String shareName, int iconId) {
        return CommonUtil.getAssetsResource(context, shareName, iconId);
//		return CommonUtil.getAssetsResource(context, "shareicon.png", R.raw.shareicon);
    }

	/*public static String getShareIconEtui(Context context){
		return CommonUtil.getAssetsResource(context, "shareicon_etui.png", R.raw.shareicon_etui);
	}*/

    public static void saveImage(Context context, String fileName, Bitmap bitmap)
            throws IOException {
        saveImage(context, fileName, bitmap, 100);
    }

    public static void saveImage(Context context, String fileName,
                                 Bitmap bitmap, int quality) throws IOException {
        if (bitmap == null || fileName == null || context == null)
            return;

        FileOutputStream fos = context.openFileOutput(fileName,
                Context.MODE_PRIVATE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, quality, stream);
        byte[] bytes = stream.toByteArray();
        fos.write(bytes);
        fos.close();
    }

    /**
     * 写图片锟侥硷拷锟斤拷SD锟斤拷
     *
     * @throws IOException
     */
    public static void saveImageToSD(Context ctx, String filePath,
                                     Bitmap bitmap, int quality) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0,
                    filePath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(filePath));
            bitmap.compress(CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            if (ctx != null) {
                scanPhoto(ctx, filePath);
            }
        }
    }

    /**
     * 锟斤拷Gallery锟斤拷锟斤拷锟斤拷锟较匡拷锟斤拷锟斤拷图片
     */
    private static void scanPhoto(Context ctx, String imgFileName) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }

    /**
     * 锟斤拷取bitmap
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getBitmap(Context context, String fileName) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = context.openFileInput(fileName);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 锟斤拷锟斤拷省锟节达拷姆锟绞斤拷锟饺★拷锟斤拷锟斤拷锟皆达拷锟酵计? 锟斤拷锟斤拷SDCard锟叫碉拷图片
     *
     * @param imagePath 图片锟斤拷SDCard锟叫碉拷路锟斤拷
     * @return
     */
    public static Bitmap getSDCardImg(String imagePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 锟斤拷取锟斤拷源图片
        return BitmapFactory.decodeFile(imagePath, opt);
    }

    /**
     * 锟斤拷取锟斤拷锟斤拷锟斤拷源图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap ReadBitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 转锟斤拷图片锟斤拷圆锟斤拷
     *
     * @param bitmap
     * @return
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 锟斤拷锟斤拷图片
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getZoomImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 锟斤拷始锟斤拷锟斤拷图片锟斤拷锟斤拷时锟斤拷options.inJustDecodeBounds 锟斤拷锟絫rue锟斤拷
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        // 图片锟侥匡拷锟?
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        int hh = 800;// 锟斤拷锟斤拷锟斤拷锟矫高讹拷为800f
        int ww = 480;// 锟斤拷锟斤拷锟斤拷锟矫匡拷锟轿?480f

        // 锟斤拷锟脚憋拷,锟斤拷锟斤拷锟角固讹拷锟斤拷锟斤拷锟斤拷锟斤拷,只锟矫高伙拷锟竭匡拷锟斤拷锟斤拷一锟斤拷锟斤拷锟捷斤拷锟叫硷拷锟姐即锟斤拷
        int be = 1;//be=1锟斤拷示锟斤拷锟斤拷锟斤拷
        if (w > h && w > ww) {// 锟斤拷锟斤拷锟饺达拷幕锟斤拷锟斤拷菘锟饺固讹拷锟斤拷小锟斤拷锟斤拷
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 锟斤拷锟斤拷叨雀叩幕锟斤拷锟斤拷莞叨裙潭锟斤拷锟叫★拷锟斤拷锟?
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 锟斤拷锟斤拷锟斤拷锟脚憋拷锟斤拷
        // 锟斤拷锟铰讹拷锟斤拷图片锟斤拷注锟斤拷锟绞憋拷丫锟斤拷锟給ptions.inJustDecodeBounds 锟斤拷锟絝alse锟斤拷
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    /**
     * 锟斤拷锟斤拷压锟斤拷
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image, String headName) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 锟斤拷锟斤拷压锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷100锟斤拷示锟斤拷压锟斤拷锟斤拷锟斤拷压锟斤拷锟斤拷锟斤拷锟斤拷荽锟脚碉拷baos锟斤拷
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    // 循锟斤拷锟叫讹拷锟斤拷锟窖癸拷锟斤拷锟酵计拷欠锟斤拷锟斤拷100kb,锟斤拷锟节硷拷锟斤拷压锟斤拷
            baos.reset();// 锟斤拷锟斤拷baos锟斤拷锟斤拷锟絙aos
            options -= 10;// 每锟轿讹拷锟斤拷锟斤拷10
            image.compress(CompressFormat.JPEG, options, baos);// 锟斤拷锟斤拷压锟斤拷options%锟斤拷锟斤拷压锟斤拷锟斤拷锟斤拷锟斤拷荽锟脚碉拷baos锟斤拷
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 锟斤拷压锟斤拷锟斤拷锟斤拷锟斤拷锟絙aos锟斤拷诺锟紹yteArrayInputStream锟斤拷
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 锟斤拷ByteArrayInputStream锟斤拷锟斤拷锟斤拷锟斤拷图片
        // 锟斤拷锟斤拷
        try {
            FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/DCIM/"
                    + headName);
            bitmap.compress(CompressFormat.PNG, 90, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 锟矫硷拷图片锟斤拷锟斤拷实锟斤拷
     *
     * @param uri
     */
    public static void startPhotoZoom(Activity activity, Uri uri, int resultCode) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 锟斤拷锟矫裁硷拷
        intent.putExtra("crop", "true");
        // aspectX aspectY 锟角匡拷叩谋锟斤拷锟?
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 锟角裁硷拷图片锟斤拷锟?
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, resultCode);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public static void startPhotoZoom(Activity activity, Uri uri, int resultCode, float ww, float hh, int outWidth, int outHeight) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", ww);
        intent.putExtra("aspectY", hh);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outWidth);
        intent.putExtra("outputY", outHeight);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, resultCode);
    }

    /**
     * 从URI获取路径
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(Context context, Uri uri) {
        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // DocumentProvider
            if (isExternalStorageDocument(uri)) {
                // ExternalStorageProvider
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                // DownloadsProvider
                String id = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                // MediaProvider
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
