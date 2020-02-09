package top.vchao.hevttc.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import top.vchao.hevttc.R;
import top.vchao.hevttc.activity.BuyActivity;
import top.vchao.hevttc.activity.ContactsActivity;
import top.vchao.hevttc.activity.CourseActivity;
import top.vchao.hevttc.activity.LoseActivity;
import top.vchao.hevttc.activity.LoveActivity;
import top.vchao.hevttc.activity.PhotoSchoolActivity;
import top.vchao.hevttc.activity.TeamActivity;
import top.vchao.hevttc.activity.WeatherActivity;
import top.vchao.hevttc.activity.WebActivity;
import top.vchao.hevttc.activity.XiaoliActivity;
import top.vchao.hevttc.adapter.MoudleAdapter;
import top.vchao.hevttc.bean.MoudleItem;
import top.vchao.hevttc.constant.MyUrl;
import xyz.zpayh.adapter.OnItemClickListener;

/**
 * @ 创建时间: 2017/7/21 on 19:39.
 * @ 描述：主页面fragment
 * @ 作者: vchao
 */
public class TabHomeFragment extends BaseFragment {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rv_main)
    RecyclerView mRecyclerView;

    private MoudleAdapter moudleAdapter;
    private ArrayList<MoudleItem> data;
    //首页轮播图   图片资源
    Integer[] imageses = {R.mipmap.img_keshi1, R.mipmap.img_keshi2, R.mipmap.img_keshi3, R.mipmap.img_keshi4};

    //模块名字   "藏书检索", , "天气"
    String[] MoudleName = {"表白墙", "校园街景",
            "校历", "学校官网", "校园通讯录",
            "图说校园", "社团组织"
    };
    //模块图片资源  R.mipmap.ic_home_news,
    int[] MoudleLogo = {R.mipmap.ic_home_loveshow, R.mipmap.ic_home_tellall,
            R.mipmap.ic_home_schooldate, R.mipmap.ic_home_schoolguide, R.mipmap.ic_home_numbernote,
            R.mipmap.ic_home_buysale, R.mipmap.ic_home_friends, R.mipmap.ic_home_friends,
            R.mipmap.ic_home_friends};
    //模块对应页面 WebActivity.class,
    Class[] clazz = {LoveActivity.class, WebActivity.class,
            XiaoliActivity.class, WebActivity.class, ContactsActivity.class,
            PhotoSchoolActivity.class, TeamActivity.class, WeatherActivity.class,
            CourseActivity.class
//            ContactsActivity.class, TeamActivity.class
    };
    //传递的信息 MyUrl.URL_QueryBook,
    String[] PutExtras = {null,

            MyUrl.URL_QQMAP,

            null,
            MyUrl.URL_HEVTTC,
            null,

            null, null, null,

            null, null, null,

            null, null, null,

            null, null, null
    };

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected View initView() {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, null);
        return inflate;
    }

    @Override
    public void initData() {
        //设置布局管理器
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        ArrayList images = new ArrayList<>();
        data = new ArrayList<>();
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //填充轮播图列表
        for (int i = 0; i < imageses.length; i++) {
            images.add(imageses[i]);
        }
        banner.setDelayTime(3500);//设置轮播时间
        banner.setImages(images);//设置图片集合
        banner.start();//banner设置方法全部调用完毕时最后调用
        //填充模块信息列表
        for (int i = 0; i < MoudleName.length; i++) {
            MoudleItem moudleItem = new MoudleItem(MoudleLogo[i], MoudleName[i], clazz[i]);
            data.add(moudleItem);
        }
        moudleAdapter = new MoudleAdapter();
        moudleAdapter.setData(data);
        mRecyclerView.setAdapter(moudleAdapter);
    }

    @Override
    public void initListener() {
        moudleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int positon) {
                MoudleItem item = data.get(positon);
                //跳转到对应功能的详情页面
                Intent intent = new Intent(getActivity(), item.getClazz());
                if (PutExtras[positon] != null) {
                    intent.putExtra("url", PutExtras[positon]);
                }
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.home_moudle1, R.id.home_moudle2, R.id.home_moudle3, R.id.home_moudle4})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.home_moudle1:
                intent = new Intent(mActivity, CourseActivity.class);
                startActivity(intent);
                break;
            case R.id.home_moudle2:
                intent = new Intent(mActivity, WebActivity.class);
                intent.putExtra("url", MyUrl.URL_Libray);
                startActivity(intent);
                break;
            case R.id.home_moudle3:
                intent = new Intent(mActivity, LoseActivity.class);
                startActivity(intent);
                break;
            case R.id.home_moudle4:
                intent = new Intent(mActivity, BuyActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * Glide图片加载方法
     */
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Glide 加载图片
            Glide.with(context).load(path).into(imageView);
        }
    }
}
