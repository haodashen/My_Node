package code.lsh.index;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexUtil 
{
	private static Directory directory = null ;
	
	public IndexUtil()
	{
		try 
		{
			//保存索引的路径
			directory = FSDirectory.open(new File("E:\\save"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void createIndex()
	{
		try
		{
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47,new StandardAnalyzer(Version.LUCENE_47));
			//创建IndexWriter用于创建索引
			IndexWriter writer = new IndexWriter(directory, config);
			//需要索引的文件
			File dir2 = new File("E:\\searchsource");
			for(File file : dir2.listFiles())
			{
				String fileName = file.getName();
				String fileContent = FileUtils.readFileToString(file, "utf-8");
				Long fileSize = FileUtils.sizeOf(file);
				//
				TextField nameField = new TextField("fileName", fileName, Store.YES) ;
				TextField contentField = new TextField("contentField",fileContent ,Store.YES);
				TextField sizeField = new TextField("sizeField",fileSize.toString(),Store.YES);
				Document doc = new Document();
				doc.add(nameField);
				doc.add(contentField);
				doc.add(sizeField);
				writer.addDocument(doc);
			}
			writer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void search()
	{
		try
		{
			IndexReader reader = DirectoryReader.open(directory);
			IndexSearcher is = new IndexSearcher(reader) ;
			QueryParser parser = new QueryParser(Version.LUCENE_47, "contentField", new StandardAnalyzer(Version.LUCENE_47));
			Query query = parser.parse("java");
			TopDocs docs = is.search(query, 100);
			ScoreDoc[] scoreDocs = docs.scoreDocs;
			for(ScoreDoc doc : scoreDocs)
			{
				Document document = is.doc(doc.doc);
				System.out.println("fileName:"+document.get("fileName"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
		IndexUtil iu = new IndexUtil();
		iu.search();
	}
}
