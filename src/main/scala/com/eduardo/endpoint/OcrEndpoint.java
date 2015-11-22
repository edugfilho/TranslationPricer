package com.eduardo.endpoint;

import com.eduardo.model.BaseModel;
import com.eduardo.service.OcrService;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import constants.Constants;

/**
 * Created by Eduardo on 2015-11-21.
 */
@Api(name = "rest", version = "v1", clientIds = { Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID })
public class OcrEndpoint {

    OcrService service = new OcrService();

    @ApiMethod(name = "getLocations", path = "locations", httpMethod = "get")
    public BaseModel getLocationsNearby()
            throws InternalServerErrorException, BadRequestException, NotFoundException, OAuthRequestException {
        return service.performOcr("Not implemented");
    }

}
