package tz.co.neelansoft.millennium;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private TextView tvVersionCode;
    private TextView tvDeveloper;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        tvVersionCode = findViewById(R.id.tvAppVersion);
        tvDeveloper = findViewById(R.id.tvDeveloper);

        String vName="";
        try {
            vName = getPackageManager().getPackageInfo(getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String appVersion = "Application Version: ".concat(vName);
        tvVersionCode.setText(appVersion);
        String developer = "Developed by: ".concat(getString(R.string.developer));
        tvDeveloper.setText(developer);
    }
}
