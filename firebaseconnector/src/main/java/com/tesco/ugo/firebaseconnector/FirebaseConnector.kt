package com.tesco.ugo.firebaseconnector

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.tesco.ugo.ConnectorType
import com.tesco.ugo.UgoConnector
import com.tesco.ugo.UgoEvent
import com.tesco.ugo.UgoProperty


class FirebaseConnector(private val context: Context) : UgoConnector<Any>() {

    private var firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun acceptEvent(event: UgoEvent): Boolean {
        if (event.connectorTypeSet == null
            || event.connectorTypeSet!!.contains(getConnectorType())
        ) {
            return true
        }
        return false
    }

    companion object {
        private val TAG: String = FirebaseConnector::class.java.canonicalName!!

        fun getConnectorType(): ConnectorType {
            return ConnectorType(TAG)
        }
    }

    override fun acceptProperty(property: UgoProperty): Boolean {
        return true
    }

    override fun logMessage(tag: String, logMessage: String) {

    }

    override fun postEvent(transformedEvent: Any) {
        if (transformedEvent is Bundle) {
            val eventName = transformedEvent["eventName"] as String
            transformedEvent.remove("eventName")
            firebaseAnalytics.logEvent(eventName, transformedEvent)
        }
    }

    override fun setProperty(transformedProperty: Any) {

    }

    override fun transformEvent(event: UgoEvent): Any {
        val parameters = event.param
        val bundle = Bundle()

        if (parameters == null) {
            return bundle
        }
        bundle.putString("eventName", event.eventName)
        val hmIterator: Iterator<*> = parameters.entries.iterator()
        while (hmIterator.hasNext()) {
            val mapElement = hmIterator.next() as Map.Entry<*, *>
            bundle.putString(mapElement.key as String, mapElement.value as String)
        }
        return bundle

    }

    override fun transformProperty(property: UgoProperty): Any {
        return property
    }
}