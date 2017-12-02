package com.jdkgroup.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jdkgroup.constant.AppConstant;
import com.jdkgroup.customviews.gson.ListOfJson;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jdkgroup.pocketquiz.R;

public class AppUtils {
    private static String TAG = "Tag";
    private static int screenWidth = 0;
    private static Pattern pattern;

    private static CharSequence charsequence;
    private static JSONObject jsonobject;
    private static Iterator iterator;
    private static Gson gson;
    private static GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    public static String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static boolean isEmpty(EditText editText) {
        return getText(editText).equalsIgnoreCase("");
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message + "", Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    public static void showToastById(Context context, int id) {
        Toast toast = Toast.makeText(context, getStringFromId(context, id), Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    public static String getStringFromId(Context mContext, int id) {
        String str = null;
        try {
            str = mContext.getString(id);
        } catch (Exception e) {
        }
        return str;
    }

    public static void toast(Context context, int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void logD(String text) {
        Log.d(TAG, text);
    }

    public static void loge(String text) {
        Log.e(TAG, text);
    }

    public static android.support.v7.app.AlertDialog createAlertDialog(Activity activity, String message, String positiveText, String negativeText, DialogInterface.OnClickListener mDialogClick) {
        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(activity).setPositiveButton(positiveText, mDialogClick)
                        .setNegativeButton(negativeText, mDialogClick)
                        .setMessage(message);
        return builder.create();
    }

    public static void hideKeyboard(Activity activity) {
        try {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getApplicationWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showKeyboard(Activity activity, AppCompatEditText view) {
        Context context = activity;
        try {
            if (context != null) {
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        } catch (Exception e) {
            Log.e("Exception on  show", e.toString());
        }
    }

    public static void requestEdittextFocus(Activity activity, AppCompatEditText view) {
        view.requestFocus();
        showKeyboard(activity, view);
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }


//    public static FileUri createImageFile(String prefix) {
//        FileUri fileUri = new FileUri();
//
//        File image = null;
//        try {
//            image = File.createTempFile(prefix + String.valueOf(System.currentTimeMillis()), ".jpg", getWorkingDirectory());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (image != null) {
//            fileUri.setFile(image);
//            fileUri.setImageUrl(Uri.parse("file:" + image.getAbsolutePath()));
//        }
//        return fileUri;
//    }

    private static File getWorkingDirectory() {
        File directory = new File(Environment.getExternalStorageDirectory(), "Mowadcom");
        return createDirectory(directory);
    }

    private static File createDirectory(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static String setImage(String imageBaseUrl) {
        // return PocketAccountConstant.IMAGE_BASE_URL.concat(imageBaseUrl);
        return null;
    }


   /* public static RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }
    public static String setImage(String url)
    {
        return PocketAccountConstant.IMAGE_URL+url;
    }*/

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }
        return screenWidth;
    }

    public static void startActivity(Context context, Class className) {
        Intent intent = new Intent(context, className);
        context.startActivity(intent);
    }

    public static SpannableString timestampToDate(String strTimestamp) {
        try {
            long timestamp = Long.parseLong(strTimestamp) * 1000L;
            DateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm aaa");
            Date netDate = (new Date(timestamp));
            String suffix = getDayOfMonthSuffix(netDate.getDate());

            String[] str = sdf.format(netDate).split(" ");
            String strDate = "";
            for (int i = 0; i < str.length; i++) {
                if (i == 0)
                    strDate = str[i] + suffix + " ";
                else if (i == str.length - 1)
                    strDate = strDate + str[i];
                else
                    strDate = strDate + str[i] + " ";
            }

            SpannableString styledString = new SpannableString(strDate);
            styledString.setSpan(new SuperscriptSpan(), 2, 4, 0);
            styledString.setSpan(new RelativeSizeSpan(0.7f), 2, 4, 0);
            styledString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, strDate.length(), 0);
            return styledString;
        } catch (Exception ex) {
            return new SpannableString("");
        }
    }

    static String getDayOfMonthSuffix(final int n) {

        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static String getDateFromTime(long mTimestamp, String mDateFormate) {
        String date;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(mDateFormate);
        dateFormatter.setTimeZone(TimeZone.getDefault());

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(mTimestamp);

        date = dateFormatter.format(cal.getTime());
        return date;
    }

    public static String getDateString(long miliis, String mDateFormate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTimeInMillis(miliis);
        SimpleDateFormat dateFormatter = new SimpleDateFormat(mDateFormate, Locale.ENGLISH);
        return dateFormatter.format(calendar.getTime());
    }

    public static long getUtC(long millis) {
        return millis + TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings();
    }

    public static long getGMT(long millis) {
        return millis - TimeZone.getDefault().getRawOffset() - TimeZone.getDefault().getDSTSavings();
    }

    public static String getDate(long timeStamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("DD MMMM yyyy", Locale.getDefault());
            Date netDate = (new Date(timeStamp * 1000));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "";
        }
    }

    public static long getDateTimeInMilliseconds(final String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy");
        Date mDate = sdf.parse(date);
        return mDate.getTime();
    }

    public static String getFormatedNumber(String val) {
        double d = 0;
        try {
            d = Double.parseDouble(val);
        } catch (Exception e) {
            d = 0.00;
        }
        // return String.format("%.2f",NumberFormat.getNumberInstance(Locale.UK).format(d));
        return NumberFormat.getNumberInstance(Locale.UK).format(d) + "";
    }

    public static JSONObject ConvertMapToJsonObject(final Map map) {
        jsonobject = new JSONObject();
        try {
            iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry) iterator.next();

                jsonobject.put(String.valueOf(pair.getKey()), String.valueOf(pair.getValue()));
            }
        } catch (Exception ex) {
        }

        return jsonobject;
    }

    public static String removeAllWhiteSpace(String value) {
        return value.replaceAll("\\s+", "");
    }

    /* VALIDATION */
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final static boolean isValidationRegularExpression(final String regularexpression, final String value) {
        return value.matches(regularexpression);
    }

    public static String getPathFromMediaUri(@NonNull Context context, @NonNull Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static File decreaseImageSize(File file) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;

            FileInputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            final int REQUIRED_SIZE = 75;

            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public static long getFileSize(File file) {
        long sizeInBytes = file.length();
        return sizeInBytes / (1024 * 1024);
    }

    private Gson SwitchGson(int param) {
        switch (param) {
            case 1:
                gson = gsonBuilder.create();
                return gson;

            case 2: //FIRST CHARACTER UPPER CAMEL
                gson = new GsonBuilder().
                        disableHtmlEscaping().
                        setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).
                        setPrettyPrinting().serializeNulls().
                        create();
                return gson;

            default:
                break;
        }
        return null;
    }

    public static int dpToPx(final float dp, final Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public void NavigationBarBackground(final Activity activity, int id) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity, id));
        }
    }

    public static Integer parseInt(String value) {
        if (value == null) {
            return 0;
        }
        Integer val = 0;
        try {
            Matcher matcher = pattern.matcher(value);
            if (matcher.find()) {
                String num = matcher.group(0);
                val = Integer.parseInt(num);
            }
        } catch (Exception e) {
        }
        return val;
    }

    public static Long parseLong(String value) {
        if (value == null) {
            return 0L;
        }
        Long val = 0L;
        try {
            Matcher matcher = pattern.matcher(value);
            if (matcher.find()) {
                String num = matcher.group(0);
                val = Long.parseLong(num);
            }
        } catch (Exception e) {
        }
        return val;
    }

    public static String parseIntToString(String value) {
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    /* public static String bytesToHex(byte[] bytes) {
         if (bytes == null) {
             return "";
         }
         char[] hexChars = new char[bytes.length * 2];
         int v;
         for (int j = 0; j < bytes.length; j++) {
             v = bytes[j] & 0xFF;
             hexChars[j * 2] = hexArray[v >>> 4];
             hexChars[j * 2 + 1] = hexArray[v & 0x0F];
         }
         return new String(hexChars);
     }
 */
    public static byte[] hexToBytes(String hex) {
        if (hex == null) {
            return null;
        }
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] computeSHA256(byte[] convertme, int offset, int len) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(convertme, offset, len);
            return md.digest();
        } catch (Exception e) {

        }
        return null;
    }

    public static long bytesToLong(byte[] bytes) {
        return ((long) bytes[7] << 56) + (((long) bytes[6] & 0xFF) << 48) + (((long) bytes[5] & 0xFF) << 40) + (((long) bytes[4] & 0xFF) << 32)
                + (((long) bytes[3] & 0xFF) << 24) + (((long) bytes[2] & 0xFF) << 16) + (((long) bytes[1] & 0xFF) << 8) + ((long) bytes[0] & 0xFF);
    }

    public static String MD5(String md5) {
        if (md5 == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int a = 0; a < array.length; a++) {
                sb.append(Integer.toHexString((array[a] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {

        }
        return null;
    }

    /*public static void replaceFragment(Activity activity, Fragment fragment, Bundle bundle) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;
        FragmentManager fragmentManager = ((BaseDrawerActivity) activity).getSupportFragmentManager();

        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
        if (!fragmentPopped && fragmentManager.findFragmentByTag(fragmentTag) == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.replace(R.id.frameLayout, fragment, backStateName);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();
        }
    }*/

    public static void lauchFramgentActivity(final Activity activity, final int redirect) {
        Fragment fragment;

        switch (redirect) {
            case 0: //LOGOUT

                break;

            default:
                System.out.println("No available drawer menu");
        }
    }

    public static String getToJson(List<?> alData) {
        gson = gsonBuilder.create();
        return gson.toJson(alData);
    }

    public static String getToJsonClass(Object src) {
        gson = gsonBuilder.create();
        return gson.toJson(src);
    }

    public static <T> T getFromJson(String str, Class<T> classType) {
        return new Gson().fromJson(str, classType);
    }

    public static void displayMap(HashMap<String, String> hashMap) {
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            //AppUtils.loge("DISPLAY MAP " + PocketAccountConstant.API_PARAMETER + pair.getKey() + pair.getValue());
        }
    }

    public static <T> List<T> getToList(String json, Class<T> typeClass) {
        gson = gsonBuilder.create();
        return gson.fromJson(json, new ListOfJson<T>(typeClass));
    }

    //TODO DUPLICATE REMOVE ITEMS
    public static <E> List<E> listRemoveDuplicates(List<E> list) {
        Set<E> uniques = new HashSet<E>();
        uniques.addAll(list);
        return new ArrayList<E>(uniques);
    }

    public static <E> List<E> linkedHashSetRemoveDuplicates(List<E> list) {
        return new ArrayList<E>(new LinkedHashSet<>(list));
    }
}
