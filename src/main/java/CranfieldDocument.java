//My representation of the Cranfield documents
public class CranfieldDocument {
    private int id;
    private String title;
    private String authors;
    private String bibliography;
    private String text;

    public CranfieldDocument(int id, String title, String authors, String bibliography, String text) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.bibliography = bibliography;
        this.text = text;
    }

    //Extract a document form a string
    public static CranfieldDocument fromString(String documentContent) {
        StringBuilder title = new StringBuilder();
        StringBuilder authors = new StringBuilder();
        StringBuilder bibliography = new StringBuilder();
        StringBuilder text = new StringBuilder();

        int index = 0;
        String[] lines = documentContent.split("\n");
        //The document id is always in the first line
        int documentId = Integer.parseInt(lines[0].split(" ")[1]);

        //I skip the lines, until I get to a line that starts with .T (and I also skip that line)
        while(!lines[index++].startsWith(".T"));

        //While I do not find a line that starts with .A, that means that I am still analyzing the title
        while(!lines[index].startsWith(".A")) {
            title.append(lines[index]).append(" ");
            index++;
        }
        //I skip the line with .A
        index++;

        //While I do not find a line that starts with .B, that means that I am still analyzing the authors
        while(!lines[index].startsWith(".B")) {
            authors.append(lines[index]).append(" ");
            index++;
        }
        //I skip the line with .B
        index++;

        //While I do not find a line that starts with .B, that means that I am still analyzing the bibliography
        while(!lines[index].startsWith(".W")) {
            bibliography.append(lines[index]).append(" ");
            index++;
        }
        //I skip the line with .W
        index++;

        //The remaining lines are the actual content of the document
        while (index < lines.length) {
            text.append(lines[index]).append(" ");
            index++;
        }

        //I return the document found when parsing
        return new CranfieldDocument(documentId,
                Util.sanitize(title.toString()),
                Util.sanitize(authors.toString()),
                Util.sanitize(bibliography.toString()),
                Util.sanitize(text.toString()));
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getBibliography() {
        return bibliography;
    }

    public String getText() {
        return text;
    }
}
