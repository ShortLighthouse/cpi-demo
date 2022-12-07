import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.transform.Field;
import groovy.json.JsonOutput;
import groovy.xml.XmlUtil;

@Field String REQUEST_JSON_PAYLOAD_LOG = 'JSON Request Payload';
@Field String RESPONSE_JSON_PAYLOAD_LOG = 'JSON Response Payload';
@Field String BEFORE_MAPPING_XML_PAYLOAD_LOG = 'Payload XML before mapping';
@Field String AFTER_MAPPING_XML_PAYLOAD_LOG = 'Payload XML after mapping';
@Field String AFTER_CONVERSION_FLAT_FILE_PAYLOAD_LOG = 'Payload Flat File after conversion';
@Field String REQUEST_XML_PAYLOAD_LOG = 'XML Request Payload';
@Field String CUSTOMER_RESPONSE_XML_PAYLOAD_LOG = 'Customer XML Response Payload';
@Field String SALES_ORDER_RESPONSE_XML_PAYLOAD_LOG = 'Sales Order XML Response Payload';
@Field String RESPONSE_XML_PAYLOAD_LOG = 'XML Response Payload';
@Field String SALESFORCE_RESPONSE_XML_PAYLOAD_LOG = 'Salesforce XML Response Payload';
@Field String REQUEST_PLAIN_PAYLOAD_LOG = 'PLAIN Request Payload';
@Field String RESPONSE_PLAIN_PAYLOAD_LOG = 'PLAIN Response Payload';
@Field String JSON_PAYLOAD_LOG = 'JSON Payload';
@Field String XML_PAYLOAD_LOG = 'XML Payload';
@Field String PLAIN_PAYLOAD_LOG = 'PLAIN Payload';
@Field String PLAIN_SALESFORCE_RECEIPT_LOG = 'PLAIN Salesforce Receipt';
@Field String JSON_EXCEPTION_LOG = 'JSON Exception';
@Field String XML_EXCEPTION_LOG = 'XML Exception';
@Field String PLAIN_EXCEPTION_LOG = 'PLAIN Exception';
@Field String NO_BODY_LOG = 'No Body';
@Field String EMPTY_IDOC_LOG = 'Empty IDoc';

// Test comment new 1
// Test comment new 2
// Test comment new 3
// Test comment new 4
// Test comment new 5

def Boolean isDebug(Message message) {
    def isDebug = false;

    def processingLogConfiguration = message.getProperty("EnableLogging") as String;
    if (processingLogConfiguration.equals("true")) {
        isDebug = true;
    }
    return isDebug;
}

def Message logEmptyIDoc(Message message) {
    processData(EMPTY_IDOC_LOG, false, message);
}

def Message logSalesforceResponseXml(Message message) {
    processData(SALESFORCE_RESPONSE_XML_PAYLOAD_LOG, true, message);
}

def Message logJsonRequest(Message message) {
    processData(REQUEST_JSON_PAYLOAD_LOG, false, message);
}

def Message logJsonResponse(Message message) {
    processData(RESPONSE_JSON_PAYLOAD_LOG, false, message);
}

def Message logXmlBeforeMapping(Message message) {
    processData(BEFORE_MAPPING_XML_PAYLOAD_LOG, true, message);
}

def Message logXmlAfterMapping(Message message) {
    processData(AFTER_MAPPING_XML_PAYLOAD_LOG, true, message);
}

def Message logFlatFileAfterConversion(Message message) {
    processData(AFTER_CONVERSION_FLAT_FILE_PAYLOAD_LOG, false, message);
}

def Message logXMLRequest(Message message) {
    processData(REQUEST_XML_PAYLOAD_LOG, true, message);
}

def Message logXMLResponse(Message message) {
    processData(RESPONSE_XML_PAYLOAD_LOG, true, message);
}

def Message logCustomerXMLResponse(Message message) {
    processData(CUSTOMER_RESPONSE_XML_PAYLOAD_LOG, true, message);
}

def Message logSalesOrderXMLResponse(Message message) {
    processData(SALES_ORDER_RESPONSE_XML_PAYLOAD_LOG, true, message);
}

def Message logPlainRequest(Message message) {
    processData(REQUEST_PLAIN_PAYLOAD_LOG, false, message);
}

def Message logPlain(Message message) {
    processData(PLAIN_PAYLOAD_LOG, false, message);
}

def Message logPlainSalesforceReceipt(Message message) {
    processData(PLAIN_SALESFORCE_RECEIPT_LOG, false, message);
}

def Message logXML(Message message) {
    processData(XML_PAYLOAD_LOG, false, message);
}

def Message logJson(Message message) {
    processData(JSON_PAYLOAD_LOG, false, message);
}

def Message logPlainResponse(Message message) {
    processData(RESPONSE_PLAIN_PAYLOAD_LOG, false, message);
}

def Message logXmlException(Message message) {
    processException(XML_EXCEPTION_LOG, "xml", message);
}

def Message logPlainException(Message message) {
    processException(PLAIN_EXCEPTION_LOG, "plain", message);
}

def Message logJsonException(Message message) {
    processException(JSON_EXCEPTION_LOG, "json", message);
}

def Message logPropertiesAndHeaders(Message message) {
    processData(NO_BODY_LOG, false, message);
}

def Message logCustomHeader(Message message) {
    def body = message.getBody(java.lang.String) as String;
    def headers = message.getHeaders() as Map<String, Object>;
    def properties = message.getProperties() as Map<String, Object>;
    def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null) {
        if(properties.get("useProperties") == "true"){
            def propertyValue = properties.get("propertyName")
            messageLog.addCustomHeaderProperty("propertyName",propertyValue);
        }

    }
    return message;
}

def Message processData(String title, boolean isXML, Message message) {
    if (isDebug(message)) {
        def body = message.getBody(java.lang.String) as String;
        def headers = message.getHeaders() as Map<String, Object>;
        def properties = message.getProperties() as Map<String, Object>;

        def propertiesAsString ="\n";
        properties.each{ it -> propertiesAsString = propertiesAsString + "${it}" + "\n" };

        def headersAsString ="\n";
        headers.each{ it -> headersAsString = headersAsString + "${it}" + "\n" };

        def messageLog = messageLogFactory.getMessageLog(message);
        if(messageLog != null) {
            if (isXML) {
                messageLog.addAttachmentAsString(title , "\n Properties \n ----------   \n" + propertiesAsString +
                        "\n Headers \n ----------   \n" + headersAsString +
                        "\n Body \n ----------  \n\n" + XmlUtil.serialize(body), "text/xml");
            } else if(title.contains("PLAIN")) {
                messageLog.addAttachmentAsString(title , "\n Properties \n ----------   \n" + propertiesAsString +
                        "\n Headers \n ----------   \n" + headersAsString +
                        "\n Body \n ----------  \n\n" + body, "text/plain");
            } else if(title.contains("No Body")) {
                messageLog.addAttachmentAsString("Props and Heads" , "\n Properties \n ----------   \n" + propertiesAsString +
                        "\n Headers \n ----------   \n" + headersAsString, "text/plain");
            } else if(title.contains("Empty IDoc")) {
                messageLog.addAttachmentAsString(title , "\n Properties \n ----------   \n" + propertiesAsString +
                        "\n Headers \n ----------   \n" + headersAsString +
                        "\n Body \n ----------  \n\n" + XmlUtil.serialize(body), "text/xml");
            } else {
                messageLog.addAttachmentAsString(title , "\n Properties \n ----------   \n" + propertiesAsString +
                        "\n Headers \n ----------   \n" + headersAsString +
                        "\n Body \n ----------  \n\n" + JsonOutput.prettyPrint(body), "application/json");
            }
        }
    }
    return message;
}

def Message processException(String title, String type, Message message) {

    def body = message.getBody(java.lang.String) as String;
    def headers = message.getHeaders() as Map<String, Object>;
    def properties = message.getProperties() as Map<String, Object>;
    def ex = properties.get("CamelExceptionCaught");
    def fedex = def ex = properties.get("CamelExceptionCaught");

    def propertiesAsString ="\n";
    properties.each{ it -> propertiesAsString = propertiesAsString + "${it}" + "\n" };

    def headersAsString ="\n";
    headers.each{ it -> headersAsString = headersAsString + "${it}" + "\n" };

    def exAsString ="\n";
    if (ex != null) {

        exAsString += "Exception Class=" + ex.getClass().getCanonicalName() + "\n";
        exAsString += "Exception Content=" + ex.getClass().toString() + "\n";
    }

    def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null) {
        if (type == "xml") {
            messageLog.addAttachmentAsString(title , "\n Properties \n ----------   \n" + propertiesAsString +
                    "\n Headers \n ----------   \n" + headersAsString +
                    "\n Exceptions \n ----------   \n" + exAsString +
                    "\n Body \n ----------  \n\n" + XmlUtil.serialize(body), "text/xml");
        } else if (type == "json"){
            messageLog.addAttachmentAsString(title , "\n Properties \n ----------   \n" + propertiesAsString +
                    "\n Headers \n ----------   \n" + headersAsString +
                    "\n Exceptions \n ----------   \n" + exAsString +
                    "\n Body \n ----------  \n\n" + JsonOutput.prettyPrint(body), "application/json");
        } else {
            messageLog.addAttachmentAsString(title , "\n Properties \n ----------   \n" + propertiesAsString +
                    "\n Headers \n ----------   \n" + headersAsString +
                    "\n Exceptions \n ----------   \n" + exAsString +
                    "\n Body \n ----------  \n\n" + body, "text/plain");
        }
    }
    return message;
}