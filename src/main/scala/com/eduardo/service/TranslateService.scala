package com.eduardo.service

import java.util.logging.Logger

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import com.eduardo.request.DummyHttpInitializer
import com.eduardo.util.Resources
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.translate.Translate


class TranslateService

object TranslateService {
  val log = Logger.getLogger(getClass().getName());
  var translate: Option[Translate] = None

  def getTranslate(): Translate = {
    def buildTranslate(): Translate = {
      val appName = Resources.getAppName()
      val builtTranslate = new Translate.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), new DummyHttpInitializer())
        .setApplicationName(appName)
        .build();
      translate = Option(builtTranslate)
      translate.get
    }
    translate.getOrElse(buildTranslate)
  }

  //Translate with auto detection in case no source provided
  def doTranslate(text: String, source: Option[String]): String = {
    try {
      val translationListResp = getTranslate().translations().list(List(text).asJava, "en")
      .setSource(source.getOrElse("pt"))
      .setKey(Resources.getSecrets())
      .execute()
      
      translationListResp.getTranslations.toList.map { trans => trans.getTranslatedText }.mkString("\n\n")
    } catch {
       case e: Exception => e.printStackTrace(); log.info(e.getMessage); e.getMessage
    }
  }

}
