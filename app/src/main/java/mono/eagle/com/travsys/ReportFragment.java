package mono.eagle.com.travsys;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Report;


public class ReportFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reportsRef = database.getReference("reports");
    RecyclerView recyclerView;
    List<Report> reports;
    CheckBox chkFromDate;
    CheckBox chkToDate;
    RadioButton radioDaily;
    RadioButton radioWeekly;
    Date dateFrom;
    Date dateTo;
    View.OnClickListener fromOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    dateFrom = new Date((year-1900), monthOfYear, dayOfMonth);
                    int s = monthOfYear + 1;
                    String a = dayOfMonth + "/" + s + "/" + (year);
                    ((CheckBox) v).setText(a);
                }
            };
            dateFrom=new Date();
            dateFrom = new Date((dateFrom.getYear()+1900),dateFrom.getMonth()+1,dateFrom.getDate());

            DatePickerDialog d = new DatePickerDialog(getContext(), dpd, dateFrom.getYear(), dateFrom.getMonth(), dateFrom.getDay());
            if (((CheckBox) v).isChecked())
                d.show();
        }
    };

    View.OnClickListener toOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    dateTo = new Date((year-1900), monthOfYear, dayOfMonth);
                    int s = monthOfYear + 1;
                    String a = dayOfMonth + "/" + s + "/" + year;
                    ((CheckBox) v).setText(a);
                }
            };

            dateTo=new Date();
            dateTo = new Date((dateTo.getYear()+1900),dateTo.getMonth(),dateTo.getDate());
            DatePickerDialog d = new DatePickerDialog(getContext(), dpd, dateTo.getYear(), dateTo.getMonth(), dateTo.getDay());
            if (((CheckBox) v).isChecked())
                d.show();
        }
    };
    private FloatingActionButton fabPrint;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateTo=new Date(1900,1,1);
        dateFrom=new Date(2900,1,1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        chkFromDate = view.findViewById(R.id.chkFromDate);
        chkFromDate.setOnClickListener(fromOnClickListener);

        chkToDate = view.findViewById(R.id.chkToDate);
        chkToDate.setOnClickListener(toOnClickListener);

        radioDaily = view.findViewById(R.id.radioDaily);
        radioWeekly = view.findViewById(R.id.radioWeekly);

        recyclerView = view.findViewById(R.id.reportsGrid);
        fabPrint = view.findViewById(R.id.fabPrint);
        fabPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPDF();
            }
        });
        reports = new ArrayList<>();
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext())
        );
        reportsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    reports.add(snapshot.getValue(Report.class));//
//                    );
                }
                recyclerView.setAdapter(
                        new ReportAdapter(
                                getContext(), reports)
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    private List<List<Report>> subReports(List<Report> reports) {
        List<String> dates = new ArrayList<>();
        for (Report report : reports) {
            String d = (report.getDate().getYear() + 1900) + " / " + (1 + report.getDate().getMonth()) + " / " + (report.getDate().getDate());
            if (!dates.contains(d)) {
                dates.add(d);
            }
        }

        List<List<Report>> result = new ArrayList<>();
        for (String date : dates) {
            List<Report> reportList = new ArrayList<>();
            for (Report report : reports) {
                String d = (report.getDate().getYear() + 1900) + " / " + (1 + report.getDate().getMonth()) + " / " + (report.getDate().getDate());
                if (d.equals(date)) {
                    reportList.add(report);
                }
            }
            result.add(reportList);
        }
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    void refresh(Document document) throws DocumentException {
        List<Report> sub = new ArrayList<>();
        for (Report report : reports) {
//            Toast.makeText(getContext(), "" +
//                            report.getDate().toString() + "\n" +
//                            dateFrom.toString() + "\n" +
//                            dateTo.toString()
//                    , Toast.LENGTH_SHORT).show();
            if (report.getDate().after(dateFrom) && report.getDate().before(dateTo)) {
                sub.add(report);
            }
        }

        FirstPdf firstPdf = new FirstPdf(
                dateToString(dateFrom),
                dateToString(dateTo),
                document,
                subReports(sub)
        );
        firstPdf.addContent();

    }

    private String dateToString(Date date) {
        return (date.getYear()+1900)+"/"+(date.getMonth()+1)+"/"+date.getDate();
    }

    private void sendFile(File file, @Nullable String[] to) {
        Uri uri = Uri.fromFile(file);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "My Body");
        emailIntent.setType("application/pdf");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        System.out.println(uri);

        startActivity(Intent.createChooser(emailIntent, "Send email using:"));

    }

    public void createPDF() {
        File dir = new File(
                Environment.getExternalStorageDirectory() + "/TravSys");
        boolean success = true;
        if (!dir.exists()) {
            success = dir.mkdir();
        }
        if (success) {
            System.out.println("success");
            File file = new File(dir, "s.pdf");
            if (write(file)) {
                sendFile(file, new String[]{""});
//                sendFile(file, new String[]{"Eagleofiraq.12@gmail.com"});
//                loadDocInReader(file);
            }
        }
    }

    public Boolean write(File file) {
        try {
            boolean success = true;
            if (!file.exists()) {
                success = file.createNewFile();
            }
            if (success) {
                Toast.makeText(getContext(), "create PDF failed \n", Toast.LENGTH_SHORT).show();
                Document document = new Document(PageSize.A4.rotate(), 10f, 10f, 10f, 10f);
                PdfWriter.getInstance(document,
                        new FileOutputStream(file.getAbsoluteFile()));
                document.open();
//                FirstPdf firstPdf = new FirstPdf(document, subReports(reports));
                refresh(document);
//                firstPdf.addTitlePage();
                //  firstPdf.addContent();
                document.close();
                return true;
            }
            return false;
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void loadDocInReader(File file) {
        if (file.exists()) {
            Uri path = Uri.fromFile(file);
            Intent objIntent = new Intent(Intent.ACTION_VIEW);
            objIntent.setDataAndType(path, "application/pdf");
            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(objIntent);
        }
    }

}
