package in.soniccomputer.in.myspiderchart;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RadarChart radarChart;

    ArrayList<Entry> yVals1;
    ArrayList<Entry> yVals2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        radarChart = (RadarChart) findViewById(R.id.radarChart);
        setData();
    }

    private void setData() {
        yVals1 = new ArrayList<Entry>();
        yVals2 = new ArrayList<Entry>();
        AssetManager am = getAssets();
        InputStream is = null;
        try {
            is = am.open("version.json");
            String json = readStream(is);
            is.close();
            JSONObject obj = (JSONObject) new JSONTokener(json).nextValue();
            JSONArray arr1 = obj.getJSONArray("version1");
            JSONArray arr2 = obj.getJSONArray("version2");
            for (int i = 0; i < arr1.length(); i++) {
                int x = arr1.getInt(i);
                int y = arr2.getInt(i);
                yVals1.add(new Entry(x, i));
                yVals2.add(new Entry(y, i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RadarDataSet dataSet1, dataSet2;
        dataSet1 = new RadarDataSet(yVals1, "Version1");
        dataSet2 = new RadarDataSet(yVals2, "Version2");

        dataSet1.setColor(Color.DKGRAY);
        dataSet2.setColor(Color.RED);

        ArrayList<IRadarDataSet> dataSets = new ArrayList<IRadarDataSet>();
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Administration");
        labels.add("Sales");
        labels.add("Marketing");
        labels.add("Development");
        labels.add("Customer Support");
        labels.add("Information Technology");

        RadarData data = new RadarData(labels, dataSets);
        radarChart.setData(data);
    }

    private String readStream(InputStream is) {
        ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
        byte[] buffer = new byte[4096];
        int n = 0;
        try {
            while (-1 != (n = is.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } catch (IOException e) {
            Log.d("graph", e.getMessage());
            return null;
        }
        return output.toString();
    }
}
