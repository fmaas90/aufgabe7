package matthes.kswe.fbg.hsbochum.de.stockapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends FragmentActivity {

    private static final String TAG = "Stockr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        TextView textView = (TextView) findViewById(R.id.textView);

        List<DataPoint> points = new ArrayList<>();
        try {
            JSONObject json = loadJSON();
            JSONObject dataset = json.getJSONObject("dataset");
            textView.setText(dataset.getString("name"));
            JSONArray data = dataset.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                //daily close is at position 4
                points.add(new DataPoint(i,
                        data.getJSONArray(i).getDouble(4)));
            }
        } catch (IOException | JSONException e) {
            Log.w(TAG, e.getMessage(), e);
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);
        DataPoint[] pointsArray = new DataPoint[points.size()];
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points.toArray(pointsArray));
        graph.addSeries(series);

    }

    private JSONObject loadJSON() throws IOException, JSONException {
        InputStream is = getResources().openRawResource(R.raw.dax);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new JSONObject(new String(buffer));
    }

}
