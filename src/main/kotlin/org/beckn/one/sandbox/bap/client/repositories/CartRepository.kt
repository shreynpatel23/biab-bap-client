package org.beckn.one.sandbox.bap.client.repositories

import arrow.core.Either
import org.beckn.one.sandbox.bap.client.daos.CartDao
import org.beckn.one.sandbox.bap.client.services.CartService
import org.beckn.one.sandbox.bap.errors.HttpError
import org.beckn.one.sandbox.bap.errors.database.DatabaseError
import org.beckn.one.sandbox.bap.message.repositories.GenericRepository
import org.litote.kmongo.eq
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class CartRepository @Autowired constructor(
  private val genericRepository: GenericRepository<CartDao>,
  private val log: Logger = LoggerFactory.getLogger(CartService::class.java)
) {
  fun saveCart(cart: CartDao): Either<HttpError, CartDao> {
    return Either.catch {
      log.info("Persisting cart: {}", cart)
      val upsertResult = genericRepository.upsert(cart, CartDao::id eq cart.id)
      log.info("Cart {} persist result: {}", cart.id, upsertResult)
      return Either.Right(cart)
    }
      .mapLeft {
        log.error("Error when saving cart $cart", it)
        return Either.Left(DatabaseError.OnWrite)
      }
  }

  fun deleteById(id: String): CartDao? = genericRepository.deleteOne(CartDao::id eq id)
}