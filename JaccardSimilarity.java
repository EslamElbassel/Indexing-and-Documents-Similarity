import java.io.*;
import java.util.*;
// Ahmed Mostafa Abdelkarim ID: 20180031
// Eslam Sameh Hamdy   ID: 20180045
// Youssef Sayed Ahmed ID: 20180342
// Mahmoud Essam Eldin  ID: 20180359

//=====================================================================
class Index {
    HashMap<String, HashSet<String>> index = new HashMap<String, HashSet<String>>();  // document name and its words

    //-----------------------------------------------------------------------
    public HashSet<String> union(HashSet<String> pL1, HashSet<String> pL2) {
        HashSet<String> result = new HashSet<String>(pL1);
        result.addAll(pL2);
        return result;
    }

    public HashSet<String> intersect(HashSet<String> pL1, HashSet<String> pL2) {
        HashSet<String> result = new HashSet<String>(pL1);
        result.retainAll(pL2);
        return result;
    }

    private HashSet<String> readDocument(String fileName) {
        HashSet<String> document = new HashSet<String>();
        try (BufferedReader file = new BufferedReader(new FileReader(fileName))) {
            String ln;
            while ((ln = file.readLine()) != null) {
                String[] words = ln.split("\\W+");
                for (String word : words) {
                    document.add(word.toLowerCase());
                }
            }
        } catch (IOException e) {
            System.out.println("File " + fileName + " not found. Skip it");
        }
        return document;
    }

    public void builtIndex(String [] documentsNames) {
        for (String docName : documentsNames) {
            index.put(docName,  readDocument(docName));
        }
    }


    public void calculateSimilarity(String sentence) {
        TreeMap<Float, String> jSimilarities = new TreeMap<Float, String>(Collections.reverseOrder());
        sentence = sentence.trim();   // Removing remove leading and trailing spaces
        System.out.println(sentence);
        HashSet<String> sentenceWords = new HashSet<String>();
        String[] words = sentence.split("\\W+");
        for (String word : words){
            sentenceWords.add(word.toLowerCase());
        }
        for (Map.Entry<String,HashSet<String>> entry : index.entrySet()) {

            HashSet<String> intersection = intersect(sentenceWords, entry.getValue());
            HashSet<String> union = union(sentenceWords, entry.getValue());

            float intersectionNum = intersection.size();
            float unionNum = union.size();
            jSimilarities.put((intersectionNum / unionNum), entry.getKey());
        }
        for (Map.Entry<Float, String> entry : jSimilarities.entrySet()) {
            System.out.println("Document: " + entry.getValue() + "  Similarity: " + entry.getKey());
        }

    }
}

//=====================================================================
public class JaccardSimilarity {
    public static void main(String args[]) throws IOException {
        Index index = new Index();

        index.builtIndex(new String[]{
                "docs\\100.txt",
                "docs\\101.txt",
                "docs\\102.txt",
                "docs\\103.txt",
                "docs\\104.txt"});

        String phrase = "Department of Information Systems";
        index.calculateSimilarity(phrase);
    }
}
