package com.alcoholcompass;

import java.io.ByteArrayOutputStream;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SuccessDialogFragment extends DialogFragment {

    public static SuccessDialogFragment newInstance(int title) {
    	SuccessDialogFragment frag = new SuccessDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
                //.setIcon(R.drawable.alert_dialog_icon)
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //((FragmentAlertDialog)getActivity()).doPositiveClick();
                        }
                    }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //((FragmentAlertDialog)getActivity()).doNegativeClick();
                        }
                    }
                )
                .create();
    }
    
    private static final int PHOTO_CODE = 132;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, PHOTO_CODE);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode == PHOTO_CODE) {
    		Bundle extras = data.getExtras();
    		Bitmap bmp = (Bitmap) extras.get("data");
    		
    		ByteArrayOutputStream stream = new ByteArrayOutputStream();
    		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
    		byte[] byteArray = stream.toByteArray();
    	}
    }
    
    public boolean isIntentAvailable(String action) {
        final PackageManager packageManager = getActivity().getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

}
