package org.beckn.one.sandbox.bap.client.services

import arrow.core.Either
import org.beckn.one.sandbox.bap.client.dtos.ClientResponse
import org.beckn.one.sandbox.bap.client.dtos.ClientSearchResponse
import org.beckn.one.sandbox.bap.client.dtos.ClientSearchResponseMessage
import org.beckn.one.sandbox.bap.client.mappers.ClientCatalogMapper
import org.beckn.one.sandbox.bap.errors.HttpError
import org.beckn.one.sandbox.bap.schemas.ProtocolContext
import org.beckn.one.sandbox.bap.schemas.ProtocolOnSearch
import org.beckn.one.sandbox.bap.schemas.ProtocolResponse

interface GenericOnPollTransformer<in Protocol : ProtocolResponse, out Output : ClientResponse> {
  fun transform(input: List<Protocol>, context: ProtocolContext): Either<HttpError, Output>
}

class SearchClientSearchResponseMapper(
  private val clientCatalogMapper: ClientCatalogMapper
) : GenericOnPollTransformer<ProtocolOnSearch, ClientSearchResponse> {
  override fun transform(
    input: List<ProtocolOnSearch>,
    context: ProtocolContext
  ): Either<HttpError, ClientSearchResponse> =
    Either.Right(
      ClientSearchResponse(
        context = context,
        message = ClientSearchResponseMessage(
          input.mapNotNull { response ->
            response.message?.catalog?.let(clientCatalogMapper::protocolToClientDto)
          })
      )
    )
}
