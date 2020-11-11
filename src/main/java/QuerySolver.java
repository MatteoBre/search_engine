import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QuerySolver {
    private String queriesFileLocation;
    private String indexLocation;

    public QuerySolver(String queriesFileLocation, String indexLocation) {
        this.queriesFileLocation = queriesFileLocation;
        this.indexLocation = indexLocation;
    }

    private List<String> getQueries() throws IOException {
        List<String> queries = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(new File(queriesFileLocation)));

        String currentLine;
        StringBuilder currentQuery = new StringBuilder();

        while((currentLine = reader.readLine()) != null) {
            if(currentLine.startsWith(".I")) {
                if(!currentQuery.toString().equals("")) {
                    queries.add(Util.sanitize(currentQuery.toString()));
                }

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

    private BooleanQuery.Builder buildQueryFromQueryText(String queryText) {
        BooleanQuery.Builder query = new BooleanQuery.Builder();
        String[] words = queryText.split(" ");

        Query term;
        for(String word : words) {
            if(word.equals(""))
                continue;

            term = new TermQuery(new Term("content", word));
            query.add(new BooleanClause(term, BooleanClause.Occur.SHOULD));
        }

        return query;
    }

    private List<BooleanQuery.Builder> buildQueries(List<String> queriesText) {
        List<BooleanQuery.Builder> queries = new ArrayList<>();

        for(String queryText : queriesText) {
            queries.add(buildQueryFromQueryText(queryText));
        }

        return queries;
    }

    private List<String> executeQuery(BooleanQuery.Builder query, IndexSearcher isearcher) throws IOException {
        List<String> results = new ArrayList<>();
        ScoreDoc[] hits = isearcher.search(query.build(), 20).scoreDocs;

        System.out.println("Documents: " + hits.length);
        for (int i = 0; i < hits.length; i++)
        {
            Document hitDoc = isearcher.doc(hits[i].doc);
            System.out.println(i + ") " + hitDoc.get("id") + " " + hits[i].score);
        }

        return results;
    }

    public void buildQueriesResults() throws IOException {
        Directory indexDirectory = FSDirectory.open(Paths.get(indexLocation));

        DirectoryReader ireader = DirectoryReader.open(indexDirectory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        List<String> queriesText = getQueries();
        List<BooleanQuery.Builder> queries = buildQueries(queriesText);

        for(BooleanQuery.Builder query : queries) {
            executeQuery(query, isearcher);
        }

        ireader.close();
        indexDirectory.close();
    }
}
