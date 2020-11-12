public class Util {
    // I use this function to remove everything that is not alphanumeric (and trim the result)
    //This is useful to remove elements that we do not need while searching documents
    public static String sanitize(String str) {
        StringBuilder result = new StringBuilder();

        char current;
        //In this loop I process the string and create the new one
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
