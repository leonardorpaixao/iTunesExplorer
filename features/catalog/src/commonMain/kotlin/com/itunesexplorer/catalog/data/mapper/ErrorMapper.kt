package com.itunesexplorer.catalog.data.mapper

import com.itunesexplorer.core.common.domain.DomainError
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException

/**
 * Maps exceptions from the network layer to domain-level errors.
 */
object ErrorMapper {
    /**
     * Convert a throwable to a DomainError.
     *
     * @param throwable The exception to map
     * @param context Optional context message for better error description
     * @return Appropriate DomainError instance
     */
    fun toDomainError(throwable: Throwable, context: String = ""): DomainError {
        return when (throwable) {
            // Network/IO errors
            is IOException -> DomainError.NetworkError(
                message = "Falha na conexão: ${throwable.message ?: "Sem conexão com a internet"}"
            )

            // Server errors (5xx)
            is ServerResponseException -> DomainError.ServerError(
                message = "Servidor indisponível: ${throwable.message ?: "Tente novamente mais tarde"}",
                code = throwable.response.status.value
            )

            // Client errors (4xx)
            is ClientRequestException -> {
                when (throwable.response.status) {
                    HttpStatusCode.NotFound -> DomainError.NotFoundError(
                        message = if (context.isNotEmpty()) {
                            "Não encontrado: $context"
                        } else {
                            "Recurso não encontrado"
                        }
                    )
                    else -> DomainError.ClientError(
                        message = "Erro na requisição: ${throwable.message ?: "Dados inválidos"}",
                        code = throwable.response.status.value
                    )
                }
            }

            // Data parsing errors
            is kotlinx.serialization.SerializationException -> DomainError.DataError(
                message = "Erro ao processar dados: ${throwable.message ?: "Formato inválido"}"
            )

            // Unknown errors
            else -> DomainError.UnknownError(
                message = throwable.message ?: "Erro inesperado",
                cause = throwable
            )
        }
    }

    /**
     * Execute a block and wrap the result in a DomainResult type,
     * converting any exceptions to DomainErrors.
     *
     * @param context Optional context for error messages
     * @param block The suspend function to execute
     * @return DomainResult containing the value or a DomainError
     */
    suspend fun <T> execute(
        context: String = "",
        block: suspend () -> T
    ): com.itunesexplorer.core.common.domain.DomainResult<T> {
        return try {
            com.itunesexplorer.core.common.domain.DomainResult.success(block())
        } catch (e: Exception) {
            com.itunesexplorer.core.common.domain.DomainResult.failure(toDomainError(e, context))
        }
    }
}
