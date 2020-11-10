public class CranfieldDocument {
    private String title;
    private String authors;
    private String bibliography;
    private String text;

    public CranfieldDocument(String title, String authors, String bibliography, String text) {
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

        return new CranfieldDocument(title.toString().trim(),
                authors.toString().trim(),
                bibliography.toString().trim(),
                text.toString().trim());
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
