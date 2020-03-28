package com.example.hesiod.lingdiantgxt.myTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hesiod on 2019/12/21.
 * 屏幕适配文件自动生成器，点击运行本Run 'DimensionTool.main()'根据dimens.xml文件生成各个屏幕适配尺寸
 */

public class DimensionTool {
    /**
     * 主函数
     * @param args 无
     */
    public static void main(String[] args) {
        gen();
    }
    /**
     * 初始化数据
     * 如果要更改，只需要改下面两步的数据，然后点击左边的△，点击运行本Run 'DimensionTool.main()'
     */
    //1、velue中基本的dimension.xml配置的调试用的屏幕
    private static final float BASIC_UIp = 360;
    //2、添加屏幕规格
    private static Integer[] allUIp = {240, 320, 360, 384,392,400,410,411,480,533,592,600,640,662,720,768,800,811,820,960,961,1024,1280,1365};

    //数据缓存基类
    private static final class SwObj {
        private StringBuilder swStringBuider = new StringBuilder();
        private String swFileName = "";
        private int swUIp = 0;
    }
    private static void gen() {
        //收集所有的屏幕规格的属性到基类，集合成List
        List<SwObj> swList = new ArrayList<>();
        for (int ct = 0; ct < allUIp.length; ct++) {
            SwObj swobj = new SwObj();
            swobj.swUIp = allUIp[ct];
            swobj.swFileName = "./app/src/main/res/values-sw" + allUIp[ct] + "dp";     //将归属的文件夹名称
            swList.add(swobj);
        }

        //以此文件夹下的dimens.xml文件内容为初始值参照
        File file = new File("./app/src/main/res/values/dimens.xml");
        BufferedReader reader = null;

        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;

            /**
             * 1、读取文件TXT，一行一行读，并计算新值添加到StringBuildler里，直到读入null为止-完成
             */
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {
                    /**<dimen name="dp1">1dp</dimen>  //一行格式，计算并替换其中的1dp的值
                    *截取<dimen></dimen>标签内的内容，从>右括号开始，到左括号减2，取得配置的数字
                    */
                    //取头，0位到>+1
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    //取尾,倒数<-2位，-2是为包括dp
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    //计算新值
                    Double num = Double.parseDouble
                            (tempString.substring(tempString.indexOf(">") + 1,
                                    tempString.indexOf("</dimen>") - 2));
                    //根据不同的尺寸，计算新的值，拼接新的字符串，并且结尾处换行。
                    for (int ct = 0; ct < allUIp.length; ct++) {
                        swList.get(ct).swStringBuider
                                .append(start)
                                .append(getNum(num * swList.get(ct).swUIp / BASIC_UIp))
                                .append(end)
                                .append("\r\n");
                    }
                } else {
                    for (int ct = 0; ct < allUIp.length; ct++) {
                        swList.get(ct).swStringBuider.append(tempString).append("");
                        System.out.println("<!--  sw" + swList.get(ct).swUIp + " -->");
                        System.out.println(swList.get(ct).swStringBuider.toString());
                    }
                }
            }
            reader.close();

            /**
             * 2、将新的内容，写入到指定的文件中去
             */
            for (int ct = 0; ct < allUIp.length; ct++) {
                writeFile(swList.get(ct).swFileName, swList.get(ct).swStringBuider.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 保留指定小数
     * @param num
     * @return
     */
    private static double getNum(double num) {
        DecimalFormat df = new DecimalFormat("#####0.0");
        return Double.parseDouble(df.format(num));
    }

    /**
     * 写入方法
     * @param file
     * @param text
     */

    public static void writeFile(String file, String text) {
        PrintWriter out = null;
        File filepack = new File(file);
        if(!filepack.exists()) {        //判断文件夹是否存在，不存在则创建
            filepack.mkdir();
        }
        try {
            String filexml = file + "/dimens.xml";
            out = new PrintWriter(new BufferedWriter(new FileWriter(filexml)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            out.close();
        }
    }
}
