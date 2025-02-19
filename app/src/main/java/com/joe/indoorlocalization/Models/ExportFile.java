package com.joe.indoorlocalization.Models;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.joe.indoorlocalization.State.ApplicationState;
import com.joe.indoorlocalization.IndoorLocalization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Exports objects to file from the ApplicationState
 * Created by joe on 08/01/16.
 */
public class ExportFile {

    static String TAG = ExportFile.class.getSimpleName();

    private ApplicationState state;
    private Context context;

    public ExportFile(Context context) {
        this.context = context;
        this.state = ((IndoorLocalization)context.getApplicationContext()).getApplicationState();
    }

    public void exportApplicationStateIntoFile() {
        if(this.state.getFingerPrints().size() == 0) {
            Toast.makeText(context, "Nothing to export", Toast.LENGTH_SHORT).show();
            return;
        }
        String fileName = "dataJoe.txt";
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);

        // Saving in format z;x;y;mac;rssi;mac;rssi;mac;rssi...
        try {
            OutputStream os = new FileOutputStream(file);
            for(FingerPrint fp : this.state.getFingerPrints()) {
               int z = fp.getZ();
               float x = fp.getX();
               float y = fp.getY();
               String row = z + ";" + x + ";" + y;
               for(Scan scan : fp.getScans()) {
                   row += ";" + scan.getMac() + ";" + scan.getRSSI();
               }
               os.write(row.getBytes());
               os.write("\n".getBytes());
           }
            os.close();
            os.flush();
            Log.i(TAG, "File " + fileName + " saved");
            Toast.makeText(context, "Exported to file " + fileName + " succesfully.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d(TAG, "Error on saving file, exception on data export");
            Log.d(TAG, e.getMessage());
        }
    }
}
