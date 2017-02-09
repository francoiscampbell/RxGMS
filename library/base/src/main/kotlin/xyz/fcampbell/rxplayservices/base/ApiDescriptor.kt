package xyz.fcampbell.rxplayservices.base

/**
 * Holder class that determines which API to connect to and any required parameters
 *
 * @param A The type of the API to wrap  (If there is no API but you still want to extend this class, use [Unit] or [Void]).
 * @param O The type of that API's options.
 *
 * @constructor
 * @param api The Google Play services to connect to
 * @param apiInterface The interface that hosts the methods that make up the API (the methods that require a [GoogleApiClient])
 * @param options Any [Api.ApiOptions] to pass use with this API
 * @param scopes Any [Scope]s required for this API
 */
class ApiDescriptor<out A, O : Api.ApiOptions>(
        val api: Api<O>,
        val apiInterface: A,
        val options: O? = null,
        vararg val scopes: Scope
)