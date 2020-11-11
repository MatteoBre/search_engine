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

    // I use this function to remove everything that is not alphanumeric (and trim the result)
    private String sanitize(String str) {
        StringBuilder result = new StringBuilder();

        char current;
        for(int i=0; i<str.length(); i++) {
            current = str.charAt(i);
            if(!Character.isLetterOrDigit(current) && current != ' ') {
                // Useful in cases like "word1(word2" -> "word1 word2"
                // Without result.append(' '); it would be -> "word1word2", not what we want
                result.append(' ');
                continue;
            }

            result.append(current);
        }

        return result.toString().trim();
    }

    private List<String> getQueries() throws IOException {
        List<String> queries = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(new File(queriesFileLocation)));

        String currentLine;
        StringBuilder currentQuery = new StringBuilder();

        while((currentLine = reader.readLine()) != null) {
            if(currentLine.startsWith(".I")) {
                if(!currentQuery.toString().equals("")) {
                    queries.add(sanitize(currentQuery.toString()));
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
            queries.add(sanitize(currentQuery.toString()));
        }

        return queries;
    }

    public void buildQueriesResults() throws IOException {
        List<String> queries = getQueries();
    }
}
