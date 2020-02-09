package top.vchao.hevttc.activity;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
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
import top.vchao.hevttc.bean.BuyItem;
import top.vchao.hevttc.bean.MyUser;
import top.vchao.hevttc.bean.SaleItem;
import top.vchao.hevttc.utils.GlideImageLoader;
import top.vchao.hevttc.utils.LogUtils;
import top.vchao.hevttc.utils.ToastUtil;
import top.vchao.hevttc.view.LoadDialog;
import top.vchao.hevttc.view.SelectDialog;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class BuyAddActivity extends BaseActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener {

    @BindView(R.id.sp_buy_kind)
    Spinner spBuyKind;
    @BindView(R.id.et_buy_title)
    EditText etBuyTitle;
    @BindView(R.id.et_buy_price)
    EditText etBuyPrice;
    @BindView(R.id.et_buy_desc)
    EditText etBuyDesc;
    @BindView(R.id.rv_buy_add_pic)
    RecyclerView rvBuyAddPic;
    @BindView(R.id.et_buy_tel)
    EditText etBuyTel;
    private String BuyUnit = "我想出手";
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 1;               //允许选择图片最大数
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter adapter;
    String[] filePaths = new String[1];
    private String BuyPrice;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_add;
    }

    @Override
    void initView() {
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        if (!TextUtils.isEmpty(user.getMobilePhoneNumber())) {
            etBuyTel.setText(user.getMobilePhoneNumber());
        }

    }

    @Override
    void initData() {
        initImagePicker();
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(BuyAddActivity.this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        rvBuyAddPic.setLayoutManager(new GridLayoutManager(BuyAddActivity.this, 3));
        rvBuyAddPic.setHasFixedSize(true);
        rvBuyAddPic.setAdapter(adapter);


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
        spBuyKind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] units = getResources().getStringArray(R.array.buykinds);
                BuyUnit = units[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void BmobUpSale(boolean hasPic) {
        LoadDialog.show(BuyAddActivity.this, "发布二手交易中...");
        LogUtils.e("开始上传……");
        if (hasPic) {
            BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> files, List<String> urls) {

                    if (!"我想出手".equals(BuyUnit)) {
                        BuyItem bean = new BuyItem();
                        MyUser user = BmobUser.getCurrentUser(MyUser.class);
                        bean.setAuthor(user.getUsername());
                        bean.setContent(BuyDesc);
                        bean.setTel(BuyTel);
                        bean.setPic(files.get(0));
                        bean.setTitle(BuyTitle);
                        bean.setPrice(BuyPrice);
                        bean.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    ToastUtil.show(BuyAddActivity.this, "发布成功", Toast.LENGTH_SHORT);
                                    LogUtils.e("创建数据成功：" + objectId);
                                    LoadDialog.dismiss(BuyAddActivity.this);
                                    startActivity(new Intent(BuyAddActivity.this, BuyActivity.class));
                                    finish();
                                } else {
                                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                                    ToastUtil.show(BuyAddActivity.this, "发布失败，请稍后再试", Toast.LENGTH_SHORT);
                                    LoadDialog.dismiss(BuyAddActivity.this);
                                }
                            }
                        });
                    } else {
                        SaleItem bean = new SaleItem();
                        MyUser user = BmobUser.getCurrentUser(MyUser.class);
                        bean.setAuthor(user.getUsername());
                        bean.setContent(BuyDesc);
                        bean.setTel(BuyTel);
                        bean.setPrice(BuyPrice);
                        bean.setPic(files.get(0));
                        bean.setTitle(BuyTitle);
                        bean.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    ToastUtil.show(BuyAddActivity.this, "发布成功", Toast.LENGTH_SHORT);
                                    LogUtils.e("创建数据成功：" + objectId);
                                    LoadDialog.dismiss(BuyAddActivity.this);
                                    startActivity(new Intent(BuyAddActivity.this, BuyActivity.class));
                                    finish();
                                } else {
                                    LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                                    ToastUtil.show(BuyAddActivity.this, "发布失败，请稍后再试", Toast.LENGTH_SHORT);
                                    LoadDialog.dismiss(BuyAddActivity.this);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onError(int statuscode, String errormsg) {
                    LogUtils.e("错误码" + statuscode + ",错误描述：" + errormsg);
                    Toast.makeText(BuyAddActivity.this, "发生错误，请稍后重试", Toast.LENGTH_SHORT).show();
                    // TODO Auto-generated method stub
                    LoadDialog.dismiss(BuyAddActivity.this);
                }

                @Override
                public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                    Log.i("life", "insertBatchDatasWithOne -onProgress :" + curIndex + "---" + curPercent + "---" + total + "----" + totalPercent);
                    // TODO Auto-generated method stub
                    LoadDialog.dismiss(BuyAddActivity.this);
                }
            });
        } else {
            if (!"我想出手".equals(BuyUnit)) {
                BuyItem bean = new BuyItem();
                MyUser user = BmobUser.getCurrentUser(MyUser.class);
                bean.setAuthor(user.getUsername());
                bean.setContent(BuyDesc);
                bean.setTel(BuyTel);
                bean.setTitle(BuyTitle);
                bean.setPrice(BuyPrice);
                bean.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            ToastUtil.show(BuyAddActivity.this, "发布成功", Toast.LENGTH_SHORT);
                            LogUtils.e("创建数据成功：" + objectId);
                            LoadDialog.dismiss(BuyAddActivity.this);
                            startActivity(new Intent(BuyAddActivity.this, BuyActivity.class));
                            finish();
                        } else {
                            LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                            ToastUtil.show(BuyAddActivity.this, "发布失败，请稍后再试", Toast.LENGTH_SHORT);
                            LoadDialog.dismiss(BuyAddActivity.this);
                        }
                    }
                });
            } else {
                SaleItem bean = new SaleItem();
                MyUser user = BmobUser.getCurrentUser(MyUser.class);
                bean.setAuthor(user.getUsername());
                bean.setContent(BuyDesc);
                bean.setTel(BuyTel);
                bean.setPrice(BuyPrice);
                bean.setTitle(BuyTitle);
                bean.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            ToastUtil.show(BuyAddActivity.this, "发布成功", Toast.LENGTH_SHORT);
                            LogUtils.e("创建数据成功：" + objectId);
                            LoadDialog.dismiss(BuyAddActivity.this);
                            startActivity(new Intent(BuyAddActivity.this, BuyActivity.class));
                            finish();
                        } else {
                            LogUtils.e("失败：" + e.getMessage() + "," + e.getErrorCode());
                            ToastUtil.show(BuyAddActivity.this, "发布失败，请稍后再试", Toast.LENGTH_SHORT);
                            LoadDialog.dismiss(BuyAddActivity.this);
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
        SelectDialog dialog = new SelectDialog(BuyAddActivity.this, R.style.transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    private ArrayList<String> photos;

    private void initLuban(List<String> paths) {
        photos = new ArrayList<>();
        Luban.with(BuyAddActivity.this)
                .load(paths)                                   // 传入要压缩的图片列表
                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                .setTargetDir(getPath())                        // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        LoadDialog.show(BuyAddActivity.this, "正在进行图片压缩ing……");
                    }

                    @Override
                    public void onSuccess(File file) {
                        LogUtils.e("压缩成功");
                        LoadDialog.dismiss(BuyAddActivity.this);
                        photos.add(file.getAbsolutePath());
                        filePaths[0] = photos.get(0);
                        BmobUpSale(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        LoadDialog.dismiss(BuyAddActivity.this);
                        Toast.makeText(BuyAddActivity.this, "压缩失败！", Toast.LENGTH_SHORT).show();
                    }
                }).launch();    //启动压缩
    }

    private String BuyDesc;
    private String BuyTel;
    private String BuyTitle;

    @OnClick(R.id.bt_buy_add)
    public void onViewClicked() {
        BuyTitle = etBuyTitle.getText().toString().trim();
        BuyDesc = etBuyDesc.getText().toString().trim();
        BuyTel = etBuyTel.getText().toString().trim();
        BuyPrice = etBuyPrice.getText().toString().trim();
        if (!TextUtils.isEmpty(BuyDesc) && (!TextUtils.isEmpty(BuyTel)) && (!TextUtils.isEmpty(BuyTitle))) {
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
                            case 0: // 直接调起相机
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(BuyAddActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(BuyAddActivity.this, ImageGridActivity.class);
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
                Intent intentPreview = new Intent(BuyAddActivity.this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);

                break;
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

}
