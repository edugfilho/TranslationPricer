package com.eduardo.service

import com.eduardo.model.BaseModel

/**
 * Created by Eduardo on 2015-11-21.
 */
class OcrService {

  def performOcr(file: String): BaseModel = {
    new BaseModel(file)
  }
}
