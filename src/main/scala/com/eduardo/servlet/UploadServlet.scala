package com.eduardo.servlet

import java.io.FileOutputStream
import javax.servlet.http.{ HttpServletResponse, HttpServletRequest, HttpServlet }
import scala.collection.JavaConversions._
import com.eduardo.util.Resources
import com.google.appengine.api.blobstore.{ BlobstoreService, BlobKey, BlobstoreServiceFactory }
import com.google.common.base.CharMatcher
import com.eduardo.service.OcrService
import java.util.logging.Logger
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.io.IOUtils
import scala.collection.mutable.MutableList

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
        val strResult = resultRaw.map { listAnnot => listAnnot.head.getDescription }.mkString("\n\n========\n\n ")
        log.info("Resul STR:"+strResult);
        if (strResult != null) {
          val result = CharMatcher.WHITESPACE.trimAndCollapseFrom(
            CharMatcher.ASCII.negate().replaceFrom(strResult, ' '),
            ' ')
          resp.getOutputStream.print(f"$result \n=============\n\nNumber of characters: ${result.size}%d")
        } else {
          resp.getOutputStream.print("Processing error")
        }
      } catch {
        case e: Exception => e.printStackTrace();
      }
  }

}
