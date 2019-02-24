package etc.com.android_1_6_0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    String TAG = LoginActivity.class.getSimpleName();

    EditText et_name;
    EditText et_password;

    Button button_login;
    Button button_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_name = (EditText)findViewById(R.id.et_name);
        et_password = (EditText)findViewById(R.id.et_password);

        button_login = (Button)findViewById(R.id.button_login);
        button_register = (Button)findViewById(R.id.button_register);
        button_login.setOnClickListener(this);
        button_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_login:
                final String name = et_name.getText().toString().trim();
                final String password = et_password.getText().toString().trim();

                //请求loginServlet,获取到num的值,如果num==1登录成功,如果num==0登录失败
                new Thread(){
                    @Override
                    public void run() {
                        try{
                            String path = "http://10.100.5.61:8080/Web_1_6_0/loginServlet?name="+name+"&password="+password;
                            URL url = new URL(path);
                            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                            connection.connect();
                            int responseCode = connection.getResponseCode();
                            if(responseCode == 200){
                                InputStream is = connection.getInputStream();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                byte[] buffer = new byte[1024];
                                int len = -1;
                                while((len=is.read(buffer))!=-1){
                                    baos.write(buffer,0,len);
                                }
                               final String result = baos.toString();
                                baos.close();
                                is.close();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(result.equals("1")){
                                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                        }else {
                                            Toast.makeText(LoginActivity.this,"账号密码错误",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }




                    }
                }.start();
                break;
            case R.id.button_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;

        }
    }
}
