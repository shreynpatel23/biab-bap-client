package org.beckn.one.sandbox.bap.client.factories

import org.beckn.one.sandbox.bap.client.dtos.CartItemProviderDto
import org.beckn.one.sandbox.bap.client.dtos.DeliveryInfoDto
import org.beckn.one.sandbox.bap.client.dtos.OrderDto
import org.beckn.one.sandbox.bap.client.dtos.OrderItemDto
import org.beckn.one.sandbox.bap.schemas.*
import java.math.BigDecimal
import java.util.*

class OrderDtoFactory {
  companion object {
    fun create(
      id: String? = getUuid(),
      transactionId: String = getUuid(),
      bpp1_id: String,
      bpp2_id: String = bpp1_id,
      provider1_id: String,
      provider2_id: String = provider1_id
    ) =
      OrderDto(
        id = id,
        transactionId = transactionId,
        items = listOf(
          OrderItemDto(
            id = getUuid(),
            bppId = bpp1_id,
            quantity = ProtocolItemQuantityAllocated(
              count = 1,
              measure = ProtocolScalar(
                value = BigDecimal.valueOf(1),
                unit = "kg"
              )
            ),
            provider = CartItemProviderDto(
              id = provider1_id,
              locations = listOf("13.001581,77.5703686")
            )
          ),
          OrderItemDto(
            id = getUuid(),
            bppId = bpp2_id,
            quantity = ProtocolItemQuantityAllocated(
              count = 1,
              measure = ProtocolScalar(
                value = BigDecimal.valueOf(1),
                unit = "kg"
              )
            ),
            provider = CartItemProviderDto(
              id = provider2_id,
              locations = listOf("13.001581,77.5703686")
            )
          )
        ),
        billingInfo = ProtocolBilling(
          name = "Test",
          phone = "9999999999",
          email = "test@gmail.com",
          address = ProtocolAddress(
            door = "A",
            country = "IND",
            city = "std:080",
            street = "Bannerghatta Road",
            areaCode = "560076",
            state = "KA",
            building = "Pine Apartments"
          )
        ),
        deliveryInfo = DeliveryInfoDto(
          phone = "9999999999",
          email = "test@gmail.com",
          deliveryLocation = ProtocolLocation(
            address = ProtocolAddress(
              door = "A",
              country = "IND",
              city = "std:080",
              street = "Bannerghatta Road",
              areaCode = "560076",
              state = "KA",
              building = "Pine Apartments"
            ),
            gps = "12,77"
          )
        )
      )

    private fun getUuid() = UUID.randomUUID().toString()
  }
}