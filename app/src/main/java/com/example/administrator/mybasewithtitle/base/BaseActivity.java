package com.example.administrator.mybasewithtitle.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.mybasewithtitle.MyApplication;
import com.example.administrator.mybasewithtitle.R;

import com.example.administrator.mybasewithtitle.utils.LogUtil;
import com.example.administrator.mybasewithtitle.utils.ToastUtil;

import butterknife.ButterKnife;

/**
 * 作者：Chris
 * 创建时间: 2016/12/18 21:42
 * 邮箱：395932265@qq.com
 * 描述:
 *      处理了返回按键，可以关闭的 Log ，
 *      保存 Activity 到 Application ，
 *      跳转到另一个 Activity
 *      处理公共的头部的 back 按钮点击事件，
 *      此Activity 没有布局 xml 文件,<include layout="@layout/header"/>,方式添加头部，可以减少代码
 *      创建一个progressdialog
 *      UI 线程中弹出 toast
 *      processClick 方法，处理除了back按钮外的点击事件
 *
 *      abstract 类不需要在清单文件注册
 */
public abstract class BaseActivity  extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private ImageView ivAdd;
    private TextView tvTitle;
    private FrameLayout flContentLayout;
    private RelativeLayout rlNoDataLayout;
    private TextView tvNoData;
    private RelativeLayout rlLoading;
    private InputMethodManager imm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        // ButterKnife绑定,子类不需要再次绑定
        ButterKnife.bind(this);

        //添加到 Application
        ((MyApplication)getApplication()).insertActivity(this);

        initView();
        initListener();
        initData();
        regCommonBtn();
    }


    /**
     * 处理公用按钮,返回键
     */
    private void regCommonBtn() {
        View back = findViewById(R.id.back);
        if (back != null) {
            back.setOnClickListener(this);
        }
    }

    /**
     * 初始化值
     */
    protected abstract void initData();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 获取布局id
     */
    protected abstract int getLayoutId();

    /**
     * 打印 log.d
     * 自动获取类名为 TAG
     */
    protected void logD(String msg) {
        LogUtil.logD(getClass().getName(), msg);
    }

    /**
     * 打印 log.e
     * 自动获取类名为 TAG
     */
    protected void logE(String msg) {
        LogUtil.logE(getClass().getName(), msg);
    }

    /**
     * 创建一个progressdialog
     */
    protected ProgressDialog makeDialog(String msg) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(msg);
        return dialog;
    }

   /* *//**
     * 显示删除联系人对话框
     *//*
    private void showDialog(String title,String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //执行删除联系人 外部可以点击
//                deleContact(position);
            }
        }).show();
    }*/

    /**
     * UI 线程中弹出 toast
     */
    protected void toast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(getApplicationContext(), msg);
            }
        });
    }

    /**
     * 处理除了back按钮外的点击事件
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add) {

        } else {
            processClick(v);
        }
    }

    /**
     * 处理除了back按钮外的点击事件
     */
    protected abstract void processClick(View v);

    /**
     * 跳转到新界面  是否关闭自己
     */
    protected void startNewActivity(Class clazz, boolean finishCurrent) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        if (finishCurrent) {
            finish();
        }
    }


    /**
     * onDestroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MyApplication)getApplication()).deleteActivity(this);// 将 Activity 从 Application 中移除
        ButterKnife.unbind(this);//解除 ButterKnife 绑定
    }
}
