package org.beckn.one.sandbox.bap.client.controllers

import org.beckn.one.sandbox.bap.client.dtos.ClientQuoteResponse
import org.beckn.one.sandbox.bap.client.dtos.ClientResponse
import org.beckn.one.sandbox.bap.client.services.GenericOnPollService
import org.beckn.protocol.schemas.ProtocolOnSelect
import org.beckn.one.sandbox.bap.schemas.factories.ContextFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OnGetQuotePollController @Autowired constructor(
  onPollService: GenericOnPollService<ProtocolOnSelect, ClientQuoteResponse>,
  contextFactory: ContextFactory
) : BaseOnPollController<ProtocolOnSelect, ClientQuoteResponse>(onPollService, contextFactory) {

  @RequestMapping("/client/v1/on_get_quote")
  @ResponseBody
  fun onSelect(@RequestParam messageId: String): ResponseEntity<out ClientResponse> = onPoll(messageId)
}