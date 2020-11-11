import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Indexer {
    private String documentLocation;
    private String indexLocation;
    private Analyzer analyzer;

    public Indexer(String documentLocation, String indexLocation) {
        this.documentLocation = documentLocation;
        this.indexLocation = indexLocation;
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
        // Set up an index writer to add process and save documents to the index
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        Directory indexDirectory = FSDirectory.open(Paths.get(indexLocation));
        IndexWriter iwriter = new IndexWriter(indexDirectory, config);

        List<Document> documents = getAllDocumentsFromCranfield();

        iwriter.addDocuments(documents);
        iwriter.close();
    }
}
