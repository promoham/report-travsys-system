package mono.eagle.com.travsys;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import io.objectbox.Box;
import models.Problem;

import static mono.eagle.com.travsys.WelcomeActivity.dbBox;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProblemFragment extends Fragment {


    LayoutInflater inflater;
    LinearLayout problemsContainer;
    Box<Problem> problemBox;
    int currentId = 0;
    Dialog dialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference problemsRef = database.getReference("problems");

    public ProblemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_problem, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        problemsContainer = view.findViewById(R.id.problemsContainer);
        problemBox = dbBox.boxFor(Problem.class);
        Button btnAdd = view.findViewById(R.id.btnAdd);
        final EditText txtProblemName = view.findViewById(R.id.txtProblemName);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Problem problem = new Problem();
                problem.setName(txtProblemName.getText().toString());
                problemBox.put(problem);
                problemsRef.push().setValue(problem);
                txtProblemName.setText("");
                viewUsers();

            }
        });
        inflater = getActivity().getLayoutInflater();
        viewUsers();
    }

    private void viewUsers() {
        problemsContainer.removeAllViews();
        List<Problem> problems = problemBox.getAll();
        for (final Problem problem : problems) {
            LinearLayout userLayout = (LinearLayout) inflater.inflate(R.layout.proplem_layout, null);
            userLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    currentId = (int) problem.getId();
                    delEdit();
                    Toast.makeText(getContext(), "" + currentId, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            TextView txtProblemId = userLayout.findViewById(R.id.txtProblemId);
            TextView txtProblemContent = userLayout.findViewById(R.id.txtProblemContent);
            txtProblemId.setText(problem.getId() + "");
            txtProblemContent.setText(problem.getName());
            problemsContainer.addView(userLayout);
        }
    }

    @SuppressLint("NewApi")
    void delEdit() {
        LinearLayout dialogdelEditLayout = new LinearLayout(getContext());
        dialogdelEditLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout delLayout = new LinearLayout(getContext());
        delLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView txtDel = new TextView(getContext());
        txtDel.setText("Delete");
        ImageView iv = new ImageView(getContext());
        iv.setMaxWidth(toDP(50));
        iv.setMaxHeight(toDP(50));
        iv.setImageDrawable(getResources().getDrawable(R.drawable.print, getContext().getTheme()));

//        delLayout.addView(iv);
        delLayout.addView(txtDel);
        delLayout.isClickable();
        delLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                problemBox.remove(currentId);
                viewUsers();
                dialog.dismiss();
            }
        });
        LinearLayout splitter = new LinearLayout(getContext());
        splitter.setBackgroundColor(Color.argb(170, 255, 100, 100));
        splitter.setMinimumWidth(toDP(500));
        splitter.setMinimumHeight(toDP(1));

        LinearLayout editLayout = new LinearLayout(getContext());
        editLayout.setOrientation(LinearLayout.VERTICAL);
        editLayout.isClickable();
        TextView txtEdit = new TextView(getContext());
        txtEdit.setText("Edit");
        editLayout.addView(txtEdit);
        editLayout.isClickable();
        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                // editBook(id, name, phone, date, time);
            }
        });
        txtDel.setWidth(toDP(500));
        txtDel.setTextSize(22.0f);
        txtEdit.setWidth(toDP(500));
        txtEdit.setTextSize(22.0f);

        txtDel.setPadding(toDP(5), toDP(5), toDP(5), toDP(5));
        txtEdit.setPadding(toDP(5), toDP(5), toDP(5), toDP(5));
        delLayout.setPadding(toDP(50), toDP(10), toDP(100), toDP(10));
        editLayout.setPadding(toDP(50), toDP(5), toDP(50), toDP(5));
        dialogdelEditLayout.addView(delLayout);
        dialogdelEditLayout.addView(splitter);
        dialogdelEditLayout.addView(editLayout);

        dialog = new Dialog(getContext());
        dialog.setContentView(dialogdelEditLayout);
        dialog.setTitle("خيارات");
        dialog.show();
    }

    public int toDP(int i) {
        return (int) TypedValue
                .applyDimension(TypedValue
                                .COMPLEX_UNIT_DIP,
                        i,
                        getResources()
                                .getDisplayMetrics());
    }
}
