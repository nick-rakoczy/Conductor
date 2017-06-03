function getRootLogger() {
    return org.apache.logging.log4j.LogManager.getLogger("JS");
}

function getLogger(name) {
    return org.apache.logging.log4j.LogManager.getLogger(name);
}

function print(msg) {
    error("Do not use print() in JS");
}

function println(msg) {
    error("Do not use println() in JS");
}

function info(msg) {
    getRootLogger().info(msg);
}

function warn(msg) {
    getRootLogger().warn(msg);
}

function error(msg) {
    getRootLogger().error(msg);
}

function debug(msg) {
    getRootLogger().debug(msg);
}

function trace(msg) {
    getRootLogger().trace(msg);
}