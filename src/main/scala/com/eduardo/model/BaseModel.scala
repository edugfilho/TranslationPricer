package com.eduardo.model

import com.eduardo.transformer.BaseModelTransformer
import com.google.api.server.spi.config.ApiTransformer

//@ApiTransformer(classOf[BaseModelTransformer])
class BaseModel(name: String) {

  def getString(): String = {
    name
  }

}
