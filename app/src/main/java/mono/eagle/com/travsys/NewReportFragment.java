package mono.eagle.com.travsys;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.objectbox.Box;
import models.AR;
import models.PC;
import models.Problem;
import models.Report;
import models.User;

import static mono.eagle.com.travsys.WelcomeActivity.dbBox;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewReportFragment extends Fragment {

    Button btnAdd;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reportsRef = database.getReference("reports");
    long currentUserId;
    List<String> PCs = new ArrayList<>();
    private Spinner spinnerPC;

    public NewReportFragment() {
        // Required empty public constructor
    }

    private void setPCsList(String c) {
        PCs = new ArrayList<>();
        PCs.add("{- select Pc .. -}");
        List<PC> pcs = new ArrayList<>();
        pcs.add(new PC() {{
            setName("PC1");
            setCounterId(1);
        }});
        pcs.add(new PC() {{
            setName("PC2");
            setCounterId(1);
        }});
        pcs.add(new PC() {{
            setName("PC3");
            setCounterId(1);
        }});
        pcs.add(new PC() {{
            setName("PC1");
            setCounterId(2);
        }});
        pcs.add(new PC() {{
            setName("PC2");
            setCounterId(2);
        }});
        pcs.add(new PC() {{
            setName("PC3");
            setCounterId(2);
        }});
        pcs.add(new PC() {{
            setName("PC1");
            setCounterId(3);
        }});
        pcs.add(new PC() {{
            setName("PC2");
            setCounterId(3);
        }});
        pcs.add(new PC() {{
            setName("PC1");
            setCounterId(4);
        }});
        pcs.add(new PC() {{
            setName("PC5");
            setCounterId(5);
        }});
        pcs.add(new PC() {{
            setName("PC6");
            setCounterId(6);
        }});
        pcs.add(new PC() {{
            setName("PC6");
            setCounterId(7);
        }});
        pcs.add(new PC() {{
            setName("PC9");
            setCounterId(7);
        }});
        for (PC pc : pcs) {
            if (!c.equals("{- select Counter .. -}"))
                if (Integer.parseInt(c) == pc.getCounterId()) {
                    PCs.add(pc.getName());
                }
        }
        ArrayAdapter<String> PCAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, PCs);
        spinnerPC.setAdapter(PCAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentUserId = getArguments().getLong("currentUserId");
        return inflater.inflate(R.layout.fragment_new_report, container, false);
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final Date now = new Date();
        TextView txtUser = view.findViewById(R.id.txtUser);
        TextView txtDate = view.findViewById(R.id.txtDate);
        TextView txtTime = view.findViewById(R.id.txtTime);

        final TextView txtAction = view.findViewById(R.id.txtAction);
        final TextView txtAirportResponse = view.findViewById(R.id.txtAirportResponse);
        final TextView txtReason = view.findViewById(R.id.txtReason);
        final TextView txtReporterName = view.findViewById(R.id.txtReporterName);


        final LinearLayout layoutCounter = view.findViewById(R.id.layoutCounter);
        final Spinner spinnerPlaces = view.findViewById(R.id.spinnerPlaces);
        final Spinner spinnerCounter = view.findViewById(R.id.spinnerCounter);
        spinnerPC = view.findViewById(R.id.spinnerPC);
        final Spinner spinnerArId = view.findViewById(R.id.spinnerArId);
        final Spinner spinnerProblem = view.findViewById(R.id.spinnerProblem);
        final Spinner spinnerFaults = view.findViewById(R.id.spinnerFaults);
        final Spinner spinnerStatues = view.findViewById(R.id.spinnerStatues);

        spinnerPlaces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                layoutCounter.setVisibility(
                        spinnerPlaces.getSelectedItem().toString().equals("Counter") ? View.VISIBLE : View.GONE);
                setPCsList(spinnerCounter.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCounter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setPCsList(spinnerCounter.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> ars = new ArrayList<>();
        ars.add("{- select AR .. -}");
        Box<AR> arBox = dbBox.boxFor(AR.class);
        for (AR ar : arBox.getAll()) {
            ars.add(ar.getCode());
        }

        List<String> problems = new ArrayList<>();
        problems.add("{- select Problem .. -}");
        Box<Problem> problemBox = dbBox.boxFor(Problem.class);
        for (Problem problem : problemBox.getAll()) {
            problems.add(problem.getName());
        }
        ArrayAdapter<String> problemsAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, problems);
        spinnerProblem.setAdapter(problemsAdapter);

        setPCsList("0");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, ars);
        spinnerArId.setAdapter(adapter);


        txtDate.setText((now.getYear() + 1900) + " / " + (1 + now.getMonth()) + " / " + (now.getDate()));
        txtTime.setText(now.getHours() + " : " + now.getMinutes());
        Box<User> userBox = dbBox.boxFor(User.class);
        String userName = txtUser.getText().toString();
        txtUser.setText(userName);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Box<Report> reportBox = dbBox.boxFor(Report.class);
                Report report = new Report();
                report.setNo(1);
                report.setTime(String.valueOf(now.getTime()));
                report.setDate(now);
                report.setPc("00");
                report.setPlace(spinnerPlaces.getSelectedItem().toString());
                report.setProblem("0015");
                report.setReason(txtReason.getText().toString());
                report.setAction(txtAction.getText().toString());
                report.setAirportResponse(txtAirportResponse.getText().toString());
                report.setArId(spinnerArId.getSelectedItem().toString());
                report.setFault(spinnerFaults.getSelectedItem().toString());
                report.setStatues(spinnerStatues.getSelectedItem().toString());
                report.setUserId(String.valueOf(currentUserId));
                report.setUserName(LocalData.get(getContext(), "userName"));
                report.setReporterName(txtReporterName.getText().toString());
                report.setCounter(spinnerCounter.getSelectedItem().toString());
                report.setPc(spinnerPC.getSelectedItem().toString());
                reportBox.put(report);
                reportsRef.push().setValue(report);
                ///..................
                new BG_send_notification(getContext()).execute(
                        "new report",
                        "TravSys",
                        "reports"
                );
// seend
                FragmentTransaction fragmentTransaction
                        = getActivity().getSupportFragmentManager().beginTransaction();
                NewReportFragment newReportFragment = new NewReportFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("currentUserId", currentUserId);
                newReportFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainFrame, newReportFragment)
                        .addToBackStack(null).commit();

//                reportsRef.setValue(null);
//                User user=new User(){{
//                    setId(0);
//                    setName("1");
//                    setPassword("1");
//                }};
//                for (Report report : reportBox.getAll()) {
//                    report.setImage(null);
//                    report.setUserId(user);
//                    reportsRef.push().setValue(report);
//                    Toast.makeText(getContext(), report.getId() + " : " + report.getReason(), Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    public static class BG_send_notification extends AsyncTask<String, Void, String> {
        Context context;


        public BG_send_notification(Context context) {
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {

            String msg = params[0];

            String UpdateUser_URL = "https://fcm.googleapis.com/fcm/send";
            try {
                URL object = new URL(UpdateUser_URL);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();

                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", "Key=AAAAVY0nIew:APA91bGzxm-JApgICrngWeC8zcofin8y-Mjl9qXnYszNy6jxekrc4zSJpMlyK2XD3WlAGbyLNUK72RTc-08mQM0z8FF7cBl_MRI-Bn5DImxJn4xaW4WMwBn9SMOPfKEKCSTvOMxuEKOp");
                con.setConnectTimeout(3000);
                con.setRequestMethod("POST");


                JSONObject json = new JSONObject();
                json.put("to", "/topics/reports");

                JSONObject data = new JSONObject();
                data.put("extra_information", "extra trext ......");

                json.put("data", data);

                JSONObject notification = new JSONObject();
                notification.put("title", "TravSys");
                notification.put("body", "New Report");
                notification.put("sound", "default");
                notification.put("click_action", "extra trext ......");

                json.put("notification", notification);


                OutputStream wr = con.getOutputStream();
                wr.write(json.toString().getBytes("UTF-8"));
                wr.flush();
                String result = "";
                int HttpResult = con.getResponseCode();

                if (HttpResult == HttpURLConnection.HTTP_OK) {

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        result += line;
                    }
                    br.close();
                }
                Log.v("notify", result);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

    public static class BG_send_notificationOrg extends AsyncTask<String, Void, String> {
        Context context;


        public BG_send_notificationOrg(Context context) {
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        protected String doInBackground(String... params) {

            String msg = params[0];
            String title = params[1];
            String user_token = params[2];
            String UpdateUser_URL = "https://fcm.googleapis.com/v1/projects/travsys01/messages:send";
            try {
                URL object = new URL(UpdateUser_URL);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();

                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", "key=AIzaSyAK7KBFzJaPaCttOl5HcIj2LKBGY4N8qM4");
                con.setConnectTimeout(10000);
                con.setRequestMethod("POST");

                JSONObject json = new JSONObject();
                JSONObject dataJson = new JSONObject();
                JSONObject bodyJson = new JSONObject();
                JSONObject notiJson = new JSONObject();

                notiJson.put("body", msg);
                notiJson.put("title", title);

                bodyJson.put("topic", user_token);
                bodyJson.put("notification", notiJson);

                dataJson.put("message", bodyJson);

                OutputStream wr = con.getOutputStream();
                wr.write(json.toString().getBytes("UTF-8"));
                wr.flush();
                String result = "";
                int HttpResult = con.getResponseCode();

                if (HttpResult == HttpURLConnection.HTTP_OK) {

                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        result += line;
                    }
                    br.close();
                }
                Log.v("notify", result);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }
}
