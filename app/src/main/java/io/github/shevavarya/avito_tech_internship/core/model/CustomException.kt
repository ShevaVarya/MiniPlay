package io.github.shevavarya.avito_tech_internship.core.model

sealed class CustomException : Exception() {
    data object EmptyError : CustomException()
    data object NetworkError : CustomException()
    data object ServerError : CustomException()
    data class RequestError(val code: Int) : CustomException()
}