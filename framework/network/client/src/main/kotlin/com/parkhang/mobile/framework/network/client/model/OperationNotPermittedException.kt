package com.parkhang.mobile.framework.network.client.model

class OperationNotPermittedException(
    cause: Throwable,
) : Exception(
        "The operation is not permitted.",
        cause,
    )
