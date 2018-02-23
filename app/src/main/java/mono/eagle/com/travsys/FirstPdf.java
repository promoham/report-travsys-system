package mono.eagle.com.travsys;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.Date;
import java.util.List;

import models.Report;

/**
 * Created by eagle on 11/11/2017.
 */

public class FirstPdf {
    String DateFrom;
    String dateTo;
    Document document;
    List<List<Report>> reportes;

    public FirstPdf(String dateFrom, String dateTo, Document document, List<List<Report>> reportes) {
        DateFrom = dateFrom;
        this.dateTo = dateTo;
        this.document = document;
        this.reportes = reportes;

        addMetaData();
        addLogo();
        addDates();
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private void createTable(Paragraph subCatPart)
            throws BadElementException {

        PdfPTable table0 = new PdfPTable(12);
        table0.setTotalWidth(PageSize.A4.rotate().getWidth());
        table0.setLockedWidth(true);

        float w = table0.getTotalWidth();
        try {
            int[] ws = {1, 3, 2, 1, 4, 4, 4, 4, 2, 2, 2, 2};

            table0.setWidths(ws);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        PdfPCell c1 = new PdfPCell(new Phrase("ID "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("Date"));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("Time"));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("PC "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("problem "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("reason "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("action "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("airportResponse "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("AR "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("fault "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("statues "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);


        c1 = new PdfPCell(new Phrase("reporterName "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        subCatPart.add(table0);

        for (List<Report> reports : reportes) {
            PdfPTable table = new PdfPTable(12);
            table.setTotalWidth(PageSize.A4.rotate().getWidth());
            table.setLockedWidth(true);


            try {
                int[] ws = {1, 3, 2, 1, 4, 4, 4, 4, 2, 2, 2, 2};

                table.setWidths(ws);
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            BaseColor sColor = new BaseColor(200, 200, 200),
                    fColor = new BaseColor(200, 200, 200);

            for (Report report : reports) {
                switch (report.getFault()) {
                    case "Routine":
                        fColor = new BaseColor(250, 150, 150);
                        break;
                    case "Minor":
                        fColor = new BaseColor(150, 150, 250);
                        break;
                    case "Major":
                        fColor = new BaseColor(150, 250, 250);
                        break;
                    case "Critical":
                        fColor = new BaseColor(150, 250, 150);
                        break;
                }

                switch (report.getStatues()) {
                    case "Close":
                        sColor = new BaseColor(250, 150, 150);
                        break;
                    case "Open":
                        sColor = new BaseColor(15, 150, 250);
                        break;
                    case "Pending":
                        sColor = new BaseColor(150, 250, 250);
                        break;
                }


                c1 = new PdfPCell(new Phrase(report.getId() + ""));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);


                c1 = new PdfPCell(new Phrase((report.getDate().getYear() + 1900) + " / " + (1 + report.getDate().getMonth()) + " / " + (report.getDate().getDate())));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getDate().getHours() + " : " + report.getDate().getMinutes()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getPc()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getProblem()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getReason()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getAction()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getAirportResponse()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getArId()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getFault()));
                c1.setBackgroundColor(fColor);
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getStatues()));
                c1.setBackgroundColor(sColor);
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);


                c1 = new PdfPCell(new Phrase(report.getReporterName()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);


            }
            subCatPart.add(table);
        }
    }

    private void createTableWeekly(Paragraph subCatPart)
            throws BadElementException {

        PdfPTable table0 = new PdfPTable(12);
        table0.setTotalWidth(PageSize.A4.rotate().getWidth());
        table0.setLockedWidth(true);

        try {
            int[] ws = {1, 3, 2, 1, 4, 4, 4, 4, 2, 2, 2, 2};
            table0.setWidths(ws);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        PdfPCell c1 = new PdfPCell(new Phrase("ID "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("Date"));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("Time"));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("PC "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("problem "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("reason "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("action "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("airportResponse "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("AR "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("fault "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        c1 = new PdfPCell(new Phrase("statues "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);


        c1 = new PdfPCell(new Phrase("reporterName "));
        c1.setBackgroundColor(new GrayColor(0.40f));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table0.addCell(c1);

        subCatPart.add(table0);

        for (List<Report> reports : reportes) {
            PdfPTable table = new PdfPTable(12);
            table.setTotalWidth(PageSize.A4.rotate().getWidth());
            table.setLockedWidth(true);


            try {
                int[] ws = {1, 3, 2, 1, 4, 4, 4, 4, 2, 2, 2, 2};

                table.setWidths(ws);
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            BaseColor sColor = new BaseColor(200, 200, 200),
                    fColor = new BaseColor(200, 200, 200);

            for (Report report : reports) {
                switch (report.getFault()) {
                    case "Routine":
                        fColor = new BaseColor(100, 250, 100);
                        break;
                    case "Minor":
                        fColor = new BaseColor(250, 200, 100);
                        break;
                    case "Major":
                        fColor = new BaseColor(250, 250, 100);
                        break;
                    case "Critical":
                        fColor = new BaseColor(250, 100, 100);
                        break;
                }

                switch (report.getStatues()) {
                    case "Close":
                        sColor = new BaseColor(100, 250, 100);
                        break;
                    case "Open":
                        sColor = new BaseColor(250, 100, 100);
                        break;
                    case "Pending":
                        sColor = new BaseColor(100, 250, 250);
                        break;
                }


                c1 = new PdfPCell(new Phrase(report.getId() + ""));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);


                c1 = new PdfPCell(new Phrase((report.getDate().getYear() + 1900) + " / " + (1 + report.getDate().getMonth()) + " / " + (report.getDate().getDate())));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getDate().getHours() + " : " + report.getDate().getMinutes()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getPc()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getProblem()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getReason()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getAction()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getAirportResponse()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getArId()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getFault()));
                c1.setBackgroundColor(fColor);
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase(report.getStatues()));
                c1.setBackgroundColor(sColor);
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);


                c1 = new PdfPCell(new Phrase(report.getReporterName()));
                c1.setBackgroundColor(new BaseColor(200, 200, 200));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);


            }
            Paragraph paragraph=new Paragraph(dateToString(reports.get(0).getDate()));
            subCatPart.add(paragraph);

            subCatPart.add(table);
        }
    }

    public void addMetaData() {
        document.addTitle("TravSySReport");
        document.addSubject("daily Report");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Eagle Co");
        document.addCreator("EagleOfIraq.12");
    }
    private String dateToString(Date date) {
        return (date.getYear()+1900)+"/"+(date.getMonth()+1)+"/"+date.getDate();
    }

    public void addContent() throws DocumentException {

        Paragraph paragraph = new Paragraph();
//        createTable(paragraph);
        createTableWeekly(paragraph);
        document.add(paragraph);
    }

    void addLogo() {
        Font blue = new Font(Font.FontFamily.HELVETICA, 72, Font.NORMAL, BaseColor.BLUE);
        Paragraph paragraph = new Paragraph("TravSys", blue);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(paragraph, 1);

        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    void addDates() {
        Font blue = new Font(Font.FontFamily.HELVETICA, 22, Font.NORMAL, BaseColor.DARK_GRAY);
        String s = "from : " + DateFrom + "\t \t To : " + dateTo;
        Paragraph paragraph = new Paragraph(s, blue);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(paragraph, 2);
        Paragraph em = new Paragraph("   ");
        try {
            document.add(em);
            document.add(paragraph);
            document.add(em);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}