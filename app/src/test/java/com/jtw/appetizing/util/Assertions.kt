package com.jtw.appetizing.util

import org.junit.Assert

infix fun <T> T?.shouldBe(expected: T?) {
    Assert.assertEquals(expected, this)
}

infix fun <T> Collection<T>.shouldContain(expected: T?) {
    Assert.assertTrue(this.contains(expected))
}

inline fun <reified T> Any?.shouldBeInstance() {
    Assert.assertNotNull("null is not an instance of expected class ${T::class.java.canonicalName}", this)
    Assert.assertTrue("Expected instance of ${T::class.java.canonicalName} but was ${this!!::class.java.canonicalName}",
            this is T)
}
