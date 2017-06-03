function assertTrue() {
    org.junit.Assert.assertTrue(arguments);
}
function assertFalse() {
    org.junit.Assert.assertFalse(arguments);
}
function assertNull() {
    org.junit.Assert.assertNull(arguments);
}
function assertNotNull() {
    org.junit.Assert.assertNotNull(arguments);
}
function assertEquals() {
    org.junit.Assert.assertEquals(arguments);
}
function assertArrayEquals() {
    org.junit.Assert.assertArrayEquals(arguments);
}
function fail() {
    org.junit.Assert.fail(arguments);
}
function assertUndefined(obj) {
    assertTrue(typeof(obj) === 'undefined');
}

