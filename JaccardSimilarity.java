import java.io.*;
import java.util.*;

//=====================================================================
class Index {
    HashMap<String, HashSet<String>> index = new HashMap<String, HashSet<String>>();  // document name and its words

    //-----------------------------------------------------------------------
    // calculating the union (OR) between two sets (sets of terms in a document)
    public HashSet<String> union(HashSet<String> pL1, HashSet<String> pL2) {
        HashSet<String> result = new HashSet<String>(pL1);
        result.addAll(pL2);
        return result;
    }
    
    // calculating the intersection (AND) between two sets (sets of terms in a document)
    public HashSet<String> intersect(HashSet<String> pL1, HashSet<String> pL2) {
        HashSet<String> result = new HashSet<String>(pL1);
        result.retainAll(pL2);
        return result;
    }
    
    // utility function for reading file and preprocessing it before building the Index
    private HashSet<String> readDocument(String fileName) {
        HashSet<String> document = new HashSet<String>();  // set of the words in the document 
        try (BufferedReader file = new BufferedReader(new FileReader(fileName))) {
            String ln;
            while ((ln = file.readLine()) != null) {
                String[] words = ln.split("\\W+");      // toknize each line using white space 
                
                for (String word : words) {
                    document.add(word.toLowerCase());   // converting words to lowecase before adding them into the set
                }
            }
        } catch (IOException e) {
            System.out.println("File " + fileName + " not found. Skip it");  // displaying error messege if there's no file with this name
        }
        return document;  
    }

    // building index (document_name -> {set of its words})
    public void builtIndex(String [] documentsNames) {  
        for (String docName : documentsNames) {
            index.put(docName,  readDocument(docName));
        }
    }

    // calculating jaccard similarity between two documents (sentence, one of our saved documents)
    public void calculateSimilarity(String sentence) {
        
        // treemap of (key: similarity score in descending order, value: docName)
        TreeMap<Float, String> jSimilarities = new TreeMap<Float, String>(Collections.reverseOrder()); 
        
        sentence = sentence.trim();                 // Removing leading and trailing spaces
        System.out.println(sentence);
        HashSet<String> sentenceWords = new HashSet<String>();
        String[] words = sentence.split("\\W+");    // toknize the search query (sentence) using white space 
        for (String word : words){
            sentenceWords.add(word.toLowerCase());  // adding sentence words to a set 
        }
        
        for (Map.Entry<String,HashSet<String>> entry : index.entrySet()) {

            HashSet<String> intersection = intersect(sentenceWords, entry.getValue());  // Document1(sentence) AND Document2(one of documents)
            HashSet<String> union = union(sentenceWords, entry.getValue());             // Document1(sentence) OR Document2(one of documents)

            float intersectionNum = intersection.size();    // number of intersection words 
            float unionNum = union.size();                  // number of union words 
            jSimilarities.put((intersectionNum / unionNum), entry.getKey());    //calculating jaccard similarity score
        }
        
        for (Map.Entry<Float, String> entry : jSimilarities.entrySet()) {     // displying the similarity scores of documents orderd descending
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
                "docs\\101.txt",        // here is an array of documents names change it with yours 
                "docs\\102.txt",
                "docs\\103.txt",
                "docs\\104.txt"});

        String phrase = "Department of Information Systems";
        index.calculateSimilarity(phrase);
    }
}
