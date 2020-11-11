import java.io.IOException;

public class Main {
    private static String CRAN_DOC_LOCATION = "cranfield_dataset/cran.all.1400";
    private static String INDEX_LOCATION = "index";

    public static void main(String[] args) throws IOException {
        Indexer indexer = new Indexer(CRAN_DOC_LOCATION, INDEX_LOCATION);

        indexer.indexDocument();
    }
}
