package com.alcoholcompass;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alcoholcompass.data.GuestbookEntry;

public class SuccessDialogFragment extends DialogFragment {

    public static SuccessDialogFragment newInstance() {
    	return new SuccessDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_success, null);
        
        TextView title = (TextView) dialogView.findViewById(R.id.textViewDialogSuccessTitle);
        title.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/cola.ttf"));
        
        ListView listView = (ListView) dialogView.findViewById(R.id.listViewDialog);
        listView.setAdapter(new GuestbookAdapter(getActivity(), getTestData()));
        
        return new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton(R.string.alert_dialog_ok,
                    new DialogInterface.OnClickListener() {
                        @Override
						public void onClick(DialogInterface dialog, int whichButton) {
                            //((FragmentAlertDialog)getActivity()).doPositiveClick();
                        }
                    }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
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
    		bmp.compress(Bitmap.CompressFormat.JPEG, 90, stream);
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
    
    private List<GuestbookEntry> getTestData(){
    	ArrayList<GuestbookEntry> entries = new ArrayList<GuestbookEntry>();
    	GuestbookEntry entry = new GuestbookEntry();
    	entry.created = 829302180;
    	entry.user = "Max Tester";
    	
    	GuestbookEntry entry2 = new GuestbookEntry();
    	entry2.created = 328193211;
    	entry2.user = "Sabine Neumann";
    	
    	entries.add(entry2);
    	entries.add(entry);
    	return entries;
    }
}
