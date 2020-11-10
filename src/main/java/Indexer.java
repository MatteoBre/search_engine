import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

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
        List<CranfieldDocument> cranfieldDocuments = new ArrayList<>();
        List<Document> documents = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(new File(documentLocation)));

        String currentLine;
        StringBuilder currentDoc = new StringBuilder();

        while((currentLine = reader.readLine()) != null) {
            if(currentLine.startsWith(".I")) {
                if(!currentDoc.toString().equals("")) {
                    cranfieldDocuments.add(CranfieldDocument.fromString(currentDoc.toString()));
                }

                currentDoc.setLength(0);
            }

            currentDoc.append(currentLine).append("\n");
        }

        // Adding the last document
        if(!currentDoc.toString().equals("")) {
            cranfieldDocuments.add(CranfieldDocument.fromString(currentDoc.toString()));
        }

        Document currentLuceneDocument;
        for(CranfieldDocument doc : cranfieldDocuments) {
            currentLuceneDocument = new Document();
            currentLuceneDocument.add(new TextField("content", doc.getText(), Field.Store.YES));
            documents.add(currentLuceneDocument);
        }
        
        return documents;
    }

    public void indexDocument() throws IOException {
        List<Document> documents = getAllDocumentsFromCranfield();
    }
}
