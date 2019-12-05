package com.example.rdr3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.service.autofill.ImageTransformation;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity {
    //public final PICKFILE_RESULT_CODE
    ImageView i;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void setMen(View view){
        Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        startActivityForResult(
                Intent.createChooser(chooseFile, "Choose a file"),
                1
        );

    }
    File source;

    ZipEntry ze;
    ZipInputStream zip;
    int page = 0;
    int buf_page = 0;
    Bitmap btm[] = new Bitmap[255];

    public void next_page(View view){
        if(page == buf_page)
        try {

            ze = zip.getNextEntry();
            Bitmap bitmap = BitmapFactory.decodeStream(zip);
            btm[++page] = bitmap;
            i.setImageBitmap(btm[page]);
            txt.setText(ze.getName());
            buf_page++;
            //page++;
        } catch(Exception e) {
            e.getStackTrace();
        }
        else
            try{
                i.setImageBitmap(btm[++page]);
            } catch (Exception ex){
                ex.getStackTrace();
            }
    }

    Uri content_describer;

    public void prev_page(View view) throws FileNotFoundException{
        /*zip = new ZipInputStream(getContentResolver().openInputStream(content_describer));
        boolean a = true;
        for(int j=0; j<page-1; j++)
            try {
                zip.getNextEntry();
            } catch (Exception e){
                a = false;
                e.getStackTrace();
            }
        if(a)*/
            try{
                //ze = zip.getNextEntry();
                //String s = ze.getName();

                //txt.setText(s);
                //Bitmap bitmap = BitmapFactory.decodeStream(zip);
                if(page > 0)
                    i.setImageBitmap(btm[--page]);
                //if(page > 0) page--;
            } catch (Exception ex){
                ex.getStackTrace();
            }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){

            content_describer = data.getData();
            String src = content_describer.getPath();
            source = new File(src);
            Log.d("src is ", source.toString());
            String filename = content_describer.getLastPathSegment();
            txt = (TextView) findViewById(R.id.textView);
            txt.setText(content_describer.toString());
            Log.d("FileName is ",filename);
            i = (ImageView) findViewById(R.id.imageMen);
            try {

                zip = new ZipInputStream(getContentResolver().openInputStream(content_describer));
                ze = zip.getNextEntry();
                //String s = ze.getName();
                //txt.setText(s);

                Bitmap bitmap = BitmapFactory.decodeStream(zip);
                page = 0;
                buf_page = 0;
                for(int j=0; j<255; j++)
                    btm[j] = null;
                btm[page] = bitmap;
                i.setImageBitmap(btm[page]);


            } catch (Exception e){
                String s = "fail";
                txt.setText(s);
            }
            /*try {
                InputStream inputStream = getContentResolver().openInputStream(content_describer);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                i.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/


        }
    }
}
