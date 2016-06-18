package com.eduardo.util

import scala.io.Source;
/**
  * Created by Eduardo on 2015-12-06.
  */
object Resources {


  def getSecrets(): (String, String) = {
    val resource = getClass.getResourceAsStream("/ocrsecrets.properties")
    val reader = Source.fromInputStream(resource).getLines()
    (reader.next, reader.next)
  }

}
