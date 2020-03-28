package com.example.hesiod.lingdiantgxt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.mySeveBitmap;
import com.example.hesiod.lingdiantgxt.activity.myHttpuilt;
import com.example.hesiod.lingdiantgxt.baseadapter.tpadapter;
import com.example.hesiod.lingdiantgxt.myJavaBean.TestBean;
import com.example.hesiod.lingdiantgxt.myJavaBean.myBitmap;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class webshow extends BaseAcvtivity {

    @BindView(R.id.tvtest)
    TextView tvtest;
    @BindView(R.id.lvtest)
    ListView lvtest;
    @BindView(R.id.btntest)
    Button btntest;
    @BindView(R.id.webshow)
    RelativeLayout webshow;
    @BindView(R.id.imgback)
    ImageView imgback;
    @BindView(R.id.ring1)
    ProgressBar ring1;
    private tpadapter adapter;
    private List<myBitmap> mybitmaplist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webshow);
        ButterKnife.bind(this);
        initview();
    }

    private void initview() {
        tvtest.setMovementMethod(ScrollingMovementMethod.getInstance());
        adapter = new tpadapter(webshow.this, R.layout.bitmap, mybitmaplist);
        lvtest.setAdapter(adapter);
    }

    @OnClick(R.id.btntest)
    public void onViewClicked() {
        String url1 = "https://suggest.taobao.com/sug?code=utf-8&q=电脑&callback=cb";
        String url2 = "http://api.expoon.com/AppNews/getNewsList/type/1/p/1";
        Toast.makeText(webshow.this, "开始查找", Toast.LENGTH_SHORT).show();
        getJson(url2);
    }

    TestBean.TestTP testTP;
    HashMap hashMap = new HashMap();

    private void getJson(final String url) {
        new myHttpuilt().getWebData(url, myHttpuilt.TYPE_JSON, new myHttpuilt.CallBack() {
            @Override
            public void OKres(myHttpuilt.uiltData uiltdata) {
                mybitmaplist.clear();
                hashMap.clear();
                //getfile("https://d.qiezzi.com/qiezi-clinic.apk");
                //getfile("http://www.baidu.com");
                getfile("http://h.hiphotos.baidu.com/news/q%3D100/sign=d38d087d5bdf8db1ba2e78643921dddb/9345d688d43f87942b2f41cbd71b0ef41ad53a06.jpg");

                Gson gson = new Gson();
                testTP = gson.fromJson(uiltdata.resmsg, TestBean.TestTP.class);
                tvtest.setText("info:" + testTP.getInfo() + "\r\n");
                tvtest.append("status:" + testTP.getStatus() + "\r\n");
                for (int ct = 0; ct < testTP.getData().size(); ct++) {
                    getTP(ct, testTP.getData().get(ct).getPic_url());
                }
            }

            @Override
            public void ERRres(myHttpuilt.uiltData uiltdata) {
                String showmsg = "gettpurl err:\r\n" + uiltdata.errmsg;
                tvtest.setText(showmsg);
            }
        });
    }

    private void getTP(final int ct, String url) {
        new myHttpuilt().getWebData(url, myHttpuilt.TYPE_BITMAP, new myHttpuilt.CallBack() {
            @Override
            public void OKres(myHttpuilt.uiltData uiltdata) {
                tvtest.append(uiltdata.type + "\r\r");

                myBitmap myBitmap = new myBitmap();
                TestBean.TestTP.DataBean dataBean = testTP.getData().get(ct);
                myBitmap.setTitle(ct + ":" + dataBean.getNews_title());
                myBitmap.setSummary(dataBean.getNews_summary());
                myBitmap.setBitmap(uiltdata.bitmap);
                mybitmaplist.add(myBitmap);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void ERRres(myHttpuilt.uiltData uiltdata) {
                String showmsg = "gettpmsg err:\r\n" + uiltdata.errmsg;
                tvtest.setText(showmsg);
            }
        });
    }

    private void getimg(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();                 //okhttp连接实例
        okHttpClient.newBuilder().connectTimeout(5, TimeUnit.SECONDS);   //连接超时
        okHttpClient.newBuilder().readTimeout(5, TimeUnit.SECONDS);      //读取超时
        Request newCall = new Request.Builder().url(url).build();       //证书验证

        new OkHttpClient().newCall(newCall).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(webshow.this, e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final myBitmap myBitmap = new myBitmap();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (response.isSuccessful()) {//回调的方法执行在子线程。
                                //InputStream  is = response.body().byteStream();
                                myBitmap.setTitle("response.code()==" + response.code());
                                //myBitmap.setSummary(getHeaderFileName(response));
                                myBitmap.setBitmap(BitmapFactory.decodeStream(response.body().byteStream()));
                                response.body().byteStream().close();
                            }
                        } catch (NullPointerException e) {
                            Toast.makeText(webshow.this, e.toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(webshow.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        webshow.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mybitmaplist.add(myBitmap);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                }).start();
            }
        });
    }

    private void getfile(String url) {
        ring1.setVisibility(View.VISIBLE);
        OkHttpClient okHttpClient = new OkHttpClient();                 //okhttp连接实例
        okHttpClient.newBuilder().connectTimeout(5, TimeUnit.SECONDS);   //连接超时
        okHttpClient.newBuilder().readTimeout(20, TimeUnit.SECONDS);      //读取超时
        Request newCall = new Request.Builder().url(url).build();       //证书验证

        new OkHttpClient().newCall(newCall).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ring1.setVisibility(View.GONE);
                Toast.makeText(webshow.this, e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final myBitmap myBitmap = new myBitmap();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (response.isSuccessful()) {//回调的方法执行在子线程。
                                myBitmap.setTitle("response.code()==" + response.code());
                                myBitmap.setSummary(response.message());
                                myBitmap.setBitmap(BitmapFactory.decodeStream(response.body().byteStream()));
                                response.body().byteStream().close();
                            }
                        } catch (NullPointerException e) {
                            //Toast.makeText(webshow.this, e.toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            //Toast.makeText(webshow.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        webshow.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ring1.setVisibility(View.GONE);
                                Bitmap bitmap = myBitmap.getBitmap();
                                Bitmap bmp = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                                SeveAndSharp(bitmap);//保存并分享图片
                                myBitmap.setBitmap(bmp);

                                mybitmaplist.add(0, myBitmap);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                }).start();
            }
        });
    }
/*
    private static String getHeaderFileName(Response response) {
        String dispositionHeader = response.header("Content-Disposition");
        if (!TextUtils.isEmpty(dispositionHeader)) {
            dispositionHeader.replace("attachment;filename=", "");
            dispositionHeader.replace("filename*=utf-8", "");
            String[] strings = dispositionHeader.split("; ");
            if (strings.length > 1) {
                dispositionHeader = strings[1].replace("filename=", "");
                dispositionHeader = dispositionHeader.replace("\"", "");
                return dispositionHeader;
            }
            return "noting";
        }
        return "null";
    }*/

    private void SeveAndSharp(Bitmap bitmap) {
        new mySeveBitmap().IsFishSeve(bitmap, new mySeveBitmap.Sevefileto() {
            @Override
            public void OKseve(File file,String errmsg) {
                    Uri imageUri;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //Uri.parse(file.toString());//
                        imageUri = FileProvider.getUriForFile(webshow.this, "com.example.hesiod.fileprovider", file);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        imageUri = Uri.fromFile(file);
                    }
                    try {
                    intent.setAction(Intent.ACTION_SEND);

                    //1、分享字符串数据内容，EXTRA_TEXT指的是，String类型
                    //intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                    //intent.setType("text/plain");       // 声明数据类型

                    ///2、发送二进制文件数据流内容（比如图片、视频、音频文件等等）
                    // 指定发送的内容，EXTRA_STREAM指的是，Uri类型参数
                    intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    intent.setType("image/jpeg");         // 声明数据类型 (MIME type)
                    startActivity(Intent.createChooser(intent, "Share to..."));//*/
                } catch (Exception e) {
                    Toast.makeText(webshow.this, errmsg+",\r\nfile:"+file.toString()+"\r\nerr:"+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void ERRseve(String errmsg) {
                Toast.makeText(webshow.this, errmsg, Toast.LENGTH_SHORT).show();
            }
        });
    }//*/
}
