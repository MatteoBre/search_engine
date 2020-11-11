public class Util {
    // I use this function to remove everything that is not alphanumeric (and trim the result)
    public static String sanitize(String str) {
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
}
