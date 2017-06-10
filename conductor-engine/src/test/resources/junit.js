function assert() {
    function argumentsToArray(args) {
        var array = [];
        for (var i = 0; i < args.length; i++) {
            array[i] = args[i];
        }
        return array;
    }

    argumentsToArray(arguments).forEach(function (e) {
        logger.info(JSON.stringify(e));
        if (e != true) {
            throw "Assertion failure";
        }
    });
}

