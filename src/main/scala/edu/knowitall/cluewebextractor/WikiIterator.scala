package edu.knowitall.cluewebextractor

import java.util.Scanner
import java.io.File
import java.io.IOException
import java.util.Calendar
import java.io.InputStream


class WikiIterator(is: InputStream){
  val wikiType = "WIKI"

  // indicates the field that gives current date
  val wikiDate = Calendar.getInstance().getTime().toString()
  
  readFile
  
  //read the XML file and return new warcRecords
  def readFile{
    val scan = new Scanner(is)
    while(scan.hasNext()){
      val firstLine = scan.nextLine();
      
      val regex = """<doc id="(\S*)" url="(\S*)" title="(.*)">""".r
      val regex(wikiArticleID, wikiUrl, wikiTitle) = firstLine
      
      var payload = ""
      while(!scan.nextLine().equals("</doc>")){
        payload = payload + scan.nextLine();
      }
      new Some(new WarcRecord(wikiType, wikiArticleID, wikiDate, wikiUrl, payload))
    }
  }
}