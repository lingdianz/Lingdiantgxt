package com.example.hesiod.lingdiantgxt.baseadapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.hesiod.lingdiantgxt.R;
import com.example.hesiod.lingdiantgxt.myJavaBean.ce_clients;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hesiod on 2019/9/13.
 */

public class myeplvadapter extends BaseExpandableListAdapter {
    //private Groupstring mMsgList;
    private List<String> onlinechild = new ArrayList<>();
    private List<String> onlinecount = new ArrayList<>();
    private List<List<ce_clients>> clientlist= new ArrayList<>();

    public myeplvadapter(List<List<ce_clients>> clientlist,List<String> onlinechild,List<String> onlinecount){
        this.clientlist = clientlist;
        this.onlinechild=onlinechild;
        this.onlinecount=onlinecount;
    }

    @Override
    // 获取分组的个数
    public int getGroupCount() {
        return clientlist.size();
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        if(clientlist.isEmpty()){return 0;}
        return clientlist.get(groupPosition).size();
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        if(clientlist.isEmpty()){return 0;}
        if(clientlist.get(0).isEmpty()){return 0;}
        return clientlist.get(groupPosition).get(0).getProject();
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(clientlist.isEmpty()){return null;}
        if(clientlist.get(0).isEmpty()){return null;}
        return clientlist.get(groupPosition).get(childPosition).getClientname();
    }

    //获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
    @Override
    public boolean hasStableIds() {
        return true;
    }
    /**
     *
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded 该组是展开状态还是伸缩状态
     * @param convertView 重用已有的视图对象
     * @param parent 返回的视图对象始终依附于的视图组
     */
// 获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.partent_item,parent,false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView)convertView.findViewById(R.id.label_group_normal);
            groupViewHolder.tvcount = (TextView)convertView.findViewById(R.id.tvcount);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder = (GroupViewHolder)convertView.getTag();
        }
        try{
            if(groupPosition==0){
                groupViewHolder.tvTitle.setText("我的工程");
            }else {
                groupViewHolder.tvTitle.setText(clientlist.get(groupPosition).get(0).getProject());
            }
            groupViewHolder.tvcount.setText(onlinecount.get(groupPosition));
        }catch (Exception e){}
        return convertView;
    }
    /**
     *groupString[groupPosition]
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild 子元素是否处于组中的最后一个
     * @param convertView 重用已有的视图(View)对象
     * @param parent 返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     *      android.view.ViewGroup)
     */

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
         ChildViewHolder childViewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item,parent,false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = (TextView)convertView.findViewById(R.id.expand_child);
            convertView.setTag(childViewHolder);

        }else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        try {
            String client=clientlist.get(groupPosition).get(childPosition).getClientname();
            childViewHolder.tvTitle.setText(client);
            if(!onlinechild.contains(client)){
                childViewHolder.tvTitle.setTextColor(Color.GRAY);
            }else {
                childViewHolder.tvTitle.setTextColor(Color.BLACK);
            }
        }catch (Exception e){}
        return convertView;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView tvTitle;
        TextView tvcount;
    }

    static class ChildViewHolder {
        TextView tvTitle;
    }
}