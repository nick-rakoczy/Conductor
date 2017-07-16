Function.prototype.receiverMethod = function () {
    return {
        __noSuchProperty__: this
    };
};

var logFactory = function (name) {
    return org.apache.logging.log4j.LogManager.getLogger(name);
};

var logManager = logFactory.receiverMethod();

var logger = logManager.JS;

function print(msg) {
    logger.error("Do not use print() in JS");
}

function println(msg) {
    logger.error("Do not use println() in JS");
}
