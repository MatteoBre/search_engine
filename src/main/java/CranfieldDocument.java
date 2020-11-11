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

    public static CranfieldDocument fromString(String documentContent) {
        StringBuilder title = new StringBuilder();
        StringBuilder authors = new StringBuilder();
        StringBuilder bibliography = new StringBuilder();
        StringBuilder text = new StringBuilder();

        int index = 0;
        String[] lines = documentContent.split("\n");
        int documentId = Integer.parseInt(lines[0].split(" ")[1]);

        while(!lines[index++].startsWith(".T"));

        while(!lines[index].startsWith(".A")) {
            title.append(lines[index]).append(" ");
            index++;
        }
        index++;

        while(!lines[index].startsWith(".B")) {
            authors.append(lines[index]).append(" ");
            index++;
        }
        index++;

        while(!lines[index].startsWith(".W")) {
            bibliography.append(lines[index]).append(" ");
            index++;
        }
        index++;

        while (index < lines.length) {
            text.append(lines[index]).append(" ");
            index++;
        }

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
