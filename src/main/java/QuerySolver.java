import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QuerySolver {
    private String queriesFileLocation;
    private String indexLocation;
    private String outputFileName;
    private Analyzer analyzer;
    private Similarity similarity;
    private QueryParser queryParser;

    public QuerySolver(String queriesFileLocation,
                       String indexLocation,
                       String outputFileName,
                       Analyzer analyzer,
                       Similarity similarity) {
        this.queriesFileLocation = queriesFileLocation;
        this.indexLocation = indexLocation;
        this.outputFileName = outputFileName;
        this.analyzer = analyzer;
        this.similarity = similarity;
        this.queryParser = new MultiFieldQueryParser(new String[]{"title", "author", "bibliography", "content"}, this.analyzer);
    }

    //I parse the Cranfield file to get the queries
    private List<String> getQueries() throws IOException {
        List<String> queries = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(new File(queriesFileLocation)));

        String currentLine;
        StringBuilder currentQuery = new StringBuilder();

        while((currentLine = reader.readLine()) != null) {
            //.I means that we are in the beginning of a new query
            if(currentLine.startsWith(".I")) {
                if(!currentQuery.toString().equals("")) {
                    queries.add(Util.sanitize(currentQuery.toString()));
                }

                //Remove the content of the previous query, so that I can process the new one
                currentQuery.setLength(0);
                // I am skipping the lines with .I as they do not contain information
                continue;
            }
            // I am skipping the lines with .W as they do not contain information
            if(currentLine.startsWith(".W")) {
                continue;
            }

            currentQuery.append(currentLine).append("\n");
        }

        // Adding the last query
        if(!currentQuery.toString().equals("")) {
            queries.add(Util.sanitize(currentQuery.toString()));
        }

        return queries;
    }

    //I execute a query in this function
    private List<DocumentPair> executeQuery(String query, IndexSearcher isearcher) throws IOException, ParseException {
        List<DocumentPair> results = new ArrayList<>();

        //This is the list of documents and associated scores I get from the query
        ScoreDoc[] hits = isearcher.search(queryParser.parse(QueryParser.escape(query)), 1400).scoreDocs;

        for (int i = 0; i < hits.length; i++)
        {
            //I add the pair Document + Score to the list
            results.add(new DocumentPair(isearcher.doc(hits[i].doc), hits[i].score));
        }

        return results;
    }

    //In this function we execute the queries and write the results in the output file
    public void buildQueriesResults() throws IOException, ParseException {
        FileWriter queryResultsWriter = new FileWriter(outputFileName);
        Directory indexDirectory = FSDirectory.open(Paths.get(indexLocation));

        DirectoryReader ireader = DirectoryReader.open(indexDirectory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        isearcher.setSimilarity(similarity);

        //I parse the file and I get all the queries
        List<String> queriesText = getQueries();

        List<DocumentPair> results;
        for(int i=0; i<queriesText.size(); i++) {
            //I get the results of a single query
            results = executeQuery(queriesText.get(i), isearcher);

            //I write the results obtained to the output file
            for(int j=0; j<results.size(); j++) {
                queryResultsWriter.write((i+1) + " 0 " + results.get(j).getDocument().get("id")
                        + " " + j + " " + results.get(j).getScore() + " Matteo\n");
            }
        }

        ireader.close();
        indexDirectory.close();
        queryResultsWriter.close();
    }
}

class DocumentPair {
    private Document document;
    private double score;

    public DocumentPair(Document document, double score) {
        this.document = document;
        this.score = score;
    }

    public Document getDocument() {
        return document;
    }

    public double getScore() {
        return score;
    }
}
