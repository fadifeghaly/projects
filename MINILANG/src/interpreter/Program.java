package interpreter;

import java.io.File;
import java.util.LinkedList;
import java.util.ArrayList;

import interpreter.analysis.*;
import interpreter.node.*;
import org.apache.commons.io.*;
import org.apache.commons.text.WordUtils;
import com.pdfcrowd.*;

import java.io.*;


public class Program extends DepthFirstAdapter {

    private static ArrayList<String> authors = new ArrayList<>();
    private static String file_name;
    private static String title;
    private static String date;
    private static String body;
    private String result;

    private Program() {
    }

    public static void start(Node tree) {
        Program program = new Program();
        tree.apply(program);
    }

    public void caseAPlainTextTerminal(APlainTextTerminal node) {
        if (node.parent().parent() instanceof AParaBlock) {
            body += "<br>" + node.toString();
        }
        this.result = node.getPlainText().getText().trim();
    }

    public void caseATitleBlock(ATitleBlock node) {
        LinkedList<PInline> inlines = node.getInlines();
        for (PInline inline : inlines) {
            body += "<div id='title' style='text-align: center'>";
            inline.apply(this);
            body += "</div><br>";
        }
    }

    public void caseATitleInline(ATitleInline node) {
        node.getTerminal().apply(this);
        if (node.parent() instanceof ATitleBlock) {
            if ((((ATitleBlock) node.parent()).getInlines().size() == 1)) {
                body += this.result;
            }
        }
        file_name = this.result;
    }

    public void caseAAuthorInline(AAuthorInline node) {
        node.getTerminal().apply(this);
        String[] authors = this.result.split(System.getProperty("line.separator"));
        for (String author : authors) {
            author = author.trim();
            if (!author.isEmpty()) {
                author = "<li>" + author + "</li>";
                this.authors.add(author);
            }
        }
    }

    public void caseADateInline(ADateInline node) {
        node.getTerminal().apply(this);
        date = this.result.trim();
    }

    public void caseALinkInline(ALinkInline node) {
        node.getTerminal().apply(this);
        body += "<a href=" + this.result + ">Link to source</a><br>";
    }

    public void caseAEmailInline(AEmailInline node) {
        node.getTerminal().apply(this);
        body += "<br><a href=\"mailto:" + this.result +
                "?Subject=Hello%20again\" target=\"_top\">Send Mail</a><br><br>";
    }

    public void caseAItalicBlock(AItalicBlock node) {
        LinkedList<PInline> inlines = node.getInlines();
        body += "<i>";
        for (PInline inline : inlines) {
            inline.apply(this);
        }
        body += "</i><br>";
    }

    public void caseAItalicInline(AItalicInline node) {
        node.getTerminal().apply(this);
        if (!(node.parent() instanceof AItalicBlock))
            body += "<i>" + this.result + "</i>";
    }

    public void caseABoldBlock(ABoldBlock node) {
        LinkedList<PInline> inlines = node.getInlines();
        body += "<strong>";
        for (PInline inline : inlines) {
            inline.apply(this);
        }
        body += "</strong><br>";
    }

    public void caseABoldInline(ABoldInline node) {
        node.getTerminal().apply(this);
        if (!(node.parent() instanceof ABoldBlock))
            body += "<strong>" + this.result + "</strong>";
    }

    public void caseAFontSizeInline(AFontSizeInline node) {
        node.getTerminal().apply(this);
        body += "<h1>" + this.result + "</h1>";
    }

    public void caseAImageInline(AImageInline node) {
        node.getTerminal().apply(this);
        body += "<br><img src=" + this.result + "><br>";
    }

    public void caseAListBlock(AListBlock node) {
        LinkedList<PListItem> list_item = node.getItems();
        for (PListItem item : list_item) {
            item.apply(this);
        }
    }

    public void caseAListItem(AListItem node) {
        node.getInlines().apply(this);
        body += "<li>" + this.result + "</li>";

    }

    public void caseATableBlock(ATableBlock node) {
        LinkedList<PTableRow> table_headers = node.getItems();
        body += "<br><table class=\"table table-striped\">";
        for (PTableRow table_header : table_headers) {
            table_header.apply(this);
        }
        body += "</table><br>";
    }

    public void caseATableRow(ATableRow node) {
        LinkedList<PTableItem> table_items = node.getItems();
        body += "<tr>";
        for (PTableItem table_item : table_items) {
            table_item.apply(this);
        }
        body += "</tr>";
    }

    public void caseAThTableItem(AThTableItem node) {
        node.getTerminal().apply(this);
        body += "<th>" + this.result + "</th>";
    }

    public void caseATdTableItem(ATdTableItem node) {
        node.getTerminal().apply(this);
        body += "<td>" + this.result + "</td>";
    }

    public void caseAParaBlock(AParaBlock node) {
        LinkedList<PInline> inlines = node.getInlines();
        for (PInline inline : inlines) {
            inline.apply(this);
        }
        body += "<br><br>";
    }

    public static void generateHTML() {
        try {
            File htmlTemplateFile = new File("template/template.html");
            String htmlString = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
            htmlString = htmlString.replace("$date", date);
            String authors_concatenate = "";
            for (String author : authors) {
                authors_concatenate += author;
            }
            htmlString = htmlString.replace("$author", authors_concatenate);
            body = body.replaceAll("null", "");
            htmlString = htmlString.replace("$body", body);
            title = WordUtils.capitalize(file_name);
            File newHtmlFile = new File(title.replaceAll("\\s+", "") + ".html");
            FileUtils.writeStringToFile(newHtmlFile, htmlString, "UTF-8");
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void generatePDF() throws IOException, Pdfcrowd.Error {
        title = WordUtils.capitalize(file_name).replaceAll("\\s+", "");
        File file = new File(title + ".html");
        if (!file.exists())
            generateHTML();
        try {
            Pdfcrowd.HtmlToPdfClient client =
                    new Pdfcrowd.HtmlToPdfClient("creation", "f9fe10544618bb340430bcc5076363db").setNoBackground(true);
            client.convertFileToFile(title.replaceAll("\\s+", "") + ".html", title + ".pdf");
        } catch (Pdfcrowd.Error e) {
            System.err.println("Conversion error: " + e.getMessage());
            throw e;
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            throw e;
        }
    }

    public static void generateImage(String format) throws IOException, PdfcrowdError {
        title = WordUtils.capitalize(file_name).replaceAll("\\s+", "");
        File file = new File(title + ".html");
        if (!file.exists())
            generateHTML();
        try {
            Pdfcrowd.HtmlToImageClient client =
                    new Pdfcrowd.HtmlToImageClient("creation", "f9fe10544618bb340430bcc5076363db").setNoBackground(true);
            format = format.replace("-", "");
            client.setOutputFormat(format);
            client.convertFileToFile(title.replaceAll("\\s+", "") + ".html",title + "." + format);
        } catch (Pdfcrowd.Error e) {
            System.err.println("Conversion error: " + e.getMessage());
            throw e;
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            throw e;
        }
    }
}
