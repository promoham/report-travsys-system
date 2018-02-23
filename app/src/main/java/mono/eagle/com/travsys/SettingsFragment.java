package mono.eagle.com.travsys;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.objectbox.Box;
import models.User;

import static mono.eagle.com.travsys.WelcomeActivity.dbBox;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    long currentUserId;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentTransaction fragmentTransaction
                    = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.settingsFrame, new ARFragment())
                    .addToBackStack(null).commit();
            switch (item.getItemId()) {
                case R.id.navigation_users:
                    Box<User> userBox = dbBox.boxFor(User.class);
                    String isAdmin=LocalData.get(getContext(),"userIsAdmin");
                    if (isAdmin.equals("true")) {
                        fragmentTransaction
                                = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.settingsFrame, new UsersFragment())
                                .addToBackStack(null).commit();
                    } else {
                        Toast.makeText(getContext(), "Admins can access only", Toast.LENGTH_SHORT).show();

                    }
                    return true;
                case R.id.ARSettings:
                    fragmentTransaction
                            = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.settingsFrame, new ARFragment())
                            .addToBackStack(null).commit();
                    return true;
                case R.id.problemSettings:
                    fragmentTransaction
                            = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.settingsFrame, new ProblemFragment())
                            .addToBackStack(null).commit();
                    return true;
            }
            return false;
        }
    };


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentUserId = getArguments().getLong("currentUserId");
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        BottomNavigationView navigationView = view.findViewById(R.id.subNavigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
