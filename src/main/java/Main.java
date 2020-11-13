import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.similarities.*;

import java.io.IOException;

public class Main {
    private static String CRAN_DOC_LOCATION = "cranfield_dataset/cran.all.1400";
    private static String QUERIES_FILE_LOCATION = "cranfield_dataset/cran.qry";
    private static String INDEX_LOCATION = "index";
    private static String OUTPUT_FILE_NAME = "query_results.txt";

    public static void main(String[] args) throws IOException, ParseException {
        Analyzer analyzer;
        Similarity similarity;

        if(args.length == 2) {
            switch(args[0]) {
                case "standard":
                    System.out.println("Using Standard Analyzer");
                    analyzer = new StandardAnalyzer();
                    break;
                case "simple":
                    System.out.println("Using Simple Analyzer");
                    analyzer = new SimpleAnalyzer();
                    break;
                default:
                    System.out.println("Using English Analyzer");
                    analyzer = new EnglishAnalyzer();
            }

            switch(args[1]) {
                case "boolean":
                    System.out.println("Using Boolean Similarity");
                    similarity = new BooleanSimilarity();
                    break;
                case "vector":
                    System.out.println("Using Vector Space Model Similarity");
                    similarity = new ClassicSimilarity();
                    break;
                default:
                    System.out.println("Using BM25 Similarity");
                    similarity = new BM25Similarity();
            }
        } else {
            System.out.println("Using English Analyzer");
            System.out.println("Using BM25 Similarity");
            analyzer = new EnglishAnalyzer();
            similarity = new BM25Similarity();
        }

        Indexer indexer = new Indexer(CRAN_DOC_LOCATION, INDEX_LOCATION, analyzer);
        QuerySolver querySolver = new QuerySolver(QUERIES_FILE_LOCATION, INDEX_LOCATION,
                OUTPUT_FILE_NAME, analyzer, similarity);

        //Indexing the documents
        indexer.indexDocument();
        //Solving the Cranfield queries
        querySolver.buildQueriesResults();
    }
}
