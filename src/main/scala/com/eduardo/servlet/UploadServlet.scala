package com.eduardo.servlet

import java.util.logging.Logger

import scala.collection.JavaConversions._
import scala.collection.mutable.MutableList

import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.io.IOUtils

import com.eduardo.service.OcrService
import com.eduardo.service.TranslateService
import com.google.appengine.api.blobstore.BlobstoreService
import com.google.appengine.api.blobstore.BlobstoreServiceFactory

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UploadServlet extends HttpServlet {

  val blobstoreService = BlobstoreServiceFactory.getBlobstoreService
  val log = Logger.getLogger(getClass().getName());

  override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    val upload = new ServletFileUpload()
    val iterator = upload.getItemIterator(req)
    val streamList = new MutableList[Array[Byte]]();
    //TODO try to find a scala way to iterate over this. DO I need ServerFileUpload?
    while (iterator.hasNext()) {
      val item = iterator.next()
      if(!item.isFormField()) { 
    	  val stream  = item.openStream()
        streamList.+=(IOUtils.toByteArray(stream))
      }
    }
    try {
        val resultRaw = OcrService.performOcr(streamList.toList);
        //Getting the first element only (contains the whole text). Subsequent elements contain separated words (map over listAnnot if needed)
        val ocrResult = resultRaw.map { listAnnot => listAnnot.head.getDescription }.mkString("\n\n========\n\n ")
        log.info("Resul STR:"+ocrResult);
        if (ocrResult != null) {
          //CharMatcher.WHITESPACE.trimAndCollapseFrom(CharMatcher.ASCII.negate().replaceFrom(strResult, ' '), ' ')
          val translatedResult = TranslateService.doTranslate(ocrResult, Option(null))
          val finalResult = f"ORIGINAL:\n$ocrResult\n\n\nTRANSLATED:\n$translatedResult\n\nNumber of characters: ${ocrResult.size}%d"
          log.info(finalResult)
          resp.getOutputStream.print(finalResult.replaceAll("(\r\n|\r|\n|\n\r)", "<br>"))
        } else {
          resp.getOutputStream.print("Processing error")
        }
      } catch {
        case e: Exception => e.printStackTrace();
      }
  }

}
