package com.gz.kidsafe.policy

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ParentGateValidatorTest {

    @Test
    fun `validate returns true for exact answer`() {
        assertTrue(ParentGateValidator.validate("42", 42))
    }

    @Test
    fun `validate trims whitespace`() {
        assertTrue(ParentGateValidator.validate(" 42 ", 42))
    }

    @Test
    fun `validate returns false for invalid number`() {
        assertFalse(ParentGateValidator.validate("abc", 42))
    }

    @Test
    fun `validate returns false for wrong answer`() {
        assertFalse(ParentGateValidator.validate("41", 42))
    }
}
