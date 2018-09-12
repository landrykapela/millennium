package tz.co.neelansoft.millennium.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadPdfTask extends AsyncTask<String,Integer,PdfDocument> {
    private static final String TAG="DownloadPdfTask";
    ViewPager viewPager;
    Context context;
    ProgressDialog pDialog;
    OutputStream output = null;
    InputStream input =null;

    public DownloadPdfTask(Context context, ViewPager vPager) {

        this.context = context;
        this.viewPager = vPager;
        this.pDialog = new ProgressDialog(context);
    }
    @Override
    protected void onPreExecute(){
        pDialog.setMessage("Opening book...");
        pDialog.setCancelable(true);
        pDialog.setIndeterminate(false);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.show();
    }

    @Override
    protected PdfDocument doInBackground(String... urls) {
        String urldisplay = urls[0];
        Log.e(TAG,"book url: "+urldisplay);



        //Bitmap mIcon11 = null;
        com.shockwave.pdfium.PdfDocument document=null;

        int count;
        try {
            URL url = new URL(urldisplay);
            URLConnection conection = url.openConnection();
            conection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = conection.getContentLength();

            // download the file
            input = conection.getInputStream();


            File pdfFile = getTempFile();//new File(context.getFilesDir(),"book.pdf");
            Log.e(TAG,"pdfFile path "+pdfFile.getAbsolutePath());

            // Output stream
            output = new FileOutputStream(pdfFile);

            byte data[] = new byte[lenghtOfFile];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;

                // publishing the progress....
                // After this onProgressUpdate will be called
                if(lenghtOfFile > 0) {
                    publishProgress((int) (total * 100/lenghtOfFile));
                }

                // writing data to file
                output.write(data, 0, count);
                Log.e(TAG,"Count "+count+"/total: "+total+"/"+lenghtOfFile);
            }


            //File tmpFile = new File(uri);


            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();


            PdfiumCore core = new PdfiumCore(context);

            if(pdfFile.exists()){
                Log.e(TAG,"File exists. Path: "+pdfFile.getAbsolutePath());
                Uri uri = Uri.fromFile(pdfFile);
                try {
                    // document = core.newDocument(new ParcelFileDescriptor(context.getContentResolver()));

                    document = core.newDocument(new ParcelFileDescriptor(context.getContentResolver().openFileDescriptor(uri, "r")));

                }
                catch (IOException e){
                    e.printStackTrace();
                    Log.e(TAG,"Can't find download.pdf");
                }

            }
            else{

                Log.e(TAG,"file does not exists. path: "+pdfFile.getAbsolutePath());
                return null;
            }





        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error: ", e.getMessage());
        }


        return document;
    }


    protected void onPostExecute(PdfDocument result) {
        pDialog.dismiss();

        if (result != null) {
            Log.e(TAG,"result is not null");
            pDialog.dismiss();
            //  Toast.makeText(context, "Downloaded Successfully", Toast.LENGTH_SHORT).show();

            LibraryCustomPagerAdapter adapter = new LibraryCustomPagerAdapter(context,result);
            viewPager.setAdapter(adapter);

        } else {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 3000);

            Log.e(TAG, "Download Failed");

        }
    /*    } catch (Exception e) {
            e.printStackTrace();

            //Change button text if exception occurs

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 3000);
            Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

        }*/
        super.onPostExecute(result);

    }
    /**
     * Updating progress bar
     * */

    protected void onProgressUpdate(Integer... progress) {
        // setting progress percentage
        pDialog.setProgress(progress[0]);
    }

    private File getTempFile() {
        File file=null;
        try {
            String fileName = "book";
            file = File.createTempFile(fileName, null, context.getCacheDir());
            Log.e(TAG,"getTempFile path: "+file.getAbsolutePath());
        } catch (IOException e) {
            // Error while creating file
        }
        return file;
    }
}
