package com.eduardo.service

import java.util.Properties

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.vision.v1.VisionScopes
import com.google.api.services.vision.v1.Vision

class VisionApiService 


object VisionApiService {
  /**
   * Connects to the Vision API using Application Default Credentials.
   */
  def get(): Vision = {
    val nameResourceIs = getClass.getResourceAsStream("my.properties")
    val p = new Properties()
    p.load(nameResourceIs)
    val appName: String = p getProperty "app.name"
    val credential =
      GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all())
    val jsonFactory = JacksonFactory.getDefaultInstance()
    new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
      .setApplicationName(appName)
      .build();
  }
  
}