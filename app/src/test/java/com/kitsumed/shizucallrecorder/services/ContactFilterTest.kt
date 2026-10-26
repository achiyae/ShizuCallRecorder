/*
 * ShizuCallRecorder: FOSS Call recording powered through ADB/Shizuku!
 *  Copyright (C) 2026-present kitsumed (Med)
 *  This software is licensed under the GNU General Public License v3 or later, with additional terms as permitted under Section 7.
 *  The full license text is available in the LICENSE file at the root of this project.
 *  This software is distributed WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package com.kitsumed.shizucallrecorder.services

import com.kitsumed.shizucallrecorder.data.AppPreferences.IgnoreContactsMode
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ContactFilterTest {

    private fun shouldIgnore(
        mode: IgnoreContactsMode,
        hasPhoneNumber: Boolean = true,
        hasContactsPermission: Boolean = true,
        isSavedContact: Boolean = false,
        isSelectedContact: Boolean = false,
    ) = ContactFilter.shouldIgnore(
        mode = mode,
        hasPhoneNumber = hasPhoneNumber,
        hasContactsPermission = hasContactsPermission,
        isSavedContact = { isSavedContact },
        isSelectedContact = { isSelectedContact },
    )

    // NONE (existing behavior)

    @Test
    fun none_neverIgnores() {
        assertFalse(shouldIgnore(IgnoreContactsMode.NONE, isSavedContact = true, isSelectedContact = true))
    }

    // ALL (existing behavior)

    @Test
    fun all_ignoresSavedContact() {
        assertTrue(shouldIgnore(IgnoreContactsMode.ALL, isSavedContact = true))
    }

    @Test
    fun all_recordsUnsavedNumber() {
        assertFalse(shouldIgnore(IgnoreContactsMode.ALL, isSavedContact = false))
    }

    @Test
    fun all_recordsWhenContactsPermissionMissing() {
        assertFalse(shouldIgnore(IgnoreContactsMode.ALL, hasContactsPermission = false, isSavedContact = true))
    }

    // SELECTED (existing behavior)

    @Test
    fun selected_ignoresSelectedContact() {
        assertTrue(shouldIgnore(IgnoreContactsMode.SELECTED, isSelectedContact = true))
    }

    @Test
    fun selected_recordsOtherNumbers() {
        assertFalse(shouldIgnore(IgnoreContactsMode.SELECTED, isSelectedContact = false))
    }

    @Test
    fun selected_recordsWhenContactsPermissionMissing() {
        assertFalse(shouldIgnore(IgnoreContactsMode.SELECTED, hasContactsPermission = false, isSelectedContact = true))
    }

    @Test
    fun selected_recordsAnonymousCall() {
        assertFalse(shouldIgnore(IgnoreContactsMode.SELECTED, hasPhoneNumber = false))
    }

    // RECORD_ONLY_SELECTED (new): records only selected contacts, fails closed

    @Test
    fun recordOnlySelected_recordsSelectedContact() {
        assertFalse(shouldIgnore(IgnoreContactsMode.RECORD_ONLY_SELECTED, isSelectedContact = true))
    }

    @Test
    fun recordOnlySelected_ignoresOtherNumbers() {
        assertTrue(shouldIgnore(IgnoreContactsMode.RECORD_ONLY_SELECTED, isSavedContact = true, isSelectedContact = false))
    }

    @Test
    fun recordOnlySelected_ignoresAnonymousCall() {
        assertTrue(shouldIgnore(IgnoreContactsMode.RECORD_ONLY_SELECTED, hasPhoneNumber = false))
    }

    @Test
    fun recordOnlySelected_ignoresWhenContactsPermissionMissing() {
        assertTrue(shouldIgnore(IgnoreContactsMode.RECORD_ONLY_SELECTED, hasContactsPermission = false, isSelectedContact = true))
    }

    // Lookups are skipped when there is nothing to look up

    @Test
    fun lookupsNotPerformedWithoutPhoneNumber() {
        IgnoreContactsMode.entries.forEach { mode ->
            ContactFilter.shouldIgnore(
                mode = mode,
                hasPhoneNumber = false,
                hasContactsPermission = true,
                isSavedContact = { throw AssertionError("saved-contact lookup ran for $mode") },
                isSelectedContact = { throw AssertionError("selected-contact lookup ran for $mode") },
            )
        }
    }
}
