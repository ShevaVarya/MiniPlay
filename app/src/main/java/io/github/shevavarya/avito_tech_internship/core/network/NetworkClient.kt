package io.github.shevavarya.avito_tech_internship.core.network

import io.github.shevavarya.avito_tech_internship.core.utils.NetworkChecker

interface NetworkClient {

}

class NetworkClientImpl(
    private val networkChecker: NetworkChecker
) : NetworkClient {

}