package com.eduardo.transformer


import com.eduardo.transients.BaseModelTo
import com.google.api.server.spi.config.Transformer
import com.eduardo.model.BaseModel

/**
 * Created by Eduardo on 2015-11-21.
 */
class BaseModelTransformer extends Transformer[BaseModel, BaseModelTo]{

  override def transformFrom(arg0: BaseModelTo): BaseModel = {
    new BaseModel(arg0.getName())
  }

  override def transformTo(arg0: BaseModel): BaseModelTo = {
    new BaseModelTo(arg0.getString())
  }

}
