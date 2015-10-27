package com.zhaoxuhai.myplan;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

public class MyGridViewAdapter extends BaseAdapter {
	int count;
	Context context;
    List<Plan> planList;

	public MyGridViewAdapter(Context context, List<Plan> planList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.count = planList.size();
        this.planList = planList;
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
            holder.iv = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.iv2 = (ImageView) convertView.findViewById(R.id.imageView2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

        String project_name = planList.get(position).getPlanName();
        String project_icon = planList.get(position).getIconImg();
        String icon_front = planList.get(position).getIconFront();
		holder.tv.setText(project_name + " ");


        try {
            Field field = R.drawable.class.getField(project_icon);
            holder.iv.setImageResource(field.getInt(new R.drawable()));
            field = R.drawable.class.getField(icon_front);
            holder.iv2.setImageResource(field.getInt(new R.drawable()));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return convertView;
	}

	class ViewHolder {
		ImageView iv;
        ImageView iv2;
		TextView tv;
	}

}
