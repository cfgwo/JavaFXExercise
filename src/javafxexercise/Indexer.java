/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxexercise;

/*The following code snippet uses APACHE LUCENE 3.4.0*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * This class will build index of the source location specified into the
 * destination specified.This class will only index txt files.
 *
 * @author Mubin Shrestha
 */
public class Indexer {
    private IndexWriter indexWriter = null;
    private String fileContent;  //temp storer of all the text parsed from doc and pdf 
    public void indexer(){
    }
    
    public void index(String indexDirectory, String dataDirectory) throws FileNotFoundException, CorruptIndexException, IOException {
        try {
            long start = System.currentTimeMillis();
            File indexDir = new File(indexDirectory);
            File dataDir  = new File(dataDirectory);
            //File dataDir = new File("C:/AndroidProgramming");
            String suffix = "java";
            
            int numIndex =  index(indexDir, dataDir, suffix);
            //checkFileValidity();
          
            long end = System.currentTimeMillis();
            System.out.println("Total Document Indexed : " + numIndex);
            System.out.println("Total time" + (end - start) / (100 * 60));
        } catch (Exception e) {
            System.out.println("Sorry task cannot be completed");
        }
    }

    /**
     * IndexWriter writes the data to the index. Its provided by Lucene
     *
     * @param analyzer : its a standard analyzer, in this case it filters out
     * englishStopWords and also analyses TFIDF
     */
    private int index(File indexDir, File dataDir, String suffix) throws Exception {
            // setup index folder
            if (!indexDir.exists()) {
                indexDir.mkdir();
            }
            FSDirectory dir = FSDirectory.open(indexDir);
            SimpleAnalyzer analyzer = new SimpleAnalyzer(Version.LUCENE_44);
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
            indexWriter = new IndexWriter(dir, config);
            
            // index Directory
            indexDirectory(indexWriter, dataDir, suffix);
            
            int numIndexed = indexWriter.maxDoc();
            //indexWriter.optimize();
            indexWriter.close();
            return numIndexed;
    }
    
    private void indexDirectory(IndexWriter indexWriter, File dataDir, String suffix) throws IOException {
        File[] files = dataDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory() ) {
                if(!f.isHidden()){
                    indexDirectory(indexWriter, f, suffix);
                }
            }
            else {
                indexFileWithIndexWriter(indexWriter, f, suffix);
            }
        }
    }

    /**
     * This function checks whenther the file passed is valid or not
     */
    private void indexFileWithIndexWriter(IndexWriter indexWriter, File file, String suffix) throws IOException { 
            //to check whenther the file is a readable file or not.
        if (file.isHidden() || file.isDirectory() || !file.canRead() || !file.exists()) {
            return;
        }
//        if (suffix!=null && !file.getName().endsWith(suffix)) {
//            return;
//        }
        System.out.println("Indexing file " + file.getCanonicalPath());
        
        if(file.getName().endsWith(".txt") || file.getName().endsWith(".java")){
            indexTextFiles(file);//if the file text file no need to parse text. 
            System.out.println("getCanonicalPath " + file.getCanonicalPath());
        }
        else if(file.getName().endsWith(".doc") || file.getName().endsWith(".pdf")){
            //different methof for indexing doc and pdf file.
            //StartIndex(file);      
        }
    }
    
    /**
     * This method is for indexing pdf file and doc file.
     * The text parsed from them are indexed along with the filename and filepath
     * @param file : the file which you want to index
     * @throws FileNotFoundException
     * @throws CorruptIndexException
     * @throws IOException 
     */
    public void StartIndex(File file) throws FileNotFoundException, CorruptIndexException, IOException {
            System.out.println("INDEXED pdf or doc " + file.getAbsolutePath() + " :-) ");
            fileContent = null;
            Document doc = new Document();
            if (file.getName().endsWith(".doc")) {
                //call the doc file parser and get the content of doc file in txt format
                //fileContent = new DocFileParser().DocFileContentParser(file.getAbsolutePath());
            }
            if (file.getName().endsWith(".pdf")) {
                //call the pdf file parser and get the content of pdf file in txt format
                //fileContent = new PdfFileParser().PdfFileParser(file.getAbsolutePath());
            }
            doc.add(new Field("content", fileContent, Field.Store.YES, Field.Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS));
            doc.add(new Field("filename", file.getName(),Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field("fullpath", file.getCanonicalPath(), Field.Store.YES, Field.Index.ANALYZED));
           
            indexWriter.addDocument(doc);
    }

    /**
     *
     * @param file
     * @throws CorruptIndexException
     * @throws IOException
     */
    private void indexTextFiles(File file) throws CorruptIndexException, IOException {
        
        System.out.println("indexTextFiles");
        Document doc = new Document();
        Map<String, String> data = indexWriter.getCommitData();
        doc.add(new Field("content", new FileReader(file)));
        doc.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("fullpath", file.getCanonicalPath(), Field.Store.YES, Field.Index.ANALYZED));
        System.out.println("file.getName():"+file.getName());
        System.out.println("file.fullpath():"+file.getCanonicalPath());
        
        indexWriter.addDocument(doc);
    }

//    public static void main(String args[]) {
//        try {
//            new Indexer();
//        } catch (Exception ex) {
//            System.out.println("Cannot Start :(");
//        }
//    }
}
