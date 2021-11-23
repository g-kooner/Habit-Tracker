package com.example.agilesprinters;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;


import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This class is a dialog fragment that allows the user to add a new habit event.
 *
 * @author Sai Rasazna Ajerla and Riyaben Patel
 */
public class addHabitEventFragment extends DialogFragment{
    private int position;
    private String EID;
    private String UID;
    private String HID;
    private String IID;
    private String FID;
    private EditText optional_comment;
    private TextView input_date;
    private EditText input_duration;
    private Spinner durationSpinner;
    private ImageView imageContainer;
    private ImageView addCamPhotoBtn;
    private ImageView addGalPhotoBtn;
    private Uri selectedImg;
    private Bitmap bitmapOfImg;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private addHabitEventFragment.OnFragmentInteractionListener listener;

    /**
     * This function saves the values sent to the fragment for future manipulation
     * @param UID is the id of the user
     * @param HID is the id of the habit
     * @param EID is the id of the instance
     * @param position is the selected item position
     * @return returns the fragment with the bundled parameters
     */
    public static addHabitEventFragment newInstance(int position, String UID, String HID, String EID, String IID) {
        addHabitEventFragment fragment = new addHabitEventFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("UID", UID);
        args.putString("HID", HID);
        args.putString("EID", EID);
        args.putString("IID", IID);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * This interface listens for when dialog is ended and sends the information and the function
     * to the User Calendar class for it to implement.
     */
    public interface OnFragmentInteractionListener {
        void onSavePressed(HabitInstance habitInstance, Bitmap bitmapOfImg);
    }

    /**
     * This function attaches the fragment to the activity and keeps track of the context of the
     * fragment so the listener knows what to listen to. Ensures that the proper methods are
     * implemented by the User calendar class.
     * @param context is the current screen
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This function creates the actual dialog on the screen and listens for user input, returning
     * the information through the listener based on which button is clicked.
     * @param savedInstanceState is a reference to the most recent object
     * @return
     * Returns the Dialog created
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit_event_fragment, null);

        // Display the calendar
        optional_comment = view.findViewById(R.id.editText_comment);
        input_date = view.findViewById(R.id.editText_date);
        input_duration = view.findViewById(R.id.editText_duration);
        durationSpinner = view.findViewById(R.id.duration_spinner);

        imageContainer = view.findViewById(R.id.imageContainer);
        addCamPhotoBtn = view.findViewById(R.id.add_Cam_Photo);
        addGalPhotoBtn = view.findViewById(R.id.add_Gal_Photo);

        addCamPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(runtimePermissionForCamera()){
                    getCameraPicture();
                //}
            }
        });

        addGalPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGalleryPicture();
                //hides alert dialog after gallery func is finished
            }
        });

        UID = getArguments().getString(getString(R.string.UID));
        HID = getArguments().getString(getString(R.string.HID));
        EID = getArguments().getString(getString(R.string.EID));
        IID = getArguments().getString(getString(R.string.IID));
        FID = getArguments().getString("FID");

        LocalDate currentDate = LocalDate.now();
        input_date.setText(currentDate.format(formatter));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Habit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    /* Do not implement anything here in order to override the button
                     * to only call the listener once all the information required has been
                     * filled out and display error messages if they have been left blank.
                     */
                }).create();

    }

    //switches view to gallery and allows user to pick photo
    private void getGalleryPicture() {
        //allow user to pick a photo from gallery
        Intent pickFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickFromGallery, 1);

    }

    private void getCameraPicture(){
        //have to give permission to app to use camera
        //android manifest give permission and then take permission at runtime from user
        //switch view to camera view
        Intent cameraView = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraView, 2);
    }


    //overrides the method when activity is returning data (prev intent on line 82)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){

            case 1:
                if(resultCode == -1){
                    //URI is string of characters used to identify a resource (either by location name or both)
                    //use android net uri
                    selectedImg =  data.getData();
                    try {
                        bitmapOfImg = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImg);
                    } catch (IOException e) {
                        System.out.println(e+"I error");
                    }
                    //set the original placeholder img to be the selected img
                    imageContainer.setImageBitmap(bitmapOfImg);
                }
                break;

            case 2:
                Log.d("CAMERA", "case 2 for camera return result ");
                if(resultCode == -1 && data != null){
                    //retrieve data sent back from activity thru bundle
                    Bundle bundle = data.getExtras();

                    //bitmap of the image matrix of dots (each dot corresponds to pixel)
                    //grabs img data from extra
                    bitmapOfImg = (Bitmap) bundle.get("data");

                    //set placeholder to bitmap of the img taken by camera
                    imageContainer.setImageBitmap(bitmapOfImg);

                }
                break;
        }
    }

    /*
    public boolean runtimePermissionForCamera(){
        //only have to check runtime permission if android version is greater than 23
        //if less than 23 then auto permission using manifest file
        if(Build.VERSION.SDK_INT >= 23){
            int cameraPermission = .checkSelfPermission(, Manifest.permission.CAMERA);
            if(cameraPermission == PackageManager.PERMISSION_DENIED){
                //asks user if allow permission or not
                requestPermissions( new String[]{Manifest.permission.CAMERA}, 102);
                return false;
            }
        }
        return true;
    }


    //return result from requestPermission (line 132)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 102 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //user has granted permission so run func to take picture
            getCameraPicture();
        }
        else{
            Toast.makeText(this.getContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();

        }

    }

    */


    /**
     * This function overrides the buttons clicked in order to only allow the dialog to be dismissed
     * when all requirements have been met.
     */
    @Override
    public void onResume(){
        super.onResume();

        final AlertDialog dialog = (AlertDialog) getDialog();
        if(dialog != null){
            Button positive = dialog.getButton(Dialog.BUTTON_POSITIVE);

            positive.setOnClickListener(view -> {
                // Boolean tracks when the all the fields have been filled out. Will turn to false
                // if anything has been left blank.
                boolean readyToClose = true;

                String comment = optional_comment.getText().toString();
                String date_entry = input_date.getText().toString();
                String duration = input_duration.getText().toString();
                String durationSetting = durationSpinner.getSelectedItem().toString();

                if (comment.length() > 20) {
                    readyToClose = false;
                    optional_comment.setError("This field cannot have more than 20 chars");
                }

                if (date_entry.matches("")) {
                    readyToClose = false;
                    input_date.setError("This field cannot be blank");
                }


                if (duration.matches("")) {
                    readyToClose = false;
                    input_duration.setError("This field cannot be blank");
                }

                if (durationSetting.matches("mins")) {
                    if (Integer.parseInt(duration) < 0 || Integer.parseInt(duration) > 60) {
                        readyToClose = false;
                        input_duration.setError("Mins value  muust be between 0 and 60");
                    }
                }

                if (durationSetting.matches("hr")) {
                    if (Integer.parseInt(duration) < 0 || Integer.parseInt(duration) > 2) {
                        readyToClose = false;
                        input_duration.setError("Hour value must be below 2");
                    } else {
                        duration = String.valueOf(Integer.parseInt(duration) * 60);
                    }
                }

                // If everything has been filled out, call the listener and send the edited
                // habit back to the Home class and dismiss the dialog.
                if(readyToClose){
                    
                    listener.onSavePressed(new HabitInstance(EID, UID, HID, comment, date_entry,
                            Integer.parseInt(duration), IID, FID), bitmapOfImg);
                    dialog.dismiss();
                }
            });
        }
    }

}
