package com.eduardo.util

import scala.io.Source;
import java.util.Properties

/**
  * Created by Eduardo on 2015-12-06.
  */
object Resources {

  def getSecrets(): String = {
    val resource = getClass.getResourceAsStream("/api.secret")
    val reader = Source.fromInputStream(resource).getLines()
    reader.next
  }
  
  def getAppName():String = {
    val nameResourceIs = getClass.getResourceAsStream("/my.properties")
    val p = new Properties()
    p.load(nameResourceIs)
    p getProperty "app.name"
  }
}
