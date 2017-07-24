component.registerPreloader(10, function (engine) {
    engine.put("preloaderData", "Hello, World!");
});

var ns = "urn:jsc:unit-test";
var componentLogger = org.apache.logging.log4j.LogManager.getLogger("component-js");

component.registerAttributeHandler(ns, "magic", 100, function (e, a, engine, proceed) {
    componentLogger.info(a);
    if (a === "true") {
        proceed.invoke();
    }
});

component.registerSimpleElementHandler(ns, "simple-type", function (e, engine) {
    componentLogger.info(e);
    engine.put("simple", e);
});

component.registerComplexElementHandler(Java.type('urn.conductor.stdlib.xml.ComplexType'), function (e, engine) {
    componentLogger.info(e.key);
    engine.put(e.key, e.value);
});


