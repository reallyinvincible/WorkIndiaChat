package com.exuberant.workindiachat.base;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

import com.exuberant.workindiachat.R;

public class BaseProgressDialog {

    private Activity activity;
    private Dialog dialog;


    public BaseProgressDialog(Activity activity) {
        this.activity = activity;
        setupDialog();
    }

    public void setupDialog() {
        dialog = new Dialog(activity, R.style.ProgressDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_loading_progress);

    }

    public void showDialog() {
        dialog.show();
    }

    public void hideDialog() {
        dialog.dismiss();
    }

}
