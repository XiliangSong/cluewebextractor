package edu.knowitall.cluewebextractor

import java.util.Scanner
import java.util.Calendar
import java.io.InputStream

//import java.io.FileReader
//import java.io.IOException

class WikiIterator(is: InputStream) extends Iterator[Option[WarcRecord]] {
  val wikiType = "response"

  // indicates the field that gives current date
  val wikiDate = Calendar.getInstance().getTime().toString()
  
  val scan = new Scanner(is)
  
  def hasNext(): Boolean = {
	scan.hasNext()
  }
  
  def next(): Option[WarcRecord] = {
    if(!hasNext) {
      throw new NoSuchElementException()
    }
    val firstLine = scan.nextLine()
     
    val regex = """<doc id="(\S*)" url="(\S*)" title="(.*)">""".r
	val regex(wikiArticleID, wikiUrl, wikiTitle) = firstLine
	  
	var notEndOfDoc = true
	var payload = ""
	  
	while(notEndOfDoc){
	  val line = scan.nextLine()
	  if(line.equals("</doc>"))
	    notEndOfDoc = false
	  else
	    payload = payload + line
	}
    
    new Some(new WarcRecord(wikiType, wikiArticleID, wikiDate, wikiUrl, payload))
  }

}

//object WikiIterator{
//  def main(args :Array[String]){
//    val scan = new Scanner(new FileReader("test.xml"))
//    if (!scan.hasNext())
//      throw new NoSuchElementException()
//    println(scan.nextLine())
//  }
//}