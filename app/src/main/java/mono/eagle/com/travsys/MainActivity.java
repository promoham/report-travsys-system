package mono.eagle.com.travsys;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.objectbox.Box;
import models.AR;
import models.Problem;
import models.Report;

import static mono.eagle.com.travsys.WelcomeActivity.dbBox;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reportsRef = database.getReference("reports");
    DatabaseReference arsRef = database.getReference("ars");
    DatabaseReference problemsRef = database.getReference("problems");
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentTransaction fragmentTransaction
                    = getSupportFragmentManager().beginTransaction();
            ReportFragment mainFragment = new ReportFragment();
            fragmentTransaction.replace(R.id.mainFrame, mainFragment)
                    .addToBackStack(null).commit();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setTitle("New Report");
                    fragmentTransaction
                            = getSupportFragmentManager().beginTransaction();
                    NewReportFragment newReportFragment = new NewReportFragment();
                    Bundle bundle = new Bundle();
                    newReportFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.mainFrame, newReportFragment)
                            .addToBackStack(null).commit();
                    return true;
                case R.id.navigation_dashboard:
                    setTitle("View reports");
                    fragmentTransaction
                            = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainFrame, new ReportFragment())
                            .addToBackStack(null).commit();
                    return true;
                case R.id.navigation_notifications:
                    setTitle("Settings");
                    fragmentTransaction
                            = getSupportFragmentManager().beginTransaction();
                    SettingsFragment settingsFragment = new SettingsFragment();
                    Bundle bundle1 = new Bundle();
                    settingsFragment.setArguments(bundle1);
                    fragmentTransaction.replace(R.id.mainFrame, settingsFragment)
                            .addToBackStack(null).commit();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (dbBox == null) {
            dbBox = models.MyObjectBox.builder().androidContext(this).build();
        }
        try {
            final Box<AR> arBox = dbBox.boxFor(AR.class);
            arsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    arBox.removeAll();
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        AR ar = snapshot.getValue(AR.class);
                        ar.setId(0);
                        arBox.put(ar);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            final Box<Problem> problemBox = dbBox.boxFor(Problem.class);
            problemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    problemBox.removeAll();
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Problem problem = snapshot.getValue(Problem.class);
                        problem.setId(0);
                        problemBox.put(problem);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            final Box<Report> reportBox = dbBox.boxFor(Report.class);
            reportsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    reportBox.removeAll();
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Report report = snapshot.getValue(Report.class);
                        report.setId(0);
                        reportBox.put(report);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } catch (Exception ignored) {
        }

        FragmentTransaction fragmentTransaction
                = getSupportFragmentManager().beginTransaction();
        ReportFragment reportFragment = new ReportFragment();
        fragmentTransaction.replace(R.id.mainFrame, reportFragment)
                .addToBackStack(null).commit();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }

}
