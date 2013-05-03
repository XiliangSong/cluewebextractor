package edu.knowitall.cluewebextractor

import java.util.Scanner
import java.io.File
import java.io.IOException
import java.util.Calendar


object WikiIterator{
	// extends Iterator[Option[WarcRecord]]
  // When found alone, indicates the start of a new WIKI record.
  val wikiType = "WIKI"

  // indicates the field that gives current date
  val wikiDate = Calendar.getInstance().getTime().toString()
  
  //read the XML file and return new warcRecords with the
  //required parameters
  object readFile {
    val scan = new Scanner(new File("text.xml"))
    while(scan.hasNext()){
      val firstLine = scan.nextLine();
      
      val regex = """<doc id="(\S*)" url="(\S*)" title="(.*)">""".r
      val regex(wikiArticleID, wikiUrl, wikiTitle) = firstLine
      
      var payload = ""
      while(!scan.nextLine().equals("</doc>")){
        payload = payload + scan.nextLine();
      }
      new WarcRecord(wikiType, wikiArticleID, wikiDate, wikiUrl, payload)
    }
  }
  
  def main(args: Array[String]){
	println(wikiDate);

  }
}