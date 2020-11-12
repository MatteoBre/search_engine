import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//This class is used to index all the 1400 Cranfield Documents
public class Indexer {
    private String documentLocation;
    private String indexLocation;
    private Analyzer analyzer;

    public Indexer(String documentLocation, String indexLocation, Analyzer analyzer) {
        this.documentLocation = documentLocation;
        this.indexLocation = indexLocation;
        this.analyzer = analyzer;
    }

    private List<Document> getAllDocumentsFromCranfield() throws IOException {
        List<CranfieldDocument> cranfieldDocuments = new ArrayList<>();
        List<Document> documents = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(new File(documentLocation)));

        String currentLine;
        StringBuilder currentDoc = new StringBuilder();

        //While loop that ends when we run out of lines in the Cranfield file
        while((currentLine = reader.readLine()) != null) {
            //If a line starts with .I, that means that this is the beginning of a document
            if(currentLine.startsWith(".I")) {
                //Not adding blank documents to the list of docs
                if(!currentDoc.toString().equals("")) {
                    //Parsing the content of the document to find a structured document (title, author, etc...)
                    cranfieldDocuments.add(CranfieldDocument.fromString(currentDoc.toString()));
                }

                //Remove the content of the previous documents, so that I can process the new one
                currentDoc.setLength(0);
            }

            //Adding a line to the document
            currentDoc.append(currentLine).append("\n");
        }

        // Adding the last document
        if(!currentDoc.toString().equals("")) {
            cranfieldDocuments.add(CranfieldDocument.fromString(currentDoc.toString()));
        }

        Document currentLuceneDocument;
        //For each document, create a Lucene document, and add the relevant features
        for(CranfieldDocument doc : cranfieldDocuments) {
            currentLuceneDocument = new Document();
            currentLuceneDocument.add(new StringField("id", ""+doc.getId(), Field.Store.YES));
            currentLuceneDocument.add(new TextField("title", doc.getTitle(), Field.Store.YES));
            currentLuceneDocument.add(new TextField("author", doc.getAuthors(), Field.Store.YES));
            currentLuceneDocument.add(new TextField("bibliography", doc.getBibliography(), Field.Store.YES));
            currentLuceneDocument.add(new TextField("content", doc.getText(), Field.Store.YES));
            documents.add(currentLuceneDocument);
        }
        
        return documents;
    }

    public void indexDocument() throws IOException {
        //Set up an index writer to add process and save documents to the index
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        //Setting the index directory
        Directory indexDirectory = FSDirectory.open(Paths.get(indexLocation));
        IndexWriter iwriter = new IndexWriter(indexDirectory, config);

        //Getting all documents from the Cranfield File
        List<Document> documents = getAllDocumentsFromCranfield();

        //Adding all the documents to the index
        iwriter.addDocuments(documents);
        iwriter.close();
    }
}
