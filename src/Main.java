import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



public class Main {

    public static String stripWord(String word) {
        return word.replaceAll("[^a-z]", "");
    }

    public static PositionList<IndexItem> lookup(String item, AVLTree<String, PositionList<IndexItem>> tree) {
        Entry<String, PositionList<IndexItem>> found = tree.find(item);
        if (found != null) {
            return found.getValue();
        }
        return null;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: " + args[0] + " <filename>");
        }

        //construct an AVL tree to act as an index
        AVLTree<String, PositionList<IndexItem>> indexTree = new AVLTree<>();

        try {
            Scanner sc = new Scanner(new File(args[0]));
            NodePositionList<IndexItem> itemList = new NodePositionList<>();
            int lineNumber = 0;
            String ln = "";
            int wordCount = 0;
            while (sc.hasNextLine()) {
            	lineNumber++;
                ln = sc.nextLine();
                String words[] = ln.split(" ");
                for(String word : words)
                {
                	wordCount++;
                	String strip = stripWord(word);
                	strip.toLowerCase();
                	IndexItem item = new IndexItem(wordCount, lineNumber);
                	
                	itemList.addLast(item);
                	indexTree.insert(strip, itemList);
                	
                }
                
            }
            System.out.println(itemList.toString());
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + args[0] + " is not found");
        }

        System.out.println("There are " + indexTree.size() + " unique items in the index.");

        System.out.println("Looking up 'computer'");
        System.out.println("\t" + lookup("computer", indexTree));
        System.out.println("Looking up 'bob'");
        System.out.println("\t" + lookup("bob", indexTree));
        System.out.println("Looking up 'wikipedia'");
        System.out.println("\t" + lookup("wikipedia", indexTree));
        System.out.println("Looking up 'turing'");
        System.out.println("\t" + lookup("turing", indexTree));

    }

}

