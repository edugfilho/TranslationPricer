package com.eduardo.transformer;

import com.eduardo.model.BaseModel;
import com.eduardo.transients.BaseModelTo;
import com.google.api.server.spi.config.Transformer;

/**
 * Created by Eduardo on 2015-11-21.
 */
public class BaseTransformer implements Transformer<BaseModel, BaseModelTo> {

    @Override
    public BaseModelTo transformTo(BaseModel baseModel) {
        return new BaseModelTo(baseModel.getString());
    }

    @Override
    public BaseModel transformFrom(BaseModelTo baseModelTo) {
        return new BaseModel(baseModelTo.getName());
    }


}
