package com.example.hesiod.lingdiantgxt.activity;

import com.example.hesiod.lingdiantgxt.myJavaBean.BasicNowBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Hesiod on 2019/12/16.
 */
public class getweather {
    private StringBuilder sbmsg = new StringBuilder("");
    //解析JSON
    public String JXJSON(String jmsg) {
        try {
            JSONObject ObjMsg = new JSONObject(jmsg);              //和风数据最外层是个大括号对象{}
            JSONArray ListMsg = ObjMsg.getJSONArray("HeWeather6");    //大对象下存放的是很一个数组list[]
            for (
                    int i = 0;
                    i < ListMsg.length(); i++) {   //因为前面链接里只请求了now天气数据，所以这里list长度只为1
                JSONObject jsonObject = ListMsg.getJSONObject(i);     //这个数组下有四个对象{}，key分别为basic，update，status，now
                //第一个对象
                JSONObject ObjBasic = jsonObject.getJSONObject("basic");  //basic里面对应的是类
                sbmsg.append("城市ID:").append(ObjBasic.getString("cid")).append("\r\n");
                sbmsg.append("区:").append(ObjBasic.getString("location")).append("\r\n");
                sbmsg.append("市:").append(ObjBasic.getString("parent_city")).append("\r\n");
                sbmsg.append("省:").append(ObjBasic.getString("admin_area")).append("\r\n");
                sbmsg.append("国家:").append(ObjBasic.getString("cnty")).append("\r\n");
                sbmsg.append("纬度:").append(ObjBasic.getString("lat")).append("\r\n");
                sbmsg.append("经度:").append(ObjBasic.getString("lon")).append("\r\n");
                sbmsg.append("时区:").append(ObjBasic.getString("tz")).append("\r\n");
                //第二个对象
                JSONObject ObjUpdate = jsonObject.getJSONObject("update");  //update里面对应的是类
                sbmsg.append("当地时间:").append(ObjUpdate.getString("loc")).append("\r\n");
                sbmsg.append("UTC时间:").append(ObjUpdate.getString("utc")).append("\r\n");
                //第三个对象，status下是字符型数据
                sbmsg.append("接口状态:").append(jsonObject.getString("status")).append("\r\n");
                //第四个对象，
                /*JSONObject ObjNow = jsonObject.getJSONObject("now");  //now里面对应的是类
                sbmsg.append("云量:").append(ObjNow.getString("cloud")).append("\r\n");
                sbmsg.append("天气代码:").append(ObjNow.getString("cond_code")).append("\r\n");
                sbmsg.append("天气描述:").append(ObjNow.getString("cond_txt")).append("\r\n");
                sbmsg.append("体感温度（℃）:").append(ObjNow.getString("fl")).append("\r\n");
                sbmsg.append("湿度:").append(ObjNow.getString("hum")).append("\r\n");
                sbmsg.append("降水量:").append(ObjNow.getString("pcpn")).append("\r\n");
                sbmsg.append("气压:").append(ObjNow.getString("pres")).append("\r\n");
                sbmsg.append("温度（℃）:").append(ObjNow.getString("tmp")).append("\r\n");
                sbmsg.append("能见度:").append(ObjNow.getString("vis")).append("\r\n");
                sbmsg.append("风向角度:").append(ObjNow.getString("wind_deg")).append("\r\n");
                sbmsg.append("风向:").append(ObjNow.getString("wind_dir")).append("\r\n");
                sbmsg.append("风力:").append(ObjNow.getString("wind_sc")).append("\r\n");
                sbmsg.append("风速:").append(ObjNow.getString("wind_spd")).append("\r\n");*/

                //第四个对象用GSON解析，与上面的JSONObject解析做对比,与上面效果完全一样
                Gson gson = new Gson();
                BasicNowBean basicNowBean = gson.fromJson(jmsg,new TypeToken<BasicNowBean>(){}.getType());
                BasicNowBean.now nowBean = basicNowBean.getHeWeather6().get(0).getNow();
                sbmsg.append("云量:").append(nowBean.getCloud()).append("\r\n");
                sbmsg.append("天气代码:").append(nowBean.getCond_code()).append("\r\n");
                sbmsg.append("天气描述:").append(nowBean.getCond_txt()).append("\r\n");
                sbmsg.append("体感温度（℃）:").append(nowBean.getFl()).append("\r\n");
                sbmsg.append("湿度:").append(nowBean.getHum()).append("\r\n");
                sbmsg.append("降水量:").append(nowBean.getPcpn()).append("\r\n");
                sbmsg.append("气压:").append(nowBean.getPres()).append("\r\n");
                sbmsg.append("温度（℃）:").append(nowBean.getTmp()).append("\r\n");
                sbmsg.append("能见度:").append(nowBean.getVis()).append("\r\n");
                sbmsg.append("风向角度:").append(nowBean.getWind_deg()).append("\r\n");
                sbmsg.append("风向:").append(nowBean.getWind_dir()).append("\r\n");
                sbmsg.append("风力:").append(nowBean.getWind_sc()).append("\r\n");
                sbmsg.append("风速:").append(nowBean.getWind_spd()).append("\r\n");
            }
        } catch (Exception ex) {
            return "解析天气失败";
        }
        return sbmsg.toString();
    }
}
