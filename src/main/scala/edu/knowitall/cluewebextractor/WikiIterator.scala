package edu.knowitall.cluewebextractor

import java.util.Scanner
import java.util.Calendar
import java.io.InputStream

/**
 *  Yue Wang, 2013
 */
class WikiIterator(is: InputStream) extends Iterator[Option[WarcRecord]] {
  // indicates the field of the response type
  val wikiType = "response"

  // indicates the field that gives current date
  val wikiDate = Calendar.getInstance().getTime().toString()

  // create a new scanner taking in a inputStream
  val scan = new Scanner(is)

  // overrides Iterator's hasNext method
  def hasNext(): Boolean = {
    scan.hasNext()
  }

  // overrides next() method, returns a warcRecord containing 
  // information of the wiki doc
  def next(): Option[WarcRecord] = {
    if (!hasNext) {
      throw new NoSuchElementException()
    }
    val firstLine = scan.nextLine()

    val regex = """<doc id="(\S*)" url="(\S*)" title="(.*)">""".r
    val regex(wikiArticleID, wikiUrl, wikiTitle) = firstLine

    var EndOfDoc = false
    var payload = ""

    while (!EndOfDoc) {
      val line = scan.nextLine()
      if (line.equals("</doc>"))
        EndOfDoc = true
      else
        payload = payload + line
    }

    Some(new WarcRecord(wikiType, wikiArticleID, wikiDate, wikiUrl, payload))
  }

}