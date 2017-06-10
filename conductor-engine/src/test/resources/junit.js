function assert() {
    function argumentsToArray(args) {
        var array = [];
        for (var i = 0; i < args.length; i++) {
            array[i] = args[i];
        }
        return array;
    }

    argumentsToArray(arguments).forEach(function (e) {
        if (e != true) {
            throw "Assertion failure";
        }
    });
}

