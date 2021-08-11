package org.beckn.one.sandbox.bap.configurations

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.resilience4j.retrofit.CircuitBreakerCallAdapter
import io.github.resilience4j.retrofit.RetryCallAdapter
import io.github.resilience4j.retry.Retry
import okhttp3.OkHttpClient
import org.beckn.one.sandbox.bap.client.external.registry.RegistryClient
import org.beckn.one.sandbox.bap.client.shared.security.SignRequestInterceptor
import org.beckn.one.sandbox.bap.factories.CircuitBreakerFactory
import org.beckn.one.sandbox.bap.factories.RetryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


@Configuration
class RegistryClientConfiguration(
  @Autowired @Value("\${registry_service.url}")
  private val registryServiceUrl: String,
  @Value("\${registry_service.retry.max_attempts}")
  private val maxAttempts: Int,
  @Value("\${registry_service.retry.initial_interval_in_millis}")
  private val initialIntervalInMillis: Long,
  @Value("\${registry_service.retry.interval_multiplier}")
  private val intervalMultiplier: Double,
  @Autowired @Value("\${bpp_registry_service.url}")
  private val bppRegistryServiceUrl: String,
  @Value("\${beckn.security.enabled}") private val enableSecurity: Boolean,
  @Autowired
  private val objectMapper: ObjectMapper,
  @Autowired
  private val interceptor: SignRequestInterceptor
) {

  @Bean
  @Primary
  fun registryServiceClient(): RegistryClient {
    val retry: Retry = RetryFactory.create(
      "RegistryClient",
      maxAttempts,
      initialIntervalInMillis,
      intervalMultiplier
    )
    val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
    val registryCircuitBreaker = CircuitBreakerFactory.create("RegistryClient")
    val retrofitBuilder = Retrofit.Builder()
      .baseUrl(registryServiceUrl)
      .addConverterFactory(JacksonConverterFactory.create(objectMapper))
      .addCallAdapterFactory(RetryCallAdapter.of(retry))
      .addCallAdapterFactory(CircuitBreakerCallAdapter.of(registryCircuitBreaker))
    val retrofit = if(enableSecurity) retrofitBuilder.client(okHttpClient).build() else retrofitBuilder.build()
    return retrofit.create(RegistryClient::class.java)
  }

  @Bean(BPP_REGISTRY_SERVICE_CLIENT)
  fun bppRegistryServiceClient(): RegistryClient {
    val retry: Retry = RetryFactory.create(
      "BppRegistryClient",
      maxAttempts,
      initialIntervalInMillis,
      intervalMultiplier
    )
    val bppRegistryCircuitBreaker = CircuitBreakerFactory.create("BppRegistryClient")
    val retrofit = Retrofit.Builder()
      .baseUrl(bppRegistryServiceUrl)
      .addConverterFactory(JacksonConverterFactory.create(objectMapper))
      .addCallAdapterFactory(RetryCallAdapter.of(retry))
      .addCallAdapterFactory(CircuitBreakerCallAdapter.of(bppRegistryCircuitBreaker))
      .build()

    return retrofit.create(RegistryClient::class.java)
  }

  companion object {
    const val BPP_REGISTRY_SERVICE_CLIENT = "bppRegistryServiceClient"

  }
}