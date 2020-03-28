package com.example.hesiod.lingdiantgxt;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.getweather;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.activity.mysocket;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_clients;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class clientmap extends BaseAcvtivity {

    private String shebeiname="",client_location="";
    private ce_clients clientobj;
    private Toolbar toolbar;
    private int mode=ALL;

    private link app;
    private messagerom messagerom;

    private MapView mMapView = null;
    private BaiduMap baiduMap = null;
    private Boolean fistshow=true;
    private TextView tvlog;
    private Button btnreload,btnputong,btnweixing,btngengxin;
    private CheckBox cbreli,cblukuang;
    private ProgressBar ring;
    private LinearLayout layoutbtn;

    private LocationClient mylocationclient = null;
    private Button btnremov;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clientmap);
        app=(link)getApplication();
        messagerom=app.getmessagerom();
        Intent intent = getIntent();
        shebeiname = intent.getStringExtra("shebeiname");
        getdata();
        initview();
        getlocation();
    }

    public static int ALL = 0;
    public static int ONE = 1;
    private void initview(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        inittoolbar();  //初始化toobar

        btngengxin = (Button)findViewById(R.id.btngengxin);
        layoutbtn = (LinearLayout)findViewById(R.id.layoutbtn);
        tvlog = (TextView)findViewById(R.id.tvlog);
        tvlog.setOnClickListener(new mClick());
        ring = (ProgressBar)findViewById(R.id.ring);
        btnreload = (Button)findViewById(R.id.btnreload);
        btnweixing = (Button)findViewById(R.id.btnweixing);
        btnputong = (Button)findViewById(R.id.btnputong);
        cbreli = (CheckBox)findViewById(R.id.cbreli);
        cblukuang = (CheckBox)findViewById(R.id.cblukuang);

        btnreload.setOnClickListener(new mClick());
        btnweixing.setOnClickListener(new mClick());
        btnputong.setOnClickListener(new mClick());

        cbreli.setOnClickListener(new mClick());
        cblukuang.setOnClickListener(new mClick());
        mMapView = (MapView)findViewById(R.id.bmapView);
    }
    private Boolean getdata(){
        if(shebeiname.equals("所有控制器")) {
            mode = ALL;
        }else{
            mode = ONE;
            for (int ct = 0; ct < messagerom.getClients().size(); ct++) {
                if (messagerom.getClients().get(ct).getClientname().equals(shebeiname)) {
                    clientobj = messagerom.getClients().get(ct);
                    client_location = clientobj.getLocation();
                }
            }
            if (clientobj == null) {
                Toast.makeText(clientmap.this, "数据错误，请返回刷新重试！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        return true;
    }

    //按钮点击事件
    private class mClick implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.btnreload:    //转到我在的位置
                    tomyaddr(mylocation);
                    break;
                case R.id.btnputong:    //普通地图
                    baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    break;
                case R.id.btnweixing:   //卫星地图
                    baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    break;
                case R.id.cbreli:   //显示城市热力
                    if(cbreli.isChecked()) {
                        baiduMap.setBaiduHeatMapEnabled(true);
                    }else{
                        baiduMap.setBaiduHeatMapEnabled(false);
                    }
                    break;
                case R.id.cblukuang:    //显示城市路况
                    if(cblukuang.isChecked()) {
                        baiduMap.setTrafficEnabled(true);
                        baiduMap.setCustomTrafficColor("#ffba0101", "#fff33131", "#ffff9e19", "#00000000");
                        //  对地图状态做更新，否则可能不会触发渲染，造成样式定义无法立即生效。
                        //MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(18);
                        //baiduMap.animateMapStatus(u);
                    }else{
                        baiduMap.setTrafficEnabled(false);
                    }
                    break;
                case R.id.tvlog:
                    tvlog.setVisibility(View.GONE);
                    break;
                case R.id.btngengxin:
                    if(getpromissionlist().size()>0){
                        Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                        intent1.setData(uri);
                        startActivity(intent1);
                        Toast.makeText(clientmap.this,"使用地图需要开启权限，请设置权限！",Toast.LENGTH_LONG).show();
                    }else{
                        if(baiduMap==null){
                            getmylocation();
                        }
                        btngengxin.setVisibility(View.GONE);
                        editmode = FALSE;
                        invalidateOptionsMenu(); //重新绘制menu
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private List<String> getpromissionlist(){
        List<String> plist = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(clientmap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            plist.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(clientmap.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            plist.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(clientmap.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            plist.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        return plist;
    }

    //先判断权限
    private void getlocation(){
        List<String> plist = getpromissionlist();   //查询是否已经获得权限
        if(!plist.isEmpty()){
            String[] ps = plist.toArray(new String[plist.size()]);
            ActivityCompat.requestPermissions(clientmap.this,ps,1);
            editmode=MISS;
            invalidateOptionsMenu(); //重新绘制menu
            layoutbtn.setVisibility(View.GONE); //隐藏地图控制按钮
            btngengxin.setVisibility(View.VISIBLE); //显示刷新按钮
            btngengxin.setOnClickListener(new mClick());
        }else{      //当权限全部通过时，直接获取我的位置并显示
            getmylocation();        //启动获取地址，退出程序才退出
        }
    }

    //获取当前手机所在的位置
    private void getmylocation(){
        if(baiduMap!=null){return;}
        baiduMap = mMapView.getMap();
        initclick();
        mylocationclient = new LocationClient(getApplicationContext());
        baiduMap.setMyLocationEnabled(true);    //开启地图定位图层

        LocationClientOption option = new LocationClientOption();//初始化监听器属性
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);   //刷新时间间隔
        option.setIsNeedAddress(true);      //开启需要地址名称
        mylocationclient.setLocOption(option);  //设置属性

        MyLocationListener myLocationListener = new MyLocationListener();   //初始化监听线程
        mylocationclient.registerLocationListener(myLocationListener);      //绑定监听线程
        mylocationclient.start();       //开始监听
    }

    private BDLocation mylocation;      //我的位置
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            mylocation = location;
            if(fistshow) {          //非第一次走人
                showclient(mylocation);
                tomyaddr(mylocation);
                layoutbtn.setVisibility(View.VISIBLE);
            }
        }
    }

    //显示到我所在的位置
    private void tomyaddr(BDLocation location){
        if(location==null){
            if(baiduMap==null){     //应该是权限设置导致为空，所以手动刷新后主动加载
                getmylocation();
            }   return; }     //为空走人

        LatLng mylocation2 = new LatLng(location.getLatitude(),location.getLongitude());  //当前我的位置;
        if(fistshow && mode==ONE && client_location.contains(":")){
            String[] cut = client_location.split(":");
            float wei1 = Float.parseFloat(cut[0]);
            float jing1 = Float.parseFloat(cut[1]);
            mylocation2 = new LatLng(wei1,jing1);
        }else{
            Toast.makeText(clientmap.this,"我的位置："+location.getAddrStr(),Toast.LENGTH_LONG).show();
        }
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(mylocation2);
        baiduMap.animateMapStatus(update);
        fistshow = false;   //清除第一次设置
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.getDirection())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        baiduMap.setMyLocationData(locData);

//        MapStatus.Builder builder = new MapStatus.Builder();
//        builder.zoom(16.0f);
//        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }

    private void showclient(BDLocation location){
        for (int ct = 0; ct < messagerom.getClients().size(); ct++) {
            String clientname = messagerom.getClients().get(ct).getClientname();
            String location_str = messagerom.getClients().get(ct).getLocation();
            float location_wei = (float)location.getLatitude() + ct * 0.0001f;      //把double转为float，因为不需要这么高精度
            float location_jing = (float)location.getLongitude() + ct * 0.0001f;    //节约空间流量
            if (location_str != null && !location_str.equals("0")) {
                String[] cutstr = location_str.split(":");
                if (cutstr.length == 2) {
                    try {
                        location_wei = Float.parseFloat(cutstr[0]);
                        location_jing = Float.parseFloat(cutstr[1]);
                    } catch (Exception e) {
                    }
                }
            }else if(mode == ONE && shebeiname.equals(clientname)){    //如果在单选模式时，设备是未有地址的，得暂时给个地址
                client_location = location_wei + "," + location_jing;
            }
            if(mode==ALL || shebeiname.equals(clientname)) {
                showpoint(clientname,big ,location_wei,location_jing);  //大图标
            }else{
                showpoint(clientname, mini,location_wei,location_jing);     //小图标
            }
        }
    }
    public static int mini = 0;
    public static int big = 1;
    private void showpoint(String client,int type,float wei,float jing) {
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_c);     //构建Marker大图标;
        float tmd = 0.8f;
        switch (type){
            case 0:
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_cmini);     //构建Marker小图标
                tmd = 0.5f;
                break;
            case 1:
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_c);     //构建Marker大图标
                tmd = 0.8f;
                break;
            default:
                break;
        }
        //定义Maker坐标点
        LatLng point = new LatLng(wei, jing);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point) //必传参数
                .perspective(true)  //远小近大
                .title(client)
                .icon(bitmap) //必传参数
                .draggable(true)
                //设置平贴地图，在地图中双指下拉查看效果
                .flat(false)
                .alpha(tmd);
        //在地图上添加Marker，并显示
        baiduMap.addOverlay(option);

    }
    private void initclick() {
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(tvlog.isCursorVisible()){
                    tvlog.setVisibility(View.GONE);
                }
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            //marker被点击时回调的方法
            //若响应点击事件，返回true，否则返回false
            //默认返回false
            @Override
            public boolean onMarkerClick(Marker marker) {
                showmsg(marker);
                return false;
            }
        });

        baiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            //在Marker拖拽过程中回调此方法，这个Marker的位置可以通过getPosition()方法获取
            //marker 被拖动的Marker对象
            @Override
            public void onMarkerDrag(Marker marker) {
                //对marker处理拖拽逻辑
            }

            //在Marker拖动完成后回调此方法， 这个Marker的位可以通过getPosition()方法获取
            //marker 被拖拽的Marker对象
            @Override
            public void onMarkerDragEnd(Marker marker) {
                if(marker.getTitle().equals(shebeiname)){
                    float wei = (float) marker.getPosition().latitude;
                    float jing = (float) marker.getPosition().longitude;
                    client_location = wei + ":" +jing;
                }
            }

            //在Marker开始被拖拽时回调此方法，这个Marker的位可以通过getPosition()方法获取
            //marker 被拖拽的Marker对象
            @Override
            public void onMarkerDragStart(Marker marker) {

            }
        });
    }
    //
    private void showmsg(Marker marker){
        ring.setVisibility(View.VISIBLE);
        Toast.makeText(clientmap.this,"正获取位置和天气信息。。。",Toast.LENGTH_SHORT).show();
        togetweather(marker);
    }
    private void togetweather(final Marker marker){
        tvlog.setText("");
        float wei = (float)marker.getPosition().latitude;
        float jing = (float)marker.getPosition().longitude;
        //和风天气免费接口网址，每天3000次获取，location位置格式 ：经度，纬度
        String weatherADDR = jing+","+wei;   //get方法，免费接口："https://free-api.heweather.net/s6/weather/now?key="+messagerom.weatherKEY+"&location="+jing+","+wei;
        Location = weatherADDR;
        WebKey = messagerom.weatherKEY;
        new DownloadTask().execute();
        //getweather get = new getweather();
        //get.getWeatherMsg(clientmap.this,messagerom.weatherKEY,weatherADDR);
        //以下用的是直接加载和风的sdk，简单  HeWeather.OnResultSearchBeansListener()
        /*HeWeather.getSearch(clientmap.this, weatherADDR,"",1, Lang.CHINESE_SIMPLIFIED, new HeWeather.OnResultSearchBeansListener(){
            @Override
            public void onError(Throwable e) {
                Toast.makeText(clientmap.this, "获取城市失败:"+e, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(Search dataObject) {
                if ( Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus()) ) {
                    //此时返回数据，这是个数据类
                    try {
                        List<Basic> basiclist = dataObject.getBasic();
                        Basic basic = basiclist.get(0);
                        StringBuilder strb = new StringBuilder();
                        strb.append("控制器：").append(marker.getTitle()).append("\n");
                        strb.append("位置：\n");
                        strb.append(basic.getCnty()).append("\r");//国
                        strb.append(basic.getAdmin_area()).append("\r");//省
                        strb.append(basic.getParent_city()).append("\r");//市
                        strb.append(basic.getLocation()).append("\n\n");//区

                        strb.append(tvlog.getText());
                        tvlog.setText(strb);
                    } catch (Exception e) {
                        Toast.makeText(clientmap.this, "获取城市失败:", Toast.LENGTH_LONG).show();
                    }
                }else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Toast.makeText(clientmap.this, "获取城市失败:"+code.getTxt(), Toast.LENGTH_LONG).show();
                }
            }
        });


        /**
         * 实况天气
         * 实况天气即为当前时间点的天气状况以及温湿风压等气象指数，具体包含的数据：体感温度、
         * 实测温度、天气状况、风力、风速、风向、相对湿度、大气压强、降水量、能见度等。
         */
        /*
        HeWeather.getWeatherNow(clientmap.this, weatherADDR, Lang.CHINESE_SIMPLIFIED , Unit.METRIC , new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable e) {
                ring.setVisibility(View.GONE);
                Toast.makeText(clientmap.this, "获取天气失败:"+e, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(Now dataObject) {
                ring.setVisibility(View.GONE);
                //Log.i(TAG, " Weather Now onSuccess: " + new Gson().toJson(dataObject));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if ( Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus()) ){
                    //此时返回数据，这是个数据类
                    NowBase now = dataObject.getNow();
                    try {
                        //Toast.makeText(clientmap.this, now.getCond_txt() + "", Toast.LENGTH_LONG).show();
                        StringBuilder strb = new StringBuilder();
                        strb.append(tvlog.getText());
                        strb.append("即时天气：").append("\n");
                        strb.append("温度：").append(now.getTmp()).append("℃\n");  //温度，默认单位：摄氏度
                        strb.append("湿度：").append(now.getHum()).append("%\n");//相对湿度
                        strb.append("天气：").append(now.getCond_txt()).append("\n");  //实况天气状况描述
                        strb.append("降水量：").append(now.getPcpn()).append("\n");//降水量
                        strb.append("能见度：").append(now.getVis()).append("公里\n");//能见度，默认单位：公里
                        strb.append("风速：").append(now.getWind_spd()).append("公里/小时");//风速，公里/小时

//                        strb.append("云量：").append(now.getCloud()).append(",\r");//云量
//                        strb.append("体感温度：").append(now.getFl()).append(",\r");    //体感温度
//                        strb.append("降水量：").append(now.getPcpn()).append(",\r");//降水量
//                        strb.append("风力：").append(now.getWind_sc()).append(",\r");//风力
//                        strb.append("大气压强：").append(now.getPres()).append(",\r");//大气压强
//                        strb.append("风向：").append(now.getWind_dir()).append(",\r");//风向
//                        strb.append("风向360角度：").append(now.getWind_deg()).append(",\r");//风向360角度
//                        strb.append("天气状况代码：").append(now.getCond_code()).append(",\r");    //实况天气状况代码

                        tvlog.setText(strb);
                        tvlog.setVisibility(View.VISIBLE);
                    }catch (Exception e){
                        Toast.makeText(clientmap.this, "获取天气失败", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    Toast.makeText(clientmap.this, "获取天气失败:"+code.getTxt(), Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }

    private StringBuilder sb = new StringBuilder("");
    private StringBuilder sbmsg = new StringBuilder("");
    private InputStream is = null;
    private BufferedReader br = null;
    private String msg = "",Location="",WebKey="";
    //异步请求类，key是web api key值，location可以是地名也可以是经纬度等等。。。
    private class DownloadTask extends AsyncTask<Void, Integer, Boolean> {
        String url = "https://free-api.heweather.net/s6/weather/now?key="+WebKey+"&location=" + Location;

        @Override
        protected Boolean doInBackground(Void... count) {
            try {
                URL uri = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
                connection.setRequestMethod("GET");     //GET方式请求数据
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);
                connection.connect();   //开启连接
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    is = connection.getInputStream();
                    //参数字符串，如果拼接在请求链接之后，需要对中文进行 URLEncode   字符集 UTF-8
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String line;
                    while ((line = br.readLine()) != null) {    //缓冲逐行读取
                        sb.append(line);
                    }
                    String jmsg = sb.toString();        //接收结果，接收到的是JSON格式的数据
                    getweather weather = new getweather();
                    msg = weather.JXJSON(jmsg);     //解析接收到的和风now天气json数据
                }else{
                    msg = "获取天气失败";
                }
            } catch (Exception ignored) {
                msg = "获取天气异常";
            }
            return true;
        }
        //结束接收
        @Override
        protected void onPostExecute(Boolean result) {
            ring.setVisibility(View.GONE);
            try {
                if (is != null) {
                    is.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (Exception ignored) {}
            if (result) {
                tvlog.setText(msg);
                tvlog.setVisibility(View.VISIBLE);    //显示解析后的结果
            } else {
                Toast.makeText(clientmap.this, "获取天气失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if(baiduMap!=null) {
            baiduMap.setMyLocationEnabled(false);
        }
        if(mylocationclient!=null) {
            mylocationclient.stop();
        }
        mMapView.onDestroy();
        mMapView=null;
        super.onDestroy();
    }

    //toolbar第一次加载
    private int editmode= FALSE;
    public static int FALSE = 0;
    public static int TRUE = 1;
    public static int MISS = 2;
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mapbar,menu);
        return true;
    }
    //toolbar更新
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem Itemdis = menu.findItem(R.id.btndis);
        MenuItem Itemsure = menu.findItem(R.id.btnsure);
        MenuItem Itemset = menu.findItem(R.id.btnset);
        if(mode==ALL|| editmode == MISS){
            Itemdis.setVisible(false);
            Itemsure.setVisible(false);
            Itemset.setVisible(false);
        }else {
            if (editmode==TRUE) {
                toolbar.setTitle("选择位置");
                Itemdis.setVisible(true);
                Itemsure.setVisible(true);
                Itemset.setVisible(false);
            } else if(editmode==FALSE){
                toolbar.setTitle(shebeiname);
                Itemdis.setVisible(false);
                Itemsure.setVisible(false);
                Itemset.setVisible(true);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }
    //toolbar点击事件
    @Override
    public boolean onOptionsItemSelected(final MenuItem item){
        switch (item.getItemId()) {
            case R.id.btnset:
                //回到选择的控制器中心
                try {
                    String[] cut = client_location.split(":");
                    if(cut.length==2) {
                        float wei1 = Float.parseFloat(cut[0]);
                        float jing1 = Float.parseFloat(cut[1]);
                        LatLng mylocation2 = new LatLng(wei1, jing1);
                        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(mylocation2);
                        baiduMap.animateMapStatus(update);
                    }
                }catch (Exception e){

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(clientmap.this);
                builder.setTitle("手动设置位置");
                builder.setMessage("按住目标拖动到目标位置，然后点击右上角“确定”！\r\n");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editmode = TRUE;
                        invalidateOptionsMenu(); //重新绘制menu

                    }
                });
                builder.setNeutralButton("取消",null);
                builder.show();
                break;
            case R.id.btnsure:
                editmode = FALSE;
                invalidateOptionsMenu(); //重新绘制menu
                if(client_location.equals(clientobj.getLocation()))
                {
                    break;
                }else{
                    sendlocation();
                }
                break;
            case R.id.btndis:
                editmode = FALSE;
                invalidateOptionsMenu(); //重新绘制menu
                break;
            default:
                break;
            }
        return true;
        }

    private Boolean busy = false;
    private void sendlocation(){
        if(busy){   //判断忙
            Toast.makeText(clientmap.this,"网络忙碌，请稍后再试！",Toast.LENGTH_SHORT).show();
            return;
        }else{busy=true;}//置忙
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int a=0;a<2;a++) {
                        socket.changeLocation(messagerom.username,shebeiname,client_location,messagerom.userlp);
                        do{Thread.sleep(100);}while (socket.getbusy());
                        if(socket.getpsw().equals("true")){
                            if(socket.getresmsg().equals("true")){
                                messagerom.seveLocation(shebeiname,client_location);
                                break;
                            }else{
                                socket.setpsw("数据错误");
                            }
                        }}
                }catch (Exception e){
                    socket.setpsw("网络异常");
                }
                clientmap.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw=socket.getpsw();
                        if(psw.equals("true")) {
                            Toast.makeText(clientmap.this,"发送成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(clientmap.this, psw, Toast.LENGTH_SHORT).show();
                            if(psw.equals("未登录")){
                                app.toload(clientmap.this);
                                busy=false;
                                return;
                            }
                        }
                        app.shownet(btnreload);
                        busy=false;
                    }
                });
            }}).start();
    }

    private void inittoolbar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(shebeiname);
        actionBar.setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        actionBar.setHomeButtonEnabled(true); //设置返回键可用
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
