package com.bashar.avalag.src.features.appversion.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UpdateStatusTest {

    @Test
    fun `fromApi maps up_to_date`() {
        assertEquals(UpdateStatus.UP_TO_DATE, UpdateStatus.fromApi("up_to_date"))
        assertEquals(UpdateStatus.UP_TO_DATE, UpdateStatus.fromApi("  UP_TO_DATE  "))
    }

    @Test
    fun `fromApi maps mandatory`() {
        assertEquals(UpdateStatus.MANDATORY, UpdateStatus.fromApi("mandatory"))
        assertEquals(UpdateStatus.MANDATORY, UpdateStatus.fromApi("  MANDATORY  "))
    }

    @Test
    fun `fromApi maps unknown to null`() {
        assertNull(UpdateStatus.fromApi(null))
        assertNull(UpdateStatus.fromApi(""))
        assertNull(UpdateStatus.fromApi("optional"))
        assertNull(UpdateStatus.fromApi("something_else"))
    }
}
