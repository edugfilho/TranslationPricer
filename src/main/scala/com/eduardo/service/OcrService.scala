package com.eduardo.service

import com.eduardo.model.BaseModel
import com.google.api.services.vision.v1.model.AnnotateImageRequest
import com.google.api.services.vision.v1.model.Feature
import com.google.api.services.vision.v1.model.Image
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.google.api.services.vision.v1.model.EntityAnnotation

/**
 * Created by Eduardo on 2015-11-21.
 */

class OcrService
object OcrService {
  
  val MAX_RESULTS = 1000
  //Improve it to receive map from file to byte, return map file to list[entityAnnotation]
  //TODO: Ideally should return List[string]. EntityAnnotation and other lib classes should be only in VisionApiService
  def performOcr(files: List[Array[Byte]]): List[List[EntityAnnotation]] = {
    val visionService = VisionApiService.get()
    
    def createImgRequest(fileData: Array[Byte]): AnnotateImageRequest = {
      val image = new Image()
      new AnnotateImageRequest()
      .setImage(new Image().encodeContent(fileData))
      .setFeatures(List(new Feature().setType("TEXT_DETECTION").setMaxResults(MAX_RESULTS)).asJava)
    }
    
    val requests = files map { fileData => createImgRequest(fileData) } asJava
    val annotate = visionService.images().annotate(new BatchAnnotateImagesRequest().setRequests(requests))
    annotate.setDisableGZipContent(true)
    //Create a function in order to build annotations along with the path?
    annotate.execute().getResponses.toList.map {  resp => resp.getTextAnnotations.toList  }
  }
}
