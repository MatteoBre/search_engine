import java.io.IOException;

public class Main {
    private static String CRAN_DOC_LOCATION = "cranfield_dataset/cran.all.1400";
    private static String QUERIES_FILE_LOCATION = "cranfield_dataset/cran.qry";
    private static String INDEX_LOCATION = "index";
    private static String OUTPUT_FILE_NAME = "query_results.txt";

    public static void main(String[] args) throws IOException {
        Indexer indexer = new Indexer(CRAN_DOC_LOCATION, INDEX_LOCATION);
        QuerySolver querySolver = new QuerySolver(QUERIES_FILE_LOCATION, INDEX_LOCATION, OUTPUT_FILE_NAME);

        indexer.indexDocument();
        querySolver.buildQueriesResults();
    }
}
