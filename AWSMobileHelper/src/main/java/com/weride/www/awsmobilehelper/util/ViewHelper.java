package com.weride.www.awsmobilehelper.util;

/**
 * Created by Francis on 3/26/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

/**
 * Utilities for Views.
 */
public final class ViewHelper {
    /**
     * Gets the String value from an EditText control.
     *
     * @param activity invoking Activity
     * @param viewId resource ID
     * @return the String value from the EditText control
     */
    public static String getStringValue(final Activity activity, final int viewId) {
        if (null == activity) {
            return "";
        }

        return ((EditText) activity.findViewById(viewId)).getText().toString();
    }

    /**
     * Displays a modal dialog with an OK button.
     *
     * @param activity invoking activity
     * @param title title to display for the dialog
     * @param body content of the dialog
     */
    public static void showDialog(final Activity activity, final String title, final String body) {
        if (null == activity) {
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(body);
        builder.setNeutralButton(android.R.string.ok, null);
        builder.show();
    }

    /**
     * Displays a modal dialog.
     *
     * @param activity invoking activity
     * @param title title to display for the dialog
     * @param body content of the dialog
     * @param positiveButton String for positive button
     * @param negativeButton String for negative button
     * @param negativeButtonListener  the listener which should be invoked when a negative button is pressed
     * @param positiveButtonListener  the listener which should be invoked when a positive button is pressed
     */
    public static void showDialog(final Activity activity, final String title, final String body, final String positiveButton, final DialogInterface.OnClickListener positiveButtonListener, final String negativeButton, final DialogInterface.OnClickListener negativeButtonListener){
        if (null == activity) {
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(body);
        builder.setPositiveButton(positiveButton,positiveButtonListener);
        builder.setNegativeButton(negativeButton, negativeButtonListener);
        builder.show();
    }
}
