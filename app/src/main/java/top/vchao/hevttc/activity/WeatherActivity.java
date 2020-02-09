package top.vchao.hevttc.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;
import top.vchao.hevttc.R;
import top.vchao.hevttc.adapter.WeatherRecyclerAdapter;
import top.vchao.hevttc.bean.WeatherBean;
import top.vchao.hevttc.utils.ToastUtil;
import top.vchao.hevttc.view.BlurredView;

public class WeatherActivity extends BaseActivity {
    /*========== 控件相关 ===========*/
    private BlurredView weatherBView;           //背景模糊图
    private RecyclerView weatherRView;          //滑动列表
    private TextView tempText;                  //温度
    private TextView weatherText;               //天气
    private TextView windText;                  //风向
    private TextView windPowerText;             //风力
    private TextView humPowerText;              //湿度
    private TextView flPowerText;               //体感温度
    /*========== 数据相关 ===========*/

    /*========== 其他 ===========*/
    private int mScrollerY;                     //滚动距离
    private int mAlpha;                         //透明值
    private static final String weather_url = "https://free-api.heweather.com/v5/weather?" +
            "city=CN101091101&key=573a3ba3c95a43ad94e70c34610720f9";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weather;
    }

    @Override
    public void initView() {
        //透明状态栏 导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        RelativeLayout rl_weather = (RelativeLayout) findViewById(R.id.rl_weather);
        View view = View.inflate(this, R.layout.item_info_basic, null);
//        rl_weather.addView(view);
//        View view = getWindow().getDecorView();
//        tempText = (TextView) view.findViewById(R.id.tv_basic_temp);
//        if (tempText==null){
//            Log.i(TAG, "initView: 控件为空");
//        }else {
//            Log.i(TAG, "initView: 控件寻找成功");
//        }
//        tempText.setText(weatherBean.getHeWeather5().get(0).getNow().getTmp());
//        Log.i(TAG, "initView: "+tempText.getText());
//        tempText.setText("1111");
//        Log.i(TAG, "initView: "+tempText.getText());
        weatherText = (TextView) findViewById(R.id.tv_basic_weather);
        windText = (TextView) findViewById(R.id.tv_basic_wind);
        windPowerText = (TextView) findViewById(R.id.tv_basic_wind_power);
        humPowerText = (TextView) findViewById(R.id.tv_basic_hum_power);
        flPowerText = (TextView) findViewById(R.id.tv_basic_fl_power);
        weatherBView = (BlurredView) findViewById(R.id.bv_weather);
        weatherRView = (RecyclerView) findViewById(R.id.rv_weather);
        weatherRView.setLayoutManager(new LinearLayoutManager(this));
        WeatherRecyclerAdapter weatherRecyclerAdapter = new WeatherRecyclerAdapter(this);

        weatherRView.setAdapter(weatherRecyclerAdapter);
    }

    @Override
    public void initData() {
        //TODO:请求网络数据
        OkGo.get(weather_url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        WeatherBean weatherBean = gson.fromJson(s, WeatherBean.class);
                        ToastUtil.showShort(WeatherActivity.this, weatherBean.getHeWeather5().get(0).getNow().getTmp());
                    }
                });
    }

    @Override
    @SuppressWarnings("deprecation")
    public void initListener() {
        weatherRView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollerY += dy;                       //滚动距离
                if (Math.abs(mScrollerY) > 1000) {      //根据滚动距离控制模糊程度 滚动距离是模糊程度的十倍
                    mAlpha = 100;
                } else {
                    mAlpha = Math.abs(mScrollerY) / 10;
                }
                weatherBView.setBlurredLevel(mAlpha);    //设置透明度等级
            }
        });
    }
}
