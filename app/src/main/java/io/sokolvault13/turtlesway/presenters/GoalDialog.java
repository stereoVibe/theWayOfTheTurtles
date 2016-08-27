package io.sokolvault13.turtlesway.presenters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import io.sokolvault13.turtlesway.R;

public abstract class GoalDialog extends DialogFragment {

    private NoticeDialogListener mNoticeDialogListener;

    protected abstract void changeKeyboardVisibility();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(this.getActivity(), R.style.SubGoalsDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        View v = dialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        wmlp.gravity = Gravity.TOP;
        wmlp.height = dialog.getWindow().getAttributes().height = (int) (getDeviceMetrics(getContext()).heightPixels * 0.34);
        wmlp.width = dialog.getWindow().getAttributes().width = (int) (getDeviceMetrics(getContext()).widthPixels * 0.95);
        dialog.getWindow().setAttributes(wmlp);
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        changeKeyboardVisibility();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        changeKeyboardVisibility();
    }


    private DisplayMetrics getDeviceMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }

    public interface NoticeDialogListener {
        void onDialogClick(View.OnClickListener dialogFragment);
    }
}
