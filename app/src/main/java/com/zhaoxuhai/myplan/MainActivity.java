package com.zhaoxuhai.myplan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OnClickListener {

    HorizontalScrollView horizontalScrollView;
    GridView gridView;
    DisplayMetrics dm;
    private Button createBtn = null;
    private Button modifyBtn = null;
    private EditText planNameTxt = null;
    private EditText planUnitTxt = null;
    private Button saveBtn = null;
    private Button delBtn = null;
    private String planName = "";
    private String planUnit = "";
    private int NUM = 2;
    private int hSpacing = 20;
    private int vSpacing = 50;
    private int version = 1;
    private AlertDialog myDialog = null;
    private MyDatabaseHelper dbHelper;
    private List<Plan> planList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        createBtn = (Button) findViewById(R.id.create_btn);
        modifyBtn = (Button) findViewById(R.id.modify_btn);

        dbHelper = new MyDatabaseHelper(this, "MyPlan.db", null, version);
        createBtn.setOnClickListener(this);
        modifyBtn.setOnClickListener(this);

        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Plan plan = planList.get(position);
                AddCountActivity.actionStart(MainActivity.this, plan.getPlanId(), plan.getPlanName(), plan.getPlanUnit());
            }
        });

        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        getScreenDen();
        setValue();
    }

    public void getData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Plan", null);
        planList.clear();
        while (cursor.moveToNext()) {
            int planId = cursor.getInt(cursor.getColumnIndex("planId"));
            String planName = cursor.getString(cursor.getColumnIndex("planName"));
            String planUnit = cursor.getString(cursor.getColumnIndex("planUnit"));
            String iconImg = cursor.getString(cursor.getColumnIndex("iconImg"));
            String iconColor = cursor.getString(cursor.getColumnIndex("iconColor"));
            Plan plan = new Plan(planId, planName, planUnit, iconImg, iconColor);
            planList.add(plan);
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_btn:
                myDialog = new AlertDialog.Builder(MainActivity.this).create();
                myDialog.show();
                myDialog.getWindow().setContentView(R.layout.create_planitem_dialog);
                myDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                planNameTxt = (EditText) myDialog.getWindow().findViewById(R.id.planName);
                planUnitTxt = (EditText) myDialog.getWindow().findViewById(R.id.planUnit);
                saveBtn = (Button) myDialog.getWindow().findViewById(R.id.save_btn);
                delBtn = (Button) myDialog.getWindow().findViewById(R.id.delete_btn);
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("click", "create_btn");
                        planName = planNameTxt.getText().toString();
                        planUnit = planUnitTxt.getText().toString();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        if (planName.length() > 0 && planUnit.length() > 0) {
                            db.execSQL("insert into Plan(planName, iconImg, iconColor, planUnit) values(?,?,?,?)",
                                    new String[]{planName, "", "", planUnit});
                        }
                        Toast.makeText(MainActivity.this, "创建项目 " + planName + " 成功", Toast.LENGTH_SHORT).show();
                        setValue();
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow()
                        .findViewById(R.id.cancel_btn)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myDialog.dismiss();
                            }
                        });
                break;
            case R.id.modify_btn:
                Log.d("modify_btn", "modify");
                break;
            default:
                break;
        }
    }


    private void setValue() {
        getData();
        MyGridViewAdapter adapter = new MyGridViewAdapter(this, planList);
        gridView.setAdapter(adapter);
        int count = adapter.getCount();
        int columnsNum = (count % 2 == 0) ? count / 2 : count / 2 + 1;
        int columnWidth = dm.widthPixels / NUM;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnsNum * columnWidth + 100,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        gridView.setLayoutParams(params);
        gridView.setColumnWidth(columnWidth);
        gridView.setHorizontalSpacing(hSpacing);
        gridView.setVerticalSpacing(dm.heightPixels / 2 / NUM);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(columnsNum);

    }

    private void getScreenDen() {
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
