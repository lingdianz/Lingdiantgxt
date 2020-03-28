package com.example.hesiod.lingdiantgxt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.hesiod.lingdiantgxt.activity.BaseAcvtivity;
import com.example.hesiod.lingdiantgxt.activity.messagerom;
import com.example.hesiod.lingdiantgxt.activity.mysocket;
import com.example.hesiod.lingdiantgxt.baseadapter.driver_adapter;
import com.example.hesiod.lingdiantgxt.baseadapter.group_adapter;
import com.example.hesiod.lingdiantgxt.baseadapter.set_adapter;
import com.example.hesiod.lingdiantgxt.baseadapter.work_adapter;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_drivers;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_groups;

import java.util.ArrayList;
import java.util.List;

public class control_room extends BaseAcvtivity {

    private String shebeiname = "";
    private int groupchoose = 0;

    private Toolbar toolbar;
    private TextView tvclient;
    private driver_adapter adapter;
    private DrawerLayout drawerlayout;
    private CardView alldriver;
    private ImageView imgadd;

    private Intent intent;
    private link app;
    private messagerom messagerom;
    private RecyclerView recyclerView;
    private ListView listView, lvset;
    private Spinner spinnergroup;
    private ProgressBar ring;

    //private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_room);
        app = (link) getApplication();
        messagerom = app.getmessagerom();

        Intent intent = getIntent();
        shebeiname = intent.getStringExtra("shebeiname");
        initview();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnmap:
                if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
                    drawerlayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerlayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.btnset:
                View contentView = LayoutInflater.from(control_room.this).inflate(R.layout.control_room, null); //获取全屏view
                View popupview = getLayoutInflater().inflate(R.layout.poplist, null, false);
                final ListView lvpoplist = (ListView) popupview.findViewById(R.id.lvpoplist);
                final String[] setstring = {"查看地图", "添加分组", "添加驱动", "替换控制器", "删除控制器"};
                set_adapter adapter = new set_adapter(control_room.this, R.layout.set_item, setstring);
                lvpoplist.setAdapter(adapter);
                final PopupWindow popwindow = new PopupWindow(popupview, getwith(), (int)(53 * 5 * app.getDensity()));   //5列，创建PopupWindow对象，指定宽度和高度
                popwindow.setAnimationStyle(R.style.popup_window_anim);     //设置动画
                popwindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9595ff")));    //设置背景颜色
                popwindow.setFocusable(true);   //设置可以获取焦点
                popwindow.setOutsideTouchable(true);    //设置可以触摸弹出框以外的区域
                popwindow.update(); //更新popupwindow的状态
                popwindow.showAtLocation(contentView, Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
                lvpoplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (setstring[position]) {
                            case "查看地图":
                                Intent intent = new Intent(control_room.this, clientmap.class);
                                intent.putExtra("shebeiname", shebeiname);
                                startActivity(intent);
                                popwindow.dismiss();
                                break;
                            case "添加分组":
                                popwindow.dismiss();
                                addgroup();
                                break;
                            case "添加驱动":
                                popwindow.dismiss();
                                List<String> showstring = new ArrayList<>();
                                for (int ct = 0; ct < messagerom.getGroups().size(); ct++) {
                                    String groupname = messagerom.getGroups().get(ct).getGroupname();
                                    showstring.add(groupname);
                                }
                                chulidriver(showstring, "添加驱动");
                                break;
                            case "替换控制器":
                                popwindow.dismiss();
                                replaceclient();
                                break;
                            case "删除控制器":
                                delclient();
                                break;
                            default:
                                break;
                        }
                    }
                });
                break;
            default:
                break;
        }
        return true;
    }

    //获取屏幕宽度
    private int getwith() {
        WindowManager manager = (WindowManager) control_room.this.getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT < 17) {
            display.getSize(point);
        } else {
            display.getRealSize(point);
        }
        int width = point.x;
        return width;
        //int height = point.y;
    }

    //替换控制器
    private void replaceclient() {
        AlertDialog.Builder builder = new AlertDialog.Builder(control_room.this);
        final EditText input = ckpassword(builder, "替换控制器:" + shebeiname);
        builder.setPositiveButton("下一步", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().trim().equals(messagerom.getPassword())) {
                    choosereplaceclient();
                } else {
                    Toast.makeText(control_room.this, "验证失败,验证密码错误", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.show();
    }

    //选择替换的控制器
    private void choosereplaceclient() {
        if (messagerom.getDrivers().size() == 0) {
            Toast.makeText(control_room.this, "当前控制器：“" + shebeiname + "”下无驱动，替换无意义！", Toast.LENGTH_LONG).show();
            return;
        }
        List<String> showstring = new ArrayList<>();
        for (int ct = 0; ct < messagerom.getClients().size(); ct++) {
            String clientname = messagerom.getClients().get(ct).getClientname();
            if (!clientname.equals(shebeiname)) {   //移除自己
                showstring.add(clientname);
            }
        }
        if (showstring.size() < 1) {
            Toast.makeText(control_room.this, "您没有其他控制器可选择替换，先去添加控制器吧！", Toast.LENGTH_LONG).show();
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(control_room.this, android.R.layout.simple_list_item_1, showstring);
        final Spinner listView = new Spinner(control_room.this);
        listView.setPadding(20, 0, 20, 0);
        listView.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(control_room.this);
        builder.setTitle("替换控制器：" + shebeiname);
        builder.setMessage("注意：1、新的控制器必须为空，否则不允许替换；\r\n    2、替换成功后旧控制器将被清空\r\n\r\n以下请选择新控制器");
        builder.setView(listView);
        builder.setPositiveButton("确定替换", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newclient = (String) listView.getSelectedItem();
                dogroup(6, shebeiname, newclient);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }


    //删除控制器
    private void delclient() {
        AlertDialog.Builder builder = new AlertDialog.Builder(control_room.this);
        final EditText input = ckpassword(builder, "移除控制器:" + shebeiname);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().trim().equals(messagerom.getPassword())) {
                    dogroup(5, "del", shebeiname);
                } else {
                    Toast.makeText(control_room.this, "移除失败,验证密码错误", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.show();
    }

    private void removdriver(ce_drivers driver) {
        final String drivername = driver.getDrivername();
        AlertDialog.Builder builder = new AlertDialog.Builder(control_room.this);
        final EditText input = ckpassword(builder, "移除驱动:" + drivername);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().trim().equals(messagerom.getPassword())) {
                    dogroup(4, "del", drivername);
                } else {
                    Toast.makeText(control_room.this, "移除失败,验证密码错误", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.show();
    }

    private EditText ckpassword(AlertDialog.Builder builder3, String title) {
        final EditText input2 = new EditText(control_room.this);
        input2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input2.setHint("password");
        input2.setGravity(1);
        input2.setBackgroundResource(R.drawable.yuajiaotxqian);
        input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        builder3.setTitle(title);
        builder3.setMessage("需要权限，请输入验证密码");
        builder3.setView(input2);
        builder3.setNegativeButton("取消", null);
        return input2;
    }

    private void initview() {
        ring = (ProgressBar) findViewById(R.id.ring);
        listView = (ListView) findViewById(R.id.lvgroup);
        lvset = (ListView) findViewById(R.id.lvset);
        tvclient = (TextView) findViewById(R.id.tvclient);
        imgadd = (ImageView) findViewById(R.id.imgadd);
        spinnergroup = (Spinner) findViewById(R.id.spinnergroup);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        alldriver = (CardView) findViewById(R.id.alldriver);
        drawerlayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_driver);

        inittoolbar(); //初始化toolbar
        Instantiation(); //初始化下拉刷新
        reloadall();    //打开界面加载驱动,因为引用了刷新的圆圈，所以一定要放在初始化下拉刷新的后面

        tvclient.setText(shebeiname);
        alldriver.setCardBackgroundColor(getResources().getColor(messagerom.getOnlineclient().contains(shebeiname) ? R.color.blue0 : R.color.gray));
        alldriver.setOnClickListener(new mClick());
        disabelr_click();
        imgadd.setOnClickListener(new mClick());
    }

    //监听滑动页面的打开与关闭事件
    private void disabelr_click() {
        drawerlayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    //显示驱动
    private void updadriver(int groupposition) {
        List<ce_drivers> drivers = new ArrayList<>();
        List<List<ce_drivers>> driverlist = new ArrayList<>();
        if (groupposition > 0) {
            driverlist = messagerom.getDriverlist();
            if (driverlist.size() > groupposition - 1) {
                drivers = driverlist.get(groupposition - 1);
            }
        } else {
            drivers = messagerom.getDrivers();
        }
        showdriver(drivers);
    }

    private void showdriver(List<ce_drivers> drivers) {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
        List<ce_groups> groups = messagerom.getGroups();

        adapter = new driver_adapter(drivers, groups, control_room.this);
        recyclerView.setAdapter(adapter);
        adapter.setOnLongItemClickListener(new driver_adapter.MyItemClickListener() {
            @Override
            public void onItemClick(final ce_drivers driver, View v) {
                View popupview = getLayoutInflater().inflate(R.layout.poplist, null, false);
                final ListView lvpoplist = (ListView) popupview.findViewById(R.id.lvpoplist);
                final List<String> work = new ArrayList<String>();
                work.add("移动驱动");
                work.add("移除驱动");
                work_adapter adapter = new work_adapter(control_room.this, R.layout.work_item, work);
                lvpoplist.setAdapter(adapter);
                final PopupWindow popwindow = new PopupWindow(popupview, (int)(120*app.getDensity()), (int)((80+2)*app.getDensity()));   //创建PopupWindow对象，指定宽度和高度
                popwindow.setAnimationStyle(R.style.popup_window_anim);     //设置动画
                popwindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9595ff")));    //设置背景颜色
                popwindow.setFocusable(true);   //设置可以获取焦点
                popwindow.setOutsideTouchable(true);    //设置可以触摸弹出框以外的区域
                popwindow.update(); //更新popupwindow的状态
                popwindow.showAsDropDown(v, (int)(-60*app.getDensity()), (int)(-(80-5)*app.getDensity()-v.getHeight()));  //以下拉的方式显示，并且可以设置显示的位置findViewById(R.id.drawerlayout)

                lvpoplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (work.get(position)) {
                            case "移动驱动":
                                popwindow.dismiss();
                                movdriver(driver);
                                break;
                            case "移除驱动":
                                popwindow.dismiss();
                                removdriver(driver);
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });
    }

    //移动驱动
    private void movdriver(final ce_drivers driver) {
        List<String> showstring = new ArrayList<>();
        for (int ct = 0; ct < messagerom.getGroups().size(); ct++) {
            String groupname = messagerom.getGroups().get(ct).getGroupname();
            if (!driver.getGroupname().equals(groupname)) {   //移除自己所在分组
                showstring.add(groupname);
            }
        }
        chulidriver(showstring, driver.getDrivername());
    }

    //处理编辑驱动事件，移动或添加
    private void chulidriver(List<String> showstring, final String driverolder) {
        if (showstring.size() < 1) {
            Toast.makeText(control_room.this, "没有其他分组哦，先创建分组吧！", Toast.LENGTH_LONG).show();
            addgroup();
            return;
        }
        final Spinner listView = new Spinner(control_room.this);
        listView.setPadding(20, 0, 20, 0);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(control_room.this, android.R.layout.simple_list_item_1, showstring);
        listView.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(control_room.this);
        builder.setTitle(driverolder.equals("添加驱动") ? "添加驱动" : "移动驱动：" + driverolder);   //判断操作是移动驱动还是添加驱动
        builder.setMessage("请选择分组");
        builder.setView(listView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newgroup = (String) listView.getSelectedItem();
                if (driverolder.equals("添加驱动")) {
                    Intent intent = new Intent(control_room.this, saoyisao.class);
                    intent.putExtra("dowhat", "adddriver");
                    intent.putExtra("what", shebeiname + ":" + newgroup);
                    startActivityForResult(intent, 1);
                } else {
                    movdriver(driverolder, newgroup);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void movdriver(final String driver, final String newgroup) {
        if (busy) {
            Toast.makeText(control_room.this, "网络忙碌，请稍后刷新！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            busy = true;
        }   //因为有多个操作，未全部完成不允许重建
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int ct = 0; ct < 2; ct++) {
                        socket.setbusy();
                        socket.amovDriver(messagerom.username, shebeiname, newgroup, driver, messagerom.userlp);
                        do {
                            Thread.sleep(100);
                        } while (socket.getbusy());
                        if (socket.getpsw().equals("true")) {
                            if (socket.getresmsg().equals("true")) {
                                messagerom.sevemovdriver(driver, newgroup);
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    socket.setpsw("网络异常");
                }
                control_room.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw = socket.getpsw();
                        if (psw.equals("true")) {
                            showgroup();
                            showspinner();
                            updadriver(groupchoose);
                            Toast.makeText(control_room.this, "移动成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(control_room.this, psw, Toast.LENGTH_SHORT).show();
                            if (psw.equals("未登录")) {
                                app.toload(control_room.this);
                            }
                        }
                        app.shownet(recyclerView);
                        busy = false;
                    }
                });
            }
        }).start();
    }

    private void showgroup() {
        List<ce_groups> groups = messagerom.getGroups();
        if (groups.size() >= 8) {
            imgadd.setVisibility(View.GONE);    //如果最分组数大于等于8则不许再添加分组了
        }
        group_adapter adaptergroup = new group_adapter(control_room.this, R.layout.groups_item, groups, control_room.this);
        listView.setAdapter(adaptergroup);
        lisentlistview();    //监听列表点击
        /*adaptergroup.setOnItemClickAddListener(new group_adapter.MyItemClickListener() {
            @Override
            public void onItemClickAdd(Intent intent) {
                startActivityForResult(intent,1);
            }
        });*/
    }

    private void showspinner() {
        List<String> groups = new ArrayList<>();
        groups.add("全部驱动");
        for (int ct = 0; ct < messagerom.getGroups().size(); ct++) {
            groups.add(messagerom.getGroups().get(ct).getGroupname());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, groups);
        spinnergroup.setAdapter(adapter);
        spinnergroup.setSelection(groupchoose < groups.size() ? groupchoose : 0);   //刷新列表不变，下面加载点击事件会触发一次刷新，所以不用写刷新
        spinnergroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                groupchoose = position;
                updadriver(groupchoose);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void lisentlistview() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Boolean isemtry = true;
                for (int ck = 0; ck < messagerom.getDrivers().size(); ck++) {
                    if (messagerom.getGroups().get(position).getGroupname().equals(messagerom.getDrivers().get(ck).getGroupname())) {
                        isemtry = false;
                    }
                }
                if (isemtry) {
                    Toast.makeText(control_room.this, "分组下没有驱动哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                ce_groups group = messagerom.getGroups().get(position);
                if (group.getIstimer().equals("0")) {
                    Intent intent = new Intent(control_room.this, tiaoguang.class);
                    intent.putExtra("shebeiname", "#g#" + messagerom.getGroups().get(position).getGroupname());
                    startActivity(intent);
                    overridePendingTransition(R.anim.show, 0);
                } else {
                    Intent intent = new Intent(control_room.this, timer.class);
                    intent.putExtra("type", "group");
                    intent.putExtra("shebeiname", group.getGroupname());
                    startActivity(intent);
                    overridePendingTransition(R.anim.show, 0);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int listposition, long id) {
                View popupview = getLayoutInflater().inflate(R.layout.poplist, null, false);
                final ListView lvpoplist = (ListView) popupview.findViewById(R.id.lvpoplist);
                final List<String> work = new ArrayList<>();
                work.add("重命名分组");
                work.add("移除分组");
                work_adapter adapter = new work_adapter(control_room.this, R.layout.work_item, work);
                lvpoplist.setAdapter(adapter);
                final PopupWindow popwindow = new PopupWindow(popupview, (int)(120*app.getDensity()), (int)((80+2)*app.getDensity()));   //创建PopupWindow对象，指定宽度和高度
                popwindow.setAnimationStyle(R.style.popup_window_anim);     //设置动画
                popwindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9595ff")));    //设置背景颜色
                popwindow.setFocusable(true);   //设置可以获取焦点
                popwindow.setOutsideTouchable(true);    //设置可以触摸弹出框以外的区域
                popwindow.update(); //更新popupwindow的状态  findViewById(R.id.recycler_driver)
                popwindow.showAsDropDown(view, (int)(view.getWidth()/2-60*app.getDensity()), (int)(-(80-5)*app.getDensity()-view.getHeight()));  //以下拉的方式显示，并且可以设置显示的位置

                lvpoplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //String popname = lvpoplist.getItemAtPosition(position).toString();
                        switch (work.get(position)) {
                            case "重命名分组":
                                popwindow.dismiss();
                                final String oldgroupname = messagerom.getGroups().get(listposition).getGroupname();
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(control_room.this);
                                final EditText input = new EditText(control_room.this);
                                input.setHint(oldgroupname);
                                input.setGravity(1);
                                input.setBackgroundResource(R.drawable.yuajiaotxqian);
                                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                                builder2.setTitle("重命名分组：" + oldgroupname);
                                builder2.setMessage("请输入新的分组名");
                                builder2.setView(input);
                                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String newgroupname = input.getText().toString().trim();
                                        if (inputgroupname("更改", newgroupname)) {
                                            dogroup(2, oldgroupname, newgroupname);
                                        }
                                    }
                                });
                                builder2.setNegativeButton("取消", null);
                                builder2.show();
                                break;
                            case "移除分组":
                                popwindow.dismiss();
                                final String groupname = messagerom.getGroups().get(listposition).getGroupname();
                                AlertDialog.Builder builder3 = new AlertDialog.Builder(control_room.this);
                                final EditText input2 = ckpassword(builder3, "移除分组:" + groupname);
                                builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (input2.getText().toString().trim().equals(messagerom.getPassword())) {
                                            dogroup(3, "del", groupname);
                                        } else {
                                            Toast.makeText(control_room.this, "移除失败,验证密码错误", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                builder3.show();
                                break;
                            default:
                                break;
                        }
                    }
                });
                return true;
            }
        });
    }

    private Boolean inputgroupname(String what, String newgroupname) {
        if (newgroupname.length() == 0) {
            Toast.makeText(control_room.this, what + "失败，输入分组名不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        if (newgroupname.equals("0")) {
            Toast.makeText(control_room.this, what + "失败，输入分组名不能为0", Toast.LENGTH_LONG).show();
            return false;
        }
        if (newgroupname.length() > 10) {
            Toast.makeText(control_room.this, what + "失败，输入分组名过长", Toast.LENGTH_LONG).show();
            return false;
        }
        for (int ct = 0; ct < messagerom.getGroups().size(); ct++) {
            if (messagerom.getGroups().get(ct).getGroupname().equals(newgroupname)) {
                Toast.makeText(control_room.this, what + "失败，存在同名分组", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private void dogroup(final int dowhat, final String oldname, final String newname) {
        if (busy) {
            Toast.makeText(control_room.this, "网络忙碌，请稍后刷新！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            busy = true;
        }   //因为有多个操作，未全部完成不允许重建
        ring.setVisibility(View.VISIBLE);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int ct = 0; ct < 2; ct++) {
                        switch (dowhat) {
                            case 1:     //处理添加分组
                                socket.setbusy();
                                socket.addGroup(messagerom.username, shebeiname, newname, messagerom.userlp);
                                break;
                            case 2:     //处理重命名分组
                                socket.setbusy();
                                socket.changeGroupname(messagerom.username, shebeiname, oldname, newname, messagerom.userlp);
                                break;
                            case 3:     //处理删除分组
                                socket.setbusy();
                                socket.delGroup(messagerom.username, shebeiname, newname, messagerom.userlp);
                                break;
                            case 4:
                                socket.setbusy();   //移除驱动，这里的组名是驱动来的
                                socket.delDriver(messagerom.username, shebeiname, newname, messagerom.userlp);
                                break;
                            case 5:
                                socket.setbusy();
                                socket.delClient(messagerom.username, shebeiname, messagerom.userlp);
                                break;
                            case 6:
                                socket.setbusy();
                                socket.replaceClient(messagerom.username, oldname, newname, messagerom.userlp);
                                break;
                            default:
                                busy = false;
                                return;
                        }
                        do {
                            Thread.sleep(100);
                        } while (socket.getbusy());
                        if (socket.getpsw().equals("true")) {
                            switch (dowhat) {
                                case 1:    //处理添加分组
                                    messagerom.addGroup(shebeiname, newname);
                                    break;
                                case 2:     //处理重命名分组
                                    messagerom.seveChangeGroup(oldname, newname);
                                    break;
                                case 3:    //处理删除分组
                                    messagerom.seveDelGroup(newname);
                                    break;
                                case 4:
                                    messagerom.sevedeldriver(newname);    //这里groupname指的是驱动名，移除驱动
                                    break;
                                case 5:         //处理删除控制器
                                    messagerom.sevedelclient(oldname);
                                    socket.setpsw("删除控制器");
                                    break;
                                case 6:
                                    //messagerom.seveReplaceClient(oldname,newname);    //工程名和地址让客户自行修改
                                    socket.setpsw("替换控制器");
                                    break;
                                default:
                                    return;
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    socket.setpsw("加载异常");
                }
                control_room.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ring.setVisibility(View.GONE);
                        String psw = socket.getpsw();
                        if (psw.equals("true")) {
                            showgroup();
                            showspinner();
                            updadriver(groupchoose);
                        } else if (psw.equals("删除控制器")) {
                            Toast.makeText(control_room.this, "删除的控制器可以添加找回", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        } else if (psw.equals("替换控制器")) {
                            Toast.makeText(control_room.this, "控制器替换成功,已自动刷新！", Toast.LENGTH_LONG).show();
                            shebeiname = newname;
                            tvclient.setText(shebeiname);
                            toolbar.setTitle(shebeiname);
                            busy = false;
                            reloadall();
                            return;
                        } else {
                            Toast.makeText(control_room.this, psw, Toast.LENGTH_SHORT).show();
                            if (psw.equals("未登录")) {
                                app.toload(control_room.this);
                                return;
                            }
                        }
                        app.shownet(recyclerView);
                        busy = false;
                    }
                });
            }
        }).start();
    }

    private class mClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.alldriver:
                    if(messagerom.getDrivers().size()>0){
                        Intent intent = new Intent(control_room.this,tiaoguang.class);
                        intent.putExtra("shebeiname",shebeiname);
                        startActivity(intent);
                        overridePendingTransition(R.anim.show,0);
                        Toast.makeText(control_room.this,"贴士：定时分组是不会被调节的哦！",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(control_room.this,"贴士，你还没有驱动哦！赶紧去添加驱动吧！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.imgadd:
                    addgroup();
                    break;
                default:
                    break;
            }
        }
    }

    //添加分组
    private void addgroup() {
        if (messagerom.getGroups().size() >= 8) {
            Toast.makeText(control_room.this, "添加失败，超过最大分组数", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder1 = new AlertDialog.Builder(control_room.this);
        final EditText input1 = new EditText(control_room.this);
        input1.setHint("新分组");
        input1.setGravity(1);
        input1.setBackgroundResource(R.drawable.yuajiaotxqian);
        input1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        builder1.setTitle("添加分组");
        builder1.setMessage("请输入新的分组名");
        builder1.setView(input1);
        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newgroupname = input1.getText().toString().trim();
                if (inputgroupname("添加", newgroupname)) {
                    dogroup(1, "new", newgroupname);
                }
            }
        });
        builder1.setNegativeButton("取消", null);
        builder1.show();
    }

    private SwipeRefreshLayout swipeRefreshLayout;

    public void Instantiation() {
        /**
         * 下拉刷新
         */
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);//箭头颜色
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressBackgroundColor(R.color.colorPrimary);//圆圈颜色
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);//圆圈高度
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                reloadall();//加载列表
            }
        });
    }

    private Boolean busy = false;

    //加载当前控制器全部驱动
    public void reloadall() {
        if (busy) {
            Toast.makeText(control_room.this, "网络忙碌，请稍后刷新！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            busy = true;
        }   //因为有多个操作，未全部完成不允许重建
        swipeRefreshLayout.setRefreshing(true);
        final mysocket socket = new mysocket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int ct = 0; ct < 2; ct++) {
                        socket.setbusy();
                        socket.loadGroup(messagerom.username, shebeiname, messagerom.userlp);
                        do {
                            Thread.sleep(100);
                        } while (socket.getbusy());
                        if (socket.getpsw().equals("true")) {
                            messagerom.sevegroups(socket.getresmsg());
                            break;
                        }
                    }
                    if (socket.getpsw().equals("true")) {
                        for (int ct = 0; ct < 2; ct++) {
                            socket.setbusy();
                            socket.loadDriver(messagerom.username, shebeiname, messagerom.userlp);
                            do {
                                Thread.sleep(100);
                            } while (socket.getbusy());
                            if (socket.getpsw().equals("true")) {
                                messagerom.seveDrivers(socket.getresmsg());
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    socket.setpsw("加载异常");
                }
                control_room.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String psw = socket.getpsw();
                        if (psw.equals("true")) {
                            showgroup();
                            showspinner();
                            updadriver(groupchoose);
                        } else {
                            Toast.makeText(control_room.this, psw, Toast.LENGTH_SHORT).show();
                            if (psw.equals("未登录")) {
                                app.toload(control_room.this);
                            }
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        app.shownet(recyclerView);
                        busy = false;
                    }
                });
            }
        }).start();
    }

    private void inittoolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (messagerom.getOnlineclient().contains(shebeiname)) {
            actionBar.setTitle(shebeiname + " 在线");
        } else {
            actionBar.setTitle(shebeiname + " 离线");
        }
        actionBar.setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        actionBar.setHomeButtonEnabled(true); //设置返回键可用
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == 1) {
            reloadall();    //添加设备返回后刷新一下
        }
    }
}
