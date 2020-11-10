import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Indexer {
    private String documentLocation;
    private Analyzer analyzer;

    public Indexer(String documentLocation) {
        this.documentLocation = documentLocation;
        this.analyzer = new StandardAnalyzer();
    }

    private List<Document> getAllDocumentsFromCranfield() throws IOException {
        List<Document> documents = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(new File(documentLocation)));

        String currentLine;
        while((currentLine = reader.readLine()) != null) {
            System.out.println(currentLine);
        }

        return documents;
    }

    public void indexDocument() throws IOException {
        List<Document> documents = getAllDocumentsFromCranfield();
    }
}
