package javafxexercise;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

public class SimpleSearcher {
       
    public static ObservableList<MyDocument> search(String path, String token){
        File indexDir = new File(path);
        String query =  token;
        int hits = 100;
        
        SimpleSearcher searcher = new SimpleSearcher();
        try {
           return searcher.searchIndex(indexDir, query, hits);
        } catch (Exception e) {
            return null;
        }
    }
    
    private static ObservableList<MyDocument> searchIndex(File indexDir, String queryStr, int maxHits) throws Exception {
        
        //Directory directory = FSDirectory.open(indexDir);
        Directory directory = NIOFSDirectory.open(indexDir);
        IndexReader reader = IndexReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser(Version.LUCENE_44, "content", new SimpleAnalyzer(Version.LUCENE_44));
        Query query = parser.parse(queryStr);
        
        TopDocs topDocs = searcher.search(query, maxHits);
        
        ScoreDoc[] hits = topDocs.scoreDocs;
        ObservableList<MyDocument> list = FXCollections.observableArrayList();
        
        for (int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            Document doc = searcher.doc(docId);
            System.out.println(doc.get("fullpath"));
       
            MyDocument simpleDoc = new MyDocument(
                                        false,
                                        doc.get("fullpath"),
                                        doc.get("filename"),
                                        doc.get("keywords"),
                                        doc.get("content"));
            list.add(simpleDoc);
        }
        System.out.println("Found " + hits.length);
        
        return list;
    }
    
//    public List<Product> searchProductByName(String query) throws ParseException, IOException {
//
//        Analyzer analyzer = new ChineseAnalyzer(Version.LUCENE_41);
//        QueryParser parser = new QueryParser(Version.LUCENE_35, "name", analyzer);
//        Query search = parser.parse(query);
//
//        ScoreDoc[] hits = indexSearcherProduct.search(search, null, MAX_DOC).scoreDocs;
//        List<SimpleDocument> lst = new ArrayList<SimpleDocument>();
//        for (int i = 0; i < hits.length; i++) {
//            Document doc = indexSearcherProduct.doc(hits[i].doc);
//            lst.add(productMapper.mapDoc(doc));
//        }
//        return lst;
//    }

    
//    private List<Document> searchDocument(final Query query) throws Exception {
//
//	final IndexSearcher searcher = getSearcher();
//	final TopScoreDocCollector collector = TopScoreDocCollector.create(limit, true);
//	searcher.search(query, collector);
//	final ScoreDoc[] hits = collector.topDocs().scoreDocs;
//	final int size = Math.min(hits.length, limit);
//	log.debug("hits size : {}", size);
//	final List<Document> list = new ArrayList<Document>(size);
//	for (int k = 0; k < size; k++) {
//		final int index = hits[k].doc;
//		final Document doc = searcher.doc(index);
//		list.add(doc);
//	}
//	return list;
//    }
    
    
//The 'filename' field is the actual Word 2007 file name.
//
//The 'title' field is the actual document title.
//
//The 'subject' field is the actual document subject.
//
//The 'creator' field is the actual document creator.
//
//The 'keywords' field contains the actual document keywords.
//
//The 'description' field is the actual document description.
//
//The 'lastModifiedBy' field is the username who has last modified the actual document.
//
//The 'revision' field is the actual document revision number.
//
//The 'modified' field is the actual document last modified date / time.
//
//The 'created' field is the actual document creation date / time.
//
//The 'body' field is the actual body content of the Word 2007 document. It only includes normal text, comments and revisions are not included. 
}

