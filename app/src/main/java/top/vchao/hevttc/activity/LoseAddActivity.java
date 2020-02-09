package top.vchao.hevttc.activity;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import top.vchao.hevttc.R;
import top.vchao.hevttc.adapter.ImagePickerAdapter;
import top.vchao.hevttc.bean.FindItem;
import top.vchao.hevttc.bean.LoseItem;
import top.vchao.hevttc.bean.MyUser;
import top.vchao.hevttc.utils.GlideImageLoader;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.utils.TimeUtil;
import top.vchao.hevttc.utils.ToastUtil;
import top.vchao.hevttc.view.LoadDialog;
import top.vchao.hevttc.view.SelectDialog;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @ 创建时间: 2017/10/3 on 16:25.
 * @ 描述：失物招领发布页面
 * @ 作者: vchao
 */
public class LoseAddActivity extends BaseActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.rv_lose_add_pic)
    RecyclerView rvLoseAddPic;
    @BindView(R.id.sp_lose_kind)
    Spinner spLoseKind;
    @BindView(R.id.et_lose_tel)
    EditText etLoseTel;
    @BindView(R.id.bt_lose_add)
    Button btLoseAdd;
    @BindView(R.id.et_lose_desc)
    EditText etLoseDesc;
    @BindView(R.id.spnYear)
    Spinner spnYear;
    @BindView(R.id.tvYear)
    TextView tvYear;
    @BindView(R.id.spnMonth)
    Spinner spnMonth;
    @BindView(R.id.tvMonth)
    TextView tvMonth;
    @BindView(R.id.spnDay)
    Spinner spnDay;
    @BindView(R.id.tvDay)
    TextView tvDay;
    @BindView(R.id.et_lose_title)
    EditText etLoseTitle;

    private String LoseUnit = "寻物启事";
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 1;               //允许选择图片最大数
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    String[] filePaths = new String[1];
    private ImagePickerAdapter adapter;
    private long longtime;
    private String timeString;
    private String loseDesc;
    private String loseTel;
    private String times;
    private String loseTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lose_add;
    }

    @Override
    void initData() {
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        if (!TextUtils.isEmpty(user.getMobilePhoneNumber())) {
            etLoseTel.setText(user.getMobilePhoneNumber());
        }
        longtime = System.currentTimeMillis();
        timeString = TimeUtil.getTime(longtime, TimeUtil.DATE_FORMAT_MIN);
        LogUtils.e("当前日期：" + timeString);
        initImagePicker();
        initSpnYear();
        initSpnMonth();
        initSpnDay(30);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(LoseAddActivity.this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        rvLoseAddPic.setLayoutManager(new GridLayoutManager(LoseAddActivity.this, 3));
        rvLoseAddPic.setHasFixedSize(true);
        rvLoseAddPic.setAdapter(adapter);
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    @Override
    void initListener() {
        spLoseKind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] units = getResources().getStringArray(R.array.losekinds);
                LoseUnit = units[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private ArrayList<String> photos;

    private void initLuban(List<String> paths) {
        photos = new ArrayList<>();
        Luban.with(LoseAddActivity.this)
                .load(paths)                                   // 传入要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                .setTargetDir(getPath())                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        LoadDialog.show(LoseAddActivity.this, "正在进行图片压缩ing……");
                    }

                    @Override
                    public void onSuccess(File file) {
                        LogUtils.e("压缩成功");
                        LoadDialog.dismiss(LoseAddActivity.this);
                        photos.add(file.getAbsolutePath());
                        filePaths[0] = photos.get(0);
                        BmobUpSale(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        LoadDialog.dismiss(LoseAddActivity.this);
                        Toast.makeText(LoseAddActivity.this, "压缩失败！", Toast.LENGTH_SHORT).show();
                    }
                }).launch();    //启动压缩
    }

    private void BmobUpSale(boolean hasPic) {
        LoadDialog.show(LoseAddActivity.this, "发布失物招领中...");
        LogUtils.e("开始上传……");
        if (hasPic) {
            BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> files, List<String> urls) {

                    if (!"寻物启事".equals(LoseUnit)) {
                        LoseItem bean = new LoseItem();
                        MyUser user = BmobUser.getCurrentUser(MyUser.class);
                        bean.setAuthor(user.getUsername());
                        bean.setContent(loseDesc);
                        bean.setTel(loseTel);
                        bean.setPic(files.get(0));
                        bean.setTitle(loseTitle);

                        bean.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    ToastUtil.show(LoseAddActivity.this, "发布成功", Toast.LENGTH_SHORT);
                                    LogUtils.e("创建数据成功：" + objectId);
                                    LoadDialog.dismiss(LoseAddActivity.this);
                                    startActivity(new Intent(LoseAddActivity.this, LoseActivity.class));
                                    finish();
                                } else {
                                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                                    ToastUtil.show(LoseAddActivity.this, "发布失败，请稍后再试", Toast.LENGTH_SHORT);
                                    LoadDialog.dismiss(LoseAddActivity.this);
                                }
                            }
                        });
                    } else {
                        FindItem bean = new FindItem();
                        MyUser user = BmobUser.getCurrentUser(MyUser.class);
                        bean.setAuthor(user.getUsername());
                        bean.setContent(loseDesc);
                        bean.setTime(times);
                        bean.setTel(loseTel);
                        bean.setPic(files.get(0));
                        bean.setTitle(loseTitle);
                        bean.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    ToastUtil.show(LoseAddActivity.this, "发布成功", Toast.LENGTH_SHORT);
                                    LogUtils.e("创建数据成功：" + objectId);
                                    LoadDialog.dismiss(LoseAddActivity.this);
                                    startActivity(new Intent(LoseAddActivity.this, LoseActivity.class));
                                    finish();
                                } else {
                                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                                    ToastUtil.show(LoseAddActivity.this, "发布失败，请稍后再试", Toast.LENGTH_SHORT);
                                    LoadDialog.dismiss(LoseAddActivity.this);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onError(int statuscode, String errormsg) {
                    LogUtils.e("错误码" + statuscode + ",错误描述：" + errormsg);
                    Toast.makeText(LoseAddActivity.this, "发生错误，请稍后重试", Toast.LENGTH_SHORT).show();

                    LoadDialog.dismiss(LoseAddActivity.this);
                }

                @Override
                public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                    Log.i("life", "insertBatchDatasWithOne -onProgress :" + curIndex + "---" + curPercent + "---" + total + "----" + totalPercent);

                    LoadDialog.dismiss(LoseAddActivity.this);
                }
            });
        } else {
            if (!"寻物启事".equals(LoseUnit)) {
                LoseItem bean = new LoseItem();
                MyUser user = BmobUser.getCurrentUser(MyUser.class);
                bean.setAuthor(user.getUsername());
                bean.setContent(loseDesc);
                bean.setTel(loseTel);
                bean.setTitle(loseTitle);
                bean.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            ToastUtil.show(LoseAddActivity.this, "发布成功", Toast.LENGTH_SHORT);
                            LogUtils.e("创建数据成功：" + objectId);
                            LoadDialog.dismiss(LoseAddActivity.this);
                            startActivity(new Intent(LoseAddActivity.this, LoseActivity.class));
                            finish();
                        } else {
                            LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                            ToastUtil.show(LoseAddActivity.this, "发布失败，请稍后再试", Toast.LENGTH_SHORT);
                            LoadDialog.dismiss(LoseAddActivity.this);
                        }
                    }
                });
            } else {
                FindItem bean = new FindItem();
                MyUser user = BmobUser.getCurrentUser(MyUser.class);
                bean.setAuthor(user.getUsername());
                bean.setContent(loseDesc);
                bean.setTime(times);
                bean.setTel(loseTel);
                bean.setTitle(loseTitle);
                bean.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            ToastUtil.show(LoseAddActivity.this, "发布成功", Toast.LENGTH_SHORT);
                            LogUtils.e("创建数据成功：" + objectId);
                            LoadDialog.dismiss(LoseAddActivity.this);
                            startActivity(new Intent(LoseAddActivity.this, LoseActivity.class));
                            finish();
                        } else {
                            LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                            ToastUtil.show(LoseAddActivity.this, "发布失败，请稍后再试", Toast.LENGTH_SHORT);
                            LoadDialog.dismiss(LoseAddActivity.this);
                        }
                    }
                });
            }
        }
    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(LoseAddActivity.this, R.style.transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                // 直接调起相机
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(LoseAddActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(LoseAddActivity.this, ImageGridActivity.class);
                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }
                    }
                }, names);

                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(LoseAddActivity.this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }

    }

    @OnClick(R.id.bt_lose_add)
    public void onViewClicked() {
        times = spnYear.getSelectedItem().toString() + "-" +
                spnMonth.getSelectedItem().toString() + "-" +
                spnDay.getSelectedItem().toString();
        LogUtils.e("选择时间是：===" + times);
        loseTitle = etLoseTitle.getText().toString().trim();
        loseDesc = etLoseDesc.getText().toString().trim();
        loseTel = etLoseTel.getText().toString().trim();
        if (!TextUtils.isEmpty(loseDesc) && (!TextUtils.isEmpty(loseTel)) && (!TextUtils.isEmpty(loseTitle))) {
            if (selImageList.size() != 0) {
                ArrayList<String> paths = new ArrayList<>();
                paths.add(selImageList.get(0).path);
                initLuban(paths);
            } else {
                BmobUpSale(false);
            }
        } else {
            Toast.makeText(this, "请将信息填写完整！", Toast.LENGTH_SHORT).show();
        }
    }

    ArrayList<ImageItem> images = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                String path = images.get(0).path;
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }


    private void initSpnYear() {
        //数据
        List<Integer> dataList;
        dataList = new ArrayList<Integer>();
        for (int i = 2016; i <= 2018; i++) {
            dataList.add(i);
        }
        //适配器
        arr_adapter = new ArrayAdapter<Integer>(this, R.layout.layout_spinner_item, dataList);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYear.setAdapter(arr_adapter);
        //设置默认值
        for (int i = 0; i <= 100; i++) {
            if (2016 + i == getYear()) {
                Log.e("zwc", "initSpnYear: " + 2016 + i);
                spnYear.setSelection(i, true);
                break;
            }
        }
    }

    private void initSpnMonth() {
        //数据
        List<Integer> dataList;
        dataList = new ArrayList<Integer>();
        for (int i = 1; i <= 12; i++) {
            dataList.add(i);
        }
        //适配器
        arr_adapter = new ArrayAdapter<Integer>(this, R.layout.layout_spinner_item, dataList);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spnMonth.setAdapter(arr_adapter);
        for (int i = 0; i <= 100; i++) {
            if (i + 1 == getMonth()) {
                spnMonth.setSelection(i, true);
                break;
            }
        }
    }

    private ArrayAdapter<Integer> arr_adapter;

    private void initSpnDay(int intDay) {
        //数据
        List<Integer> dataList;
        dataList = new ArrayList<Integer>();
        for (int i = 1; i <= intDay; i++) {
            dataList.add(i);
        }
        //适配器
        arr_adapter = new ArrayAdapter<Integer>(this, R.layout.layout_spinner_item, dataList);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDay.setAdapter(arr_adapter);
        for (int i = 0; i <= 50; i++) {
            if (i + 1 == getDay()) {
                spnDay.setSelection(i, true);
                break;
            }
        }
    }

    public int getYear() {
        return Integer.parseInt(timeString.substring(0, 4));
    }

    public int getMonth() {
        return Integer.parseInt(timeString.substring(5, 7));
    }

    public int getDay() {
        return Integer.parseInt(timeString.substring(8, 10));
    }

}
