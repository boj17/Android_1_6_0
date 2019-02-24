package etc.com.android_1_6_0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    String TAG = RegisterActivity.class.getSimpleName();

    EditText et_name;
    EditText et_password;

    Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_name = (EditText)findViewById(R.id.et_name);
        et_password = (EditText)findViewById(R.id.et_password);

        button_register = (Button)findViewById(R.id.button_register);
        button_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_register:
                final String name = et_name.getText().toString().trim();
                final String password = et_password.getText().toString().trim();

                //调用registerServlet注册接口
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            String path = "http://10.100.5.61:8080/Web_1_6_0/registerServlet?name="+name+"&password="+password;
                            URL url= new URL(path);
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

                                runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        if("1".equals(result)){
                                            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }
}
