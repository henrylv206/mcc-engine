package org.apache.lucene.demo;

import java.io.File;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexFiles {
	private IndexFiles() {}
	
	public static void main(String[] args) {
		String usage = "java org.apache.lucene.demo.IndexFiles" +
				" [-index INDEX_PATH] [-docs DOCS_PATH] [-update] \n\n" +
				" This indexes the documents in DOCS_PATH, create a lucene index" +
				"in INDEX_PATH that can be searched with SearchFiles";
		
		String indexPath = "index";
		String docsPath = null;
		boolean create = true;
		for (int i = 0; i < args.length; i++) {
			if ("-index".equals(args[i])) {
				indexPath = args[i+1];
				i++;
			} else if ("-docs".equals(args[i])) {
				docsPath = args[i+1];
				i++;
			} else if ("-update".equals(args[i])) {
				create = false;
			}
		}
		
		if (docsPath == null) {
			System.out.println("Usage:" + usage);
			System.exit(1);
		}
		
		final File docDir = new File(docsPath);
		if (!docDir.exists() || !docDir.canRead()) {
			System.out.println("Document directory '" + docDir.getAbsolutePath() + "' does not exist or is not readable, please check the path");
			System.exit(1);
		}
		
		Date start = new Date();
		try {
			System.out.println("Indexing to directory' " + indexPath + "'...");
			
			Directory dir = FSDirectory.open(new File(indexPath));
			
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
			IndexWriterConfig iwc  = new IndexWriterConfig(Version.LUCENE_40, analyzer);
			
			if (create) {
				iwc.setOpenMode(OpenMode.CREATE);
			} else {
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}
			
			IndexWriter writer = new IndexWriter(dir, iwc);
			indexDocs(writer, docDir);
		}
	}
}
