package com.example.hesiod.lingdiantgxt.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.LauncherApps;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.webshow;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Hesiod on 2019/12/19.
 */

public class myHttpuilt {
    public static final String TYPE_JSON = "json";
    public static final String TYPE_BITMAP = "bitmap";

    private CallBack listener;
    private uiltData uiltdata = new uiltData();
    public class uiltData{
        public Bitmap bitmap = null;
        public String errmsg = "";
        public String resmsg = "";
        public String type = "";
        public String url = "";
    }

    public void getWebData (String url,String type,CallBack listener){
        new DownloadTask().execute(url,type);
        this.listener = listener;
    }
    public interface CallBack{
        //回调方法
        void OKres(uiltData uiltdata);
        void ERRres(uiltData uiltdata);
        //void TPres(Bitmap bitmap);
    }

    //网络异步请求，url，返回JSON或图片
    private class DownloadTask extends AsyncTask<String, Integer, Boolean> {
        private InputStream is = null;
        private BufferedReader br = null;
        private StringBuilder sb = new StringBuilder("");

        @Override
        protected Boolean doInBackground(String... param) {
            try {
                uiltdata.url = param[0];
                uiltdata.type = param[1];
                URL uri = new URL(uiltdata.url);
                HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
                connection.setRequestMethod("GET");     //GET方式请求数据
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);
                connection.connect();   //开启连接
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    is = connection.getInputStream();
                    switch (uiltdata.type){
                        case TYPE_BITMAP:
                            uiltdata.bitmap = BitmapFactory.decodeStream(is);
                            return true;
                        case TYPE_JSON:
                            //参数字符串，如果拼接在请求链接之后，需要对中文进行 URLEncode   字符集 UTF-8
                            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                            String line;
                            while ((line = br.readLine()) != null) {    //缓冲逐行读取
                                sb.append(line);
                            }
                            uiltdata.resmsg = sb.toString();        //接收结果，接收到的是JSON格式的数据
                            return true;
                        default:
                            uiltdata.errmsg = "返回数据类型错误";
                            return false;
                    }
                }else{
                    uiltdata.errmsg = "请求错误："+connection.getResponseCode();
                    return false;
                }
            } catch (Exception ignored) {
                uiltdata.errmsg = "连接异常";
                return false;
            }
        }
        //结束接收
        @Override
        protected void onPostExecute(Boolean result) {
            try {
                if (is != null) {
                    is.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (Exception ignored) {}
            if (result) {
                listener.OKres(uiltdata);
            } else {
                listener.ERRres(uiltdata);    //显示解析后的结果
            }
        }
    }
}
