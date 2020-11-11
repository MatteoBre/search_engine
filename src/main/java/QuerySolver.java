import java.io.*;
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

    public void buildQueriesResults() throws IOException {
        List<String> queries = getQueries();
    }
}
