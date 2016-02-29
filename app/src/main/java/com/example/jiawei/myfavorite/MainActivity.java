package com.example.jiawei.myfavorite;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static TextView textView;
    private Button getCurrency;
    private Button getPM;
    private Button getStock;

    public static final int SHOW_CURRENCY = 0;
    public static final int SHOW_PM = 1;
    public static final int SHOW_STOCK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.information);
        getCurrency = (Button)findViewById(R.id.get_currency);
        getPM = (Button)findViewById(R.id.get_pm);
        getStock = (Button)findViewById(R.id.get_stock);
        getCurrency.setOnClickListener(this);
        getPM.setOnClickListener(this);
        getStock.setOnClickListener(this);
      //  textView.setText("Hello");
    }

    @Override
    protected void onStart(){
        super.onStart();

    }


    static private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            StringBuilder sb = new StringBuilder();

            switch (msg.what) {
                case SHOW_CURRENCY:

                    sb.append("Current Canadian Dollar Currency: ");
                    try{
                        double currency;
                     //   JSONArray jsonArray = new JSONArray((String) msg.obj);
                        JSONObject jsonObject = new JSONObject((String)msg.obj);
                        JSONObject contentObject = jsonObject.getJSONObject("result");
                        currency = contentObject.getDouble("rate");
                        sb.append(currency);
                        sb.append("\n");
                        textView.setText(sb);
                    }catch(Exception e){
                        textView.setText(e.toString());
                    }

                    break;
                case SHOW_PM:
                    String pm25 = (String)msg.obj;
                    sb.append(pm25);
                    textView.setText(sb);
                    break;

                case SHOW_STOCK:
                    String result = (String)msg.obj;
                    String[] strArray;
                    strArray = result.split(",");
                    sb.append("Thundersoft 今天日开盘: ");
                    sb.append(strArray[1]);
                    sb.append(" 今日最高： ");
                    sb.append(strArray[4]);
                    sb.append(" 今日最低： ");
                    sb.append(strArray[5]);
                    sb.append(" 当前价格： ");
                    sb.append(strArray[3]);
                    sb.append("\n");
                    textView.setText(sb);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.get_currency:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        try {
                            String address = "http://api.k780.com:88/?app=finance.rate&scur=CAD&tcur=CNY&appkey=17680&" +
                                    "sign=73259d75a11a489ce09f8b284d87b719&format=json";
                            URL url = new URL(address);
                            connection = (HttpURLConnection) url.openConnection();

                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            InputStream in = connection.getInputStream();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            Message message = new Message();
                            message.what = SHOW_CURRENCY;

                            message.obj = response.toString();
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (connection != null) {
                                connection.disconnect();
                            }
                        }
                    }
                }).start();
                break;

            case R.id.get_pm:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;

                        try {
                        //    String address = "https://api.heweather.com/x3/weather?" +
                        //            "cityid=CN101270101&key=3317fd7adc944d9ab672ded1fac24b28";
                            String address = "http://aqicn.org/aqicn/json/android/chengdu/json";
                            URL url = new URL(address);
                            connection = (HttpURLConnection) url.openConnection();

                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            InputStream in = connection.getInputStream();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder response = new StringBuilder();
                            String line;
                        //    while ((line = reader.readLine()) != null) {
                        //        response.append(line);
                        //    }
                            line = reader.readLine();
                            int pos = line.indexOf(',');
                            String str = line.substring(1,pos);
                            response.append("Chengdu: ");
                            response.append(str);

                            //city 2
                            address = "http://aqicn.org/aqicn/json/android/nantong/json";
                            url = new URL(address);
                            connection = (HttpURLConnection) url.openConnection();

                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            in = connection.getInputStream();

                            reader = new BufferedReader(new InputStreamReader(in));

                            //    while ((line = reader.readLine()) != null) {
                            //        response.append(line);
                            //    }
                            line = reader.readLine();
                            pos = line.indexOf(',');
                            str = line.substring(1, pos);
                            response.append("\nnantong: ");
                            response.append(str);

                            //city 3
                            address = "http://aqicn.org/aqicn/json/android/beijing/json";
                            url = new URL(address);
                            connection = (HttpURLConnection) url.openConnection();

                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            in = connection.getInputStream();

                            reader = new BufferedReader(new InputStreamReader(in));

                            //    while ((line = reader.readLine()) != null) {
                            //        response.append(line);
                            //    }
                            line = reader.readLine();
                            pos = line.indexOf(',');
                            str = line.substring(1, pos);
                            response.append("\nbeijing: ");
                            response.append(str);

                            //city 4
                            address = "http://aqicn.org/aqicn/json/android/toronto/json";
                            url = new URL(address);
                            connection = (HttpURLConnection) url.openConnection();

                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            in = connection.getInputStream();

                            reader = new BufferedReader(new InputStreamReader(in));

                            //    while ((line = reader.readLine()) != null) {
                            //        response.append(line);
                            //    }
                            line = reader.readLine();
                            pos = line.indexOf(',');
                            str = line.substring(1,pos);
                            response.append("\ntoronto: ");
                            response.append(str);

                            Message message = new Message();
                            message.what = SHOW_PM;
// 将服务器返回的结果存放到Message中
                            message.obj = response.toString();
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (connection != null) {
                                connection.disconnect();
                            }
                        }
                    }
                }).start();
                break;

            case R.id.get_stock:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        try {
                            String address = "http://hq.sinajs.cn/list=sz300496";
                            URL url = new URL(address);
                            connection = (HttpURLConnection) url.openConnection();

                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            InputStream in = connection.getInputStream();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            Message message = new Message();
                            message.what = SHOW_STOCK;

                            message.obj = response.toString();
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (connection != null) {
                                connection.disconnect();
                            }
                        }
                    }
                }).start();
                break;

            default:
                break;
        }
    }
}
