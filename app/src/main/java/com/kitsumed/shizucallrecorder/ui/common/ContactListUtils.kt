/*
 * ShizuCallRecorder: FOSS Call recording powered through ADB/Shizuku!
 *  Copyright (C) 2026-present kitsumed (Med)
 *  This software is licensed under the GNU General Public License v3 or later, with additional terms as permitted under Section 7.
 *  The full license text is available in the LICENSE file at the root of this project.
 *  This software is distributed WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package com.kitsumed.shizucallrecorder.ui.common

import com.kitsumed.shizucallrecorder.ui.viewmodels.ContactEntry

/**
 * Pure helpers for displaying the selected contacts list.
 */
object ContactListUtils {

    /**
     * Moves the selected contacts to the top of the list, keeping the existing order within the
     * selected and unselected groups (the sort is stable).
     *
     * @param contacts The contacts, already in display order (e.g. alphabetical).
     * @param selectedLookupKeys The lookup keys of the selected contacts.
     * @return The same contacts, selected ones first.
     */
    fun selectedFirst(contacts: List<ContactEntry>, selectedLookupKeys: Set<String>): List<ContactEntry> =
        contacts.sortedByDescending { it.lookupKey in selectedLookupKeys }

    /**
     * Returns the selected contacts, in the order of [contacts]. Saved keys that no longer match a
     * device contact (e.g. a deleted contact) are dropped.
     *
     * @param contacts The contacts, in display order.
     * @param selectedLookupKeys The lookup keys of the selected contacts.
     * @return The selected contacts only.
     */
    fun selectedEntries(contacts: List<ContactEntry>, selectedLookupKeys: Set<String>): List<ContactEntry> =
        contacts.filter { it.lookupKey in selectedLookupKeys }
}
