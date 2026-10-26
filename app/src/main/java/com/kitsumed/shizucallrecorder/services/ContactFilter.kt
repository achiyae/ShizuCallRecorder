/*
 * ShizuCallRecorder: FOSS Call recording powered through ADB/Shizuku!
 *  Copyright (C) 2026-present kitsumed (Med)
 *  This software is licensed under the GNU General Public License v3 or later, with additional terms as permitted under Section 7.
 *  The full license text is available in the LICENSE file at the root of this project.
 *  This software is distributed WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package com.kitsumed.shizucallrecorder.services

import com.kitsumed.shizucallrecorder.data.AppPreferences

/**
 * Pure decision logic for the contact filter used by [RecordingDecisionEngine].
 *
 * Kept free of Android dependencies so it can be unit tested. The Contacts lookups are passed in as
 * lambdas and are only invoked when the selected mode actually needs them.
 */
object ContactFilter {

    /**
     * Determines whether automatic recording should be skipped for a call, based on the contact filter mode.
     *
     * The ignore modes (ALL, SELECTED) fail open: when the contact can't be checked (no phone number, no
     * Contacts permission), the call is recorded. RECORD_ONLY_SELECTED fails closed: the call is only
     * recorded when it positively matches a selected contact.
     *
     * @param mode The contact filter mode for the call direction
     * @param hasPhoneNumber false if the number is blank (e.g. anonymous call)
     * @param hasContactsPermission Whether the app can read the device Contacts
     * @param isSavedContact Lookup returning true if the number belongs to any saved contact
     * @param isSelectedContact Lookup returning true if the number belongs to a contact in the selected list
     * @return true if the call should be ignored (not automatically recorded), false otherwise
     */
    fun shouldIgnore(
        mode: AppPreferences.IgnoreContactsMode,
        hasPhoneNumber: Boolean,
        hasContactsPermission: Boolean,
        isSavedContact: () -> Boolean,
        isSelectedContact: () -> Boolean,
    ): Boolean {
        val canLookUp = hasPhoneNumber && hasContactsPermission
        return when (mode) {
            AppPreferences.IgnoreContactsMode.NONE -> false
            AppPreferences.IgnoreContactsMode.ALL -> canLookUp && isSavedContact()
            AppPreferences.IgnoreContactsMode.SELECTED -> canLookUp && isSelectedContact()
            AppPreferences.IgnoreContactsMode.RECORD_ONLY_SELECTED -> !(canLookUp && isSelectedContact())
        }
    }
}
