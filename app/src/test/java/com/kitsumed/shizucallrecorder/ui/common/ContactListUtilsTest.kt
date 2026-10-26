/*
 * ShizuCallRecorder: FOSS Call recording powered through ADB/Shizuku!
 *  Copyright (C) 2026-present kitsumed (Med)
 *  This software is licensed under the GNU General Public License v3 or later, with additional terms as permitted under Section 7.
 *  The full license text is available in the LICENSE file at the root of this project.
 *  This software is distributed WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package com.kitsumed.shizucallrecorder.ui.common

import com.kitsumed.shizucallrecorder.ui.viewmodels.ContactEntry
import org.junit.Assert.assertEquals
import org.junit.Test

class ContactListUtilsTest {

    private val alice = ContactEntry("a", "Alice", "+1 555-0101")
    private val bob = ContactEntry("b", "Bob", "+1 555-0202")
    private val carol = ContactEntry("c", "Carol", "+1 555-0303")
    private val dave = ContactEntry("d", "Dave", "+1 555-0404")
    private val alphabetical = listOf(alice, bob, carol, dave)

    @Test
    fun selectedFirst_putsSelectedContactsAtTop() {
        val result = ContactListUtils.selectedFirst(alphabetical, setOf("c", "b"))
        assertEquals(listOf(bob, carol, alice, dave), result)
    }

    @Test
    fun selectedFirst_keepsOrderWhenNothingSelected() {
        assertEquals(alphabetical, ContactListUtils.selectedFirst(alphabetical, emptySet()))
    }

    @Test
    fun selectedEntries_returnsOnlySelectedInListOrder() {
        val result = ContactListUtils.selectedEntries(alphabetical, setOf("d", "a"))
        assertEquals(listOf(alice, dave), result)
    }

    @Test
    fun selectedEntries_dropsKeysOfDeletedContacts() {
        val result = ContactListUtils.selectedEntries(alphabetical, setOf("b", "deleted-contact"))
        assertEquals(listOf(bob), result)
    }
}
