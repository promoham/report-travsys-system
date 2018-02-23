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
import models.AR;

import static mono.eagle.com.travsys.WelcomeActivity.dbBox;


/**
 * A simple {@link Fragment} subclass.
 */
public class ARFragment extends Fragment {


    LayoutInflater inflater;
    LinearLayout ARsContainer;
    Box<AR> arBox;
    int currentId = 0;
    Dialog dialog;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference arsRef = database.getReference("ars");

    public ARFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ARsContainer = view.findViewById(R.id.ARsContainer);
        arBox = dbBox.boxFor(AR.class);
        Button btnAdd = view.findViewById(R.id.btnAdd);
        final EditText txtName = view.findViewById(R.id.txtName);
        final EditText txtCode = view.findViewById(R.id.txtCode);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AR ar = new AR();
                ar.setName(txtName.getText().toString());
                ar.setCode(txtCode.getText().toString());

                arBox.put(ar);
                arsRef.push().setValue(ar);
                txtName.setText("");
                txtCode.setText("");
                viewARs();

            }
        });
        inflater = getActivity().getLayoutInflater();
        viewARs();
    }

    private void viewARs() {
        ARsContainer.removeAllViews();
        List<AR> ars = arBox.getAll();
        for (final AR ar : ars) {
            LinearLayout userLayout = (LinearLayout) inflater.inflate(R.layout.ar_layout, null);
            userLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    currentId = (int) ar.getId();
                    delEdit();
                    Toast.makeText(getContext(), "" + currentId, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            TextView txtARId = userLayout.findViewById(R.id.txtARId);
            TextView txtARName = userLayout.findViewById(R.id.txtARName);
            TextView txtARCode = userLayout.findViewById(R.id.txtARCode);
            txtARId.setText(ar.getId() + "");
            txtARName.setText(ar.getName());
            txtARCode.setText(ar.getCode());
            ARsContainer.addView(userLayout);
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
                arBox.remove(currentId);
                viewARs();
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
