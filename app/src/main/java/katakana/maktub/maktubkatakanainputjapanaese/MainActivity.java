package katakana.maktub.maktubkatakanainputjapanaese;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private MaktubEditTextKatakana mEdtKatakana;
    private Button mBtnGet;
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdtKatakana = (MaktubEditTextKatakana) findViewById(R.id.edt_katakana);
        mBtnGet = (Button) findViewById(R.id.btn_get);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mBtnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvResult.setText(mEdtKatakana.getText().toString().trim());
            }
        });
    }
}
