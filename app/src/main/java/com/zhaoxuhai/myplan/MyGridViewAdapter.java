package com.zhaoxuhai.myplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridViewAdapter extends BaseAdapter {
	int count;
	Context context;

	public MyGridViewAdapter(Context context, int count) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.count = count;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stud
		ViewHolder holder;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.listitem, null);
			holder.tv = (TextView) convertView.findViewById(R.id.textView1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
        String project_name = position==0?"俯卧撑":(position==1?"仰卧起坐":(position==2?"跑步":position==3?"饮水":"其他"));
		holder.tv.setText(project_name + " ");
		return convertView;
	}

	class ViewHolder {
		ImageView iv;
		TextView tv;
	}

}
