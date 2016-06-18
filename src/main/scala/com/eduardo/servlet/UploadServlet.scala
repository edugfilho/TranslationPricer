package com.eduardo.servlet

import java.io.FileOutputStream
import javax.servlet.http.{ HttpServletResponse, HttpServletRequest, HttpServlet }

import com.eduardo.util.Resources
import com.google.appengine.api.blobstore.{ BlobstoreService, BlobKey, BlobstoreServiceFactory }
import com.google.common.base.CharMatcher
import com.eduardo.service.OcrService
import com.eduardo.servlet.UploadServlet
import java.util.logging.Logger

class UploadServlet extends HttpServlet {

  val blobstoreService = BlobstoreServiceFactory.getBlobstoreService
  val log = Logger.getLogger(getClass().getName());
  
  //TODO Deploy local to debug
  override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    val reason = req.getParameter("reason")
    if (reason != null && reason.equals("getUrl")) {
      val out = resp.getOutputStream
      out.print(blobstoreService.createUploadUrl("/upload"))
    } else {
      val blobKey = new BlobKey(req.getParameter("blob-key"));

      val fileContent = blobstoreService.fetchData(blobKey, 0, BlobstoreService.MAX_BLOB_FETCH_SIZE - 1);
      try {
        val result = OcrService.performOcr(List(fileContent));
        log.info("Result:"+result);
        val strResult = result.map { listAnnot => listAnnot.map { annot => annot.getDescription }.mkString(" ") }.mkString("\n\n========\n\n ")
        if (strResult != null) {
          val result = CharMatcher.WHITESPACE.trimAndCollapseFrom(
            CharMatcher.JAVA_LETTER_OR_DIGIT.negate().replaceFrom(strResult, ' '),
            ' ')
          resp.getOutputStream.print(f"$result =============/n\nNumber of characters: ${result.size}%d")
        } else {
          resp.getOutputStream.print("Processing error")
        }
      } catch {
        case e: Exception => e.printStackTrace();
      }

    }
  }

  override def doPost(req: HttpServletRequest, resp: HttpServletResponse): Unit = {

    val blobs = blobstoreService.getUploads(req)
    //TODO Wrap in option
    val blobKeys = blobs.get("myFile")

    if (blobKeys == null || blobKeys.isEmpty()) {
      resp.sendRedirect("/");
    } else {
      resp.sendRedirect("/upload?blob-key=" + blobKeys.get(0).getKeyString());
    }
  }

}
