/*
 * Knot.x - Reactive microservice assembler - View Engine Verticle
 *
 * Copyright (C) 2016 Cognifide Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cognifide.knotx.engine.view.service;

import com.cognifide.knotx.dataobjects.HttpResponseWrapper;
import com.cognifide.knotx.dataobjects.EngineRequest;
import com.cognifide.knotx.engine.view.ViewEngineConfiguration;

import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.Message;
import rx.Observable;

public class ServiceEngine {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEngine.class);
  private static final String RESULT_NAMESPACE_KEY = "_result";
  private static final String RESPONSE_NAMESPACE_KEY = "_response";

  private final ViewEngineConfiguration configuration;

  private final EventBus eventBus;

  public ServiceEngine(EventBus eventBus, ViewEngineConfiguration serviceConfiguration) {
    this.eventBus = eventBus;
    this.configuration = serviceConfiguration;
  }

  public Observable<JsonObject> doServiceCall(ServiceEntry serviceEntry,
                                              EngineRequest engineRequest) {
    JsonObject serviceMessage = new JsonObject();
    serviceMessage.put("request", engineRequest.request().toJson());
    serviceMessage.put("params", serviceEntry.getParams());

    return eventBus.<JsonObject>sendObservable(serviceEntry.getAddress(), serviceMessage)
        .compose(transformResponse());
  }

  private Observable.Transformer<Message<JsonObject>, JsonObject> transformResponse() {
    return messageObs -> messageObs.map(msg -> new HttpResponseWrapper(msg.body()))
        .map(this::buildResultObject);
  }

  public ServiceEntry mergeWithConfiguration(final ServiceEntry serviceEntry) {
    return configuration.getServices().stream()
        .filter(service -> serviceEntry.getName().matches(service.getName()))
        .findFirst().map(metadata ->
            serviceEntry.setAddress(metadata.getAddress())
                .mergeParams(metadata.getParams())
                .overrideCacheKey(metadata.getCacheKey())
        )
        .get();
  }

  private JsonObject buildResultObject(HttpResponseWrapper response) {
    JsonObject object = new JsonObject();

    String rawData = response.body().toString().trim();

    if (rawData.charAt(0) == '[') {
      object.put(RESULT_NAMESPACE_KEY, new JsonArray(rawData));
    } else if (rawData.charAt(0) == '{') {
      object.put(RESULT_NAMESPACE_KEY, response.toJson());
    } else {
      throw new DecodeException("Result is neither Json Array nor Json Object");
    }
    object.put(RESULT_NAMESPACE_KEY, new JsonObject(response.toJson().getString("body")));
    object.put(RESPONSE_NAMESPACE_KEY, new JsonObject().put("statusCode", response.statusCode().codeAsText()));
    return object;
  }

  private void traceServiceCall(Buffer results, ServiceEntry entry) {
    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("Service call returned <{}> <{}> <{}>", results.toString(), entry.getAddress(), entry.getParams());
    }
  }
}
