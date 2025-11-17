package com.itunesexplorer.catalog.domain.usecase

import com.itunesexplorer.core.common.domain.DomainResult

/**
 * Base interface for use cases that don't require input parameters.
 *
 * @param Result The type of data returned by the use case
 */
interface UseCase<out Result> {
    suspend operator fun invoke(): DomainResult<Result>
}

/**
 * Base interface for use cases that require input parameters.
 *
 * @param Params The type of input parameters
 * @param Result The type of data returned by the use case
 */
interface UseCaseWithParams<in Params, out Result> {
    suspend operator fun invoke(params: Params): DomainResult<Result>
}
