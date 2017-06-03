(function () {
    function receiverMethod() {
        return {
            __noSuchProperty__: this
        }
    }

    Function.prototype.receiverMethod = receiverMethod;
})();

var logFactory = function (name) {
    return org.apache.logging.log4j.LogManager.getLogger(name);
};

var logManager = logFactory.receiverMethod();

var logger = logManager.JS;

logger.info("test");

function print(msg) {
    logger.JS.error("Do not use print() in JS");
}

function println(msg) {
    logger.JS.error("Do not use println() in JS");
}