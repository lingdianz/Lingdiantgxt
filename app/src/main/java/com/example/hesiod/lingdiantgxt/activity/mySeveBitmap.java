package com.example.hesiod.lingdiantgxt.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Hesiod on 2019/12/20.
 */

public class mySeveBitmap {

    private class SevefileTask extends AsyncTask<Bitmap,Integer,Boolean> {
        File file=null;
        String errmsg="";

        @Override
        protected Boolean doInBackground(Bitmap... param) {
            try {
            String path = "";
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                path = Environment.getExternalStorageDirectory() + File.separator;//保存到sd根目录下
            }
            file = new File(path, "share" + ".jpg");
                if (file.exists()) {
                    file.delete();
                }
                Bitmap bitmap = param[0];
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                bitmap.recycle();
                out.flush();
                out.close();
                errmsg = "保存完成";
                return true;
            } catch (FileNotFoundException e) {
                errmsg = "保存异常"+e.toString();
                return false;
            } catch (IOException e) {
                errmsg = "保存异常"+e.toString();
                return false;
            }
        }
        //结束接收
        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                listener.OKseve(file,errmsg);
            }else{
                listener.ERRseve(errmsg);
            }
        }
    }
    private Sevefileto listener;
    public void IsFishSeve(Bitmap bitmap,Sevefileto listener){
        new SevefileTask().execute(bitmap);
        this.listener=listener;
    }
    public interface Sevefileto{
        void OKseve(File file,String errmsg);
        void ERRseve(String errmsg);
    }//*/
}
