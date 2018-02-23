package mono.eagle.com.travsys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

import models.Report;

import static io.objectbox.android.AndroidObjectBrowser.TAG;

/**
 * Created by eagle on 10/21/2017.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportHolder> {
    @SuppressLint("StaticFieldLeak")
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reportsRef = database.getReference("reports");

    private List<Report> dataSet;
    private Context ctx;


    ReportAdapter(Context ctx, List<Report> dataSet) {
        this.dataSet = dataSet;
        this.ctx = ctx;
    }

    @Override
    public ReportHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.report_layout, parent, false);
        return new ReportHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ReportHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txtId.setText(dataSet.get(position).getId() + "");
        final Date date = dataSet.get(position).getDate();
        holder.txtDate.setText("Date : " + (date.getYear() + 1900) + " / " + (1 + date.getMonth()) + " / " + (date.getDate()));
        holder.txtTime.setText("Time : " + date.getHours() + " : " + date.getMinutes());
        holder.txtId.setText("Id : " + dataSet.get(position).getId());
        holder.txtAction.setText("Action : " + dataSet.get(position).getAction());
        holder.txtReason.setText("Reason : " + dataSet.get(position).getReason());
        holder.txtStatues.setText("Statues : " + dataSet.get(position).getStatues());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ctx, "report " + dataSet.get(position).getId() + "deleted", Toast.LENGTH_SHORT).show();

                Query query = reportsRef.orderByChild("id").equalTo(dataSet.get(position).getId());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
                for (Report report : dataSet) {
                    if (report.getId() == dataSet.get(position).getId()) {
                        dataSet.remove(report);
                    }
                }
                
                return false;
            }
        });
        holder.txtReporter.setText("Reporter: " + dataSet.get(position).getReporterName());
        holder.txtProblem.setText("Problem: " + dataSet.get(position).getProblem());
        holder.txtAR.setText("AR: " + dataSet.get(position).getArId());
        holder.txtPC.setText("PC: " + dataSet.get(position).getPc());
        holder.txtCounter.setText("Counter: " + dataSet.get(position).getCounter());
        holder.txtFault.setText("Fault: " + dataSet.get(position).getFault());
        holder.txtUser.setText("User: " + dataSet.get(position).getUserName());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}


class ReportHolder extends RecyclerView.ViewHolder {
    LinearLayout view;
    TextView txtId;
    TextView txtTime;
    TextView txtDate;
    TextView txtPC;
    TextView txtCounter;
    TextView txtProblem;
    TextView txtReason;
    TextView txtAction;
    TextView txtAR;
    TextView txtFault;
    TextView txtStatues;
    TextView txtUser;
    TextView txtReporter;


    ReportHolder(View itemView) {
        super(itemView);
        view = (LinearLayout) itemView;
        txtId = itemView.findViewById(R.id.txtId);
        txtTime = itemView.findViewById(R.id.txtTime);
        txtDate = itemView.findViewById(R.id.txtDate);
        txtPC = itemView.findViewById(R.id.txtPC);
        txtCounter = itemView.findViewById(R.id.txtCounter);
        txtProblem = itemView.findViewById(R.id.txtProblem);
        txtReason = itemView.findViewById(R.id.txtReason);
        txtAction = itemView.findViewById(R.id.txtAction);
        txtAR = itemView.findViewById(R.id.txtAR);
        txtFault = itemView.findViewById(R.id.txtFault);
        txtStatues = itemView.findViewById(R.id.txtStatues);
        txtUser = itemView.findViewById(R.id.txtUser);
        txtReporter = itemView.findViewById(R.id.txtReporter);
    }
}


