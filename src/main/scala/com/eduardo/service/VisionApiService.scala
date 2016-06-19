package com.eduardo.service

import java.util.Properties

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.vision.v1.VisionScopes
import com.google.api.services.vision.v1.Vision
import com.eduardo.util.Resources
import com.google.api.services.storage.Storage

class VisionApiService 

//TODO refactor to become single google service creator (there is repeated code here and in storage)
object VisionApiService {
  var vision: Option[Vision] = None
  
  /**
   * Connects to the Vision API using Application Default Credentials.
   */
  def get(): Vision = {
    
    def buildVision(): Vision = {
      val appName = Resources.getAppName()
      val credential =
        GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all())
      val jsonFactory = JacksonFactory.getDefaultInstance()
      val builtVision = new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
        .setApplicationName(appName)
        .build();
      vision = Option(builtVision)
      vision.get
    }
    vision.getOrElse(buildVision)
  }
  
}